package kg.apc.jmeter.samplers;

import kg.apc.jmeter.dummy.DummyElement;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.Interruptible;
import org.apache.jmeter.samplers.SampleResult;

public class DummySampler extends AbstractSampler implements Interruptible {
    private final DummyElement dummy;

    public DummySampler() {
        this.dummy = new DummyElement(this);
    }

    public boolean interrupt() {
        Thread.currentThread().interrupt();
        return true;
    }

    @Override
    public SampleResult sample(Entry e) {
        return dummy.sample();
    }

    public DummyElement getDummy() {
        return dummy;
    }
}
