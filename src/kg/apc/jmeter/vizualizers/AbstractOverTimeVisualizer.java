package kg.apc.jmeter.vizualizers;

import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author apc
 */
abstract class AbstractOverTimeVisualizer extends AbstractGraphPanelVisualizer {
    private boolean useRelativeTime;
    private boolean isRelativeTimeSet;

    public AbstractOverTimeVisualizer()
    {
      graphPanel.getGraphObject().setxAxisLabelRenderer(new DateTimeRenderer("HH:mm:ss"));
      graphPanel.getGraphObject().setxAxisLabel("Elapsed time");
      graphPanel.getGraphObject().setDrawFinalZeroingLines(true);
      graphPanel.getGraphObject().setDisplayPrecision(true);
    }

    void setUseRelativeTime(boolean selected)
    {
        useRelativeTime=selected;
    }

    public void add(SampleResult sample)
    {
        if (useRelativeTime && !isRelativeTimeSet)
        {
            graphPanel.getGraphObject().setxAxisLabelRenderer(new DateTimeRenderer("HH:mm:ss", sample.getStartTime()));
            isRelativeTimeSet=true;
        }
    }
}
