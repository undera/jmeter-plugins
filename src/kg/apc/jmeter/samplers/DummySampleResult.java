package kg.apc.jmeter.samplers;

import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author undera
 */
class DummySampleResult extends SampleResult {

    public DummySampleResult(int time) {
        long t = System.currentTimeMillis();
        setStartTime(t);
        setEndTime(t + time);
    }
}
