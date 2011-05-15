package kg.apc.charting.elements;

import kg.apc.charting.AbstractGraphPanelChartElement;

/** {@inheritDoc} */
public class GraphPanelChartAverageElement
      extends AbstractGraphPanelChartElement
{
   private int count = 0;
   private double avgValue = 0;

   /**
    *
    * @param yVal
    */
   public GraphPanelChartAverageElement(double yVal)
   {
      add(yVal);
   }

   public GraphPanelChartAverageElement()
   {
   }

   /**
    *
    * @param yVal
    */
   public void add(double yVal)
   {
      avgValue = (avgValue * count + yVal) / (++count);
   }

   /** {@inheritDoc} */
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

    @Override
    public boolean isPointRepresentative(int limit)
    {
        return count > limit;
    }
}
