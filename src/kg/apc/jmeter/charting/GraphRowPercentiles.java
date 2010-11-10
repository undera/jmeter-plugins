package kg.apc.jmeter.charting;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * The algorithm used for percentiles calculation is the one from
 * org.apache.commons.math.stat.descriptive.rank.Percentile
 * @author Stephane Hoblingre
 */
public class GraphRowPercentiles extends AbstractGraphRow
{
    /*
     * The calculated percentiles
     */
    private ConcurrentSkipListMap<Long, GraphPanelChartPercentileElement> percentiles = new ConcurrentSkipListMap<Long, GraphPanelChartPercentileElement>();

    /*
     * The data received from JMeter samples
     * Contains as key the response time, as value the occurence count
     */
    private ConcurrentSkipListMap<Long, GraphPanelChartPercentileElement> values;
    private long virtualSize = 0L;
    private long minRespTime = Long.MAX_VALUE;
    private long maxRespTime = 0L;

    public GraphRowPercentiles()
    {
        super();
        values = new ConcurrentSkipListMap<Long, GraphPanelChartPercentileElement>();
        //create percentiles objects, and reuse them to avoid GC
        for (long p = 1; p < 101; p++)
        {
            percentiles.put(p, new GraphPanelChartPercentileElement(100));
        }
    }

    public void addResponseTime(long respTime)
    {
        if (values.containsKey(respTime))
        {
            values.get(respTime).incValue();
        } else
        {
            values.put(respTime, new GraphPanelChartPercentileElement(1));
        }

        if (minRespTime > respTime)
        {
            minRespTime = respTime;
            super.add(1, minRespTime);
        }
        if (maxRespTime < respTime)
        {
            maxRespTime = respTime;
            super.add(100, maxRespTime);
        }

        virtualSize++;
    }

    private void calculatePercentiles()
    {
        double count = virtualSize;

        if (count == 0)
        {
            return;
        }

        if (count == 1)
        {
            for (long p = 1; p < 101; p++)
            {
                percentiles.get(p).setValue(minRespTime);
            }
        } else
        {
            Iterator<Entry<Long, GraphPanelChartPercentileElement>> iter = values.entrySet().iterator();
            Entry<Long, GraphPanelChartPercentileElement> entry = iter.next();
            long currentVirtualIndex = (long)entry.getValue().getValue();

            for (long p = 1; p < 100; p++)
            {
                double pos = p * (count + 1) / 100;
                double fpos = Math.floor(pos);
                int intPos = (int) fpos;
                double dif = pos - fpos;
                if (pos < 1)
                {
                    percentiles.get(p).setValue(minRespTime);
                } else
                {
                    long upper = -1;
                    long lower = -1;

                    while (upper == -1)
                    {
                        if (intPos - 1 <= currentVirtualIndex && lower == -1)
                        {
                            lower = entry.getKey();
                        }
                        if (intPos <= currentVirtualIndex)
                        {
                            upper = entry.getKey();
                        }
                        if (upper == -1)
                        {
                            entry = iter.next();
                            currentVirtualIndex += entry.getValue().getValue();
                        }
                    }
                   percentiles.get(p).setValue((double) lower + dif * (upper - lower));
                }
            }

            percentiles.get(100l).setValue(maxRespTime); //100 in long, not 1001 !!!
        }
    }

    @Override
    public Iterator iterator()
    {
        calculatePercentiles();
        return percentiles.entrySet().iterator();
    }

    @Override
    public int size()
    {
        if (values.isEmpty())
        {
            return 0;
        } else
        {
            return 100;
        }
    }
}
