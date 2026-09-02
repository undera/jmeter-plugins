package kg.apc.jmeter.reporters;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

    // --- New: relative window-to-window percentile tests ---

    /** Creates a SampleResult with the given elapsed time in ms. */
    private static SampleResult resultWithTime(long elapsedMs) {
        SampleResult r = new SampleResult();
        r.sampleStart();
        r.setEndTime(r.getStartTime() + elapsedMs);
        r.setSuccessful(true);
        return r;
    }

    @Test
    public void testSetGetRelPercentileValue() {
        System.out.println("setGetRelPercentileValue");
        AutoStop instance = new AutoStop();
        instance.setRelPercentileValue("95");
        assertEquals("95", instance.getRelPercentileValue());
    }

    @Test
    public void testSetGetRelWindowSecs() {
        System.out.println("setGetRelWindowSecs");
        AutoStop instance = new AutoStop();
        instance.setRelWindowSecs("30");
        assertEquals("30", instance.getRelWindowSecs());
    }

    @Test
    public void testSetGetRelThresholdPct() {
        System.out.println("setGetRelThresholdPct");
        AutoStop instance = new AutoStop();
        instance.setRelThresholdPct("20");
        assertEquals("20", instance.getRelThresholdPct());
    }

    /** Feature is a no-op when percentile rank is not configured (P0 must not be compared). */
    @Test
    public void testRelativeWindowDisabledWhenNotConfigured() throws InterruptedException {
        System.out.println("relativeWindowDisabledWhenNotConfigured");
        System.clearProperty("auto_stopped");
        AutoStop instance = new AutoStop();
        // Rank blank, window and threshold set (Andrei's reproduction case)
        instance.setRelWindowSecs("1");
        instance.setRelThresholdPct("10");
        instance.testStarted();
        for (int i = 0; i < 5; i++) instance.sampleOccurred(new SampleEvent(resultWithTime(100), ""));
        Thread.sleep(1100);
        for (int i = 0; i < 5; i++) instance.sampleOccurred(new SampleEvent(resultWithTime(100), ""));
        Thread.sleep(1100);
        for (int i = 0; i < 5; i++) instance.sampleOccurred(new SampleEvent(resultWithTime(900), ""));
        Thread.sleep(1100);
        instance.sampleOccurred(new SampleEvent(resultWithTime(900), ""));

        assertNull("Blank percentile rank must not trigger shutdown", System.getProperty("auto_stopped"));
    }

    /** Percentile breach across windows stops the test. */
    @Test
    public void testRelativeWindowBreachStopsTest() throws InterruptedException {
        System.out.println("relativeWindowBreachStopsTest");
        System.clearProperty("auto_stopped");
        AutoStop instance = new AutoStop();
        instance.setRelPercentileValue("90");
        instance.setRelWindowSecs("1");
        instance.setRelThresholdPct("50");
        instance.testStarted();

        SampleEvent fastEvent = new SampleEvent(resultWithTime(100), "");
        for (int i = 0; i < 10; i++) { instance.sampleOccurred(fastEvent); }

        Thread.sleep(1100);

        SampleEvent slowEvent = new SampleEvent(resultWithTime(500), "");
        for (int i = 0; i < 10; i++) { instance.sampleOccurred(slowEvent); }

        Thread.sleep(1100);
        instance.sampleOccurred(slowEvent);

        assertEquals("true", System.getProperty("auto_stopped"));
    }

    /** Stable latency across windows must not trigger stop. */
    @Test
    public void testRelativeWindowNoTriggerWhenStable() throws InterruptedException {
        System.out.println("relativeWindowNoTriggerWhenStable");
        System.clearProperty("auto_stopped");
        AutoStop instance = new AutoStop();
        instance.setRelPercentileValue("90");
        instance.setRelWindowSecs("1");
        instance.setRelThresholdPct("50");
        instance.testStarted();

        SampleEvent event = new SampleEvent(resultWithTime(100), "");
        for (int i = 0; i < 10; i++) { instance.sampleOccurred(event); }
        Thread.sleep(1100);
        for (int i = 0; i < 10; i++) { instance.sampleOccurred(event); }
        Thread.sleep(1100);
        instance.sampleOccurred(event);

        assertNull("Stable latency should not stop test", System.getProperty("auto_stopped"));
    }

    /** Verifies that concurrent sampleOccurred calls do not throw exceptions or corrupt state. */
    @Test
    public void testConcurrentSampleOccurredThreadSafety() throws Exception {
        System.out.println("concurrentSampleOccurredThreadSafety");
        System.clearProperty("auto_stopped");
        final AutoStop instance = new AutoStop();
        instance.setRelPercentileValue("90");
        instance.setRelWindowSecs("10");
        instance.setRelThresholdPct("50");
        instance.setErrorCount("100000");
        instance.setErrorCountSecs("10");
        instance.testStarted();

        int numThreads = 8;
        final int samplesPerThread = 500;
        Thread[] threads = new Thread[numThreads];
        final java.util.concurrent.atomic.AtomicBoolean hasError = new java.util.concurrent.atomic.AtomicBoolean(false);

        for (int t = 0; t < numThreads; t++) {
            final int threadId = t;
            threads[t] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < samplesPerThread; i++) {
                            boolean fail = (i % 5 == 0);
                            SampleResult r = resultWithTime(50 + (i % 100));
                            r.setSuccessful(!fail);
                            instance.sampleOccurred(new SampleEvent(r, "Thread " + threadId));
                        }
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                        hasError.set(true);
                    }
                }
            });
            threads[t].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        assertFalse("Concurrent samples must not throw exceptions or corrupt state", hasError.get());
    }

    // --- New: per-window error count ceiling tests ---

    /** Creates a failed SampleResult. */
    private static SampleResult resultFailed() {
        SampleResult r = new SampleResult();
        r.sampleStart();
        r.setEndTime(r.getStartTime() + 50);
        r.setSuccessful(false);
        return r;
    }

    @Test
    public void testSetGetErrorCount() {
        System.out.println("setGetErrorCount");
        AutoStop instance = new AutoStop();
        instance.setErrorCount("15");
        assertEquals("15", instance.getErrorCount());
    }

    @Test
    public void testSetGetErrorCountSecs() {
        System.out.println("setGetErrorCountSecs");
        AutoStop instance = new AutoStop();
        instance.setErrorCountSecs("10");
        assertEquals("10", instance.getErrorCountSecs());
    }

    /** Feature is disabled when error count is not configured. */
    @Test
    public void testErrorCountDisabledWhenNotConfigured() throws InterruptedException {
        System.out.println("errorCountDisabledWhenNotConfigured");
        System.clearProperty("auto_stopped");
        AutoStop instance = new AutoStop();
        instance.setErrorCountSecs("5");
        instance.testStarted();
        SampleEvent event = new SampleEvent(resultFailed(), "");
        for (int i = 0; i < 10; i++) {
            instance.sampleOccurred(event);
        }
        Thread.sleep(1100);
        instance.sampleOccurred(event);

        assertNull("Unconfigured error count must not stop test", System.getProperty("auto_stopped"));
    }

    /** Error count exceeding threshold stops test. */
    @Test
    public void testErrorCountExceededStopsTest() throws InterruptedException {
        System.out.println("errorCountExceededStopsTest");
        System.clearProperty("auto_stopped");
        AutoStop instance = new AutoStop();
        instance.setErrorCount("3");
        instance.setErrorCountSecs("10");
        instance.testStarted();

        SampleEvent event = new SampleEvent(resultFailed(), "");
        for (int i = 0; i < 5; i++) {
            instance.sampleOccurred(event);
        }
        Thread.sleep(1100);
        instance.sampleOccurred(event);

        assertEquals("true", System.getProperty("auto_stopped"));
    }

    /** Error count within limit does not stop test across window boundary. */
    @Test
    public void testErrorCountBelowThresholdDoesNotStop() throws InterruptedException {
        System.out.println("errorCountBelowThresholdDoesNotStop");
        System.clearProperty("auto_stopped");
        AutoStop instance = new AutoStop();
        instance.setErrorCount("5");
        instance.setErrorCountSecs("1");
        instance.testStarted();

        SampleEvent event = new SampleEvent(resultFailed(), "");
        // 2 errors in window 1 (< 5)
        for (int i = 0; i < 2; i++) {
            instance.sampleOccurred(event);
        }
        Thread.sleep(1100);
        // 2 errors in window 2 (< 5)
        for (int i = 0; i < 2; i++) {
            instance.sampleOccurred(event);
        }
        Thread.sleep(1100);
        instance.sampleOccurred(new SampleEvent(resultWithTime(10), ""));

        assertNull("Errors below limit should not stop test", System.getProperty("auto_stopped"));
    }
}
