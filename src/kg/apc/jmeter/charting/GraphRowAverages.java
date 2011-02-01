package kg.apc.jmeter.charting;

import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author apc
 */
public class GraphRowAverages
        extends AbstractGraphRow
{
    private ConcurrentSkipListMap<Long, GraphPanelChartAverageElement> values;

    /**
     *
     */
    public GraphRowAverages()
    {
        super();
        values = new ConcurrentSkipListMap<Long, GraphPanelChartAverageElement>();
    }

    /**
     *
     * @param xVal
     * @param yVal
     */
    @Override
    public void add(long xVal, double yVal)
    {
        GraphPanelChartAverageElement el;
        if (values.containsKey(xVal))
        {
            el = values.get(xVal);
            el.add(yVal);
            yVal = el.getValue();
        } else
        {
            el = new GraphPanelChartAverageElement(yVal);
            values.put(xVal, el);
        }

        super.add(xVal, yVal);
    }

    /**
     *
     * @return
     */
    @Override
    public Iterator iterator()
    {
        return values.entrySet().iterator();
    }

    @Override
    public int size()
    {
        return values.size();
    }

    @Override
    public AbstractGraphPanelChartElement getElement(long value)
    {
        return values.get(value);
    }

    @Override
    public AbstractGraphPanelChartElement getFloorElement(long value)
    {
        Long floor = values.navigableKeySet().floor(value);
        if (floor != null)
        {
            return getElement(floor);
        } else
        {
            return null;
        }
    }
}
