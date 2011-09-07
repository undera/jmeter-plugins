package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.graphs.AbstractOverTimeVisualizer;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.charting.AbstractGraphRow;
import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author Stephane Hoblingre
 */
public class BytesThroughputOverTimeGui
        extends AbstractOverTimeVisualizer
{
    //private static final Logger log = LoggingManager.getLoggerForClass();

    /**
     *
     */
    public BytesThroughputOverTimeGui()
    {
        super();
        setGranulation(1000);
        graphPanel.getGraphObject().setYAxisLabel("Bytes /sec");
    }

    private void addBytes(String threadGroupName, long time, int value)
    {
        AbstractGraphRow row = model.get(threadGroupName);

        if (row == null)
        {
         row = getNewRow(model, AbstractGraphRow.ROW_SUM_VALUES, threadGroupName, AbstractGraphRow.MARKER_SIZE_SMALL, false, false, false, true, true);
        }

        //fix to have values/sec in all cases
        if (getGranulation() > 0)
        {
            row.add(time, value * 1000.0d / getGranulation());
        }
    }

    public String getLabelResource()
    {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel()
    {
        return JMeterPluginsUtils.prefixLabel("Bytes Throughput Over Time");
    }

   @Override
   public void add(SampleResult res)
   {
        super.add(res);
        addBytes("Bytes Received per Second", normalizeTime(res.getEndTime()), res.getBytes());
        int sentBytes = 0;
        String samplerData = res.getSamplerData();
        if(samplerData != null) sentBytes = samplerData.length();
        addBytes("Bytes Sent per Second", normalizeTime(res.getEndTime()), sentBytes);
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
        return "BytesThroughput";
    }
}
