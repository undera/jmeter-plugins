package kg.apc.jmeter.threads;

import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jorphan.collections.HashTree;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author apc
 */
public class SteppingThreadGroupTest {

    /**
     *
     */
    public SteppingThreadGroupTest() {
    }

    /**
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    /**
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     *
     */
    @Before
    public void setUp() {
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
     * Test of scheduleThread method, of class SteppingThreadGroup.
     */
    @Test
    public void testScheduleThread() {
        System.out.println("scheduleThread");
        HashTree hashtree = new HashTree();
        hashtree.add(new LoopController());
        JMeterThread thread = new JMeterThread(hashtree, null, null);
        SteppingThreadGroup instance = new SteppingThreadGroup();
        instance.setNumThreads(10);
        instance.setInUserCount("5");
        instance.setInUserPeriod("30");
        instance.setRampUp("10");
        instance.setThreadGroupDelay("5");
        instance.setFlightTime("60");

        long s1 = -1, s2;
        for (int n = 0; n < 5; n++) {
            thread.setThreadNum(n);
            instance.scheduleThread(thread);
            s2 = thread.getStartTime();
            if (s1 >= 0) {
                assertEquals(2000, s2 - s1);
            }
            s1 = s2;
        }

        thread.setThreadNum(6);
        instance.scheduleThread(thread);
        s2 = thread.getStartTime();
        assertEquals(34000, s2 - s1);
        s1 = s2;

        for (int n = 7; n < 9; n++) {
            thread.setThreadNum(n);
            instance.scheduleThread(thread);
            s2 = thread.getStartTime();
            if (s1 >= 0) {
                assertEquals(2000, s2 - s1);
            }
            s1 = s2;
        }
    }

    /**
     * Test of getThreadGroupDelay method, of class SteppingThreadGroup.
     */
    @Test
    public void testGetThreadGroupDelay() {
        System.out.println("getThreadGroupDelay");
        SteppingThreadGroup instance = new SteppingThreadGroup();
        String expResult = "";
        String result = instance.getThreadGroupDelay();
        assertEquals(expResult, result);
    }

    /**
     * Test of setThreadGroupDelay method, of class SteppingThreadGroup.
     */
    @Test
    public void testSetThreadGroupDelay() {
        System.out.println("setThreadGroupDelay");
        String delay = "";
        SteppingThreadGroup instance = new SteppingThreadGroup();
        instance.setThreadGroupDelay(delay);
    }

    /**
     * Test of getInUserPeriod method, of class SteppingThreadGroup.
     */
    @Test
    public void testGetInUserPeriod() {
        System.out.println("getInUserPeriod");
        SteppingThreadGroup instance = new SteppingThreadGroup();
        String expResult = "";
        String result = instance.getInUserPeriod();
        assertEquals(expResult, result);
    }

    /**
     * Test of setInUserPeriod method, of class SteppingThreadGroup.
     */
    @Test
    public void testSetInUserPeriod() {
        System.out.println("setInUserPeriod");
        String value = "";
        SteppingThreadGroup instance = new SteppingThreadGroup();
        instance.setInUserPeriod(value);
    }

    /**
     * Test of getInUserCount method, of class SteppingThreadGroup.
     */
    @Test
    public void testGetInUserCount() {
        System.out.println("getInUserCount");
        SteppingThreadGroup instance = new SteppingThreadGroup();
        String expResult = "";
        String result = instance.getInUserCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of setInUserCount method, of class SteppingThreadGroup.
     */
    @Test
    public void testSetInUserCount() {
        System.out.println("setInUserCount");
        String delay = "";
        SteppingThreadGroup instance = new SteppingThreadGroup();
        instance.setInUserCount(delay);
    }

    /**
     * Test of getFlightTime method, of class SteppingThreadGroup.
     */
    @Test
    public void testGetFlightTime() {
        System.out.println("getFlightTime");
        SteppingThreadGroup instance = new SteppingThreadGroup();
        String expResult = "";
        String result = instance.getFlightTime();
        assertEquals(expResult, result);
    }

    /**
     * Test of setFlightTime method, of class SteppingThreadGroup.
     */
    @Test
    public void testSetFlightTime() {
        System.out.println("setFlightTime");
        String delay = "";
        SteppingThreadGroup instance = new SteppingThreadGroup();
        instance.setFlightTime(delay);
    }

    /**
     * Test of getOutUserPeriod method, of class SteppingThreadGroup.
     */
    @Test
    public void testGetOutUserPeriod() {
        System.out.println("getOutUserPeriod");
        SteppingThreadGroup instance = new SteppingThreadGroup();
        String expResult = "";
        String result = instance.getOutUserPeriod();
        assertEquals(expResult, result);
    }

    /**
     * Test of setOutUserPeriod method, of class SteppingThreadGroup.
     */
    @Test
    public void testSetOutUserPeriod() {
        System.out.println("setOutUserPeriod");
        String delay = "";
        SteppingThreadGroup instance = new SteppingThreadGroup();
        instance.setOutUserPeriod(delay);
    }

    /**
     * Test of getOutUserCount method, of class SteppingThreadGroup.
     */
    @Test
    public void testGetOutUserCount() {
        System.out.println("getOutUserCount");
        SteppingThreadGroup instance = new SteppingThreadGroup();
        String expResult = "";
        String result = instance.getOutUserCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of setOutUserCount method, of class SteppingThreadGroup.
     */
    @Test
    public void testSetOutUserCount() {
        System.out.println("setOutUserCount");
        String delay = "";
        SteppingThreadGroup instance = new SteppingThreadGroup();
        instance.setOutUserCount(delay);
    }

    /**
     * Test of getRampUp method, of class SteppingThreadGroup.
     */
    @Test
    public void testGetRampUp() {
        System.out.println("getRampUp");
        SteppingThreadGroup instance = new SteppingThreadGroup();
        String expResult = "";
        String result = instance.getRampUp();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRampUp method, of class SteppingThreadGroup.
     */
    @Test
    public void testSetRampUp() {
        System.out.println("setRampUp");
        String delay = "";
        SteppingThreadGroup instance = new SteppingThreadGroup();
        instance.setRampUp(delay);
    }

    /**
     * Test of getThreadGroupDelayAsInt method, of class SteppingThreadGroup.
     */
    @Test
    public void testGetThreadGroupDelayAsInt() {
        System.out.println("getThreadGroupDelayAsInt");
        SteppingThreadGroup instance = new SteppingThreadGroup();
        int expResult = 0;
        int result = instance.getThreadGroupDelayAsInt();
        assertEquals(expResult, result);
    }

    /**
     * Test of getInUserPeriodAsInt method, of class SteppingThreadGroup.
     */
    @Test
    public void testGetInUserPeriodAsInt() {
        System.out.println("getInUserPeriodAsInt");
        SteppingThreadGroup instance = new SteppingThreadGroup();
        int expResult = 0;
        int result = instance.getInUserPeriodAsInt();
        assertEquals(expResult, result);
    }

    /**
     * Test of getInUserCountAsInt method, of class SteppingThreadGroup.
     */
    @Test
    public void testGetInUserCountAsInt() {
        System.out.println("getInUserCountAsInt");
        SteppingThreadGroup instance = new SteppingThreadGroup();
        int expResult = 0;
        int result = instance.getInUserCountAsInt();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRampUpAsInt method, of class SteppingThreadGroup.
     */
    @Test
    public void testGetRampUpAsInt() {
        System.out.println("getRampUpAsInt");
        SteppingThreadGroup instance = new SteppingThreadGroup();
        int expResult = 0;
        int result = instance.getRampUpAsInt();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFlightTimeAsInt method, of class SteppingThreadGroup.
     */
    @Test
    public void testGetFlightTimeAsInt() {
        System.out.println("getFlightTimeAsInt");
        SteppingThreadGroup instance = new SteppingThreadGroup();
        int expResult = 0;
        int result = instance.getFlightTimeAsInt();
        assertEquals(expResult, result);
    }

    /**
     * Test of getOutUserPeriodAsInt method, of class SteppingThreadGroup.
     */
    @Test
    public void testGetOutUserPeriodAsInt() {
        System.out.println("getOutUserPeriodAsInt");
        SteppingThreadGroup instance = new SteppingThreadGroup();
        int expResult = 0;
        int result = instance.getOutUserPeriodAsInt();
        assertEquals(expResult, result);
    }

    /**
     * Test of getOutUserCountAsInt method, of class SteppingThreadGroup.
     */
    @Test
    public void testGetOutUserCountAsInt() {
        System.out.println("getOutUserCountAsInt");
        SteppingThreadGroup instance = new SteppingThreadGroup();
        int expResult = 0;
        int result = instance.getOutUserCountAsInt();
        assertEquals(expResult, result);
    }

    /**
     * Test of setNumThreads method, of class SteppingThreadGroup.
     */
    @Test
    public void testSetNumThreads() {
        System.out.println("setNumThreads");
        String execute = "";
        SteppingThreadGroup instance = new SteppingThreadGroup();
        instance.setNumThreads(execute);
    }

    /**
     * Test of getNumThreadsAsString method, of class SteppingThreadGroup.
     */
    @Test
    public void testGetNumThreadsAsString() {
        System.out.println("getNumThreadsAsString");
        SteppingThreadGroup instance = new SteppingThreadGroup();
        String expResult = "";
        String result = instance.getNumThreadsAsString();
        assertEquals(expResult, result);
    }

    /**
     * Test of testStarted method, of class SteppingThreadGroup.
     */
    @Test
    public void testTestStarted_0args() {
        System.out.println("testStarted");
        SteppingThreadGroup instance = new SteppingThreadGroup();
        instance.testStarted();
    }

    /**
     * Test of testStarted method, of class SteppingThreadGroup.
     */
    @Test
    public void testTestStarted_String() {
        System.out.println("testStarted");
        String string = "";
        SteppingThreadGroup instance = new SteppingThreadGroup();
        instance.testStarted(string);
    }

    /**
     * Test of testEnded method, of class SteppingThreadGroup.
     */
    @Test
    public void testTestEnded_0args() {
        System.out.println("testEnded");
        SteppingThreadGroup instance = new SteppingThreadGroup();
        instance.testEnded();
    }

    /**
     * Test of testEnded method, of class SteppingThreadGroup.
     */
    @Test
    public void testTestEnded_String() {
        System.out.println("testEnded");
        String string = "";
        SteppingThreadGroup instance = new SteppingThreadGroup();
        instance.testEnded(string);
    }

    /**
     * Test of testIterationStart method, of class SteppingThreadGroup.
     */
    @Test
    public void testTestIterationStart() {
        System.out.println("testIterationStart");
        LoopIterationEvent lie = null;
        SteppingThreadGroup instance = new SteppingThreadGroup();
        instance.testIterationStart(lie);
    }
}
