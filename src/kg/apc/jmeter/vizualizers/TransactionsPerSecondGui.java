package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.charting.AbstractGraphRow;
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
        graphPanel.getGraphObject().setxAxisLabel("Elapsed time");
        graphPanel.getGraphObject().setyAxisLabel("Number of transactions");
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
            row = getNewRow(model, AbstractGraphRow.ROW_SUM_VALUES, realRowName, AbstractGraphRow.MARKER_SIZE_SMALL, false, false, false, true, true);
        }

        if (rowAgg == null)
        {
            rowAgg = getNewRow(modelAggregate, AbstractGraphRow.ROW_SUM_VALUES, rowAggName, AbstractGraphRow.MARKER_SIZE_SMALL, false, false, false, true, isSuccess ? ColorsDispatcher.GREEN : ColorsDispatcher.RED, true);
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
