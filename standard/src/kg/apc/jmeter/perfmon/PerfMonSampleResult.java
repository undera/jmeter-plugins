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
    }

    // store as responseTime, multiply by 1000 to keep floating precision
    public void setValue(double value) {
        setStartTime(ts);
        setEndTime(ts + (long) (value * 1000));
    }

    @Deprecated
    public double getValue() {
        return ((double) getTime()) / 1000d;
    }

    //needed for CSV reload as object created by JMeter is not PerfMonSampleResult but SampleResult
    public static double getValue(SampleResult res) {
        return ((double) res.getTime()) / 1000d;
    }
}
