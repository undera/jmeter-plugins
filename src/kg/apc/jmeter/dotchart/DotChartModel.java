package kg.apc.jmeter.dotchart;


import java.util.HashMap;
import org.apache.jmeter.samplers.SampleResult;


/**
 *
 * @author apc
 */
public class DotChartModel
   extends HashMap
{
    public DotChartModel()
    {
        super(0);
    }

    public void addSample(SampleResult res)
    {
        String label = res.getSampleLabel();
        SamplingStatCalculatorColored row;
        if (containsKey(label))
        {
            row = (SamplingStatCalculatorColored) get(label);
        }
        else
        {
            row = new SamplingStatCalculatorColored(label);
            put(label, row);
        }

        row.addSample(res);
    }

    public SamplingStatCalculatorColored get(String key)
    {
        return (SamplingStatCalculatorColored) super.get(key);
    }

    /*
    public Sample getCurrentSample()
    {
        if (!containsKey(lastAddedLabel))
            return null;

        SamplingStatCalculatorColored row = null;
        row = (SamplingStatCalculatorColored) get(lastAddedLabel);

        return row.getCurrentSample();
    }
     */
}
