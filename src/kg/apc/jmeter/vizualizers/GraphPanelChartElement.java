package kg.apc.jmeter.vizualizers;

class GraphPanelChartElement
{
   private int count = 0;
   private double avgValue = 0;

   public GraphPanelChartElement(double yVal)
   {
      add(yVal);
   }

   public void add(double yVal)
   {
      avgValue = (avgValue * count + yVal) / (++count);
   }
}
