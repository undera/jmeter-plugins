package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.charting.GraphRowAverages;
import kg.apc.jmeter.charting.GraphRowOverallAverages;
import kg.apc.jmeter.charting.AbstractGraphRow;
import java.awt.Color;
import java.util.concurrent.ConcurrentSkipListMap;
import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author apc
 */
public class TimesVsThreadsGui
        extends AbstractGraphPanelVisualizer
{

    /**
     *
     */
    public TimesVsThreadsGui()
    {
        super();
        graphPanel.getGraphObject().setDrawCurrentX(true);
        graphPanel.getGraphObject().setForcedMinX(0);
    }

    public String getLabelResource()
    {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel()
    {
        return "Response Times vs Threads";
    }

    public void add(SampleResult res)
    {
        String label = res.getSampleLabel();
        String averageLabel = "Average " + res.getSampleLabel();
        String aggLabel = "Aggregated Response Times";
        String avgAggLabel = "Average " + aggLabel;
        GraphRowAverages row;
        GraphRowOverallAverages avgRow;
        GraphRowAverages rowAgg;
        GraphRowOverallAverages avgRowAgg;
        if (!model.containsKey(label) || !model.containsKey(averageLabel))
        {
            row = getNewRow(model, label);
            row.setColor(colors.getNextColor());
            avgRow = getNewAveragesRow(model, averageLabel, row.getColor());
        } else
        {
            row = (GraphRowAverages) model.get(label);
            avgRow = (GraphRowOverallAverages) model.get(averageLabel);
        }

        if (!modelAggregate.containsKey(aggLabel) || !modelAggregate.containsKey(avgAggLabel))
        {
            rowAgg = getNewRow(modelAggregate, aggLabel);
            rowAgg.setColor(Color.RED);
            avgRowAgg = getNewAveragesRow(modelAggregate, avgAggLabel, rowAgg.getColor());
        } else
        {
            rowAgg = (GraphRowAverages) modelAggregate.get(aggLabel);
            avgRowAgg = (GraphRowOverallAverages) modelAggregate.get(avgAggLabel);
        }

        row.add(res.getAllThreads(), res.getTime());
        avgRow.add(res.getAllThreads(), res.getTime());
        rowAgg.add(res.getAllThreads(), res.getTime());
        avgRowAgg.add(res.getAllThreads(), res.getTime());

        graphPanel.getGraphObject().setCurrentX(res.getAllThreads());
        updateGui(null);
    }

    private synchronized GraphRowOverallAverages getNewAveragesRow(ConcurrentSkipListMap<String, AbstractGraphRow> model, String averageLabel, Color color)
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
        } else {
            avgRow = (GraphRowOverallAverages) model.get(averageLabel);
        }
        return avgRow;
    }

    private synchronized GraphRowAverages getNewRow(ConcurrentSkipListMap<String, AbstractGraphRow> model, String label)
    {
        GraphRowAverages row = null;
        if (!model.containsKey(label))
        {
            row = new GraphRowAverages();
            row.setLabel(label);
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
        return new JSettingsPanel(this, false, true, true, false, false, false, true, true);
    }
}
