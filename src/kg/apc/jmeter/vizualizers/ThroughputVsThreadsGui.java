package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.charting.GraphRowAverages;
import kg.apc.jmeter.charting.GraphRowOverallAverages;
import kg.apc.jmeter.charting.AbstractGraphRow;
import java.text.DecimalFormatSymbols;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.gui.RateRenderer;

/**
 * 
 * @author apc
 */
public class ThroughputVsThreadsGui
        extends AbstractGraphPanelVisualizer
{

    /**
     *
     */
    public ThroughputVsThreadsGui()
    {
        super();
        graphPanel.getGraphObject().setDrawCurrentX(true);
        graphPanel.getGraphObject().setyAxisLabelRenderer(new CustomRateRenderer("#.0"));
        graphPanel.getGraphObject().setForcedMinX(0);
    }

    @Override
    public String getLabelResource()
    {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel()
    {
        return "Transaction Throughput vs Threads";
    }

    @Override
    public void add(SampleResult res)
    {
        long time = res.getTime();
        if (time < 1)
        {
            return;
        }

        String label = res.getSampleLabel();
        String averageLabel = "Average " + res.getSampleLabel();
        GraphRowAverages row = (GraphRowAverages) model.get(label);
        GraphRowOverallAverages avgRow = (GraphRowOverallAverages) model.get(averageLabel);

        if(row == null || avgRow == null)
        {
            row = (GraphRowAverages) getNewRow(model, AbstractGraphRow.ROW_AVERAGES, label, AbstractGraphRow.MARKER_SIZE_SMALL , false, false, false, true, false);
            avgRow = (GraphRowOverallAverages) getNewRow(model, AbstractGraphRow.ROW_OVERALL_AVERAGES, averageLabel, AbstractGraphRow.MARKER_SIZE_BIG , false, true, false, false, row.getColor(), false);
        }

        int allThreads = res.getAllThreads();
        double throughput = (double) allThreads * 1000.0d / time;
        row.add(allThreads, throughput);
        avgRow.add(allThreads, throughput);
        graphPanel.getGraphObject().setCurrentX(allThreads);
        updateGui(null);
    }

    @Override
    protected JSettingsPanel getSettingsPanel()
    {
        return new JSettingsPanel(this, false, true, true, false, false, false, true, false);
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
