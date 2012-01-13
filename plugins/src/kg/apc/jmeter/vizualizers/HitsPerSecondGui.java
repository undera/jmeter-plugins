package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.graphs.AbstractOverTimeVisualizer;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.charting.AbstractGraphRow;
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
        graphPanel.getGraphObject().setYAxisLabel("Number of hits /sec");
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

    private void addHits(SampleResult res) {
        SampleResult[] subResults = res.getSubResults();

        if(!isFromTransactionControler(res)) {
            addHit("Server Hits per Second", normalizeTime(res.getStartTime()), 1);
        }
        for(int i=0; i<subResults.length; i++) {
            addHits(subResults[i]);
        }
    }
    
    @Override
    public void add(SampleResult res)
    {
        super.add(res);
        addHits(res);
        updateGui(null);
    }

    @Override
    protected JSettingsPanel createSettingsPanel()
    {
        return new JSettingsPanel(this,
                JSettingsPanel.TIMELINE_OPTION |
                JSettingsPanel.GRADIENT_OPTION |
                JSettingsPanel.FINAL_ZEROING_OPTION |
                JSettingsPanel.LIMIT_POINT_OPTION |
                JSettingsPanel.RELATIVE_TIME_OPTION |
                JSettingsPanel.MAXY_OPTION);
    }

    @Override
    public String getWikiPage() {
        return "HitsPerSecond";
    }
}
