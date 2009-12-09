package kg.apc.jmeter.dotchart;

import org.apache.jmeter.samplers.SampleResult;

public class DotChartSampleResult
{

   private int repeatCount;
   private int threads;
   private long time;
   private String label;
   private double throughput;

   public DotChartSampleResult()
   {
      repeatCount = 1;
   }

   public DotChartSampleResult(SampleResult res)
   {
      this();
      threads = res.getAllThreads();
      time = res.getTime();
      label = res.getSampleLabel();

      if ((res.getTime() > 0))
      {
         throughput = 1000 * ((double) res.getAllThreads() / (double) res.getTime());
      }
      else
      {
         throughput = 0;
      }
   }

   int getThreads()
   {
      return threads;
   }

   long getTime()
   {
      return time;
   }

   double getThroughput()
   {
      return throughput;
   }

   String getLabel()
   {
      return label;
   }

   public int getRepeatCount()
   {
      return repeatCount;
   }

   void addRepeat()
   {
      repeatCount++;
   }
}
