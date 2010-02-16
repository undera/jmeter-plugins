package kg.apc.jmeter.vizualizers;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

public class GraphRowAverages
     extends AbstractGraphRow
{
   private ConcurrentSkipListMap<Long, GraphPanelChartAverageElement> values;

   public GraphRowAverages()
   {
      super();
      values = new ConcurrentSkipListMap<Long, GraphPanelChartAverageElement>();
   }

   @Override
   public void add(long xVal, double yVal)
   {
      GraphPanelChartAverageElement el;
      if (values.containsKey(xVal))
      {
         el = values.get(xVal);
         el.add(yVal);
         yVal = el.getValue();
      }
      else
      {
         el = new GraphPanelChartAverageElement(yVal);
         values.put(xVal, el);
      }

      super.add(xVal, yVal);
   }

   public Iterator iterator()
   {
      return values.entrySet().iterator();
   }
}
