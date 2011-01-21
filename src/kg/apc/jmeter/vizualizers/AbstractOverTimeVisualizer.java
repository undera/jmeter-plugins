package kg.apc.jmeter.vizualizers;

/**
 *
 * @author apc
 */
abstract class AbstractOverTimeVisualizer extends AbstractGraphPanelVisualizer {
    private boolean useRelativeTime;

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
}
