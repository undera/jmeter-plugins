package kg.apc.charting.rows;

import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.elements.GraphPanelChartAverageElement;
import kg.apc.charting.AbstractGraphPanelChartElement;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author apc
 */
public class GraphRowAverages
        extends AbstractGraphRow {

    private ConcurrentSkipListMap<Long, AbstractGraphPanelChartElement> values;

    /**
     *
     */
    public GraphRowAverages() {
        super();
        values = new ConcurrentSkipListMap<Long, AbstractGraphPanelChartElement>();
    }

    /**
     *
     * @param xVal
     * @param yVal
     */
    @Override
    public void add(long xVal, double yVal) {
        GraphPanelChartAverageElement el;
        if (values.containsKey(xVal)) {
            el = (GraphPanelChartAverageElement) values.get(xVal);
            el.add(yVal);
            yVal = el.getValue();
        } else {
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
    public Iterator<Entry<Long, AbstractGraphPanelChartElement>> iterator() {
        return values.entrySet().iterator();
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public AbstractGraphPanelChartElement getElement(long value) {
        return values.get(value);
    }

    @Override
    public AbstractGraphPanelChartElement getLowerElement(long value) {
        Long lower = values.navigableKeySet().lower(value);

        if (lower != null) {
            return getElement(lower);
        } else {
            return null;
        }
    }
}
