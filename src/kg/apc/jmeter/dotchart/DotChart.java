package kg.apc.jmeter.dotchart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import java.util.Iterator;
import javax.swing.JComponent;
import javax.swing.Scrollable;

import org.apache.jmeter.samplers.Clearable;

public class DotChart
     extends JComponent
     implements Scrollable, Clearable
{
   private DotChartModel model;
   private final int xborder = 30;

   /**
    * Constructor for the Graph object.
    */
   public DotChart()
   {
      setSize(100, 100);
      init();
   }

   /**
    * Constructor for the Graph object.
    */
   public DotChart(DotChartModel model)
   {
      this();
      setModel(model);
   }

   private int getChartHeight(int offsetY)
   {
      return getHeight() - offsetY - xborder * 2;
   }

   private int getChartWidth()
   {
      return getWidth() - xborder - xborder;
   }

   private void init()
   {// called from ctor, so must not be overridable
      repaint();
   }

   /**
    * Gets the ScrollableTracksViewportWidth attribute of the Graph object.
    *
    * @return the ScrollableTracksViewportWidth value
    */
   public boolean getScrollableTracksViewportWidth()
   {
      return true;
   }

   /**
    * Gets the ScrollableTracksViewportHeight attribute of the Graph object.
    *
    * @return the ScrollableTracksViewportHeight value
    */
   public boolean getScrollableTracksViewportHeight()
   {
      return true;
   }

   /**
    * Sets the Model attribute of the Graph object.
    */
   private void setModel(Object model)
   {
      this.model = (DotChartModel) model;
      repaint();
   }

   /**
    * Gets the PreferredScrollableViewportSize attribute of the Graph object.
    *
    * @return the PreferredScrollableViewportSize value
    */
   public Dimension getPreferredScrollableViewportSize()
   {
      return this.getPreferredSize();
   }

   /**
    * Gets the ScrollableUnitIncrement attribute of the Graph object.
    *
    * @return the ScrollableUnitIncrement value
    */
   public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction)
   {
      return 5;
   }

   /**
    * Gets the ScrollableBlockIncrement attribute of the Graph object.
    *
    * @return the ScrollableBlockIncrement value
    */
   public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction)
   {
      return (int) (visibleRect.width * .9);
   }

   /**
    * Clears this graph.
    */
   public void clearData()
   {
      model.clear();
   }

   /**
    * Method is responsible for calling drawSample and updating the graph.
    */
   @Override
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      final DotChartModel m = this.model;
      synchronized (m)
      {
         int offsetY = drawLegend(m, g);
         drawGraphLayout(m, g, offsetY);
         drawRows(m, g, offsetY);
      }
   }

   private void drawRows(DotChartModel p_model, Graphics g, int offsetY)
   {
      SamplingStatCalculatorColored row;
      for (Iterator it = p_model.values().iterator(); it.hasNext();)
      {
         row = (SamplingStatCalculatorColored) it.next();
         drawSamples(row, g, offsetY);
      }

      for (Iterator it = p_model.values().iterator(); it.hasNext();)
      {
         row = (SamplingStatCalculatorColored) it.next();
         drawAverage(row, g, offsetY);
      }
   }

   private void drawSamples(SamplingStatCalculatorColored row, Graphics g, int offsetY)
   {
      if (row.getCount() < 1)
         return;

      int sampleCount = row.getCount();
      int maxThreads = row.getMaxThreads();
      long maxTime = row.getMaxTime();
      if (maxThreads < 1 || maxTime < 1)
         return;

      int x1 = xborder;
      int y1 = offsetY + xborder;
      int x2 = x1 + getChartWidth();
      int y2 = y1 + getChartHeight(offsetY);

      double dotW = (double) getChartWidth() / (double) (maxThreads + 1);
      double dotH = (double) getChartHeight(offsetY) / (double) (maxTime + 1);

      LeanSampleResult res;
      for (int sampleNo = 0; sampleNo < sampleCount; sampleNo++)
      {
         res = row.getSample(sampleNo);

         int x = x1 + (int) (dotW * (double) res.getThreads());
         int y = y2 - (int) (dotH * (double) res.getTime());

         g.setColor(row.getColor());
         g.fillRect(x, y, (int) dotW < 1 ? 1 : (int) dotW, (int) dotH < 1 ? 1 : (int) dotH);
      }
   }

   private void drawAverage(SamplingStatCalculatorColored row, Graphics g, int offsetY)
   {
      if (row.getCount() < 1)
         return;

      int maxThreads = row.getMaxThreads();
      long maxTime = row.getMaxTime();
      if (maxThreads < 1 || maxTime < 1)
         return;

      int x1 = xborder;
      int y1 = offsetY + xborder;
      int x2 = x1 + getChartWidth();
      int y2 = y1 + getChartHeight(offsetY);

      final int radius = 3;
      double dotW = (double) getChartWidth() / (double) (maxThreads + 1);
      double dotH = (double) getChartHeight(offsetY) / (double) (maxTime + 1);

      int x = x1 + (int) (dotW * (row.getAvgThreads() + 0.5));
      double avgTime = row.getAvgTime();
      int y = y2 - (int) (dotH * avgTime);

      g.setColor(row.getColor());
      g.fillOval(x - radius, y - radius, (radius) * 2, (radius) * 2);
      g.setColor(Color.black);
      g.drawOval(x - radius, y - radius, radius * 2, radius * 2);

      g.drawString(Long.toString(Math.round(avgTime)), x + radius + radius, y + radius + (int) dotH);
   }

   private int drawLegend(DotChartModel model, Graphics g)
   {
      int offsetX = 1, offsetY = 1;
      int currentX = offsetX, currentY = offsetY;

      FontMetrics fm = g.getFontMetrics(g.getFont());
      int rectH = fm.getHeight();
      int rectW = rectH;

      SamplingStatCalculatorColored row;
      for (Iterator it = model.values().iterator(); it.hasNext();)
      {
         row = (SamplingStatCalculatorColored) it.next();

         g.setColor(row.getColor());
         g.fillRect(currentX, currentY, rectW, rectH);
         g.setColor(Color.black);
         g.drawRect(currentX, currentY, rectW, rectH);

         currentX += rectW + rectW / 5;
         g.drawString(row.getLabel(), currentX, (int) (currentY + rectH * 0.9));
         currentX += fm.stringWidth(row.getLabel()) + rectW / 2;

         if (currentX + rectW * 3 >= getWidth())
         {
            currentY += rectH * 1.2;
            currentX = offsetX;
         }
      }

      return currentY + xborder / 2;
   }

   private void drawGraphLayout(DotChartModel model, Graphics g, int offsetY)
   {
      // get max values
      SamplingStatCalculatorColored row;
      int maxThreads = 0;
      long maxTime = 0;
      for (Iterator it = model.values().iterator(); it.hasNext();)
      {
         row = (SamplingStatCalculatorColored) it.next();
         if (row.getMaxThreads() > maxThreads)
            maxThreads = row.getMaxThreads();
         if (row.getMaxTime() > maxTime)
            maxTime = row.getMaxTime();
      }

      int x1 = xborder;
      int y1 = offsetY + xborder;
      int x2 = x1 + getChartWidth();
      int y2 = y1 + getChartHeight(offsetY);

      // clear bg
      g.setColor(Color.white);
      g.fillRect(x1, y1, x2, y2);

      // lets draw the grid
      FontMetrics fm = g.getFontMetrics(g.getFont());
      int coordY;
      String yVal;
      final int gridLinesCount = 4;
      int labelPos;
      for (int y = 0; y <= gridLinesCount; y++)
      {
         coordY = y1 + (gridLinesCount - y) * getChartHeight(offsetY) / gridLinesCount;
         yVal = String.valueOf(y * maxTime / gridLinesCount);

         g.setColor(Color.lightGray);
         g.drawLine(x1, coordY, x2, coordY);
         g.setColor(Color.black);
         labelPos = x1 - fm.stringWidth(yVal) - 1;
         g.drawString(yVal, labelPos > 0 ? labelPos : 0, coordY + fm.getHeight() / 2);
      }

      int coordX;
      String xVal;
      for (int x = 0; x <= gridLinesCount; x++)
      {
         coordX = x1 + x * getChartWidth() / gridLinesCount;
         xVal = String.valueOf(x * maxThreads / gridLinesCount);

         g.setColor(Color.lightGray);
         g.drawLine(coordX, y1, coordX, y2);
         g.setColor(Color.black);
         g.drawString(xVal, coordX - fm.stringWidth(xVal) / 2, y2 + fm.getHeight() + 1);
      }

      // bounds
      g.setColor(Color.black);
      g.drawLine(x1, y1, x1, y2);
      g.drawLine(x1, y1, x2, y1);
      g.drawLine(x2, y1, x2, y2);
      g.drawLine(x1, y2, x2, y2);
   }
}
