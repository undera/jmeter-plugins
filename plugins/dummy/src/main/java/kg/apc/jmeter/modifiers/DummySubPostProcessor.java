package kg.apc.jmeter.modifiers;

import kg.apc.jmeter.dummy.DummyElement;
import org.apache.jmeter.processor.PostProcessor;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.threads.JMeterContext;

public class DummySubPostProcessor extends AbstractTestElement implements PostProcessor {
    private final DummyElement dummy;

    public DummySubPostProcessor() {
        this.dummy = new DummyElement(this);
    }

    @Override
    public void process() {
        JMeterContext context = getThreadContext();
        SampleResult res = context.getPreviousResult();
        res.addSubResult(dummy.sample());
    }

    public DummyElement getDummy() {
        return dummy;
    }
}
