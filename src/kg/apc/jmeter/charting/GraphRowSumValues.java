package kg.apc.jmeter.charting;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;
//import org.apache.jorphan.logging.LoggingManager;
//import org.apache.log.Logger;

/**
 *
 * @author apc
 */
public class GraphRowSumValues
      extends AbstractGraphRow
      implements Iterator<Entry<Long, AbstractGraphPanelChartElement>>
{
   //private static final Logger log = LoggingManager.getLoggerForClass();
   private ConcurrentSkipListMap<Long, GraphPanelChartSumElement> values;
   private double rollingSum;
   private Iterator<Entry<Long, GraphPanelChartSumElement>> iterator;
   private boolean isRollingSum = true;

   /**
    *
    */
   public GraphRowSumValues()
   {
      super();
      values = new ConcurrentSkipListMap<Long, GraphPanelChartSumElement>();
   }

   public GraphRowSumValues(boolean doRollingSum)
   {
      super();
      values = new ConcurrentSkipListMap<Long, GraphPanelChartSumElement>();
      isRollingSum = doRollingSum;
   }

   /**
    *
    * @param val
    */
   public void setMaxY(double val)
   {
      maxY = val;
   }

   /**
    *
    * @param xVal
    * @param yVal
    */
   @Override
   public void add(long xVal, double yVal)
   {
      GraphPanelChartSumElement el;
      if (values.containsKey(xVal))
      {
         el = values.get(xVal);
         el.add(yVal);
         yVal = el.getValue();
      }
      else
      {
         el = new GraphPanelChartSumElement(yVal);
         values.put(xVal, el);
      }

      super.add(xVal, yVal);
   }

   /**
    *
    * @return
    */
   public Iterator<Entry<Long, AbstractGraphPanelChartElement>> iterator()
   {
      rollingSum = 0;
      iterator = values.entrySet().iterator();
      return this;
   }

   public boolean hasNext()
   {
      return iterator.hasNext();
   }

   public Entry<Long, AbstractGraphPanelChartElement> next()
   {
      Entry<Long, GraphPanelChartSumElement> entry = iterator.next();
      GraphPanelChartSumElement ret = entry.getValue();
      
      //log.info("Rolling: " + entry.getKey() + " " + rollingSum);
      ExactEntry retValue = null;
      if(isRollingSum) {
         rollingSum += ret.getValue();
         retValue = new ExactEntry(entry.getKey(), new GraphPanelChartSumElement(rollingSum));
       } else {
          retValue = new ExactEntry(entry.getKey(), new GraphPanelChartSumElement(ret.getValue()));
       }
      return retValue;
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
}
