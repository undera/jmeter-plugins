package kg.apc.jmeter.vizualizers;

import java.util.Iterator;
import java.util.Map.Entry;

public class GraphRowOverallAverages
     extends AbstractGraphRow
     implements Iterator, Entry<Long, GraphPanelChartElement>
{
   private boolean hasNext;
   private GraphPanelChartElement element;
   private double avgX = 0;

   public GraphRowOverallAverages()
   {
      super();
      element = new GraphPanelChartElement();
      hasNext = true;
   }

   @Override
   public void add(long xVal, double yVal)
   {
      avgX = (avgX * element.getCount() + xVal) / (element.getCount() + 1);
      element.add(yVal);

      super.add((long) avgX, element.getAvgValue());
   }

   public Iterator<Entry<Long, GraphPanelChartElement>> iterator()
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

   public GraphPanelChartElement getValue()
   {
      return element;
   }

   public GraphPanelChartElement setValue(GraphPanelChartElement value)
   {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}
