package kg.apc.jmeter.reporters;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AutoStopTest {
    @BeforeClass
    public static void setUpClass() {
        TestJMeterUtils.createJmeterEnv();
    }

    @Test
    public void testSampleOccurred() throws InterruptedException {
        System.out.println("sampleOccurred");
        SampleResult res = new SampleResult();
        res.setLatency(500);
        SampleEvent se = new SampleEvent(res, "");
        AutoStop instance = new AutoStop();
        instance.setResponseTime("10");
        instance.setResponseTimeSecs("3");
        instance.setErrorRate("0");
        instance.sampleOccurred(se);
        for (int n = 0; n < 5; n++) {
            synchronized (this) {
                wait(1000);
            }
            instance.sampleOccurred(se);
        }
    }

    @Test
    public void testSampleOccurred_error() throws InterruptedException {
        System.out.println("sampleOccurred error");
        SampleResult res = new SampleResult();
        SampleEvent se = new SampleEvent(res, "");
        AutoStop instance = new AutoStop();
        instance.setResponseTime("0");
        instance.setErrorRate("60.6");
        instance.setErrorRateSecs("3");
        instance.setPercentileValue("90");
        instance.setPercentileResponseTimeSecs("10");
        instance.sampleOccurred(se);
        for (int n = 0; n < 5; n++) {
            synchronized (this) {
                wait(1000);
            }
            instance.sampleOccurred(se);
        }
    }

    @Test
    public void testSampleStarted() {
        System.out.println("sampleStarted");
        SampleEvent se = null;
        AutoStop instance = new AutoStop();
        instance.sampleStarted(se);
    }

    @Test
    public void testSampleStopped() {
        System.out.println("sampleStopped");
        SampleEvent se = null;
        AutoStop instance = new AutoStop();
        instance.sampleStopped(se);
    }

    @Test
    public void testTestStarted_0args() {
        System.out.println("testStarted");
        AutoStop instance = new AutoStop();
        instance.testStarted();
    }

    @Test
    public void testTestStarted_String() {
        System.out.println("testStarted");
        String string = "";
        AutoStop instance = new AutoStop();
        instance.testStarted(string);
    }

    @Test
    public void testTestEnded_0args() {
        System.out.println("testEnded");
        AutoStop instance = new AutoStop();
        instance.testEnded();
    }

    @Test
    public void testTestEnded_String() {
        System.out.println("testEnded");
        String string = "";
        AutoStop instance = new AutoStop();
        instance.testEnded(string);
    }

    @Test
    public void testSetResponseTime() {
        System.out.println("setResponseTime");
        String text = "";
        AutoStop instance = new AutoStop();
        instance.setResponseTime(text);
    }

    @Test
    public void testSetErrorRate() {
        System.out.println("setErrorRate");
        String text = "";
        AutoStop instance = new AutoStop();
        instance.setErrorRate(text);
    }

    @Test
    public void testSetResponseTimeSecs() {
        System.out.println("setResponseTimeSecs");
        String text = "";
        AutoStop instance = new AutoStop();
        instance.setResponseTimeSecs(text);
    }

    @Test
    public void testSetErrorRateSecs() {
        System.out.println("setErrorRateSecs");
        String text = "";
        AutoStop instance = new AutoStop();
        instance.setErrorRateSecs(text);
    }

    @Test
    public void testGetResponseTime() {
        System.out.println("getResponseTime");
        AutoStop instance = new AutoStop();
        String expResult = "";
        String result = instance.getResponseTime();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetResponseTimeSecs() {
        System.out.println("getResponseTimeSecs");
        AutoStop instance = new AutoStop();
        String expResult = "";
        String result = instance.getResponseTimeSecs();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetErrorRate() {
        System.out.println("getErrorRate");
        AutoStop instance = new AutoStop();
        String expResult = "";
        String result = instance.getErrorRate();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetErrorRateSecs() {
        System.out.println("getErrorRateSecs");
        AutoStop instance = new AutoStop();
        String expResult = "";
        String result = instance.getErrorRateSecs();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetResponseLatency() {
        System.out.println("setResponseLatency");
        String text = "";
        AutoStop instance = new AutoStop();
        instance.setResponseLatency(text);
    }

    @Test
    public void testSetResponseLatencySecs() {
        System.out.println("setResponseLatencySecs");
        String text = "";
        AutoStop instance = new AutoStop();
        instance.setResponseLatencySecs(text);
    }

    @Test
    public void testGetResponseLatency() {
        System.out.println("getResponseLatency");
        AutoStop instance = new AutoStop();
        String expResult = "";
        String result = instance.getResponseLatency();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetResponseLatencySecs() {
        System.out.println("getResponseLatencySecs");
        AutoStop instance = new AutoStop();
        String expResult = "";
        String result = instance.getResponseLatencySecs();
        assertEquals(expResult, result);
    }

    public void testSetPercentileResponseTime() {
        System.out.println("setPercentileResponseTime");
        String text = "";
        AutoStop instance = new AutoStop();
        instance.setPercentileResponseTime(text);
    }

    public void testSetPercentileResponseTimeSecs() {
        System.out.println("setPercentileResponseTimeSecs");
        String text = "";
        AutoStop instance = new AutoStop();
        instance.setPercentileResponseTimeSecs(text);
    }

    public void testSetPercentileValue() {
        System.out.println("setPercentileValue");
        String text = "";
        AutoStop instance = new AutoStop();
        instance.setPercentileValue(text);
    }

    public void testGetPercentileResponseTime() {
        System.out.println("getPercentileResponseTime");
        AutoStop instance = new AutoStop();
        String expResult = "";
        String result = instance.getPercentileResponseTime();
        assertEquals(expResult, result);
    }

    public void testGetPercentileResponseTimeSecs() {
        System.out.println("getPercentileResponseTimeSecs");
        AutoStop instance = new AutoStop();
        String expResult = "";
        String result = instance.getPercentileResponseTimeSecs();
        assertEquals(expResult, result);
    }

    public void testGetPercentileValue() {
        System.out.println("getPercentileValue");
        AutoStop instance = new AutoStop();
        String expResult = "";
        String result = instance.getPercentileValue();
        assertEquals(expResult, result);
    }
}
