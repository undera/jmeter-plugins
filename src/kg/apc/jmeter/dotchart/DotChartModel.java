package kg.apc.jmeter.dotchart;

import java.util.concurrent.ConcurrentHashMap;
import kg.apc.jmeter.vizualizers.ColorsDispatcher;
import org.apache.jmeter.samplers.SampleResult;

public class DotChartModel
     extends ConcurrentHashMap
{
   private int maxThreads;
   private long maxTime;
   private double maxThroughput;
   private ColorsDispatcher colors;

   public DotChartModel()
   {
      super(0);
      colors=new ColorsDispatcher();
   }

   public void addSample(SampleResult res)
   {
      if (res.getGroupThreads() == 0)
      {
         return;
      }

      String label = res.getSampleLabel();
      DotChartColoredRow row = putSampleIntoRowAndGetThatRow(label);

      row.addSample(res);
      calculateMaxValues(res);
   }

   public DotChartColoredRow get(String key)
   {
      return (DotChartColoredRow) super.get(key);
   }

   public int getMaxThreads()
   {
      return maxThreads;
   }

   long getMaxTime()
   {
      return maxTime;
   }

   private void calculateMaxValues(SampleResult res)
   {
      int threads = res.getAllThreads();
      long time = res.getTime();
      double throughput = 0;

      if (threads > maxThreads)
      {
         maxThreads = threads;
      }

      if (time > maxTime)
      {
         maxTime = time;
      }

      if (time > 0)
      {
         throughput = 1000 * (double) 1 / (double) time;
      }

      if (throughput > maxThroughput)
      {
         maxThroughput = throughput;
      }
   }

   @Override
   public void clear()
   {
      super.clear();
      maxThreads = 0;
      maxTime = 0;
      maxThroughput = 0;
   }

   double getMaxThroughput()
   {
      return maxThroughput;
   }

   private DotChartColoredRow putSampleIntoRowAndGetThatRow(String label)
   {
      DotChartColoredRow row;
      if (containsKey(label))
      {
         row = (DotChartColoredRow) get(label);
      }
      else
      {
         row = new DotChartColoredRow(label, colors.getNextColor());
         put(label, row);
      }
      return row;
   }
}
