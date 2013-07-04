package kg.apc.charting.rows;

import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.elements.GraphPanelChartExactElement;
import kg.apc.charting.AbstractGraphPanelChartElement;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author apc
 */
public class GraphRowExactValues
      extends AbstractGraphRow
      implements Iterator<Entry<Long, AbstractGraphPanelChartElement>>
{
   private ConcurrentSkipListMap<Long, AbstractGraphPanelChartElement> values;
   private Iterator<Entry<Long, AbstractGraphPanelChartElement>> iterator;

   /**
    * 
    */
   public GraphRowExactValues()
   {
      super();
      values = new ConcurrentSkipListMap<Long, AbstractGraphPanelChartElement>();
   }

   /**
    *
    * @param xVal
    * @param yVal
    */
   @Override
   public void add(long xVal, double yVal)
   {
      GraphPanelChartExactElement el;
      el = new GraphPanelChartExactElement(xVal, yVal);
      values.put((long) values.size(), el);

      super.add(xVal, yVal);
   }

   /**
    *
    * @return
    */
   @Override
   public Iterator<Entry<Long, AbstractGraphPanelChartElement>> iterator()
   {
      iterator = values.entrySet().iterator();
      return this;
   }

   public boolean hasNext()
   {
      return iterator==null?false:iterator.hasNext();
   }

   public Entry<Long, AbstractGraphPanelChartElement> next()
   {
      GraphPanelChartExactElement el = (GraphPanelChartExactElement) iterator.next().getValue();
      return new ExactEntry(el.getX(), el);
   }

   public void remove()
   {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   private class ExactEntry
         implements Entry<Long, AbstractGraphPanelChartElement>
   {
      private long key;
      private final AbstractGraphPanelChartElement value;

      public ExactEntry(long aKey, AbstractGraphPanelChartElement aValue)
      {
         key = aKey;
         value = aValue;
      }

      public Long getKey()
      {
         return key;
      }

      public AbstractGraphPanelChartElement getValue()
      {
         return value;
      }

      public AbstractGraphPanelChartElement setValue(AbstractGraphPanelChartElement value)
      {
         throw new UnsupportedOperationException("Not supported yet.");
      }
   }

    @Override
    public int size()
    {
        return values.size();
    }

    @Override
    public AbstractGraphPanelChartElement getElement(long value)
    {
        AbstractGraphPanelChartElement ret = null;
        Iterator<Entry<Long, AbstractGraphPanelChartElement>> it = values.entrySet().iterator();

        while(it.hasNext() && ret == null)
        {
            GraphPanelChartExactElement el = (GraphPanelChartExactElement) it.next().getValue();
            if(el.getX() == value)
            {
                ret = el;
            }
        }

        return ret;
    }
}
