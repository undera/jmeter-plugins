package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.graphs.AbstractOverTimeVisualizer;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.charting.AbstractGraphRow;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.gui.RateRenderer;

/**
 *
 * @author apc
 */
@Deprecated
public class ThroughputOverTimeGui
        extends AbstractOverTimeVisualizer
{
    //private static final Logger log = LoggingManager.getLoggerForClass();
   //do not insert this vizualiser in any JMeter menu
    private Collection<String> emptyCollection = new ArrayList<String>();
    @Override
    public Collection<String> getMenuCategories() {
        return emptyCollection;
    }

    /**
     *
     */
    public ThroughputOverTimeGui()
    {
        super();
        graphPanel.getGraphObject().setyAxisLabelRenderer(new CustomRateRenderer("#.0"));
        graphPanel.getGraphObject().setYAxisLabel("Number of estimated transactions /sec");
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
        return JMeterPluginsUtils.prefixLabel("Transaction Throughput Over Time");
    }

   @Override
   public void add(SampleResult res)
   {
        super.add(res);
        //change precision to double for accurate results and change calculation
        //to same as throughput vs thread
        double val = res.getTime() == 0 ? 0 : 1000.0d * res.getAllThreads() / res.getTime();
        addThreadGroupRecord(res.getSampleLabel(), normalizeTime(res.getEndTime()), val);
        updateGui(null);
    }

    @Override
    protected JSettingsPanel createSettingsPanel()
    {
        return new JSettingsPanel(this,
                JSettingsPanel.TIMELINE_OPTION |
                JSettingsPanel.GRADIENT_OPTION |
                JSettingsPanel.FINAL_ZEROING_OPTION |
                JSettingsPanel.LIMIT_POINT_OPTION |
                JSettingsPanel.MAXY_OPTION |
                JSettingsPanel.RELATIVE_TIME_OPTION);
    }

    @Override
    public String getWikiPage() {
       return "ThroughputOverTime";
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
