package kg.apc.jmeter.jmxmon;

import org.apache.jmeter.samplers.SampleResult;

public class JMXMonSampleResult
        extends SampleResult {

    private final long ts;

    public JMXMonSampleResult() {
        ts = System.currentTimeMillis();
    }

    // store as responseMessage, as db query can return any value (bigger than float precision)
    public void setValue(double value) {
        setStartTime(ts);
        setResponseMessage(new Double(value).toString());
    }

    @Override
    public void setResponseMessage(String msg) {
        super.setResponseMessage(msg);
        setStartTime(ts);
    }

    @Deprecated
    public double getValue() {
        return Double.valueOf(getResponseMessage());
    }

    //needed for CSV reload as object created by JMeter is not PerfMonSampleResult but SampleResult
    public static double getValue(SampleResult res) {
        return Double.valueOf(res.getResponseMessage());
    }
}
