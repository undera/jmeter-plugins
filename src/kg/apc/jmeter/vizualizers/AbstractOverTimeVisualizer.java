package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.charting.DateTimeRenderer;
import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author apc
 */
abstract class AbstractOverTimeVisualizer extends AbstractGraphPanelVisualizer {
    private boolean useRelativeTime;
    private long relativeStartTime=0;

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
        if (relativeStartTime==0)
        {
            relativeStartTime=sample.getStartTime();

            if (useRelativeTime)
                graphPanel.getGraphObject().setxAxisLabelRenderer(new DateTimeRenderer("HH:mm:ss", relativeStartTime));
        }
    }
}
