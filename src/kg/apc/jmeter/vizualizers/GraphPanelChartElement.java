package kg.apc.jmeter.vizualizers;

public class GraphPanelChartElement
{
   private int count = 0;
   private double avgValue = 0;

   public GraphPanelChartElement(double yVal)
   {
      add(yVal);
   }

   GraphPanelChartElement()
   {
   }

   public void add(double yVal)
   {
      avgValue = (avgValue * count + yVal) / (++count);
   }

   public double getAvgValue()
   {
      return avgValue;
   }

   /**
    * @return the count
    */
   public int getCount()
   {
      return count;
   }
}
