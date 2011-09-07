package kg.apc.jmeter.perfmon;

import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author APC
 */
public class PerfMonSampleResult
        extends SampleResult {

    private final long ts;

    public PerfMonSampleResult() {
        ts = System.currentTimeMillis();
        //setTimeStamp(ts);
        //setStartTime(ts);
    }

    // storing as latency, multiply by 1000 to keep floating precision
    public void setValue(double value) {
        setStampAndTime(ts, (long) (value * 1000));
        //setEndTime(ts + (int) (value * 1000));
    }

    public double getValue() {
        return ((double) getTime()) / 1000d;
    }
}
