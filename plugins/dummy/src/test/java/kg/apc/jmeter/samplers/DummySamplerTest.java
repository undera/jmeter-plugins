package kg.apc.jmeter.samplers;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DummySamplerTest {
    @Before
    public void setUpClass() throws Exception {
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
        System.out.println("sample");
        String data = "test";
        DummySampler instance = new DummySampler();
        instance.setResponseData(data);
        SampleResult result = instance.sample(null);
        Assert.assertEquals(data, result.getResponseDataAsString());
    }

    @Test
    public void testSample_chinese() {
        String data = "大众";
        DummySampler instance = new DummySampler();
        instance.setResponseData(data);
        SampleResult result = instance.sample(null);
        // freaking "static final" DEFAULT_ENCODING field in SampleResult does not allow us to assert this
        // Assert.assertEquals(data, result.getResponseDataAsString());
    }

    @Test
    public void testSetSuccessful() {
        System.out.println("setSuccessful");
        DummySampler instance = new DummySampler();
        instance.setSuccessful(false);
    }

    @Test
    public void testSetResponseCode() {
        System.out.println("setResponseCode");
        String text = "";
        DummySampler instance = new DummySampler();
        instance.setResponseCode(text);
    }

    @Test
    public void testSetResponseMessage() {
        System.out.println("setResponseMessage");
        String text = "";
        DummySampler instance = new DummySampler();
        instance.setResponseMessage(text);
    }

    @Test
    public void testSetResponseData() {
        System.out.println("setResponseData");
        String text = "";
        DummySampler instance = new DummySampler();
        instance.setResponseData(text);
    }

    @Test
    public void testIsSuccessfull() {
        System.out.println("isSuccessfull");
        DummySampler instance = new DummySampler();
        boolean result = instance.isSuccessfull();
        Assert.assertEquals(false, result);
    }

    @Test
    public void testGetResponseCode() {
        System.out.println("getResponseCode");
        DummySampler instance = new DummySampler();
        String expResult = "";
        String result = instance.getResponseCode();
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testGetResponseMessage() {
        System.out.println("getResponseMessage");
        DummySampler instance = new DummySampler();
        String expResult = "";
        String result = instance.getResponseMessage();
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testGetResponseData() {
        System.out.println("getResponseData");
        DummySampler instance = new DummySampler();
        String expResult = "";
        String result = instance.getResponseData();
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testGetResponseTime() {
        System.out.println("getResponseTime");
        DummySampler instance = new DummySampler();
        int expResult = 0;
        int result = instance.getResponseTime();
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testSetResponseTime() {
        System.out.println("setResponseTime");
        DummySampler instance = new DummySampler();
        instance.setResponseTime("10");
    }

    @Test
    public void testSetRequestData() {
        System.out.println("setRequestData");
        String text = "";
        DummySampler instance = new DummySampler();
        instance.setRequestData(text);
    }

    @Test
    public void testGetRequestData() {
        System.out.println("getRequestData");
        DummySampler instance = new DummySampler();
        String expResult = "";
        String result = instance.getRequestData();
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testGetLatency() {
        System.out.println("getLatency");
        DummySampler instance = new DummySampler();
        int expResult = 0;
        int result = instance.getLatency();
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testSetSimulateWaiting() {
        System.out.println("setSimulateWaiting");
        DummySampler instance = new DummySampler();
        instance.setSimulateWaiting(false);
    }

    @Test
    public void testIsSimulateWaiting() {
        System.out.println("isSimulateWaiting");
        DummySampler instance = new DummySampler();
        boolean result = instance.isSimulateWaiting();
        Assert.assertEquals(false, result);
    }

    @Test
    public void testSetLatency() {
        System.out.println("setLatency");
        String time = "";
        DummySampler instance = new DummySampler();
        instance.setLatency(time);
    }

    @Test
    public void testInterrupt() {
        System.out.println("interrupt");
        DummySampler instance = new DummySampler();
        boolean result = instance.interrupt();
        Assert.assertEquals(true, result);
    }
}
