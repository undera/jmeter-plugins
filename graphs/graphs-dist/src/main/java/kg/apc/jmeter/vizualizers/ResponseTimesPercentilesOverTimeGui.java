package kg.apc.jmeter.vizualizers;

import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.rows.GraphRowOverTimePercentile;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.graphs.AbstractOverTimeVisualizer;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ResponseTimesPercentilesOverTimeGui extends AbstractOverTimeVisualizer {

    private static final Logger log = LoggerFactory.getLogger(ResponseTimesPercentilesOverTimeGui.class);
    protected List<Double> percentiles;
    protected Map<Double, Double> shadingFactors; // render each percentile with a lighter alpha value
    protected Map<String, Color> baseColors;

    public ResponseTimesPercentilesOverTimeGui() {
        super();
        setGranulation(30000);
        getGraphPanelChart().getChartSettings().setLineWidth(3);
        getGraphPanelChart().setYAxisLabel("Response times in ms");

        String percentilesConfig = JMeterUtils.getPropDefault("jmeterPlugin.percentilesOverTime", "50,90,95,99");
        try {
            percentiles = Arrays.stream(percentilesConfig.split(","))
                    .map(Double::valueOf)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            log.error("Invalid percentiles configuration", e);
            percentiles = Collections.singletonList(50d);
        }
        shadingFactors = new HashMap<>();
        for (int i = 0; i < percentiles.size(); i++) {
            // color shades should not get lighter than an alpha value of 32 out of 255
            shadingFactors.put(percentiles.get(i), percentiles.size()>1 ? Math.pow(32d/256d, (double)i/(percentiles.size()-1)) : 1);
        }
        baseColors = new HashMap<>();
    }

    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Response Times Percentiles Over Time");
    }

    protected synchronized AbstractGraphRow getNewRow(String label, boolean isAggregate, double percentile) {
        final AbstractGraphRow newRow = new GraphRowOverTimePercentile(percentile);
        final String suffix = new DecimalFormat(" (p#.####)").format(percentile);
        newRow.setLabel(label+suffix);
        newRow.setMarkerSize(AbstractGraphRow.MARKER_SIZE_NONE);
        newRow.setDrawBar(false);
        newRow.setDrawLine(true);
        newRow.setDrawValueLabel(false);
        newRow.setDrawThickLines(false);
        newRow.setShowInLegend(true);

        // Avoid cycling of colors for subsequent percentiles: Let base class assign color only once, then store it.
        Color color = baseColors.get(label);
        if (color != null) {
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(),
                    (int)(color.getAlpha() * shadingFactors.get(percentile)));
        }
        AbstractGraphRow addedRow = getNewRow(newRow, color, isAggregate, true);
        if (color == null) {
            baseColors.put(label, addedRow.getColor());
        }
        return addedRow;
    }

    @Override
    public void add(SampleResult res) {
        if (!isSampleIncluded(res)) {
            return;
        }
        super.add(res);

        final String labelAgg = "Overall Response Times";
        String label = res.getSampleLabel();
        long time = normalizeTime(res.getEndTime());
        long elapsed = res.getTime();

        for (double p : percentiles) {
            getNewRow(label, false, p).add(time, elapsed);
            getNewRow(labelAgg, true, p).add(time, elapsed);
        }

        updateGui(null);
    }

    @Override
    protected JSettingsPanel createSettingsPanel() {
        return new JSettingsPanel(this,
                JSettingsPanel.TIMELINE_OPTION
                        | JSettingsPanel.GRADIENT_OPTION
                        | JSettingsPanel.LIMIT_POINT_OPTION
                        | JSettingsPanel.AGGREGATE_OPTION
                        | JSettingsPanel.MAXY_OPTION
                        | JSettingsPanel.RELATIVE_TIME_OPTION
                        | JSettingsPanel.MARKERS_OPTION);
    }

    @Override
    public String getWikiPage() {
        return "ResponseTimesPercentilesOverTime";
    }
}
