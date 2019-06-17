package kg.apc.jmeter.modifiers;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DummySubPostProcessorTest {
    @Before
    public void setUp() {
        JMeterContext threadContext = JMeterContextService.getContext();
        SampleResult res = new SampleResult();
        res.sampleStart();
        res.sampleEnd();
        threadContext.setPreviousResult(res);
    }

    @Test
    public void process() {
        DummySubPostProcessor te = new DummySubPostProcessor();
        te.process();
    }
}