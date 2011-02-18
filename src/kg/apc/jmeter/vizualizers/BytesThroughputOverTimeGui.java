package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.charting.AbstractGraphRow;
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
        graphPanel.getGraphObject().setyAxisLabel("Bytes received /sec");
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
        return "Bytes Throughput Over Time";
    }

   @Override
   public void add(SampleResult res)
   {
        super.add(res);
        addBytes("Bytes Received per Second", normalizeTime(res.getEndTime()), res.getBytes());
        addBytes("Bytes Sent per Second", normalizeTime(res.getEndTime()), res.getSamplerData().length());
        updateGui(null);
    }

    @Override
    protected JSettingsPanel getSettingsPanel()
    {
        return new JSettingsPanel(this, true, true, false, true, true, false, false, false, true);
    }
}
