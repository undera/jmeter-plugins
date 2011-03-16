package kg.apc.jmeter.reporters;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.samplers.SampleEvent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author undera
 */
public class AutoStopTest {

    public AutoStopTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of sampleOccurred method, of class AutoStop.
     */
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

    /**
     * Test of sampleOccurred method, of class AutoStop.
     */
    @Test
    public void testSampleOccurred_error() throws InterruptedException {
        System.out.println("sampleOccurred error");
        SampleResult res = new SampleResult();
        SampleEvent se = new SampleEvent(res, "");
        AutoStop instance = new AutoStop();
        instance.setResponseTime("0");
        instance.setErrorRate("60.6");
        instance.setErrorRateSecs("3");
        instance.sampleOccurred(se);
        for (int n = 0; n < 5; n++) {
            synchronized (this) {
                wait(1000);
            }
            instance.sampleOccurred(se);
        }
    }

    /**
     * Test of sampleStarted method, of class AutoStop.
     */
    @Test
    public void testSampleStarted() {
        System.out.println("sampleStarted");
        SampleEvent se = null;
        AutoStop instance = new AutoStop();
        instance.sampleStarted(se);
    }

    /**
     * Test of sampleStopped method, of class AutoStop.
     */
    @Test
    public void testSampleStopped() {
        System.out.println("sampleStopped");
        SampleEvent se = null;
        AutoStop instance = new AutoStop();
        instance.sampleStopped(se);
    }

    /**
     * Test of testStarted method, of class AutoStop.
     */
    @Test
    public void testTestStarted_0args() {
        System.out.println("testStarted");
        AutoStop instance = new AutoStop();
        instance.testStarted();
    }

    /**
     * Test of testStarted method, of class AutoStop.
     */
    @Test
    public void testTestStarted_String() {
        System.out.println("testStarted");
        String string = "";
        AutoStop instance = new AutoStop();
        instance.testStarted(string);
    }

    /**
     * Test of testEnded method, of class AutoStop.
     */
    @Test
    public void testTestEnded_0args() {
        System.out.println("testEnded");
        AutoStop instance = new AutoStop();
        instance.testEnded();
    }

    /**
     * Test of testEnded method, of class AutoStop.
     */
    @Test
    public void testTestEnded_String() {
        System.out.println("testEnded");
        String string = "";
        AutoStop instance = new AutoStop();
        instance.testEnded(string);
    }

    /**
     * Test of testIterationStart method, of class AutoStop.
     */
    @Test
    public void testTestIterationStart() {
        System.out.println("testIterationStart");
        LoopIterationEvent lie = null;
        AutoStop instance = new AutoStop();
        instance.testIterationStart(lie);
    }

    /**
     * Test of setResponseTime method, of class AutoStop.
     */
    @Test
    public void testSetResponseTime() {
        System.out.println("setResponseTime");
        String text = "";
        AutoStop instance = new AutoStop();
        instance.setResponseTime(text);
    }

    /**
     * Test of setErrorRate method, of class AutoStop.
     */
    @Test
    public void testSetErrorRate() {
        System.out.println("setErrorRate");
        String text = "";
        AutoStop instance = new AutoStop();
        instance.setErrorRate(text);
    }

    /**
     * Test of setResponseTimeSecs method, of class AutoStop.
     */
    @Test
    public void testSetResponseTimeSecs() {
        System.out.println("setResponseTimeSecs");
        String text = "";
        AutoStop instance = new AutoStop();
        instance.setResponseTimeSecs(text);
    }

    /**
     * Test of setErrorRateSecs method, of class AutoStop.
     */
    @Test
    public void testSetErrorRateSecs() {
        System.out.println("setErrorRateSecs");
        String text = "";
        AutoStop instance = new AutoStop();
        instance.setErrorRateSecs(text);
    }

    /**
     * Test of getResponseTime method, of class AutoStop.
     */
    @Test
    public void testGetResponseTime() {
        System.out.println("getResponseTime");
        AutoStop instance = new AutoStop();
        String expResult = "";
        String result = instance.getResponseTime();
        assertEquals(expResult, result);
    }

    /**
     * Test of getResponseTimeSecs method, of class AutoStop.
     */
    @Test
    public void testGetResponseTimeSecs() {
        System.out.println("getResponseTimeSecs");
        AutoStop instance = new AutoStop();
        String expResult = "";
        String result = instance.getResponseTimeSecs();
        assertEquals(expResult, result);
    }

    /**
     * Test of getErrorRate method, of class AutoStop.
     */
    @Test
    public void testGetErrorRate() {
        System.out.println("getErrorRate");
        AutoStop instance = new AutoStop();
        String expResult = "";
        String result = instance.getErrorRate();
        assertEquals(expResult, result);
    }

    /**
     * Test of getErrorRateSecs method, of class AutoStop.
     */
    @Test
    public void testGetErrorRateSecs() {
        System.out.println("getErrorRateSecs");
        AutoStop instance = new AutoStop();
        String expResult = "";
        String result = instance.getErrorRateSecs();
        assertEquals(expResult, result);
    }

    /**
     * Test of setResponseLatency method, of class AutoStop.
     */
    @Test
    public void testSetResponseLatency() {
        System.out.println("setResponseLatency");
        String text = "";
        AutoStop instance = new AutoStop();
        instance.setResponseLatency(text);
    }

    /**
     * Test of setResponseLatencySecs method, of class AutoStop.
     */
    @Test
    public void testSetResponseLatencySecs() {
        System.out.println("setResponseLatencySecs");
        String text = "";
        AutoStop instance = new AutoStop();
        instance.setResponseLatencySecs(text);
    }

    /**
     * Test of getResponseLatency method, of class AutoStop.
     */
    @Test
    public void testGetResponseLatency() {
        System.out.println("getResponseLatency");
        AutoStop instance = new AutoStop();
        String expResult = "";
        String result = instance.getResponseLatency();
        assertEquals(expResult, result);
    }

    /**
     * Test of getResponseLatencySecs method, of class AutoStop.
     */
    @Test
    public void testGetResponseLatencySecs() {
        System.out.println("getResponseLatencySecs");
        AutoStop instance = new AutoStop();
        String expResult = "";
        String result = instance.getResponseLatencySecs();
        assertEquals(expResult, result);
    }
}
