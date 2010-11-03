package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.charting.GraphRowAverages;
import kg.apc.jmeter.charting.GraphRowOverallAverages;
import kg.apc.jmeter.charting.AbstractGraphRow;
import java.awt.Color;
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
        GraphRowAverages row;
        GraphRowOverallAverages avgRow;
        if (!model.containsKey(label) || !model.containsKey(averageLabel))
        {
            row = getNewRow(label);
            avgRow = getNewAveragesRow(averageLabel, row.getColor());
        } else
        {
            row = (GraphRowAverages) model.get(label);
            avgRow = (GraphRowOverallAverages) model.get(averageLabel);
        }

        int allThreads = res.getAllThreads();
        double throughput = (double) allThreads * 1000.0d / time;
        row.add(allThreads, throughput);
        avgRow.add(allThreads, throughput);
        graphPanel.getGraphObject().setCurrentX(allThreads);
        updateGui(null);
    }

    private synchronized GraphRowOverallAverages getNewAveragesRow(String averageLabel, Color color)
    {
        GraphRowOverallAverages avgRow = null;
        if (!model.containsKey(averageLabel))
        {
            avgRow = new GraphRowOverallAverages();
            avgRow.setLabel(averageLabel);
            avgRow.setColor(color);
            avgRow.setMarkerSize(AbstractGraphRow.MARKER_SIZE_BIG);
            avgRow.setDrawValueLabel(true);
            avgRow.setShowInLegend(false);
            model.put(averageLabel, avgRow);
            graphPanel.addRow(avgRow);
        } else
        {
            avgRow = (GraphRowOverallAverages) model.get(averageLabel);
        }
        return avgRow;
    }

    private synchronized GraphRowAverages getNewRow(String label)
    {
        GraphRowAverages row = null;
        if (!model.containsKey(label))
        {
            row = new GraphRowAverages();
            row.setLabel(label);
            row.setColor(colors.getNextColor());
            row.setDrawLine(true);
            row.setMarkerSize(AbstractGraphRow.MARKER_SIZE_SMALL);
            model.put(label, row);
            graphPanel.addRow(row);
        } else
        {
            row = (GraphRowAverages) model.get(label);
        }
        return row;
    }

    @Override
    protected JSettingsPanel getSettingsPanel()
    {
        return new JSettingsPanel(this, false, true, true, false, false);
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
