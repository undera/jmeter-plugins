/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.reporters;

import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author undera
 */
public class ConsoleStatusLoggerTest {

    public ConsoleStatusLoggerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
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
     * Test of sampleOccurred method, of class ConsoleStatusLogger.
     */
    @Test
    public void testSampleOccurred() throws InterruptedException {
        System.out.println("sampleOccurred");
        SampleResult res = new SampleResult();
        res.setResponseCode("200");
        SampleEvent se = new SampleEvent(res, "testTG");
        ConsoleStatusLogger instance = new ConsoleStatusLogger();
        instance.testStarted();
        instance.sampleOccurred(se);
        instance.sampleOccurred(se);
        Thread.sleep(1020);
        instance.sampleOccurred(se);
        instance.sampleOccurred(se);
        Thread.sleep(1020);
        instance.sampleOccurred(se);
        instance.sampleOccurred(se);
    }

    /**
     * Test of sampleStarted method, of class ConsoleStatusLogger.
     */
    @Test
    public void testSampleStarted() {
        System.out.println("sampleStarted");
        SampleEvent se = null;
        ConsoleStatusLogger instance = new ConsoleStatusLogger();
        instance.sampleStarted(se);
    }

    /**
     * Test of sampleStopped method, of class ConsoleStatusLogger.
     */
    @Test
    public void testSampleStopped() {
        System.out.println("sampleStopped");
        SampleEvent se = null;
        ConsoleStatusLogger instance = new ConsoleStatusLogger();
        instance.sampleStopped(se);
    }

    /**
     * Test of testStarted method, of class ConsoleStatusLogger.
     */
    @Test
    public void testTestStarted_0args() {
        System.out.println("testStarted");
        ConsoleStatusLogger instance = new ConsoleStatusLogger();
        instance.testStarted();
    }

    /**
     * Test of testStarted method, of class ConsoleStatusLogger.
     */
    @Test
    public void testTestStarted_String() {
        System.out.println("testStarted");
        String string = "";
        ConsoleStatusLogger instance = new ConsoleStatusLogger();
        instance.testStarted(string);
    }

    /**
     * Test of testEnded method, of class ConsoleStatusLogger.
     */
    @Test
    public void testTestEnded_0args() {
        System.out.println("testEnded");
        ConsoleStatusLogger instance = new ConsoleStatusLogger();
        instance.testEnded();
    }

    /**
     * Test of testEnded method, of class ConsoleStatusLogger.
     */
    @Test
    public void testTestEnded_String() {
        System.out.println("testEnded");
        String string = "";
        ConsoleStatusLogger instance = new ConsoleStatusLogger();
        instance.testEnded(string);
    }

    /**
     * Test of testIterationStart method, of class ConsoleStatusLogger.
     */
    @Test
    public void testTestIterationStart() {
        System.out.println("testIterationStart");
        LoopIterationEvent lie = null;
        ConsoleStatusLogger instance = new ConsoleStatusLogger();
        instance.testIterationStart(lie);
    }
}
