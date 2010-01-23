package kg.apc.jmeter.vizualizers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
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

   public GraphPanelChart()
   {
      setBackground(Color.white);
      setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.lightGray, Color.darkGray));

      legendRect = new Rectangle();
      yAxisRect = new Rectangle();
      xAxisRect = new Rectangle();
      chartRect = new Rectangle();

      calculateDimensions();
   }

   private void calculateDimensions()
   {
      setDefaultDimensions();
      calculateLegendDimensions();
      calculateYAxisDimensions();
      calculateXAxisDimensions();
   }

   private void setDefaultDimensions()
   {
      chartRect.setBounds(spacing, spacing, getWidth() - spacing * 2, getHeight() - spacing * 2);
      legendRect.setBounds(zeroRect);
      xAxisRect.setBounds(zeroRect);
      yAxisRect.setBounds(zeroRect);
   }

   private void calculateLegendDimensions()
   {
      final int legendHeight = 20;
      legendRect.setBounds(chartRect.x, chartRect.y, chartRect.width, legendHeight);
      chartRect.setBounds(chartRect.x, chartRect.y + legendHeight+spacing, chartRect.width, chartRect.height - legendHeight-spacing);
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

      calculateDimensions();

      try
      {
         paintLegend(g);
         paintXAxis(g);
         paintYAxis(g);
         paintChart(g);
      }
      catch (Exception e)
      {
         log.error("Error in paintComponent", e);
      }
   }

   private void paintLegend(Graphics g)
   {
      g.setColor(Color.red);
      g.fillRect(legendRect.x, legendRect.y, legendRect.width, legendRect.height);
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
}
