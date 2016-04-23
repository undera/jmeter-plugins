package kg.apc.jmeter.vizualizers;

import kg.apc.charting.AbstractGraphRow;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.graphs.AbstractOverTimeVisualizer;
import org.apache.jmeter.samplers.SampleResult;

import java.awt.*;

public class ConnectTimesOverTimeGui extends AbstractOverTimeVisualizer {
    public ConnectTimesOverTimeGui() {
        super();
        graphPanel.getGraphObject().setYAxisLabel("Connect time in ms");
    }

    private void addThreadGroupRecord(String threadGroupName, long time, long numThreads) {
        String labelAgg = "Overall Connect Times";
        AbstractGraphRow row = model.get(threadGroupName);
        AbstractGraphRow rowAgg = modelAggregate.get(labelAgg);
        if (row == null) {
            row = getNewRow(model, AbstractGraphRow.ROW_AVERAGES, threadGroupName, AbstractGraphRow.MARKER_SIZE_SMALL, false, false, false, true, true);
        }
        if (rowAgg == null) {
            rowAgg = getNewRow(modelAggregate, AbstractGraphRow.ROW_AVERAGES, labelAgg, AbstractGraphRow.MARKER_SIZE_SMALL, false, false, false, true, Color.RED, true);
        }

        row.add(time, numThreads);
        rowAgg.add(time, numThreads);
    }

    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Connect Times Over Time");
    }

    @Override
    public void add(SampleResult res) {
        if (!isSampleIncluded(res)) {
            return;
        }
        super.add(res);

        long connTime = res.getConnectTime();

        if (isFromTransactionControler(res)) {
            SampleResult[] subResults = res.getSubResults();
            for (SampleResult subResult : subResults) {
                connTime += subResult.getConnectTime();
            }
        }
        addThreadGroupRecord(res.getSampleLabel(),
                normalizeTime(res.getEndTime()), connTime);
        updateGui(null);
    }

    @Override
    protected JSettingsPanel createSettingsPanel() {
        return new JSettingsPanel(this,
                JSettingsPanel.TIMELINE_OPTION
                        | JSettingsPanel.GRADIENT_OPTION
                        | JSettingsPanel.FINAL_ZEROING_OPTION
                        | JSettingsPanel.LIMIT_POINT_OPTION
                        | JSettingsPanel.RELATIVE_TIME_OPTION
                        | JSettingsPanel.MAXY_OPTION
                        | JSettingsPanel.AGGREGATE_OPTION
                        | JSettingsPanel.MARKERS_OPTION);
    }

    @Override
    public String getWikiPage() {
        return "ConnectTimeOverTime";
    }
}
