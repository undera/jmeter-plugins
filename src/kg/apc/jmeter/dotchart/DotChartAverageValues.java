package kg.apc.jmeter.dotchart;

class DotChartAverageValues
{

   private int count;
   private double avgTime;
   private double avgThroughput;

   public DotChartAverageValues()
   {
      count = 0;
      avgTime = 0;
   }

   int getCount()
   {
      return count;
   }

   void setAvgTime(double l)
   {
      avgTime = l;
   }

   public double getAvgTime()
   {
      return avgTime;
   }

   public double getAvgThroughput()
   {
      return avgThroughput;
   }

   void addCount()
   {
      count++;
   }

   void setAvgThroughput(double newAvgThroughput)
   {
      avgThroughput = newAvgThroughput;
   }
}
