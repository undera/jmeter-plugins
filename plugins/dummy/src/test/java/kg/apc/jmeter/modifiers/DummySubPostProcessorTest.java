package kg.apc.jmeter.modifiers;

import kg.apc.jmeter.samplers.DummySampler;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;

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

    @Test
    public void testSerialize() throws IOException {
        Object instance = new DummySubPostProcessor();
        ObjectOutputStream os = new ObjectOutputStream(System.out);
        os.writeObject(instance);
    }
}