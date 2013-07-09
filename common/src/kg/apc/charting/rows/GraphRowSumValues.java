package kg.apc.charting.rows;

import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.elements.GraphPanelChartSumElement;
import kg.apc.charting.AbstractGraphPanelChartElement;
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
        implements Iterator<Entry<Long, AbstractGraphPanelChartElement>> {
    //private static final Logger log = LoggingManager.getLoggerForClass();

    private ConcurrentSkipListMap<Long, GraphPanelChartSumElement> values;
    private double rollingSum;
    private Iterator<Entry<Long, GraphPanelChartSumElement>> iterator;
    private boolean isRollingSum = true;
    private boolean excludeOutOfRangeValues = false;
    private final static long excludeCount = 15; //we don't take in account for max x if more than 15 empty bars
    private int countX = 0;

    @Override
    public void setExcludeOutOfRangeValues(boolean excludeOutOfRangeValues) {
        this.excludeOutOfRangeValues = excludeOutOfRangeValues;
    }

    /**
     *
     */
    public GraphRowSumValues() {
        super();
        values = new ConcurrentSkipListMap<Long, GraphPanelChartSumElement>();
    }

    public GraphRowSumValues(boolean doRollingSum) {
        super();
        values = new ConcurrentSkipListMap<Long, GraphPanelChartSumElement>();
        isRollingSum = doRollingSum;
    }

    /**
     *
     * @param xVal
     * @param yVal
     */
    @Override
    public void add(long xVal, double yVal) {
        GraphPanelChartSumElement el;
        if (values.containsKey(xVal)) {
            el = values.get(xVal);
            el.add(yVal);
            yVal = el.getValue();
        } else {
            el = new GraphPanelChartSumElement(yVal);
            values.put(xVal, el);
            countX++;
        }
        super.add(xVal, yVal);
    }

    /**
     *
     * @return
     */
    public Iterator<Entry<Long, AbstractGraphPanelChartElement>> iterator() {
        rollingSum = 0;
        iterator = values.entrySet().iterator();
        return this;
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public Entry<Long, AbstractGraphPanelChartElement> next() {
        Entry<Long, GraphPanelChartSumElement> entry = iterator.next();
        GraphPanelChartSumElement ret = entry.getValue();

        //log.info("Rolling: " + entry.getKey() + " " + rollingSum);
        ExactEntry retValue = null;
        if (isRollingSum) {
            rollingSum += ret.getValue();
            retValue = new ExactEntry(entry.getKey(), new GraphPanelChartSumElement(rollingSum));
        } else {
            retValue = new ExactEntry(entry.getKey(), new GraphPanelChartSumElement(ret.getValue()));
        }
        return retValue;
    }

    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private class ExactEntry
            implements Entry<Long, AbstractGraphPanelChartElement> {

        private long key;
        private final AbstractGraphPanelChartElement value;

        public ExactEntry(long aKey, AbstractGraphPanelChartElement aValue) {
            key = aKey;
            value = aValue;
        }

        public Long getKey() {
            return key;
        }

        public AbstractGraphPanelChartElement getValue() {
            return value;
        }

        public AbstractGraphPanelChartElement setValue(AbstractGraphPanelChartElement value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @Override
    public int size() {
        return countX;
    }

    @Override
    /**
     * Method used to getMaxX without taking in account out of range
     * values. I.E. we don't take in account on value if the distance
     * with the last point is > getGranulationValue() * excludeCount.
     * @return the evaluated MaxX
     */
    public long getMaxX() {
        if (!excludeOutOfRangeValues) {
            return super.getMaxX();
        } else {
            long retMax = 0;
            Iterator<Long> iter = values.keySet().iterator();

            if (iter.hasNext()) {
                retMax = iter.next();
            }
            long excludeValue = getGranulationValue() * excludeCount;
            while (iter.hasNext()) {
                long value = iter.next();

                if (value > retMax) {
                    if ((value - retMax) < excludeValue) {
                        retMax = value;
                    }
                }
            }
            return retMax;
        }
    }

    @Override
    public AbstractGraphPanelChartElement getElement(long value) {
        if (!isRollingSum) {
            return values.get(value);
        } else {
            if (!values.containsKey(value)) {
                return null;
            } else {
                long sum = 0;
                Iterator<Entry<Long, GraphPanelChartSumElement>> it = values.entrySet().iterator();
                boolean valueReached = false;
                while (it.hasNext() && !valueReached) {
                    Entry<Long, GraphPanelChartSumElement> entry = it.next();
                    sum += entry.getValue().getValue();
                    if (entry.getKey() == value) {
                        valueReached = true;
                    }
                }
                return new GraphPanelChartSumElement(sum);
            }
        }
    }
}
