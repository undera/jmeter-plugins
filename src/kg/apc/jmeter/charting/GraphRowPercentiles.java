package kg.apc.jmeter.charting;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * The algorithm used is the one from org.apache.commons.math.stat.descriptive.rank.Percentile
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
    private long virtualSize = 0;
    private long min = Long.MAX_VALUE;
    private long max = Long.MIN_VALUE;

    public GraphRowPercentiles()
    {
        super();
        values = new ConcurrentSkipListMap<Long, GraphPanelChartPercentileElement>();
        //init percentiles
        for (long p = 0; p < 100; p++)
        {
            percentiles.put(p, new GraphPanelChartPercentileElement(0));
        }
    }

    public void addResponseTime(long respTime)
    {
        if (values.containsKey(respTime))
        {
            values.get(respTime).incValue();
        } else
        {
            values.put(respTime, new GraphPanelChartPercentileElement(respTime));
        }

        if (min > respTime)
        {
            min = respTime;
        }
        if (max < respTime)
        {
            max = respTime;
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
                percentiles.get(p).setValue(min);
            }
        } else
        {
            Iterator<Entry<Long, GraphPanelChartPercentileElement>> iter = values.entrySet().iterator();
            Entry<Long, GraphPanelChartPercentileElement> entry = iter.next();
            double currentVirtualIndex = entry.getValue().getValue();

            percentiles.get(1l).setValue(min); //1 in long, not 11 !!!

            for (long p = 2; p < 100; p++)
            {
                double pos = p * (count + 1) / 100;
                double fpos = Math.floor(pos);
                int intPos = (int) fpos;
                double dif = pos - fpos;

                long upper = -1;
                long lower = -1;

                while (lower == -1 && upper == -1)
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

                percentiles.get(p).setValue((double)lower + dif * (upper - lower));

            }

            percentiles.get(100l).setValue(max); //100 in long, not 1001 !!!
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
