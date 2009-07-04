package kg.apc.jmeter.dotchart;

import java.awt.Color;
import java.util.Random;
import java.util.Vector;
import org.apache.jmeter.samplers.SampleResult;

public class DotChartColoredRow
{
   private Color color;
   private String label;
   private Vector values;
   private double avgThreads;
   private double avgTime;
   private int count;
   private int maxThreads;
   private long maxTime;
   private Vector avgTimeByThreads;

   DotChartColoredRow(String alabel)
   {
      label = alabel;
      Random r = new Random();
      color = new Color(r.nextInt(0xFFFFFF));
      values = new Vector(0);
      avgTimeByThreads = new Vector(0);
   }

   public Color getColor()
   {
      return color;
   }

   public void addSample(SampleResult res)
   {
      DotChartSampleResult oldres;
      for (int n = 0; n < values.size(); n++)
      {
         oldres = (DotChartSampleResult) values.elementAt(n);
         if (oldres.getThreads()==res.getAllThreads() && oldres.getTime()==res.getTime())
         {
            oldres.addRepeat();
            return;
         }
      }

      DotChartSampleResult leanres = new DotChartSampleResult(res);
      values.add(leanres);
      calculateAggregates(leanres);
      addAvgTimeByThreads(leanres);
   }

   public int getCount()
   {
      return values.size();
   }

   public String getLabel()
   {
      return label;
   }

   public DotChartSampleResult getSample(int count)
   {
      return (DotChartSampleResult) values.get(count);
   }

   double getAvgThreads()
   {
      return avgThreads;
   }

   public double getAvgTime()
   {
      return avgTime;
   }

   private void calculateAggregates(DotChartSampleResult leanres)
   {
      int threads = leanres.getThreads();
      if (threads > maxThreads)
         maxThreads = threads;
      avgThreads = (avgThreads * count + threads) / (count + 1);
      long time = leanres.getTime();
      if (time > maxTime)
         maxTime = time;
      avgTime = (avgTime * count + time) / (count + 1);
      count++;
   }

   private int getMaxThreads()
   {
      return maxThreads;
   }

   private long getMaxTime()
   {
      return maxTime;
   }

   Vector getAvgTimeByThreads()
   {
      return avgTimeByThreads;
   }

   private void addAvgTimeByThreads(DotChartSampleResult leanres)
   {
      int threads = leanres.getThreads();
      if (avgTimeByThreads.size() <= threads)
         avgTimeByThreads.setSize(threads + 1);

      if (avgTimeByThreads.elementAt(threads) == null)
      {
         DotChartAverageValues New = new DotChartAverageValues();
         avgTimeByThreads.setElementAt(New, threads);
      }

      DotChartAverageValues CountAndAvg = (DotChartAverageValues) avgTimeByThreads.elementAt(threads);
      double avgTime1 = CountAndAvg.getAvgTime();
      int count1 = CountAndAvg.getCount();
      double newAvgTime = (avgTime1 * count1 + leanres.getTime()) / (count1 + 1);
      CountAndAvg.setAvgTime(newAvgTime);
      CountAndAvg.addCount();
   }
}
