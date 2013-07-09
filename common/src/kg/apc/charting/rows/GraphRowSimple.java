package kg.apc.charting.rows;

import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.elements.GraphPanelChartSimpleElement;
import kg.apc.charting.AbstractGraphPanelChartElement;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author apc
 */
public class GraphRowSimple
        extends AbstractGraphRow
{
    private ConcurrentSkipListMap<Long, AbstractGraphPanelChartElement> values;

    /**
     *
     */
    public GraphRowSimple()
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
        GraphPanelChartSimpleElement el;
        if (values.containsKey(xVal))
        {
            el = (GraphPanelChartSimpleElement) values.get(xVal);
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
    public Iterator<Entry<Long, AbstractGraphPanelChartElement>> iterator()
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
