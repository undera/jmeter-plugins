package kg.apc.jmeter.graphs;

import kg.apc.charting.DateTimeRenderer;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContextService;

/**
 *
 * @author apc
 */
public abstract class AbstractOverTimeVisualizer
        extends AbstractGraphPanelVisualizer {

    protected long relativeStartTime = 0;

    public AbstractOverTimeVisualizer() {
        graphPanel.getGraphObject().setxAxisLabelRenderer(new DateTimeRenderer(DateTimeRenderer.HHMMSS));
        graphPanel.getGraphObject().setxAxisLabel("Elapsed time");
        graphPanel.getGraphObject().setDisplayPrecision(true);
    }

    @Override
    public void add(SampleResult sample) {
        if (relativeStartTime == 0) {
            relativeStartTime = JMeterContextService.getTestStartTime();
            if (relativeStartTime == 0) {
                relativeStartTime = sample.getStartTime();
            }
            handleRelativeStartTime();
        }
    }

    protected void handleRelativeStartTime() {
        if (graphPanel.getGraphObject().getChartSettings().isUseRelativeTime()) {
            graphPanel.getGraphObject().setxAxisLabelRenderer(new DateTimeRenderer(DateTimeRenderer.HHMMSS, relativeStartTime));
        }
        graphPanel.getGraphObject().setTestStartTime(relativeStartTime);
        graphPanel.getGraphObject().setForcedMinX(relativeStartTime);
    }

    @Override
    public void clearData() {
        super.clearData();
        relativeStartTime = 0;
        updateGui();
    }

    protected long normalizeTime(long time) {
        return time - (time - relativeStartTime) % getGranulation();
    }
}
