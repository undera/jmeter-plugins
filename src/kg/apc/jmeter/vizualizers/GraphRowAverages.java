package kg.apc.jmeter.vizualizers;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

public class GraphRowAverages
     extends AbstractGraphRow
{
   private ConcurrentSkipListMap<Long, GraphPanelChartElement> values;

   GraphRowAverages()
   {
      super();
      values = new ConcurrentSkipListMap<Long, GraphPanelChartElement>();
   }

   public void add(long xVal, double yVal)
   {
      GraphPanelChartElement el;
      if (values.containsKey(xVal))
      {
         el = values.get(xVal);
         el.add(yVal);
         yVal = el.getAvgValue();
      }
      else
      {
         el = new GraphPanelChartElement(yVal);
         values.put(xVal, el);
      }

      if (xVal > maxX)
         maxX = xVal;
      if (yVal > maxY)
         maxY = yVal;
      if (xVal < minX)
         minX = xVal;
      if (yVal < minY)
         minY = yVal;

   }

   public Iterator<Entry<Long, GraphPanelChartElement>> iterator()
   {
      return values.entrySet().iterator();
   }
}
