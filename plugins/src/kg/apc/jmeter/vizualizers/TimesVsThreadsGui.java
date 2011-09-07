package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.graphs.AbstractVsThreadVisualizer;
import java.awt.Color;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.charting.rows.GraphRowAverages;
import kg.apc.charting.rows.GraphRowOverallAverages;
import kg.apc.charting.AbstractGraphRow;
import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author apc
 */
public class TimesVsThreadsGui
        extends AbstractVsThreadVisualizer
{
    /**
     *
     */
    public TimesVsThreadsGui()
    {
        super();
        graphPanel.getGraphObject().setYAxisLabel("Response times in ms");
    }

    @Override
    public String getLabelResource()
    {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel()
    {
        return JMeterPluginsUtils.prefixLabel("Response Times vs Threads");
    }

    @Override
    public void add(SampleResult res)
    {
        super.add(res);

        String label = res.getSampleLabel();
        String averageLabel = "Average " + res.getSampleLabel();
        String aggLabel = "Overall Response Times";
        String avgAggLabel = "Average " + aggLabel;
        GraphRowAverages row = (GraphRowAverages) model.get(label);
        GraphRowOverallAverages avgRow = (GraphRowOverallAverages) model.get(averageLabel);
        GraphRowAverages rowAgg = (GraphRowAverages) modelAggregate.get(aggLabel);
        GraphRowOverallAverages avgRowAgg = (GraphRowOverallAverages) modelAggregate.get(avgAggLabel);

        if(row == null || avgRow == null)
        {
            row = (GraphRowAverages) getNewRow(model, AbstractGraphRow.ROW_AVERAGES, label, AbstractGraphRow.MARKER_SIZE_SMALL , false, false, false, true, false);
            avgRow = (GraphRowOverallAverages) getNewRow(model, AbstractGraphRow.ROW_OVERALL_AVERAGES, averageLabel, AbstractGraphRow.MARKER_SIZE_BIG , false, true, false, false, row.getColor(), false);
        }

        if(rowAgg == null || avgRowAgg == null)
        {
            rowAgg = (GraphRowAverages) getNewRow(modelAggregate, AbstractGraphRow.ROW_AVERAGES, aggLabel, AbstractGraphRow.MARKER_SIZE_SMALL , false, false, false, true, Color.RED, false);
            avgRowAgg = (GraphRowOverallAverages) getNewRow(modelAggregate, AbstractGraphRow.ROW_OVERALL_AVERAGES, avgAggLabel, AbstractGraphRow.MARKER_SIZE_BIG , false, true, false, false, Color.RED, false);
        }

        int threadsCount = getCurrentThreadCount(res);

        row.add(threadsCount, res.getTime());
        avgRow.add(threadsCount, res.getTime());
        rowAgg.add(threadsCount, res.getTime());
        avgRowAgg.add(threadsCount, res.getTime());

        graphPanel.getGraphObject().setCurrentX(res.getAllThreads());
        updateGui(null);
    }

    @Override
    protected JSettingsPanel createSettingsPanel()
    {
        return new JSettingsPanel(this,
                JSettingsPanel.GRADIENT_OPTION |
                JSettingsPanel.CURRENTX_OPTION |
                JSettingsPanel.HIDE_NON_REP_VALUES_OPTION |
                JSettingsPanel.MAXY_OPTION |
                JSettingsPanel.AGGREGATE_OPTION);
    }

    @Override
    public String getWikiPage() {
       return "ResponseTimesVsThreads";
    }
}
