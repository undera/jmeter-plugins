package kg.apc.charting.rows;

import kg.apc.charting.elements.GraphPanelChartExactElement;
import kg.apc.charting.AbstractGraphPanelChartElement;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class GraphRowPercentiles extends GraphRowSumValues {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private ConcurrentSkipListMap<Long, AbstractGraphPanelChartElement> percentiles = new ConcurrentSkipListMap<Long, AbstractGraphPanelChartElement>();
    private long totalCount = 0L;
    private static final int FRACTION = 10;

    public GraphRowPercentiles() {
        super();
        //create percentiles objects, and reuse them to avoid GC
        //we remove p=100 as we now have 99.9 percentile which is enough, and by doing this loiterings will not break chart anymore
        for (long p = 1; p <= 100 * FRACTION-1; p++) {
            percentiles.put(p, new GraphPanelChartExactElement(p, 0));
        }
    }

    @Override
    public void add(long xVal, double yVal) {
        super.add(xVal, yVal);
        totalCount++;
    }

    @Override
    public long getMinX() {
        return 0;
    }

    @Override
    public long getMaxX() {
        return 100 * FRACTION;
    }

    private void calculatePercentiles() {
        double calculatedPerc = 0;
        Iterator<Entry<Long, AbstractGraphPanelChartElement>> valIT = super.iterator();
        Entry<Long, AbstractGraphPanelChartElement> el = null;
        Iterator<Entry<Long, AbstractGraphPanelChartElement>> percIT = percentiles.entrySet().iterator();
        Long timeLevel = 0L;

        while (percIT.hasNext()) {
            Entry<Long, AbstractGraphPanelChartElement> percEl = percIT.next();
            double percLevel = percEl.getKey() / (double) FRACTION;

            while (calculatedPerc < percLevel && valIT.hasNext()) {
                el = valIT.next();
                calculatedPerc = 100 * el.getValue().getValue() / (double) totalCount;
            }

            if (el != null) {
                timeLevel = el.getKey();
            }

            percEl.getValue().add(timeLevel);
        }
    }

    @Override
    public Iterator<Entry<Long, AbstractGraphPanelChartElement>> iterator() {
        calculatePercentiles();
        return percentiles.entrySet().iterator();
    }

    @Override
    public int size() {
        if (super.size() == 0) {
            return 0;
        } else {
            return percentiles.size();
        }
    }

    @Override
    public AbstractGraphPanelChartElement getElement(long value) {
        return percentiles.get(value);
    }

}
