package kg.apc.jmeter.dotchart;

import java.awt.Color;
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
   private Vector averagesByThreads;
   private double avgThroughput;

   DotChartColoredRow(String alabel, Color acolor)
   {
      label = alabel;
      color = acolor;
      values = new Vector(0);
      averagesByThreads = new Vector(0);
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
         if (oldres.getThreads() == res.getAllThreads() && oldres.getTime() == res.getTime())
         {
            oldres.addRepeat();
            return;
         }
      }

      DotChartSampleResult leanres = new DotChartSampleResult(res);
      values.add(leanres);
      calculateAggregates(leanres);
      addAvgeragesByThreads(leanres);
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
      {
         maxThreads = threads;
      }
      avgThreads = (avgThreads * count + threads) / (count + 1);
      long time = leanres.getTime();
      avgTime = (avgTime * count + time) / (count + 1);

      double throughput = leanres.getThroughput();
      avgThroughput = (avgThroughput * count + throughput) / (count + 1);

      count++;
   }

   Vector getAveragesByThreads()
   {
      return averagesByThreads;
   }

   private void addAvgeragesByThreads(DotChartSampleResult leanres)
   {
      int threads = leanres.getThreads();
      if (averagesByThreads.size() <= threads)
      {
         averagesByThreads.setSize(threads + 1);
      }

      if (averagesByThreads.elementAt(threads) == null)
      {
         DotChartAverageValues New = new DotChartAverageValues();
         averagesByThreads.setElementAt(New, threads);
      }

      DotChartAverageValues countAndAvg = (DotChartAverageValues) averagesByThreads.elementAt(threads);
      int count1 = countAndAvg.getCount();
      double newAvgTime = (countAndAvg.getAvgTime() * count1 + leanres.getTime()) / (count1 + 1);
      countAndAvg.setAvgTime(newAvgTime);
      double newAvgThroughput = (countAndAvg.getAvgThroughput() * count1 + leanres.getThroughput()) / (count1 + 1);
      countAndAvg.setAvgThroughput(newAvgThroughput);
      countAndAvg.addCount();
   }

   double getAvgThoughput()
   {
      return avgThroughput;
   }
}
