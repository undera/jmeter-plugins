package kg.apc.jmeter.vizualizers;

import java.text.DecimalFormatSymbols;
import kg.apc.jmeter.charting.AbstractGraphRow;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.gui.RateRenderer;

/**
 *
 * @author apc
 */
public class ThroughputOverTimeGui
        extends AbstractGraphPanelVisualizer
{
    //private static final Logger log = LoggingManager.getLoggerForClass();

    /**
     *
     */
    public ThroughputOverTimeGui()
    {
        super();
        graphPanel.getGraphObject().setxAxisLabelRenderer(new DateTimeRenderer(
                "HH:mm:ss"));
        graphPanel.getGraphObject().setyAxisLabelRenderer(new CustomRateRenderer("#.0"));
        graphPanel.getGraphObject().setDrawFinalZeroingLines(true);
        graphPanel.getGraphObject().setxAxisLabel("Elapsed time");
        graphPanel.getGraphObject().setyAxisLabel("Number of estimated transactions /sec");
    }

    private void addThreadGroupRecord(String threadGroupName, long time,
            double rTime)
    {
        AbstractGraphRow row = model.get(threadGroupName);

        if (row == null)
        {
         row = getNewRow(model, AbstractGraphRow.ROW_AVERAGES, threadGroupName, AbstractGraphRow.MARKER_SIZE_SMALL, false, false, false, true, true);
        }

        row.add(time, rTime);
    }

    @Override
    public String getLabelResource()
    {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel()
    {
        return "Transaction Throughput Over Time";
    }

    @Override
    public void add(SampleResult res)
    {
        //change precision to double for accurate results and change calculation
        //to same as throughput vs thread
        double val = res.getTime() == 0 ? 0 : 1000.0d * res.getAllThreads() / res.getTime();
        addThreadGroupRecord(res.getSampleLabel(), res.getEndTime() - res.getEndTime() % getGranulation(), val);
        updateGui(null);
    }

    @Override
    protected JSettingsPanel getSettingsPanel()
    {
        return new JSettingsPanel(this, true, true, false, true, true);
    }

        private class CustomRateRenderer
            extends RateRenderer
    {

        private String zeroLabel;

        public CustomRateRenderer(String format)
        {
            super(format);
            zeroLabel = "0" + new DecimalFormatSymbols().getDecimalSeparator() + "0/sec";
        }

        @Override
        public void setValue(Object value)
        {
            if (value != null && (value instanceof Double) && ((Double)value).doubleValue() == 0)
            {
                setText(zeroLabel);
            } else
            {
                super.setValue(value);
            }
        }
    }
}
