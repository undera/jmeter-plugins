package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.graphs.AbstractOverTimeVisualizer;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.charting.AbstractGraphRow;
import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author Stephane Hoblingre
 */
public class ResponseCodesPerSecondGui
        extends AbstractOverTimeVisualizer
{
    private String prefix = null;

    /**
     *
     */
    public ResponseCodesPerSecondGui()
    {
        super();
        setGranulation(1000);
        graphPanel.getGraphObject().setYAxisLabel("Number of reponses /sec");
    }

    private void addResponse(String threadGroupName, long time)
    {
        AbstractGraphRow row = model.get(threadGroupName);

        if (row == null)
        {
         row = getNewRow(model, AbstractGraphRow.ROW_SUM_VALUES, threadGroupName, AbstractGraphRow.MARKER_SIZE_SMALL, false, false, false, true, true);
        }

        //fix to have /sec values in all cases
        if (getGranulation() > 0)
        {
            row.add(time, 1 * 1000.0d / getGranulation());
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
        return JMeterPluginsUtils.prefixLabel("Response Codes per Second");
    }

    @Override
    public void clearData()
    {
        super.clearData();
        prefix = null;
    }

    private String getRespCodeLabel(SampleResult res)
    {
        //double ref to be thread safe on clearData call
        String ret = prefix;
        if(ret == null)
        {
            if(res instanceof HTTPSampleResult)
            {
                prefix = "HTTP_";
                ret = "HTTP_";
            } else {
                prefix = "";
                ret = "";
            }
        }
        return ret + res.getResponseCode();
    }
    
    private void addCodes(SampleResult res) {
        SampleResult[] subResults = res.getSubResults();
        if(!isFromTransactionControler(res)) {
            addResponse(getRespCodeLabel(res), normalizeTime(res.getEndTime()));
        }
        
        for(int i=0; i<subResults.length; i++) {
            addCodes(subResults[i]);
        }
    }

    @Override
    public void add(SampleResult res)
    {
        super.add(res);
        addCodes(res);
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
                JSettingsPanel.MAXY_OPTION |
                JSettingsPanel.RELATIVE_TIME_OPTION);
    }

    @Override
    public String getWikiPage() {
        return "ResponseCodesPerSecond";
    }
}
