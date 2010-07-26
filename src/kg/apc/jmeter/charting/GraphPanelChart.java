package kg.apc.jmeter.charting;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
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
{
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

   /**
    *
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
            continue;

         if (rowValue.getMaxY() > maxYVal)
            maxYVal = rowValue.getMaxY();

         if (rowValue.getMaxX() > maxXVal)
            maxXVal = rowValue.getMaxX();

         if (rowValue.getMinX() < minXVal)
            minXVal = rowValue.getMinX();
      }

      //maxYVal *= 1 + (double) 1 / (double) gridLinesCount;

      if (forcedMinX >= 0)
         minXVal = forcedMinX;
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

      g.setColor(Color.white);
      g.fillRect(0, 0, getWidth(), getHeight());

      if (rows.isEmpty())
      {
         String msg = "Waiting for samples...";
         g.setColor(Color.BLACK);
         g.drawString(msg, getWidth() / 2 - g.getFontMetrics(g.getFont()).stringWidth(msg) / 2, getHeight() / 2);
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
            continue;

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

      for (int n = 0; n <= gridLinesCount; n++)
      {
         gridLineY = chartRect.y + (int) ((gridLinesCount - n) * (double) chartRect.height / gridLinesCount);

         // draw grid line with tick
         g.setColor(Color.lightGray);
         g.drawLine(chartRect.x - spacing, gridLineY, chartRect.x + chartRect.width, gridLineY);
         g.setColor(Color.black);

         // draw label
         yAxisLabelRenderer.setValue(n * maxYVal / gridLinesCount);
         valueLabel = yAxisLabelRenderer.getText();
         labelXPos = yAxisRect.x + yAxisRect.width - fm.stringWidth(valueLabel) - spacing - spacing / 2;
         g.drawString(valueLabel, labelXPos, gridLineY + fm.getAscent() / 2);
      }
   }

   private void paintXAxis(Graphics g)
   {
      FontMetrics fm = g.getFontMetrics(g.getFont());

      String valueLabel;
      int labelXPos;
      int gridLineX;

      for (int n = 0; n <= gridLinesCount; n++)
      {
         gridLineX = chartRect.x + (int) (n * ((double) chartRect.width / gridLinesCount));

         // draw grid line with tick
         g.setColor(Color.lightGray);
         g.drawLine(gridLineX, chartRect.y, gridLineX, chartRect.y + chartRect.height + spacing);
         g.setColor(Color.black);

         // draw label
         xAxisLabelRenderer.setValue(minXVal + n * (maxXVal - minXVal) / gridLinesCount);
         valueLabel = xAxisLabelRenderer.getText();
         labelXPos = gridLineX - fm.stringWidth(valueLabel) / 2;
         g.drawString(valueLabel, labelXPos, xAxisRect.y + fm.getAscent() + spacing);
      }

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
            paintRow(g, row.getValue());
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
            continue;

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
}
