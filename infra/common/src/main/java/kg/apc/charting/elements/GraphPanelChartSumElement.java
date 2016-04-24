package kg.apc.charting.elements;

import kg.apc.charting.AbstractGraphPanelChartElement;

/** {@inheritDoc} */
public class GraphPanelChartSumElement
      extends AbstractGraphPanelChartElement
{
   private int count = 0;
   private double sumValue = 0;

   /**
    *
    * @param yVal
    */
   public GraphPanelChartSumElement(double yVal)
   {
      add(yVal);
   }

   GraphPanelChartSumElement()
   {
   }

   /**
    *
    * @param yVal
    */
   public void add(double yVal)
   {
      sumValue += yVal;
      count++;
   }

   /** {@inheritDoc} */
   public double getValue()
   {
      return sumValue;
   }

   /**
    *
    * @return
    */
   public int getCount()
   {
      return count;
   }
}
