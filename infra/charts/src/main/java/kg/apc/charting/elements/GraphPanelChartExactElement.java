package kg.apc.charting.elements;

import kg.apc.charting.AbstractGraphPanelChartElement;

/** {@inheritDoc} */
public class GraphPanelChartExactElement
      extends AbstractGraphPanelChartElement
{
   private long x = 0;
   private double y = 0;

   public GraphPanelChartExactElement(long xVal, double yVal)
   {
      x = xVal;
      y = yVal;
   }

   /** {@inheritDoc} */
   public double getValue()
   {
      return y;
   }

   /**
    *
    * @return
    */
   public long getX()
   {
      return x;
   }

    public void add(double val) {
        y=val;
    }
}
