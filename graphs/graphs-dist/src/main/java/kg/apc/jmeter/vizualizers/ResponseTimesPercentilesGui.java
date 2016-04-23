package kg.apc.jmeter.vizualizers;

import java.awt.Color;
import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.DividerRenderer;
import kg.apc.charting.rows.GraphRowPercentiles;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.graphs.AbstractGraphPanelVisualizer;
import org.apache.jmeter.samplers.SampleResult;

public class ResponseTimesPercentilesGui
        extends AbstractGraphPanelVisualizer {

    /**
     *
     */
    public ResponseTimesPercentilesGui() {
        super();
        graphPanel.getGraphObject().setForcedMinX(0);
        graphPanel.getGraphObject().setxAxisLabel("Percentiles");
        graphPanel.getGraphObject().setYAxisLabel("Percentile value in ms");
        graphPanel.getGraphObject().setxAxisLabelRenderer(new DividerRenderer(10));
    }

    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Response Times Percentiles");
    }

    @Override
    public void add(SampleResult res) {
        if (!isSampleIncluded(res)) {
            return;
        }
        String label = res.getSampleLabel();
        String aggregateLabel = "Overall Response Times";
        GraphRowPercentiles row = (GraphRowPercentiles) model.get(label);
        GraphRowPercentiles rowAgg = (GraphRowPercentiles) modelAggregate.get(label);

        if (row == null) {
            row = (GraphRowPercentiles) getNewRow(model, AbstractGraphRow.ROW_PERCENTILES, label, AbstractGraphRow.MARKER_SIZE_NONE, false, false, false, true, false);
        }

        if (rowAgg == null) {
            rowAgg = (GraphRowPercentiles) getNewRow(modelAggregate, AbstractGraphRow.ROW_PERCENTILES, aggregateLabel, AbstractGraphRow.MARKER_SIZE_NONE, false, false, false, true, Color.RED, false);
        }

        row.add(res.getTime(), 1);
        rowAgg.add(res.getTime(), 1);
        updateGui(null);
    }

    @Override
    protected JSettingsPanel createSettingsPanel() {
        return new JSettingsPanel(this,
                JSettingsPanel.GRADIENT_OPTION
                | JSettingsPanel.MAXY_OPTION
                | JSettingsPanel.AGGREGATE_OPTION
                | JSettingsPanel.LIMIT_POINT_OPTION
                | JSettingsPanel.MARKERS_OPTION_DISABLED);
    }

    @Override
    public String getWikiPage() {
        return "RespTimePercentiles";
    }
}
