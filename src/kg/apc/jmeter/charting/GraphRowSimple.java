package kg.apc.jmeter.charting;

import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author apc
 */
public class GraphRowSimple
        extends AbstractGraphRow
{
    private ConcurrentSkipListMap<Long, GraphPanelChartSimpleElement> values;

    /**
     *
     */
    public GraphRowSimple()
    {
        super();
        values = new ConcurrentSkipListMap<Long, GraphPanelChartSimpleElement>();
    }

    /**
     *
     * @param xVal
     * @param yVal
     */
    @Override
    public void add(long xVal, double yVal)
    {
        GraphPanelChartSimpleElement el;
        if (values.containsKey(xVal))
        {
            el = values.get(xVal);
            el.add(yVal);
        } else
        {
            el = new GraphPanelChartSimpleElement(yVal);
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
    public Long getHigherKey(long value)
    {
        return values.navigableKeySet().higher(value);
    }
}
