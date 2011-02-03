package kg.apc.jmeter.vizualizers;

import java.util.Iterator;
import java.util.Map.Entry;
import kg.apc.jmeter.charting.AbstractGraphPanelChartElement;
import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.charting.ColorsDispatcher;
import kg.apc.jmeter.charting.GraphPanelChartSimpleElement;
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

    private void rebuildAggRow(AbstractGraphRow row)
    {
        Iterator<Entry<Long, AbstractGraphPanelChartElement>> iter = row.iterator();
        while(iter.hasNext())
        {
            Entry<Long, AbstractGraphPanelChartElement> entry = iter.next();
            GraphPanelChartSimpleElement elt = (GraphPanelChartSimpleElement)entry.getValue();
            elt.add(getAllThreadCount(entry.getKey()));
        }
    }

    private void addThreadGroupRecord(String threadGroupName, long time, int numThreads)
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
        rowAgg.add(time, getAllThreadCount(time));

        if(lastAggUpdateTime != time)
        {
            rebuildAggRow(rowAgg);
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
        return "Active Threads Over Time";
    }

    @Override
    public void add(SampleResult res)
    {
        super.add(res);
        String threadName = res.getThreadName();
        threadName = threadName.lastIndexOf(" ") >= 0 ? threadName.substring(0, threadName.lastIndexOf(" ")) : threadName;

        addThreadGroupRecord(threadName, normalizeTime(res.getEndTime()), res.getGroupThreads());
        updateGui(null);
    }

    @Override
    protected JSettingsPanel getSettingsPanel()
    {
        return new JSettingsPanel(this, true, true, false, true, true, false, false, true, true);
    }
}
