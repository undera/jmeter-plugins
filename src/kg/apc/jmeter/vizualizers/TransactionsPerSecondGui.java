package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.charting.ColorsDispatcher;
import kg.apc.jmeter.charting.AbstractGraphRow;
import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author Stephane hoblingre
 */
public class TransactionsPerSecondGui
        extends AbstractOverTimeVisualizer
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
        setGranulation(1000);
        graphPanel.getGraphObject().setyAxisLabel("Number of transactions /sec");
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
            //always add 0 to agg failure row
            if(isSuccess)
            {
                rowAgg = modelAggregate.get(labelAggFailure);
                if (rowAgg == null)
                {
                    rowAgg = getNewRow(modelAggregate, AbstractGraphRow.ROW_SUM_VALUES, labelAggFailure, AbstractGraphRow.MARKER_SIZE_SMALL, false, false, false, true, ColorsDispatcher.RED, true);
                }
                rowAgg.add(time, 0);
            }
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
        return JMeterPluginsUtils.prefixLabel("Transactions per Second");
    }

   @Override
   public void add(SampleResult res)
   {
        super.add(res);
        if (res.isSuccessful())
        {
            addTransaction(true, res.getSampleLabel(), normalizeTime(res.getEndTime()), 1);

        } else
        {
            addTransaction(false, res.getSampleLabel(), normalizeTime(res.getEndTime()), 1);
        }
        updateGui(null);
    }

    @Override
    protected JSettingsPanel getSettingsPanel()
    {
        return new JSettingsPanel(this, true, true, false, true, true, false, false, true, true);
    }

    @Override
    public String getWikiPage() {
        return "TransactionsPerSecond";
    }
}
