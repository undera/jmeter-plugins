package kg.apc.jmeter.vizualizers;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.BevelBorder;
import org.apache.jorphan.gui.NumberRenderer;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

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
   private ConcurrentHashMap<String, GraphPanelChartRow> rows;
   private double maxYVal;
   private long maxXVal;
   private long minXVal;
   private static final int gridLinesCount = 5;
   private NumberRenderer yAxisLabelRenderer;
   private NumberRenderer xAxisLabelRenderer;

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

      Iterator<Entry<String, GraphPanelChartRow>> it = rows.entrySet().iterator();
      Entry<String, GraphPanelChartRow> row;
      GraphPanelChartRow rowValue;
      while (it.hasNext())
      {
         row = it.next();
         rowValue = row.getValue();

         if (rowValue.getMaxY() > maxYVal)
            maxYVal = rowValue.getMaxY();

         if (rowValue.getMaxX() > maxXVal)
            maxXVal = rowValue.getMaxX();

         if (rowValue.getMinX() < minXVal)
            minXVal = rowValue.getMinX();
      }

      /*
      xAxisLabelRenderer.setValue(minXVal);
      log.info("Min X: " + xAxisLabelRenderer.getText() + " " + Double.toString(minXVal));
      xAxisLabelRenderer.setValue(maxXVal);
      log.info("Max X: " + xAxisLabelRenderer.getText() + " " + Double.toString(maxXVal));
       * 
       */
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
      // TODO: first value on X axis may take negative X coord, 
      // we need to handle this and make Y axis wider
      int axisHeight = fm.getHeight() + spacing;
      xAxisLabelRenderer.setValue(maxXVal);
      int axisEndSpace = fm.stringWidth(xAxisLabelRenderer.getText()) / 2 + spacing;
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

      Iterator<Entry<String, GraphPanelChartRow>> it = rows.entrySet().iterator();
      Entry<String, GraphPanelChartRow> row;
      int currentX = chartRect.x;
      int currentY = chartRect.y;
      int legendHeight = it.hasNext() ? rectH + spacing : 0;
      while (it.hasNext())
      {
         row = it.next();

         // wrap row if overflowed
         if (currentX + rectW + spacing / 2 + fm.stringWidth(row.getKey()) > getWidth())
         {
            currentY += rectH + spacing / 2;
            legendHeight += rectH + spacing;
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
         gridLineY = chartRect.y + (gridLinesCount - n) * chartRect.height / gridLinesCount;

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
      //log.debug("Painting X axis");
      FontMetrics fm = g.getFontMetrics(g.getFont());

      String valueLabel;
      int labelXPos;
      int gridLineX;

      for (int n = 0; n <= gridLinesCount; n++)
      {
         gridLineX = chartRect.x + n * (chartRect.width / gridLinesCount);

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
      //log.debug("Painting X axis finished");
   }

   private void paintChart(Graphics g)
   {
      g.setColor(Color.yellow);
      //g.fillRect(chartRect.x, chartRect.y, chartRect.width, chartRect.height);
      Iterator<Entry<String, GraphPanelChartRow>> it = rows.entrySet().iterator();
      while (it.hasNext())
      {
         Entry<String, GraphPanelChartRow> row = it.next();
         //log.info(row.getKey());
      }
   }

   public void setRows(ConcurrentHashMap<String, GraphPanelChartRow> aRows)
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
}
