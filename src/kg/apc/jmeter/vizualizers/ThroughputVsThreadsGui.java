package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.graphs.AbstractVsThreadVisualizer;
import kg.apc.charting.rows.GraphRowAverages;
import kg.apc.charting.rows.GraphRowOverallAverages;
import kg.apc.charting.AbstractGraphRow;
import java.text.DecimalFormatSymbols;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.gui.RateRenderer;

/**
 * 
 * @author apc
 */
public class ThroughputVsThreadsGui
        extends AbstractVsThreadVisualizer
{

    /**
     *
     */
    public ThroughputVsThreadsGui()
    {
        super();
        graphPanel.getGraphObject().setyAxisLabelRenderer(new CustomRateRenderer("#.0"));
        graphPanel.getGraphObject().setYAxisLabel("Number of estimated transactions /sec");
    }

    @Override
    public String getLabelResource()
    {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel()
    {
        return JMeterPluginsUtils.prefixLabel("Transaction Throughput vs Threads");
    }

    @Override
    public void add(SampleResult res)
    {
        long time = res.getTime();
        if (time < 1)
        {
            return;
        }

        super.add(res);

        String label = res.getSampleLabel();
        String averageLabel = "Average " + res.getSampleLabel();
        GraphRowAverages row = (GraphRowAverages) model.get(label);
        GraphRowOverallAverages avgRow = (GraphRowOverallAverages) model.get(averageLabel);

        if(row == null || avgRow == null)
        {
            row = (GraphRowAverages) getNewRow(model, AbstractGraphRow.ROW_AVERAGES, label, AbstractGraphRow.MARKER_SIZE_SMALL , false, false, false, true, false);
            avgRow = (GraphRowOverallAverages) getNewRow(model, AbstractGraphRow.ROW_OVERALL_AVERAGES, averageLabel, AbstractGraphRow.MARKER_SIZE_BIG , false, true, false, false, row.getColor(), false);
        }

        int allThreads = getCurrentThreadCount(res);
        double throughput = (double) allThreads * 1000.0d / time;
        row.add(allThreads, throughput);
        avgRow.add(allThreads, throughput);
        graphPanel.getGraphObject().setCurrentX(allThreads);
        updateGui(null);
    }

    @Override
    protected JSettingsPanel createSettingsPanel()
    {
        return new JSettingsPanel(this,
                JSettingsPanel.GRADIENT_OPTION |
                JSettingsPanel.CURRENTX_OPTION |
                JSettingsPanel.MAXY_OPTION |
                JSettingsPanel.HIDE_NON_REP_VALUES_OPTION);
    }

    @Override
    public String getWikiPage() {
        return "TransactionThroughputVsThreads";
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
