package kg.apc.jmeter.dbmon;

import org.apache.jmeter.samplers.SampleResult;

public class DbMonSampleResult
        extends SampleResult {

    private final long ts;

    public DbMonSampleResult() {
        ts = System.currentTimeMillis();
    }

    // store as responseMessage, as db query can return any value (bigger than float precision)
    public void setValue(double value) {
        setStartTime(ts);
        setResponseMessage(Double.toString(value));
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
