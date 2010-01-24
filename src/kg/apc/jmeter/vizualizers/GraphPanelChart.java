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
import org.apache.jmeter.samplers.Clearable;
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
   private double maxXVal;
   private double minXVal;
   private static final int gridLinesCount = 10;
   private NumberRenderer renderer;

   public GraphPanelChart()
   {
      setBackground(Color.white);
      setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.lightGray, Color.darkGray));

      renderer = new NumberRenderer("#.#");
      legendRect = new Rectangle();
      yAxisRect = new Rectangle();
      xAxisRect = new Rectangle();
      chartRect = new Rectangle();

      setDefaultDimensions();
   }

   private void getMinMaxDataValues()
   {
      maxXVal = maxYVal = minXVal = 0;
      Iterator<Entry<String, GraphPanelChartRow>> it = rows.entrySet().iterator();
      Entry<String, GraphPanelChartRow> row;
      while (it.hasNext())
      {
         row = it.next();
         if (row.getValue().getMaxY() > maxYVal)
            maxYVal = row.getValue().getMaxY();
         if (row.getValue().getMaxX() > maxXVal)
            maxXVal = row.getValue().getMaxX();
         if (row.getValue().getMinX() > minXVal)
            minXVal = row.getValue().getMinX();
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
      renderer.setValue(maxYVal);
      int axisWidth = fm.stringWidth(renderer.getText()) + spacing * 3;
      yAxisRect.setBounds(chartRect.x, chartRect.y, axisWidth, chartRect.height);
      chartRect.setBounds(chartRect.x + axisWidth, chartRect.y, chartRect.width - axisWidth, chartRect.height);
   }

   private void calculateXAxisDimensions(FontMetrics fm)
   {
      int axisHeight = fm.getHeight() + spacing;
      int axisEndSpace = fm.stringWidth(Double.toString(maxXVal)) + spacing;
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

   private void paintXAxis(Graphics g)
   {
      g.setColor(Color.green);
      g.fillRect(xAxisRect.x, xAxisRect.y, xAxisRect.width, xAxisRect.height);
   }

   private void paintYAxis(Graphics g)
   {
      FontMetrics fm = g.getFontMetrics(g.getFont());

      String valueLabel;
      int labelXPos;
      int gridLineY;

      for (int y = 0; y <= gridLinesCount; y++)
      {
         gridLineY = yAxisRect.y + (gridLinesCount - y) * yAxisRect.height / gridLinesCount;

         // draw grid line with tick
         g.setColor(Color.lightGray);
         g.drawLine(chartRect.x - spacing, gridLineY, chartRect.x + chartRect.width, gridLineY);
         g.setColor(Color.black);

         // draw label
         renderer.setValue(y * maxYVal / gridLinesCount);
         valueLabel = renderer.getText();
         labelXPos = yAxisRect.x + yAxisRect.width - fm.stringWidth(valueLabel) - spacing - spacing / 2;
         g.drawString(valueLabel, labelXPos > 0 ? labelXPos : 0, gridLineY + fm.getAscent() / 2);
      }
   }

   private void paintChart(Graphics g)
   {
      g.setColor(Color.yellow);
      g.fillRect(chartRect.x, chartRect.y, chartRect.width, chartRect.height);
      Iterator<Entry<String, GraphPanelChartRow>> it = rows.entrySet().iterator();
      while (it.hasNext())
      {
         Entry<String, GraphPanelChartRow> row = it.next();
         log.info(row.getKey());
      }
   }

   public void setRows(ConcurrentHashMap<String, GraphPanelChartRow> aRows)
   {
      rows = aRows;
   }
}
