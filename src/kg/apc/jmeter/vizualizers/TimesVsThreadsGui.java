package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.charting.GraphRowAverages;
import kg.apc.jmeter.charting.GraphRowOverallAverages;
import kg.apc.jmeter.charting.AbstractGraphRow;
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
        GraphRowAverages row = (GraphRowAverages) model.get(label);
        GraphRowOverallAverages avgRow = (GraphRowOverallAverages) model.get(averageLabel);
        GraphRowAverages rowAgg = (GraphRowAverages) modelAggregate.get(aggLabel);
        GraphRowOverallAverages avgRowAgg = (GraphRowOverallAverages) modelAggregate.get(avgAggLabel);

        if(row == null || avgRow == null)
        {
            row = (GraphRowAverages) getNewRow(model, AbstractGraphRow.ROW_AVERAGES, label, AbstractGraphRow.MARKER_SIZE_SMALL , false, false, false, true);
            avgRow = (GraphRowOverallAverages) getNewRow(model, AbstractGraphRow.ROW_OVERALL_AVERAGES, averageLabel, AbstractGraphRow.MARKER_SIZE_BIG , false, true, false, false, row.getColor());
        }

        if(rowAgg == null || avgRowAgg == null)
        {
            rowAgg = (GraphRowAverages) getNewRow(modelAggregate, AbstractGraphRow.ROW_AVERAGES, aggLabel, AbstractGraphRow.MARKER_SIZE_SMALL , false, false, false, true, ColorsDispatcher.RED);
            avgRowAgg = (GraphRowOverallAverages) getNewRow(modelAggregate, AbstractGraphRow.ROW_OVERALL_AVERAGES, avgAggLabel, AbstractGraphRow.MARKER_SIZE_BIG , false, true, false, false, ColorsDispatcher.RED);
        }

        row.add(res.getAllThreads(), res.getTime());
        avgRow.add(res.getAllThreads(), res.getTime());
        rowAgg.add(res.getAllThreads(), res.getTime());
        avgRowAgg.add(res.getAllThreads(), res.getTime());

        graphPanel.getGraphObject().setCurrentX(res.getAllThreads());
        updateGui(null);
    }

    @Override
    protected JSettingsPanel getSettingsPanel()
    {
        return new JSettingsPanel(this, false, true, true, false, false, false, true, true);
    }
}
