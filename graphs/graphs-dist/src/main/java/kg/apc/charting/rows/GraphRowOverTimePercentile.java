package kg.apc.charting.rows;

import kg.apc.charting.AbstractGraphPanelChartElement;
import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.elements.GraphPanelChartPercentileElement;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class GraphRowOverTimePercentile extends AbstractGraphRow {
    private final ConcurrentSkipListMap<Long, AbstractGraphPanelChartElement> values; // key is sample time interval
    private final double percentile;

    public GraphRowOverTimePercentile(double percentile) {
        super();
        values = new ConcurrentSkipListMap<>();
        this.percentile = percentile;
    }

    @Override
    public void add(long xVal, double yVal) {
        GraphPanelChartPercentileElement el = (GraphPanelChartPercentileElement) values.get(xVal);
        if (el == null) {
            el = new GraphPanelChartPercentileElement(percentile);
            values.put(xVal, el);
        }
        el.add(yVal);
        yVal = el.getValue();
        super.add(xVal, yVal);
    }

    @Override
    public Iterator<Map.Entry<Long, AbstractGraphPanelChartElement>> iterator() {
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
}
