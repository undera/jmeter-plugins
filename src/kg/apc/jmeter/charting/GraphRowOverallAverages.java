package kg.apc.jmeter.charting;

import java.util.Iterator;
import java.util.Map.Entry;

/**
 *
 * @author apc
 */
public class GraphRowOverallAverages
     extends AbstractGraphRow
     implements Iterator, Entry<Long, GraphPanelChartAverageElement>
{
   private boolean hasNext;
   private GraphPanelChartAverageElement element;
   private double avgX = 0;

   /**
    *
    */
   public GraphRowOverallAverages()
   {
      super();
      element = new GraphPanelChartAverageElement();
      hasNext = true;
   }

   /**
    *
    * @param xVal
    * @param yVal
    */
   @Override
   public void add(long xVal, double yVal)
   {
      avgX = (avgX * element.getCount() + xVal) / (element.getCount() + 1);
      element.add(yVal);

      super.add((long) avgX, element.getValue());
   }

   /**
    *
    * @return
    */
   public Iterator iterator()
   {
      hasNext = true;
      return this;
   }

   public boolean hasNext()
   {
      return hasNext;
   }

   public Object next()
   {
      hasNext = false;
      return this;
   }

   public void remove()
   {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public Long getKey()
   {
      return Math.round(avgX);
   }

   public GraphPanelChartAverageElement getValue()
   {
      return element;
   }

   public GraphPanelChartAverageElement setValue(GraphPanelChartAverageElement value)
   {
      throw new UnsupportedOperationException("Not supported yet.");
   }

    @Override
    public int size()
    {
        return 1;
    }
}
