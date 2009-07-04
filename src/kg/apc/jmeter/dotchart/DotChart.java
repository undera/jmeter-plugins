package kg.apc.jmeter.dotchart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import java.util.Iterator;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.Scrollable;

import org.apache.jmeter.samplers.Clearable;

public class DotChart
     extends JComponent
     implements Scrollable, Clearable
{
   private DotChartModel model;
   private final int xborder = 30;

   public DotChart()
   {
      setSize(100, 100); // this is for unit tests
      init();
   }

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
      if (getParent().getHeight() - xborder != getHeight())
         setSize(getWidth(), getParent().getHeight() - xborder);
      
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
      DotChartColoredRow row;
      for (Iterator it = p_model.values().iterator(); it.hasNext();)
      {
         row = (DotChartColoredRow) it.next();
         drawSamples(p_model, row, g, offsetY);
      }

      for (Iterator it = p_model.values().iterator(); it.hasNext();)
      {
         row = (DotChartColoredRow) it.next();
         drawThreadsAverage(p_model, row, g, offsetY);
      }

      for (Iterator it = p_model.values().iterator(); it.hasNext();)
      {
         row = (DotChartColoredRow) it.next();
         drawAverage(p_model, row, g, offsetY);
      }
   }

   private void drawSamples(DotChartModel p_model, DotChartColoredRow row, Graphics g, int offsetY)
   {
      if (row.getCount() < 1)
         return;

      int maxThreads = p_model.getMaxThreads();
      long maxTime = p_model.getMaxTime();
      if (maxThreads < 1 || maxTime < 1)
         return;

      int x1 = xborder;
      int y1 = offsetY + xborder;
      int x2 = x1 + getChartWidth();
      int y2 = y1 + getChartHeight(offsetY);

      double dotW = (double) getChartWidth() / (double) (maxThreads + 1);
      double dotH = (double) getChartHeight(offsetY) / (double) (maxTime + 1);

      DotChartSampleResult res;
      int sampleCount = row.getCount();
      for (int sampleNo = 0; sampleNo < sampleCount; sampleNo++)
      {
         res = row.getSample(sampleNo);

         int x = x1 + (int) (dotW * (double) res.getThreads());
         int y = y2 - (int) (dotH * (double) res.getTime());

         g.setColor(row.getColor());
         g.fillRect(x, y, 1 /*(int) dotW < 1 ? 1 : (int) dotW*/, 1 /*(int) dotH < 1 ? 1 : (int) dotH*/);
      }
   }

   private void drawAverage(DotChartModel p_model, DotChartColoredRow row, Graphics g, int offsetY)
   {
      if (row.getCount() < 1)
         return;

      int maxThreads = p_model.getMaxThreads();
      long maxTime = p_model.getMaxTime();
      if (maxThreads < 1 || maxTime < 1)
         return;

      int x1 = xborder;
      int y1 = offsetY + xborder;
      int x2 = x1 + getChartWidth();
      int y2 = y1 + getChartHeight(offsetY);

      final int radius = 4;
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

   private void drawThreadsAverage(DotChartModel p_model, DotChartColoredRow row, Graphics g, int offsetY)
   {
      if (row.getCount() < 1)
         return;

      int maxThreads = p_model.getMaxThreads();
      long maxTime = p_model.getMaxTime();
      if (maxThreads < 1 || maxTime < 1)
         return;

      int x1 = xborder;
      int y1 = offsetY + xborder;
      int x2 = x1 + getChartWidth();
      int y2 = y1 + getChartHeight(offsetY);

      final int radius = 2;
      double dotW = (double) getChartWidth() / (double) (maxThreads + 1);
      double dotH = (double) getChartHeight(offsetY) / (double) (maxTime + 1);

      Vector avgTimeByThreads = row.getAvgTimeByThreads();
      int prevX = 0, prevY = 0;
      for (int threads = 0; threads < avgTimeByThreads.size(); threads++)
      {
         if (avgTimeByThreads.elementAt(threads) == null)
            continue;

         double avgTime = ((DotChartAverageValues) avgTimeByThreads.get(threads)).getAvgTime();

         int x = x1 + (int) (dotW * (double) threads + 0.5);

         int y = y2 - (int) (dotH * avgTime);

         g.setColor(row.getColor());
         //if (prevX > 0)
         //   g.drawLine(prevX, prevY, x, y);

         g.fillOval(x - radius, y - radius, (radius) * 2, (radius) * 2);
         g.setColor(Color.black);
         g.drawOval(x - radius, y - radius, radius * 2, radius * 2);

         prevX = x;
         prevY = y;
         //g.drawString(Long.toString(Math.round(avgTime)), x + radius + radius, y + radius + (int) dotH);
      }
   }

   private int drawLegend(DotChartModel model, Graphics g)
   {
      int offsetX = 1, offsetY = 1;
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

      return currentY + xborder / 2;
   }

   private void drawGraphLayout(DotChartModel p_model, Graphics g, int offsetY)
   {
      // get max values
      DotChartColoredRow row;
      int maxThreads = p_model.getMaxThreads();
      long maxTime = p_model.getMaxTime();

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
