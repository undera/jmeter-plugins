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
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class GraphPanelChart
     extends JComponent //implements Scrollable
{
   private static final int spacing = 5;
   private static final Logger log = LoggingManager.getLoggerForClass();
   private Rectangle legendRect;
   private Rectangle xAxisRect;
   private Rectangle yAxisRect;
   private Rectangle chartRect;
   private static final Rectangle zeroRect = new Rectangle();
   private ConcurrentHashMap<String, GraphPanelChartRow> rows;

   public GraphPanelChart()
   {
      setBackground(Color.white);
      setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.lightGray, Color.darkGray));

      legendRect = new Rectangle();
      yAxisRect = new Rectangle();
      xAxisRect = new Rectangle();
      chartRect = new Rectangle();

      setDefaultDimensions();
   }

   private void setDefaultDimensions()
   {
      chartRect.setBounds(spacing, spacing, getWidth() - spacing * 2, getHeight() - spacing * 2);
      legendRect.setBounds(zeroRect);
      xAxisRect.setBounds(zeroRect);
      yAxisRect.setBounds(zeroRect);
   }

   private void calculateYAxisDimensions()
   {
      int axisWidth = 20;
      yAxisRect.setBounds(chartRect.x, chartRect.y, axisWidth, chartRect.height);
      chartRect.setBounds(chartRect.x + axisWidth, chartRect.y, chartRect.width - axisWidth, chartRect.height);
   }

   private void calculateXAxisDimensions()
   {
      int axisHeight = 20;
      int axisEndSpace = 20;
      xAxisRect.setBounds(chartRect.x, chartRect.y + chartRect.height - axisHeight, chartRect.width, axisHeight);
      chartRect.setBounds(chartRect.x, chartRect.y, chartRect.width - axisEndSpace, chartRect.height - axisHeight);
   }

   @Override
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);

      g.setColor(Color.white);
      g.fillRect(0, 0, getWidth(), getHeight());

      setDefaultDimensions();

      try
      {
         paintLegend(g);
         calculateYAxisDimensions();
         paintYAxis(g);
         calculateXAxisDimensions();
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
      chartRect.setBounds(chartRect.x, chartRect.y + legendHeight, chartRect.width, chartRect.height - legendHeight - spacing);
   }

   private void paintXAxis(Graphics g)
   {
      g.setColor(Color.green);
      g.fillRect(xAxisRect.x, xAxisRect.y, xAxisRect.width, xAxisRect.height);
   }

   private void paintYAxis(Graphics g)
   {
      g.setColor(Color.blue);
      g.fillRect(yAxisRect.x, yAxisRect.y, yAxisRect.width, yAxisRect.height);
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

   private void logRect(String prefix, Rectangle r)
   {
      log.info(prefix + ": "
           + Integer.toString(r.x) + " "
           + Integer.toString(r.y) + " "
           + Integer.toString(r.width) + " "
           + Integer.toString(r.height) + " "
           + " ");
   }

   public void setRows(ConcurrentHashMap<String, GraphPanelChartRow> aRows)
   {
      rows = aRows;
   }
}
