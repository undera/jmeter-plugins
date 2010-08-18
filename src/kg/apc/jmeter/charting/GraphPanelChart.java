package kg.apc.jmeter.charting;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.border.BevelBorder;

import org.apache.jorphan.gui.NumberRenderer;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author apc
 */
public class GraphPanelChart
      extends JComponent
      implements ClipboardOwner
{
   private static final String AD_TEXT = "http://apc.kg";
   private static final String NO_SAMPLES = "Waiting for samples...";
   private static final int spacing = 5;
   private static final Logger log = LoggingManager.getLoggerForClass();
   private Rectangle legendRect;
   private Rectangle xAxisRect;
   private Rectangle yAxisRect;
   private Rectangle chartRect;
   private static final Rectangle zeroRect = new Rectangle();
   private AbstractMap<String, AbstractGraphRow> rows;
   private double maxYVal;
   private long maxXVal;
   private long minXVal;
   private long currentXVal;
   private static final int gridLinesCount = 10;
   private NumberRenderer yAxisLabelRenderer;
   private NumberRenderer xAxisLabelRenderer;
   private boolean drawStartFinalZeroingLines = false;
   private boolean drawCurrentX = false;
   private int forcedMinX = -1;

   private Stroke dashStroke = new BasicStroke(
		   1.0f,   				// Width
           BasicStroke.CAP_SQUARE,    			// End cap
           BasicStroke.JOIN_MITER,    			// Join style
           10.0f,                     			// Miter limit
           new float[] {1.0f,4.0f}, 			// Dash pattern
           0.0f);                     			// Dash phase

   /**
    * Creates new chart object with default parameters
    */
   public GraphPanelChart()
   {
      setBackground(Color.white);
      setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.lightGray, Color.darkGray));

      yAxisLabelRenderer = new NumberRenderer("#.#");
      xAxisLabelRenderer = new NumberRenderer("#.#");
      legendRect = new Rectangle();
      yAxisRect = new Rectangle();
      xAxisRect = new Rectangle();
      chartRect = new Rectangle();

      setDefaultDimensions();

      registerPopup();
   }

   private void getMinMaxDataValues()
   {
      maxXVal = 0;
      maxYVal = 0;
      minXVal = Long.MAX_VALUE;

      Iterator<Entry<String, AbstractGraphRow>> it = rows.entrySet().iterator();
      Entry<String, AbstractGraphRow> row;
      AbstractGraphRow rowValue;
      while (it.hasNext())
      {
         row = it.next();
         rowValue = row.getValue();

         if (!rowValue.isDrawOnChart())
         {
            continue;
         }

         if (rowValue.getMaxY() > maxYVal)
         {
            maxYVal = rowValue.getMaxY();
         }

         if (rowValue.getMaxX() > maxXVal)
         {
            maxXVal = rowValue.getMaxX();
         }

         if (rowValue.getMinX() < minXVal)
         {
            minXVal = rowValue.getMinX();
         }
      }

      //maxYVal *= 1 + (double) 1 / (double) gridLinesCount;

      if (forcedMinX >= 0)
      {
         minXVal = forcedMinX;
      }
   }

   private void setDefaultDimensions()
   {
      chartRect.setBounds(spacing, spacing, getWidth() - spacing * 2, getHeight() - spacing * 2);
      legendRect.setBounds(zeroRect);
      xAxisRect.setBounds(zeroRect);
      yAxisRect.setBounds(zeroRect);
   }

   private void calculateYAxisDimensions(FontMetrics fm)
   {
      // TODO: middle value labels often wider than max
      yAxisLabelRenderer.setValue(maxYVal);
      int axisWidth = fm.stringWidth(yAxisLabelRenderer.getText()) + spacing * 3;
      yAxisRect.setBounds(chartRect.x, chartRect.y, axisWidth, chartRect.height);
      chartRect.setBounds(chartRect.x + axisWidth, chartRect.y, chartRect.width - axisWidth, chartRect.height);
   }

   private void calculateXAxisDimensions(FontMetrics fm)
   {
      // FIXME: first value on X axis may take negative X coord,
      // we need to handle this and make Y axis wider
      int axisHeight = fm.getHeight() + spacing;
      xAxisLabelRenderer.setValue(maxXVal);
      int axisEndSpace = fm.stringWidth(xAxisLabelRenderer.getText()) / 2;
      xAxisRect.setBounds(chartRect.x, chartRect.y + chartRect.height - axisHeight, chartRect.width, axisHeight);
      chartRect.setBounds(chartRect.x, chartRect.y, chartRect.width - axisEndSpace, chartRect.height - axisHeight);
      yAxisRect.setBounds(yAxisRect.x, yAxisRect.y, yAxisRect.width, chartRect.height);
   }

   @Override
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);

      BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2d = image.createGraphics();
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      drawPanel(g2d);
      g.drawImage(image, 0, 0, this);
   }

   private void drawPanel(Graphics2D g)
   {
      g.setColor(Color.white);
      g.fillRect(0, 0, getWidth(), getHeight());
      paintAd(g);

      if (rows.isEmpty())
      {
         g.setColor(Color.BLACK);
         g.drawString(NO_SAMPLES,
               getWidth() / 2 - g.getFontMetrics(g.getFont()).stringWidth(NO_SAMPLES) / 2,
               getHeight() / 2);
         return;
      }

      setDefaultDimensions();
      getMinMaxDataValues();

      try
      {
         paintLegend(g);
         calculateYAxisDimensions(g.getFontMetrics(g.getFont()));
         calculateXAxisDimensions(g.getFontMetrics(g.getFont()));
         paintYAxis(g);
         paintXAxis(g);
         paintChart(g);
      }
      catch (Exception e)
      {
         log.error("Error in paintComponent", e);
      }
   }

   private void paintLegend(Graphics g)
   {
      FontMetrics fm = g.getFontMetrics(g.getFont());
      int rectH = fm.getHeight();
      int rectW = rectH;

      Iterator<Entry<String, AbstractGraphRow>> it = rows.entrySet().iterator();
      Entry<String, AbstractGraphRow> row;
      int currentX = chartRect.x;
      int currentY = chartRect.y;
      int legendHeight = it.hasNext() ? rectH + spacing : 0;
      while (it.hasNext())
      {
         row = it.next();

         if (!row.getValue().isShowInLegend() || !row.getValue().isDrawOnChart())
         {
            continue;
         }

         // wrap row if overflowed
         if (currentX + rectW + spacing / 2 + fm.stringWidth(row.getKey()) > getWidth())
         {
            currentY += rectH + spacing / 2;
            legendHeight += rectH + spacing / 2;
            currentX = chartRect.x;
         }

         // draw legend color box
         g.setColor(row.getValue().getColor());
         g.fillRect(currentX, currentY, rectW, rectH);
         g.setColor(Color.black);
         g.drawRect(currentX, currentY, rectW, rectH);

         // draw legend item label
         currentX += rectW + spacing / 2;
         g.drawString(row.getKey(), currentX, (int) (currentY + rectH * 0.9));
         currentX += fm.stringWidth(row.getKey()) + spacing;
      }

      legendRect.setBounds(chartRect.x, chartRect.y, chartRect.width, legendHeight);
      chartRect.setBounds(chartRect.x, chartRect.y + legendHeight + spacing, chartRect.width, chartRect.height - legendHeight - spacing);
   }

   private void paintYAxis(Graphics g)
   {
      FontMetrics fm = g.getFontMetrics(g.getFont());

      String valueLabel;
      int labelXPos;
      int gridLineY;

      // shift 2nd and more lines
      int shift = 0;
      // for strokes swapping
      Stroke oldStroke = ((Graphics2D) g).getStroke();

      //draw markers
      g.setColor(Color.lightGray);
      for (int n = 0; n <= gridLinesCount; n++)
      {
    	  gridLineY = chartRect.y + (int) ((gridLinesCount - n) * (double) chartRect.height / gridLinesCount);
    	  g.drawLine(chartRect.x - 3, gridLineY, chartRect.x + 3, gridLineY);
      }

      for (int n = 0; n <= gridLinesCount; n++)
      {
         //draw 2nd and more axis dashed and shifted
         if(n!=0) {
    		 ((Graphics2D) g).setStroke(dashStroke);
    		 shift = 7;
    	 }

         gridLineY = chartRect.y + (int) ((gridLinesCount - n) * (double) chartRect.height / gridLinesCount);

         // draw grid line with tick
         g.setColor(Color.lightGray);
         g.drawLine(chartRect.x + shift, gridLineY, chartRect.x + chartRect.width, gridLineY);
         g.setColor(Color.black);

         // draw label
         yAxisLabelRenderer.setValue(n * maxYVal / gridLinesCount);
         valueLabel = yAxisLabelRenderer.getText();
         labelXPos = yAxisRect.x + yAxisRect.width - fm.stringWidth(valueLabel) - spacing - spacing / 2;
         g.drawString(valueLabel, labelXPos, gridLineY + fm.getAscent() / 2);
      }
      //restore stroke
      ((Graphics2D) g).setStroke(oldStroke);
   }

   private void paintXAxis(Graphics g)
   {
      FontMetrics fm = g.getFontMetrics(g.getFont());

      String valueLabel;
      int labelXPos;
      int gridLineX;

      // shift 2nd and more lines
      int shift = 0;
      // for strokes swapping
      Stroke oldStroke = ((Graphics2D) g).getStroke();

      g.setColor(Color.lightGray);
      //draw markers
      for (int n = 0; n <= gridLinesCount; n++)
      {
    	  gridLineX = chartRect.x + (int) (n * ((double) chartRect.width / gridLinesCount));
    	  g.drawLine(gridLineX, chartRect.y + chartRect.height - 3, gridLineX, chartRect.y + chartRect.height + 3);
      }

      for (int n = 0; n <= gridLinesCount; n++)
      {
         //draw 2nd and more axis dashed and shifted
         if(n!=0) {
    		 ((Graphics2D) g).setStroke(dashStroke);
    		 shift = 7;
    	 }

         gridLineX = chartRect.x + (int) (n * ((double) chartRect.width / gridLinesCount));

         // draw grid line with tick
         g.setColor(Color.lightGray);
         g.drawLine(gridLineX, chartRect.y + chartRect.height - shift, gridLineX, chartRect.y);
         g.setColor(Color.black);

         // draw label
         xAxisLabelRenderer.setValue(minXVal + n * (maxXVal - minXVal) / gridLinesCount);
         valueLabel = xAxisLabelRenderer.getText();
         labelXPos = gridLineX - fm.stringWidth(valueLabel) / 2;
         g.drawString(valueLabel, labelXPos, xAxisRect.y + fm.getAscent() + spacing);
      }

      //restore stroke
      ((Graphics2D) g).setStroke(oldStroke);

      if (drawCurrentX)
      {
         gridLineX = chartRect.x + (int) ((currentXVal - minXVal) * (double) chartRect.width / (maxXVal - minXVal));
         g.setColor(Color.GRAY);
         g.drawLine(gridLineX, chartRect.y, gridLineX, chartRect.y + chartRect.height);
         g.setColor(Color.black);
      }
   }

   private void paintChart(Graphics g)
   {
      g.setColor(Color.yellow);
      Iterator<Entry<String, AbstractGraphRow>> it = rows.entrySet().iterator();
      while (it.hasNext())
      {
         Entry<String, AbstractGraphRow> row = it.next();
         if (row.getValue().isDrawOnChart())
         {
            paintRow(g, row.getValue());
         }
      }
   }

   private void paintRow(Graphics g, AbstractGraphRow row)
   {
      FontMetrics fm = g.getFontMetrics(g.getFont());
      Iterator<Entry<Long, AbstractGraphPanelChartElement>> it = row.iterator();
      Entry<Long, AbstractGraphPanelChartElement> element;
      int radius = row.getMarkerSize();
      int x, y;
      int prevX = drawStartFinalZeroingLines ? chartRect.x : -1;
      int prevY = chartRect.y + chartRect.height;
      final double dxForDVal = (maxXVal <= minXVal) ? 0 : (double) chartRect.width / (maxXVal - minXVal);
      final double dyForDVal = maxYVal <= 0 ? 0 : (double) chartRect.height / (maxYVal);
      while (it.hasNext())
      {
         element = it.next();

         if (!row.isDrawOnChart())
         {
            continue;
         }

         x = chartRect.x + (int) ((element.getKey() - minXVal) * dxForDVal);
         AbstractGraphPanelChartElement elementValue = (AbstractGraphPanelChartElement) element.getValue();
         y = chartRect.y + chartRect.height - (int) (elementValue.getValue() * dyForDVal);

         // draw lines
         if (row.isDrawLine())
         {
            if (prevX > 0)
            {
               g.setColor(row.getColor());
               g.drawLine(prevX, prevY, x, y);
            }
            prevX = x;
            prevY = y;
         }

         if (row.isDrawValueLabel())
         {
            g.setColor(Color.DARK_GRAY);
            yAxisLabelRenderer.setValue(elementValue.getValue());
            g.drawString(yAxisLabelRenderer.getText(),
                  x + row.getMarkerSize() + spacing,
                  y + fm.getAscent() / 2);
         }

         // draw markers
         if (radius != AbstractGraphRow.MARKER_SIZE_NONE)
         {
            g.setColor(row.getColor());
            g.fillOval(x - radius, y - radius, (radius) * 2, (radius) * 2);
            //g.setColor(Color.black);
            //g.drawOval(x - radius, y - radius, radius * 2, radius * 2);
         }
      }

      // draw final lines
      if (row.isDrawLine() && drawStartFinalZeroingLines)
      {
         g.setColor(row.getColor());
         g.drawLine(prevX, prevY, (int) (prevX + dxForDVal), chartRect.y + chartRect.height);
      }
   }

   /**
    *
    * @param aRows
    */
   public void setRows(AbstractMap<String, AbstractGraphRow> aRows)
   {
      rows = aRows;
   }

   /**
    * @param yAxisLabelRenderer the yAxisLabelRenderer to set
    */
   public void setyAxisLabelRenderer(NumberRenderer yAxisLabelRenderer)
   {
      this.yAxisLabelRenderer = yAxisLabelRenderer;
   }

   /**
    * @param xAxisLabelRenderer the xAxisLabelRenderer to set
    */
   public void setxAxisLabelRenderer(NumberRenderer xAxisLabelRenderer)
   {
      this.xAxisLabelRenderer = xAxisLabelRenderer;
   }

   /**
    * @param drawFinalZeroingLines the drawFinalZeroingLines to set
    */
   public void setDrawFinalZeroingLines(boolean drawFinalZeroingLines)
   {
      this.drawStartFinalZeroingLines = drawFinalZeroingLines;
   }

   /**
    * @param drawCurrentX the drawCurrentX to set
    */
   public void setDrawCurrentX(boolean drawCurrentX)
   {
      this.drawCurrentX = drawCurrentX;
   }

   /**
    * @param currentX the currentX to set
    */
   public void setCurrentX(long currentX)
   {
      this.currentXVal = currentX;
   }

   /**
    *
    * @param i
    */
   public void setForcedMinX(int i)
   {
      forcedMinX = i;
   }

   private void paintAd(Graphics g)
   {
      Font tmp = g.getFont();
      g.setFont(g.getFont().deriveFont(10F));
      g.setColor(new Color(0x00DDDDDD));
      g.drawString(AD_TEXT,
            getWidth() - g.getFontMetrics().stringWidth(AD_TEXT) - spacing,
            g.getFontMetrics().getHeight() - spacing + 1);
      g.setFont(tmp);
   }

   // Adding a popup menu to copy image in clipboard
   @Override
   public void lostOwnership(Clipboard clipboard, Transferable contents)
   {
      // do nothing
   }

   private Image getImage()
   {
      BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2 = image.createGraphics();
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      this.drawPanel(g2);

      return image;
   }

   /**
    * Thanks to stephane.hoblingre
    */
   private void registerPopup()
   {
      JPopupMenu popup = new JPopupMenu();
      this.setComponentPopupMenu(popup);
      JMenuItem item = new JMenuItem("Copy Image to Clipboard");

      item.addActionListener(new CopyAction());
      popup.add(item);
   }

   private class CopyAction
         implements ActionListener
   {
      public void actionPerformed(final ActionEvent e)
      {
         Clipboard clipboard = getToolkit().getSystemClipboard();
         Transferable transferable = new Transferable()
         {
            public Object getTransferData(DataFlavor flavor)
            {
               if (isDataFlavorSupported(flavor))
               {
                  return getImage();
               }
               return null;
            }

            public DataFlavor[] getTransferDataFlavors()
            {
               return new DataFlavor[]
                     {
                        DataFlavor.imageFlavor
                     };
            }

            public boolean isDataFlavorSupported(DataFlavor flavor)
            {
               return DataFlavor.imageFlavor.equals(flavor);
            }
         };
         clipboard.setContents(transferable, GraphPanelChart.this);
      }
   }
}
