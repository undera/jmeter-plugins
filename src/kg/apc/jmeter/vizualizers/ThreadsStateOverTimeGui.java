package kg.apc.jmeter.vizualizers;

import java.util.Iterator;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.charting.AbstractGraphPanelChartElement;
import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.charting.ColorsDispatcher;
import kg.apc.jmeter.charting.GraphPanelChartSimpleElement;
import kg.apc.jmeter.charting.GraphRowSimple;
import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author apc
 */
public class ThreadsStateOverTimeGui
        extends AbstractOverTimeVisualizer
{
    //private static final Logger log = LoggingManager.getLoggerForClass();
    private long lastAggUpdateTime = 0;
    /**
     *
     */
    public ThreadsStateOverTimeGui()
    {
        super();
        graphPanel.getGraphObject().setyAxisLabel("Number of active threads");
    }

    private double getAllThreadCount(long time)
    {
        double ret = 0;
        Iterator<AbstractGraphRow> rowsIter = model.values().iterator();
        while (rowsIter.hasNext())
        {
            AbstractGraphRow row = rowsIter.next();

            //if the tg finished, last value = 0, else we take last known value
            if (time <= (row.getMaxX() + row.getGranulationValue()))
            {
                AbstractGraphPanelChartElement element = row.getElement(time);

                if (element == null)
                {
                    element = row.getLowerElement(time);
                }
                if (element != null)
                {
                    ret += element.getValue();
                }
            }
        }

        return ret;
    }

    //perf fix: only process elements between time and last processed - sampler duration
    private void rebuildAggRow(GraphRowSimple row, long time, long duration)
    {
        long key = row.getHigherKey(lastAggUpdateTime - duration - 1);
        while (key < time  && key != -1)
        {
            GraphPanelChartSimpleElement elt = (GraphPanelChartSimpleElement) row.getElement(key);
            elt.add(getAllThreadCount(key));

            Long nextKey = row.getHigherKey(key);
            if (nextKey != null)
            {
                key = nextKey;
            } else
            {
                key = -1;
            }
        }
    }

    private void addThreadGroupRecord(String threadGroupName, long time, int numThreads, long duration)
    {
        String labelAgg = "Overall Active Threads";
        AbstractGraphRow row = model.get(threadGroupName);
        AbstractGraphRow rowAgg = modelAggregate.get(labelAgg);

        if (row == null)
        {
            row = getNewRow(model, AbstractGraphRow.ROW_AVERAGES, threadGroupName, AbstractGraphRow.MARKER_SIZE_SMALL, false, false, false, true, true);
        }
        if (rowAgg == null)
        {
            rowAgg = getNewRow(modelAggregate, AbstractGraphRow.ROW_SIMPLE, labelAgg, AbstractGraphRow.MARKER_SIZE_SMALL, false, false, false, true, ColorsDispatcher.RED, true);
        }

        row.add(time, numThreads);

        //rebuild is a heavy process, avoided if possible
        if(model.size() == 1)
        {
            rowAgg.add(time, row.getElement(time).getValue());
        } else
        {
            rowAgg.add(time, getAllThreadCount(time));

            //handle 3rd and more jtl reload, the time are reset to start time, so we
            //invalidate lastAggUpdateTime
            if(time<lastAggUpdateTime) lastAggUpdateTime = time-duration;
            
            if(time != lastAggUpdateTime) rebuildAggRow((GraphRowSimple)rowAgg, time, duration); 

            lastAggUpdateTime = time;
        }
    }

    @Override
    public String getLabelResource()
    {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel()
    {
        return JMeterPluginsUtils.prefixLabel("ActiveThreadsOverTime");
    }

    @Override
    public void add(SampleResult res)
    {
        super.add(res);
        String threadName = res.getThreadName();
        threadName = threadName.lastIndexOf(" ") >= 0 ? threadName.substring(0, threadName.lastIndexOf(" ")) : threadName;

        //fix response to fast can miss points
        long timeForAgg = Math.max(getGranulation(), res.getTime());
        addThreadGroupRecord(threadName, normalizeTime(res.getEndTime()), res.getGroupThreads(), timeForAgg);
        updateGui(null);
    }

    @Override
    protected JSettingsPanel getSettingsPanel()
    {
        return new JSettingsPanel(this, true, true, false, true, true, false, false, true, true);
    }

    @Override
    protected String getWikiPage() {
        return "ThreadsStateOverTimeGui";
    }
}
