package kg.apc.jmeter.charting;

public class GraphPanelChartSumElement
      extends AbstractGraphPanelChartElement
{
   private int count = 0;
   private double sumValue = 0;

   public GraphPanelChartSumElement(double yVal)
   {
      add(yVal);
   }

   GraphPanelChartSumElement()
   {
   }

   public void add(double yVal)
   {
      sumValue += yVal;
      count++;
   }

   public double getValue()
   {
      return sumValue;
   }

   public int getCount()
   {
      return count;
   }
}
