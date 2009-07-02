package kg.apc.jmeter.dotchart;


import org.apache.jmeter.samplers.SampleResult;
// TODO implement lean idea
public class LeanSampleResult
{

    private int repeatCount;
    private int threads;
    private long time;
    private String label;

    public LeanSampleResult()
    {
        repeatCount = 1;
    }

    public LeanSampleResult(SampleResult res)
    {
        this();
        threads = res.getAllThreads();
        time = res.getTime();
        label = (res.getSampleLabel());
    }

    int getThreads()
    {
        return threads;
    }

    long getTime()
    {
        return time;
    }

    String getLabel()
    {
        return label;
    }

    public int getRepeatCount()
    {
        return repeatCount;
    }
}
