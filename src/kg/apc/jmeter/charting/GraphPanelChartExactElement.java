package kg.apc.jmeter.charting;

/** {@inheritDoc} */
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
}
