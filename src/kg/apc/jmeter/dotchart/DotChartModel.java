package kg.apc.jmeter.dotchart;

import java.awt.Color;
import java.util.HashMap;
import java.util.Random;
import org.apache.jmeter.gui.util.JMeterColor;
import org.apache.jmeter.samplers.SampleResult;

public class DotChartModel
     extends HashMap
{
   private int maxThreads;
   private long maxTime;
   private final Color[] fixedColors =
   {
      Color.RED,
      Color.GREEN,
      Color.BLUE,

      JMeterColor.purple,
      Color.ORANGE,
      Color.CYAN,
      Color.MAGENTA,
      Color.PINK,
      Color.YELLOW,
      JMeterColor.LAVENDER,
      JMeterColor.dark_green,
      Color.GRAY,
      Color.LIGHT_GRAY,
   };

   public DotChartModel()
   {
      super(0);
   }

   public void addSample(SampleResult res)
   {
      if (res.getGroupThreads() == 0)
         return;

      String label = res.getSampleLabel();
      DotChartColoredRow row;
      Color color;
      if (containsKey(label))
         row = (DotChartColoredRow) get(label);
      else
      {
         if (size() >= fixedColors.length)
         {
            Random r = new Random();
            color = new Color(r.nextInt(0xFFFFFF));
         }
         else
            color = fixedColors[size()];
         row = new DotChartColoredRow(label, color);
         put(label, row);
      }

      row.addSample(res);
      calculateAggregates(res);
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

   private void calculateAggregates(SampleResult res)
   {
      int threads = res.getAllThreads();
      if (threads > maxThreads)
         maxThreads = threads;
      //avgThreads = (avgThreads * count + threads) / (count + 1);
      long time = res.getTime();
      if (time > maxTime)
         maxTime = time;
      //avgTime = (avgTime * count + time) / (count + 1);
      //count++;
   }

   public void clear()
   {
      super.clear();
      maxThreads = 0;
      maxTime = 0;
   }
}
