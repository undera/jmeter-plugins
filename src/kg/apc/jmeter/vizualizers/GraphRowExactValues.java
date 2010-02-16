package kg.apc.jmeter.vizualizers;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Vector;

public class GraphRowExactValues
      extends AbstractGraphRow
      implements Iterator<Entry<Long, Object>>
{
   //private ConcurrentSkipListMap<Long, GraphPanelChartAverageElement> values;
   private Vector<GraphPanelChartExactElement> values;
   private Iterator<GraphPanelChartExactElement> iterator;

   public GraphRowExactValues()
   {
      super();
      //values = new ConcurrentSkipListMap<Long, GraphPanelChartAverageElement>();
      values = new Vector<GraphPanelChartExactElement>();
   }

   @Override
   public void add(long xVal, double yVal)
   {
      GraphPanelChartExactElement el;
      el = new GraphPanelChartExactElement(xVal, yVal);
      values.add(el);

      super.add(xVal, yVal);
   }

   @Override
   public Iterator<Entry<Long, Object>> iterator()
   {
      iterator = values.iterator();
      return this;
   }

   public boolean hasNext()
   {
      return iterator.hasNext();
   }

   public Entry<Long, Object> next()
   {
      GraphPanelChartExactElement el = iterator.next();
      return new ExactEntry(el.getX(), el);
   }

   public void remove()
   {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   private class ExactEntry
         implements Entry<Long, Object>
   {
      private long key;
      private final Object value;

      public ExactEntry(long aKey, Object aValue)
      {
         key = aKey;
         value = aValue;
      }

      public Long getKey()
      {
         return key;
      }

      public Object getValue()
      {
         return value;
      }

      public Object setValue(Object value)
      {
         throw new UnsupportedOperationException("Not supported yet.");
      }
   }
}
