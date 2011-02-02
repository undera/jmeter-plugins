package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.charting.DateTimeRenderer;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContextService;

/**
 *
 * @author apc
 */
abstract class AbstractOverTimeVisualizer extends AbstractGraphPanelVisualizer {
    
    private long relativeStartTime=0;

    public AbstractOverTimeVisualizer()
    {
      graphPanel.getGraphObject().setxAxisLabelRenderer(new DateTimeRenderer(DateTimeRenderer.HHMMSS));
      graphPanel.getGraphObject().setxAxisLabel("Elapsed time");
      graphPanel.getGraphObject().setDrawFinalZeroingLines(true);
      graphPanel.getGraphObject().setDisplayPrecision(true);
    }

    @Override
    public void add(SampleResult sample)
    {
        if (relativeStartTime==0)
        {
            //relativeStartTime = JMeterUtils.getPropDefault("TESTSTART.MS", sample.getStartTime());
            relativeStartTime = JMeterContextService.getTestStartTime();
            if(relativeStartTime == 0) relativeStartTime = sample.getStartTime();

            if (graphPanel.getGraphObject().isUseRelativeTime())
                graphPanel.getGraphObject().setxAxisLabelRenderer(new DateTimeRenderer(DateTimeRenderer.HHMMSS, relativeStartTime));
            graphPanel.getGraphObject().setTestStartTime(relativeStartTime);
            graphPanel.getGraphObject().setForcedMinX(relativeStartTime);
        }
    }

    @Override
    public void clearData()
    {
        super.clearData();
        relativeStartTime=0;
    }

    protected long normalizeTime(long time)
    {
        return time - (time - relativeStartTime)%getGranulation();
    }
}
