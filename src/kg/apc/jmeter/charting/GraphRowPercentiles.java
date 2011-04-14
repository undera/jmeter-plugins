package kg.apc.jmeter.charting;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * The algorithm used for percentiles calculation is the one from
 * org.apache.commons.math.stat.descriptive.rank.Percentile
 * @author Stephane Hoblingre
 */
public class GraphRowPercentiles extends GraphRowSumValues {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private ConcurrentSkipListMap<Long, AbstractGraphPanelChartElement> percentiles = new ConcurrentSkipListMap<Long, AbstractGraphPanelChartElement>();
    private long totalCount = 0L;

    public GraphRowPercentiles() {
        super();
        //create percentiles objects, and reuse them to avoid GC
        for (long p = 0; p <= 1000; p++) {
            percentiles.put(p, new GraphPanelChartExactElement(p, 0));
        }
    }

    @Override
    public void add(long xVal, double yVal) {
        super.add(xVal, yVal);
        totalCount++;
    }

    private void calculatePercentiles() {
        long calculatedPerc = 0;
        int rollingCount = 0;
        Iterator<Entry<Long, AbstractGraphPanelChartElement>> it = super.iterator();
        Entry<Long, AbstractGraphPanelChartElement> el = null;
        Iterator<Entry<Long, AbstractGraphPanelChartElement>> percIT = percentiles.entrySet().iterator();
        while (percIT.hasNext()) {
            Entry<Long, AbstractGraphPanelChartElement> percEl = percIT.next();
            double percLevel = percEl.getKey() / 10D;
            while (calculatedPerc < percLevel && it.hasNext()) {
                el = it.next();
                rollingCount += el.getValue().getValue();
                calculatedPerc = 100 * rollingCount / totalCount;
            }

            if (el != null) {
                log.debug(percLevel + " P: " + calculatedPerc + " T: " + el.getKey());
                percEl.getValue().add(el.getKey());
            }
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
