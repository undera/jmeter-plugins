package kg.apc.jmeter.vizualizers;

import java.util.Iterator;
import java.util.Map.Entry;
import kg.apc.jmeter.charting.AbstractGraphPanelChartElement;
import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.charting.ColorsDispatcher;
import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author apc
 */
public class ThreadsStateOverTimeGui
        extends AbstractOverTimeVisualizer
{
    //private static final Logger log = LoggingManager.getLoggerForClass();
    /**
     *
     */
    public ThreadsStateOverTimeGui()
    {
        super();
        graphPanel.getGraphObject().setyAxisLabel("Number of active threads");
    }

    private int getAllThreadCount(long time)
    {
        int ret = 0;
        Iterator<AbstractGraphRow> rowsIter = model.values().iterator();
        while (rowsIter.hasNext())
        {
            AbstractGraphRow row = rowsIter.next();
            AbstractGraphPanelChartElement element = row.getElement(time);

            if (element == null)
            {
                element = row.getFloorElement(time);
            }
            if (element != null)
            {
                ret += element.getValue();
            }
        }

        return ret;
    }

    private void addThreadGroupRecord(String threadGroupName, long time, int numThreads)
    {
        String labelAgg = "Overall Active Threads ";
        AbstractGraphRow row = model.get(threadGroupName);
        AbstractGraphRow rowAgg = modelAggregate.get(labelAgg);

        if (row == null)
        {
            row = getNewRow(model, AbstractGraphRow.ROW_AVERAGES, threadGroupName, AbstractGraphRow.MARKER_SIZE_SMALL, false, false, false, true, true);
            //remove all further aggValues - in case of jtl reload on distributed tests
            //we will not loose data as the jtl have the same time period
            if (rowAgg != null)
            {
                Iterator<Entry<Long, AbstractGraphPanelChartElement>> rowItems = rowAgg.iterator();
                while (rowItems.hasNext())
                {
                    Entry<Long, AbstractGraphPanelChartElement> element = rowItems.next();
                    if (element.getKey() >= time)
                    {
                        rowItems.remove();
                    }
                }
            }
        }
        if (rowAgg == null)
        {
            rowAgg = getNewRow(modelAggregate, AbstractGraphRow.ROW_AVERAGES, labelAgg, AbstractGraphRow.MARKER_SIZE_SMALL, false, false, false, true, ColorsDispatcher.RED, true);
        }

        row.add(time, numThreads);
        rowAgg.add(time, getAllThreadCount(time));
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

        addThreadGroupRecord(threadName, res.getEndTime() - res.getEndTime() % getGranulation(), res.getGroupThreads());
        updateGui(null);
    }

    @Override
    protected JSettingsPanel getSettingsPanel()
    {
        return new JSettingsPanel(this, true, true, false, true, true, false, false, true, true);
    }
}
