package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.charting.GraphRowSumValues;
import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author Stephane Hoblingre
 */
public class TotalTransactionsPerSecondGui
        extends AbstractGraphPanelVisualizer
{
    //private static final Logger log = LoggingManager.getLoggerForClass();

    /**
     *
     */
    public TotalTransactionsPerSecondGui()
    {
        super();
        graphPanel.getGraphObject().setxAxisLabelRenderer(new DateTimeRenderer(
                "HH:mm:ss"));
        graphPanel.getGraphObject().setDrawFinalZeroingLines(true);
        setGranulation(1000);
    }

    private synchronized AbstractGraphRow getNewRow(String label)
    {
        AbstractGraphRow row = null;
        if(!model.containsKey(label)) {
            row = new GraphRowSumValues(false);
            row.setLabel(label);
            row.setColor(colors.getNextColor());
            row.setDrawLine(true);
            row.setMarkerSize(AbstractGraphRow.MARKER_SIZE_SMALL);
            model.put(label, row);
            graphPanel.addRow(row);
        } else {
            row = model.get(label);
        }

        return row;
    }

    private void addTransaction(String threadGroupName, long time, double count)
    {
        AbstractGraphRow row = model.get(threadGroupName);
        if (row == null)
        {
            row = getNewRow(threadGroupName);
        }

        //fix to have trans/sec values in all cases
        if (getGranulation() > 0)
        {
            row.add(time, count * 1000.0d / getGranulation());
        }
    }

    public String getLabelResource()
    {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel()
    {
        return "Total Transactions Per Second";
    }

    public void add(SampleResult res)
    {
        //always add 0 failed transactions
        if (res.isSuccessful())
        {
            addTransaction("Failed Transactions per Second", res.getEndTime() - res.getEndTime() % getGranulation(), 0);
            addTransaction("Successful Transactions per Second", res.getEndTime() - res.getEndTime() % getGranulation(), 1);
        } else
        {
            addTransaction("Failed Transactions per Second", res.getEndTime() - res.getEndTime() % getGranulation(), 1);
        }
        updateGui(null);
    }

    @Override
    protected JSettingsPanel getSettingsPanel()
    {
        return new JSettingsPanel(this, true, true, false, true, true);
    }
}
