package kg.apc.jmeter.vizualizers;

public class GraphPanelChartExactElement
      extends AbstractGraphPanelChartElement
{
   private long x = 0;
   private double y = 0;

   GraphPanelChartExactElement(long xVal, double yVal)
   {
      x = xVal;
      y = yVal;
   }

   public double getValue()
   {
      return y;
   }

   public long getX()
   {
      return x;
   }
}
