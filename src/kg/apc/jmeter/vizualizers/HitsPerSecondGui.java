package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.charting.AbstractGraphRow;
import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author Stephane Hoblingre
 */
public class HitsPerSecondGui
        extends AbstractOverTimeVisualizer
{
    /**
     *
     */
    public HitsPerSecondGui()
    {
        super();
        setGranulation(1000);
        graphPanel.getGraphObject().setyAxisLabel("Number of hits /sec");
    }

    private void addHit(String threadGroupName, long time, int count)
    {
        AbstractGraphRow row = model.get(threadGroupName);

        if (row == null)
        {
         row = getNewRow(model, AbstractGraphRow.ROW_SUM_VALUES, threadGroupName, AbstractGraphRow.MARKER_SIZE_SMALL, false, false, false, true, true);
        }

        //fix to have trans/sec values in all cases
        if (getGranulation() > 0)
        {
            row.add(time, count * 1000.0d / getGranulation());
        }
    }

    @Override
    public String getLabelResource()
    {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel()
    {
        return JMeterPluginsUtils.prefixLabel("Hits per Second");
    }

    @Override
    public void add(SampleResult res)
    {
        super.add(res);

        int count = res.getSubResults().length;
        if(count == 0)
        {
            count++;
        } else if(!isFromTransactionControler(res))
        {
            count++;
        }
        addHit("Server Hits per Second", normalizeTime(res.getStartTime()), count);
        updateGui(null);
    }

    @Override
    protected JSettingsPanel getSettingsPanel()
    {
        return new JSettingsPanel(this,
                JSettingsPanel.TIMELINE_OPTION |
                JSettingsPanel.GRADIENT_OPTION |
                JSettingsPanel.FINAL_ZEROING_OPTION |
                JSettingsPanel.LIMIT_POINT_OPTION |
                JSettingsPanel.RELATIVE_TIME_OPTION);
    }

    @Override
    public String getWikiPage() {
        return "HitsPerSecond";
    }
}
