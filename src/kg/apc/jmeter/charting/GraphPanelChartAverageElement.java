package kg.apc.jmeter.charting;

import kg.apc.jmeter.charting.AbstractGraphPanelChartElement;

public class GraphPanelChartAverageElement
      extends AbstractGraphPanelChartElement
{
   private int count = 0;
   private double avgValue = 0;

   public GraphPanelChartAverageElement(double yVal)
   {
      add(yVal);
   }

   GraphPanelChartAverageElement()
   {
   }

   public void add(double yVal)
   {
      avgValue = (avgValue * count + yVal) / (++count);
   }

   public double getValue()
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
