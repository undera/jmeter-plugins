package kg.apc.jmeter.dotchart;

import java.awt.Color;
import java.util.Random;
import java.util.Vector;
import org.apache.jmeter.samplers.SampleResult;

public class SamplingStatCalculatorColored
{

    private Color color;
    private String label;
    private Vector values;
    private double  avgThreads;
    private double avgTime;
    private int count;
    private int maxThreads;
    private long maxTime;

    SamplingStatCalculatorColored(String alabel)
    {
        label = alabel;
        Random r = new Random();
        color = new Color(r.nextInt(0xFFFFFF));
        values = new Vector(0);
    }

    public Color getColor()
    {
        return color;
    }

    public void addSample(SampleResult res)
    {
        // TODO convert to lean
        LeanSampleResult leanres = new LeanSampleResult(res);
        int threads = leanres.getThreads();
        if (threads>maxThreads) maxThreads=threads;
        avgThreads = (avgThreads * count + threads) / (count + 1);
        long time = leanres.getTime();
        if (time>maxTime) maxTime=time;
        avgTime = (avgTime * count + time) / (count + 1);
        count++;

        this.values.add(leanres);
    }

    public int getCount()
    {
        return values.size();
    }

    public String getLabel()
    {
        return label;
    }

    public LeanSampleResult getSample(int count)
    {
        return (LeanSampleResult) values.get(count);
    }

    double getAvgThreads()
    {
        return avgThreads;
    }

    public double getAvgTime()
    {
        return avgTime;
    }

    private int getMaxThreads()
    {
        return maxThreads;
    }

    private long getMaxTime()
    {
        return maxTime;
    }
}
