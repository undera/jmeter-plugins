package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.charting.AbstractGraphRow;
import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author Stephane Hoblingre
 */
public class HitsPerSecondGui
        extends AbstractGraphPanelVisualizer
{
    //private static final Logger log = LoggingManager.getLoggerForClass();

    /**
     *
     */
    public HitsPerSecondGui()
    {
        super();
        graphPanel.getGraphObject().setxAxisLabelRenderer(new DateTimeRenderer(
                "HH:mm:ss"));
        graphPanel.getGraphObject().setDrawFinalZeroingLines(true);
        setGranulation(1000);
        graphPanel.getGraphObject().setxAxisLabel("Elapsed time");
        graphPanel.getGraphObject().setyAxisLabel("Number of hits /sec");
    }

    private void addHit(String threadGroupName, long time)
    {
        AbstractGraphRow row = model.get(threadGroupName);

        if (row == null)
        {
         row = getNewRow(model, AbstractGraphRow.ROW_SUM_VALUES, threadGroupName, AbstractGraphRow.MARKER_SIZE_SMALL, false, false, false, true, true);
        }

        //fix to have trans/sec values in all cases
        if (getGranulation() > 0)
        {
            row.add(time, 1 * 1000.0d / getGranulation());
        }
    }

    public String getLabelResource()
    {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel()
    {
        return "Hits Per Second";
    }

    public void add(SampleResult res)
    {
        addHit("Server Hits per Second", res.getStartTime() - res.getStartTime() % getGranulation());
        updateGui(null);
    }

    @Override
    protected JSettingsPanel getSettingsPanel()
    {
        return new JSettingsPanel(this, true, true, false, true, true);
    }
}
