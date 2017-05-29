package kg.apc.jmeter.vizualizers;

import org.apache.jmeter.samplers.SampleResult;

public class MonitoringSampleResult
        extends SampleResult {

    private final long ts;

    public MonitoringSampleResult() {
        ts = System.currentTimeMillis();
    }

    // store as responseMessage, as monitoring query can return any value (bigger than float precision)
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

    //needed for CSV reload as object created by JMeter is not MonitoringSampleResult but SampleResult
    public static double getValue(SampleResult res) {
        return Double.valueOf(res.getResponseMessage());
    }
}
