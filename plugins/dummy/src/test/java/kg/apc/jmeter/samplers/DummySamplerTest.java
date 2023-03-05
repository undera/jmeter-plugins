package kg.apc.jmeter.samplers;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class DummySamplerTest {
    @Before
    public void setUpClass() {
        TestJMeterUtils.createJmeterEnv(); // to set UTF-8
    }

    /**
     * Call interrupted to unset the interrupted state, as other classes that implement Interruptible will have an
     * exception thrown as a consequence. eg. HTTPRawSamplerTest.testProcessIO_fileOnly() will always have a
     * java.nio.channels.ClsedByInterruptedException thrown.
     */
    @After
    public void tearDown() {
        Thread.interrupted();
    }

    @Test
    public void testSample() {
        String data = "test";
        DummySampler instance = new DummySampler();
        instance.getDummy().setResponseData(data);
        SampleResult result = instance.sample(null);
        Assert.assertEquals(data, result.getResponseDataAsString());
    }

    @Test
    public void testSample_waiting() {
        DummySampler instance = new DummySampler();
        instance.getDummy().setSimulateWaiting(true);
        instance.getDummy().setResponseTime("100");
        SampleResult result = instance.sample(null);
        Assert.assertNotNull(result);
    }

    @Test
    public void testSample_http() {
        DummySampler instance = new DummySampler();
        instance.getDummy().setSimulateWaiting(true);
        instance.getDummy().setResultClass(HTTPSampleResult.class.getCanonicalName());
        SampleResult result = instance.sample(null);
        Assert.assertTrue(result instanceof HTTPSampleResult);
    }

    @Test
    public void testSample_chinese() {
        String enc1 = JMeterUtils.getProperty("sampleresult.default.encoding");
        String data = "大众";
        DummySampler instance = new DummySampler();
        instance.getDummy().setSuccessful(true);
        instance.getDummy().setResponseData(data);
        SampleResult result = instance.sample(null);
        System.out.println("Enc: " + enc1 + '/' + result.getDataEncodingWithDefault());
        Assert.assertNotNull(result);
        // freaking "static final" DEFAULT_ENCODING field in SampleResult does not allow us to assert this
        Assert.assertTrue("nt With enc: " + enc1 + '/' + result.getDataEncodingWithDefault() + ": " + result.getResponseMessage(), result.isSuccessful());
        Assert.assertEquals("ne With enc: " + enc1 + '/' + result.getDataEncodingWithDefault(), data, result.getResponseDataAsString());
    }

    @Test
    public void testSetSuccessful() {
        DummySampler instance = new DummySampler();
        instance.getDummy().setSuccessful(false);
    }

    @Test
    public void testSetResponseCode() {
        String text = "";
        DummySampler instance = new DummySampler();
        instance.getDummy().setResponseCode(text);
    }

    @Test
    public void testSetResponseMessage() {
        String text = "";
        DummySampler instance = new DummySampler();
        instance.getDummy().setResponseMessage(text);
    }

    @Test
    public void testSetResponseData() {
        String text = "";
        DummySampler instance = new DummySampler();
        instance.getDummy().setResponseData(text);
    }

    @Test
    public void testIsSuccessfull() {
        DummySampler instance = new DummySampler();
        boolean result = instance.getDummy().isSuccessfull();
        Assert.assertFalse(result);
    }

    @Test
    public void testGetResponseCode() {
        DummySampler instance = new DummySampler();
        String expResult = "";
        String result = instance.getDummy().getResponseCode();
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testGetResponseMessage() {
        DummySampler instance = new DummySampler();
        String expResult = "";
        String result = instance.getDummy().getResponseMessage();
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testGetResponseData() {
        DummySampler instance = new DummySampler();
        String expResult = "";
        String result = instance.getDummy().getResponseData();
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testGetResponseTime() {
        DummySampler instance = new DummySampler();
        int expResult = 0;
        int result = instance.getDummy().getResponseTime();
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testSetResponseTime() {
        DummySampler instance = new DummySampler();
        instance.getDummy().setResponseTime("10");
    }

    @Test
    public void testSetRequestData() {
        String text = "";
        DummySampler instance = new DummySampler();
        instance.getDummy().setRequestData(text);
    }

    @Test
    public void testGetRequestData() {
        DummySampler instance = new DummySampler();
        String expResult = "";
        String result = instance.getDummy().getRequestData();
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testGetLatency() {
        DummySampler instance = new DummySampler();
        int expResult = 0;
        int result = instance.getDummy().getLatency();
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testSetSimulateWaiting() {
        DummySampler instance = new DummySampler();
        instance.getDummy().setSimulateWaiting(false);
    }

    @Test
    public void testIsSimulateWaiting() {
        DummySampler instance = new DummySampler();
        boolean result = instance.getDummy().isSimulateWaiting();
        Assert.assertFalse(result);
    }

    @Test
    public void testSetLatency() {
        String time = "";
        DummySampler instance = new DummySampler();
        instance.getDummy().setLatency(time);
    }

    @Test
    public void testInterrupt() {
        DummySampler instance = new DummySampler();
        boolean result = instance.interrupt();
        Assert.assertTrue(result);
    }

    @Test
    public void testSerialize() throws IOException {
        DummySampler instance = new DummySampler();
        ObjectOutputStream os = new ObjectOutputStream(System.out);
        os.writeObject(instance);
    }
}
