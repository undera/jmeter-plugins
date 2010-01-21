package kg.apc.jmeter.dotchart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import java.util.Iterator;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.Scrollable;

import org.apache.jmeter.samplers.Clearable;
import org.apache.jorphan.gui.RateRenderer;

// TODO needs strong refactoring
public class DotChart
   extends JComponent
   implements Scrollable, Clearable
{

   public static final int DM_TIMES = 0;
   public static final int DM_THROUGHPUT = 1;
   private DotChartModel model;
   private final int xborder = 30;
   private boolean drawSamples = false;
   private boolean drawThreadAverages = true;
   private boolean drawAverages = true;
   private final int gridLinesCount = 9;
   private int offsetY;
   private int chartLeftX;
   private int chartTopY;
   private int chartRightX;
   private int chartBottomY;
   private double dotW;
   private double dotH;
   private int currentThreads;
   private int yAxisLimit = 0;
   private int displayMode;
   private final RateRenderer rateRenderer;

   public DotChart()
   {
      setSize(100, 100); // this is default, for unit tests
      displayMode = DM_TIMES;
      init();
      rateRenderer = new RateRenderer("#.0");
   }

   public DotChart(DotChartModel model)
   {
      this();
      setModel(model);
   }

   private void calculateGraphCoords(int maxThreads, double maxY)
   {
      // handle axis limit
      if (yAxisLimit > 0)
      {
         maxY = yAxisLimit;
      }

      chartLeftX = xborder;
      chartTopY = offsetY + xborder;
      chartRightX = chartLeftX + getChartWidth();
      chartBottomY = chartTopY + getChartHeight();
      dotW = (double) getChartWidth() / (double) (maxThreads + 1);
      dotH = (double) getChartHeight() / (double) (maxY + 1);
   }

   private void drawXAxis(Graphics g, int maxThreads, int x1, int y1, int y2, FontMetrics fm)
   {
      int coordX;
      String xVal;
      for (int x = 0; x <= gridLinesCount; x++)
      {
         coordX = x1 + x * getChartWidth() / gridLinesCount;
         xVal = String.valueOf(x * maxThreads / gridLinesCount);
         if (x == gridLinesCount)
         {
            xVal += " threads";
         }
         g.setColor(Color.lightGray);
         g.drawLine(coordX, y1, coordX, y2);
         g.setColor(Color.black);
         g.drawString(xVal, coordX - fm.stringWidth(xVal) / 2, y2 + fm.getHeight() + 1);
      }
   }

   private void drawYAxis(Graphics g, double maxY, int y1, int x1, int x2, FontMetrics fm)
   {
      // handle axis limit
      if (yAxisLimit > 0)
      {
         maxY = yAxisLimit;
      }

      double coordY;
      String yVal;
      int labelPos;
      for (int y = 0; y <= gridLinesCount; y++)
      {
         coordY = y1 + (gridLinesCount - y) * getChartHeight() / gridLinesCount;

         if (displayMode == DM_THROUGHPUT)
         {
            rateRenderer.setValue(y * maxY / gridLinesCount);
            yVal = rateRenderer.getText();
         }
         else
         {
            yVal = Integer.toString((int) (y * maxY / gridLinesCount));
         }

         if (y == gridLinesCount)
         {
            yVal += displayMode == DM_THROUGHPUT ? "" : " msec";
         }
         g.setColor(Color.lightGray);
         g.drawLine(x1, (int) coordY, x2, (int) coordY);
         g.setColor(Color.black);
         labelPos = x1 - fm.stringWidth(yVal) - 1;
         g.drawString(yVal, labelPos > 0 ? labelPos : 0, (int) (coordY - 1));
      }
   }

   private int getChartHeight()
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

   public boolean getScrollableTracksViewportWidth()
   {
      return true;
   }

   public boolean getScrollableTracksViewportHeight()
   {
      return true;
   }

   private void setModel(Object model)
   {
      this.model = (DotChartModel) model;
      repaint();
   }

   public Dimension getPreferredScrollableViewportSize()
   {
      return this.getPreferredSize();
   }

   public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction)
   {
      return 5;
   }

   public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction)
   {
      return (int) (visibleRect.width * .9);
   }

   public void clearData()
   {
      model.clear();
   }

   @Override
   public void paintComponent(Graphics g)
   {
      //if (getParent().getHeight() - xborder != getHeight())
      //   setSize(getWidth(), getParent().getHeight() - xborder);

      super.paintComponent(g);
      final DotChartModel m = this.model;
      synchronized (m)
      {
         drawLegend(m, g);
         drawAxis(m, g);
         drawRows(m, g);
      }
   }

   private void drawRows(DotChartModel p_model, Graphics g)
   {
      DotChartColoredRow row;
      if (isDrawSamples())
      {
         for (Iterator it = p_model.values().iterator(); it.hasNext();)
         {
            row = (DotChartColoredRow) it.next();
            drawSamples(p_model, row, g);
         }
      }

      if (isDrawThreadAverages())
      {
         for (Iterator it = p_model.values().iterator(); it.hasNext();)
         {
            row = (DotChartColoredRow) it.next();
            drawThreadAverages(p_model, row, g);
         }
      }

      if (isDrawAverages())
      {
         for (Iterator it = p_model.values().iterator(); it.hasNext();)
         {
            row = (DotChartColoredRow) it.next();
            drawAverages(p_model, row, g);
         }
      }
   }

   private void drawSamples(DotChartModel p_model, DotChartColoredRow row, Graphics g)
   {
      if (row.getCount() < 1)
      {
         return;
      }

      int maxThreads = p_model.getMaxThreads();
      double maxY = displayMode == DM_THROUGHPUT ? p_model.getMaxThroughput() : p_model.getMaxTime();
      if (maxThreads < 1 || maxY <= 0)
      {
         return;
      }

      calculateGraphCoords(maxThreads, maxY);

      DotChartSampleResult res;
      int sampleCount = row.getCount();
      for (int sampleNo = 0; sampleNo < sampleCount; sampleNo++)
      {
         res = row.getSample(sampleNo);

         int x = chartLeftX + (int) (dotW * ((double) res.getThreads() - 0.5));
         int y = chartBottomY - (int) (dotH * ((double) (displayMode == DM_THROUGHPUT ? res.getThroughput() : res.getTime()) - 0.5));

         if (y < chartTopY)
         {
            y = chartTopY;
         }

         g.setColor(row.getColor());
         g.fillRect(x, y, (int) dotW < 1 ? 1 : (int) dotW, (int) dotH < 1 ? 1 : (int) dotH);
      }
   }

   private void drawAverages(DotChartModel p_model, DotChartColoredRow row, Graphics g)
   {
      if (row.getCount() < 1)
      {
         return;
      }

      int maxThreads = p_model.getMaxThreads();
      double maxY = displayMode == DM_THROUGHPUT ? p_model.getMaxThroughput() : p_model.getMaxTime();
      if (maxThreads < 1 || maxY < 0)
      {
         return;
      }

      calculateGraphCoords(maxThreads, maxY);

      final int radius = 4;

      int x = chartLeftX + (int) (dotW * (double) row.getAvgThreads());
      double avgY = displayMode == DM_THROUGHPUT ? row.getAvgThoughput() : row.getAvgTime();
      int y = chartBottomY - (int) (dotH * avgY);

      if (y < chartTopY)
      {
         y = chartTopY;
      }

      g.setColor(row.getColor());
      g.fillOval(x - radius, y - radius, (radius) * 2, (radius) * 2);
      g.setColor(Color.black);
      g.drawOval(x - radius, y - radius, radius * 2, radius * 2);

      g.drawString(Long.toString(Math.round(avgY)), x + radius + radius, y + radius + (int) dotH);
   }

   private void drawThreadAverages(DotChartModel p_model, DotChartColoredRow row, Graphics g)
   {
      if (row.getCount() < 1)
      {
         return;
      }

      int maxThreads = p_model.getMaxThreads();
      double maxY = displayMode == DM_THROUGHPUT ? p_model.getMaxThroughput() : p_model.getMaxTime();
      if (maxThreads < 1 || maxY <= 0)
      {
         return;
      }

      calculateGraphCoords(maxThreads, maxY);

      final int radius = 2;

      Vector averagesByThreads = row.getAveragesByThreads();
      //int prevX = 0, prevY = 0;
      for (int threads = 0; threads < averagesByThreads.size(); threads++)
      {
         if (averagesByThreads.elementAt(threads) == null)
         {
            continue;
         }

         double avgY;
         if (displayMode == DM_THROUGHPUT)
         {
            avgY = ((DotChartAverageValues) averagesByThreads.get(threads)).getAvgThroughput();
         }
         else
         {
            avgY = ((DotChartAverageValues) averagesByThreads.get(threads)).getAvgTime();
         }

         int x = chartLeftX + (int) (dotW * (double) threads);

         int y = chartBottomY - (int) (dotH * avgY);

         if (y < chartTopY)
         {
            y = chartTopY;
         }

         g.setColor(row.getColor());
         //if (prevX > 0)
         //   g.drawLine(prevX, prevY, x, y);

         g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
         g.setColor(Color.black);
         g.drawOval(x - radius, y - radius, radius * 2, radius * 2);

         //prevX = x;
         //prevY = y;
         //g.drawString(Long.toString(Math.round(avgTime)), x + radius + radius, y + radius + (int) dotH);
      }
   }

   private void drawLegend(DotChartModel model, Graphics g)
   {
      int offsetX = 1;
      offsetY = 1;
      int currentX = offsetX, currentY = offsetY;

      FontMetrics fm = g.getFontMetrics(g.getFont());
      int rectH = fm.getHeight();
      int rectW = rectH;

      DotChartColoredRow row;
      for (Iterator it = model.values().iterator(); it.hasNext();)
      {
         row = (DotChartColoredRow) it.next();

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

      offsetY = currentY + xborder / 2;
   }

   private void drawAxis(DotChartModel p_model, Graphics g)
   {
      // get max values
      int maxThreads = p_model.getMaxThreads();
      double maxY = displayMode == DM_THROUGHPUT ? p_model.getMaxThroughput() : p_model.getMaxTime();

      calculateGraphCoords(maxThreads, maxY);

      // clear bg
      g.setColor(Color.white);
      g.fillRect(chartLeftX, chartTopY, chartRightX, chartBottomY);

      // lets draw the grid
      Font f = g.getFont().deriveFont((float) (g.getFont().getSize() * 0.75));
      FontMetrics fm = g.getFontMetrics(f);

      drawYAxis(g, maxY, chartTopY, chartLeftX, chartRightX, fm);

      drawXAxis(g, maxThreads, chartLeftX, chartTopY, chartBottomY, fm);

      // current threads line
      g.setColor(Color.GRAY);
      int coordX = chartLeftX + (int) ((double) currentThreads * dotW);
      g.drawLine(coordX, chartTopY, coordX, chartBottomY);

      // bounds
      g.setColor(Color.black);
      g.drawLine(chartLeftX, chartTopY, chartLeftX, chartBottomY);
      g.drawLine(chartLeftX, chartTopY, chartRightX, chartTopY);
      g.drawLine(chartRightX, chartTopY, chartRightX, chartBottomY);
      g.drawLine(chartLeftX, chartBottomY, chartRightX, chartBottomY);
   }

   public void setDrawSamples(boolean b)
   {
      drawSamples = b;
      repaint();
   }

   /**
    * @return the drawSamples
    */
   public boolean isDrawSamples()
   {
      return drawSamples;
   }

   /**
    * @return the drawThreadAverages
    */
   public boolean isDrawThreadAverages()
   {
      return drawThreadAverages;
   }

   /**
    * @param drawThreadAverages the drawThreadAverages to set
    */
   public void setDrawThreadAverages(boolean drawThreadAverages)
   {
      this.drawThreadAverages = drawThreadAverages;
      repaint();
   }

   /**
    * @return the drawAverages
    */
   public boolean isDrawAverages()
   {
      return drawAverages;
   }

   /**
    * @param drawAverages the drawAverages to set
    */
   public void setDrawAverages(boolean drawAverages)
   {
      this.drawAverages = drawAverages;
      repaint();
   }

   public void setCurrentThreads(int allThreads)
   {
      currentThreads = allThreads;
   }

   public void setYAxisLimit(int i)
   {
      yAxisLimit = i;
   }

   void setDisplayMode(int mode)
   {
      displayMode = mode;
   }
}
