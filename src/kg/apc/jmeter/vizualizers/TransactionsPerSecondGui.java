package kg.apc.jmeter.vizualizers;

import java.awt.Color;
import java.util.concurrent.ConcurrentSkipListMap;
import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.charting.GraphRowSumValues;
import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author Stephane hoblingre
 */
public class TransactionsPerSecondGui
        extends AbstractGraphPanelVisualizer
{
    //private static final Logger log = LoggingManager.getLoggerForClass();

    /**
     *
     */
    private static String labelAggSuccess = "Successful Transactions per Second";
    private static String labelAggFailure = "Failed Transactions per Second";

    public TransactionsPerSecondGui()
    {
        super();
        graphPanel.getGraphObject().setxAxisLabelRenderer(new DateTimeRenderer(
                "HH:mm:ss"));
        graphPanel.getGraphObject().setDrawFinalZeroingLines(true);
        setGranulation(1000);
    }

    private synchronized AbstractGraphRow getNewRow(ConcurrentSkipListMap<String, AbstractGraphRow> model, String label, Color forcedColor)
    {
        AbstractGraphRow row = null;
        if (!model.containsKey(label))
        {
            row = new GraphRowSumValues(false);
            row.setLabel(label);
            if(forcedColor == null)
            {
                row.setColor(colors.getNextColor());
            } else
            {
                row.setColor(forcedColor);
            }
            row.setDrawLine(true);
            row.setMarkerSize(AbstractGraphRow.MARKER_SIZE_SMALL);
            model.put(label, row);
            graphPanel.addRow(row);
        } else
        {
            row = model.get(label);
        }

        return row;
    }

    private AbstractGraphRow getNewRow(ConcurrentSkipListMap<String, AbstractGraphRow> model, String label)
    {
        return getNewRow(model, label, null);
    }

    private void addTransaction(boolean isSuccess, String rowName, long time, double count)
    {
        String realRowName = null;
        String rowAggName = null;

        if(isSuccess)
        {
            realRowName = rowName + " (success)";
            rowAggName = labelAggSuccess;
        } else
        {
            realRowName = rowName + " (failure)";
            rowAggName = labelAggFailure;
        }

        AbstractGraphRow row = model.get(realRowName);
        AbstractGraphRow rowAgg = modelAggregate.get(rowAggName);

        if (row == null)
        {
            row = getNewRow(model, realRowName);
        }
        if (rowAgg == null)
        {
            rowAgg = getNewRow(modelAggregate, rowAggName, isSuccess ? Color.green : Color.red);
        }

        //fix to have trans/sec values in all cases
        if (getGranulation() > 0)
        {
            row.add(time, count * 1000.0d / getGranulation());
            rowAgg.add(time, count * 1000.0d / getGranulation());
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
        return "Transactions per Second";
    }

    @Override
    public void add(SampleResult res)
    {
        //always add 0 failed transactions
        if (res.isSuccessful())
        {
            addTransaction(false, res.getSampleLabel(), res.getEndTime() - res.getEndTime() % getGranulation(), 0);
            addTransaction(true, res.getSampleLabel(), res.getEndTime() - res.getEndTime() % getGranulation(), 1);

        } else
        {
            addTransaction(false, res.getSampleLabel(), res.getEndTime() - res.getEndTime() % getGranulation(), 1);
        }
        updateGui(null);
    }

    @Override
    protected JSettingsPanel getSettingsPanel()
    {
        return new JSettingsPanel(this, true, true, false, true, true, false, false, true);
    }
}
