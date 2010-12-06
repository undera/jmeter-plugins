package kg.apc.jmeter.charting;

/**
 * 
 * @author apc
 */
public abstract class AbstractGraphPanelChartElement
{
   /**
    *
    * @return
    */
   public abstract double getValue();

   public boolean isPointRepresentative(int limit)
   {
       return true;
   }
}
