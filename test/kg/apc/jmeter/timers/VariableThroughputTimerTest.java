package kg.apc.jmeter.timers;

import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.gui.util.PowerTableModel;
import java.util.LinkedList;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.util.JMeterUtils;
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
public class VariableThroughputTimerTest {

    private static class VariableThroughputTimerEmul extends VariableThroughputTimer {

        @Override
        protected void stopTest() {
            throw new RuntimeException("Immediate stop");
        }
    }
    private PowerTableModel dataModel;

    public VariableThroughputTimerTest() {
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
        dataModel = new PowerTableModel(VariableThroughputTimer.columnIdentifiers, VariableThroughputTimer.columnClasses);
        dataModel.addRow(new Integer[]{
                    1, 10, 3
                });
        dataModel.addRow(new Integer[]{
                    15, 15, 3
                });
        dataModel.addRow(new Integer[]{
                    15, 1, 3
                });
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of delay method, of class VariableThroughputTimer.
     */
    @Test
    public void testDelay() {
        System.out.println("delay");
        VariableThroughputTimer instance = new VariableThroughputTimer();
        long expResult = 0L;
        long result = instance.delay();
        assertEquals(expResult, result);
    }

    @Test
    public void testDelay_Prop() {
        System.out.println("delay from property");
        String load = "const(10,10s) line(10,100,1m) step(5,25,5,1h)";
        JMeterUtils.setProperty(VariableThroughputTimer.DATA_PROPERTY, load);
        VariableThroughputTimer instance = new VariableThroughputTimer();
        JMeterUtils.setProperty(VariableThroughputTimer.DATA_PROPERTY, ""); // clear!
        JMeterProperty result = instance.getData();
        assertEquals("[[10, 10, 10], [10, 100, 60], [5, 5, 3600], [10, 10, 3600], [15, 15, 3600], [20, 20, 3600], [25, 25, 3600]]", result.toString());
    }

    @Test
    public void testDelay1000() throws InterruptedException {
        System.out.println("delay 1000");
        VariableThroughputTimer instance = new VariableThroughputTimerEmul();
        CollectionProperty prop = JMeterPluginsUtils.tableModelRowsToCollectionProperty(dataModel, VariableThroughputTimer.DATA_PROPERTY);
        instance.setData(prop);
        long start = System.currentTimeMillis();
        long result = 0;
        while ((System.currentTimeMillis() - start) < 10 * 1000) // 10 seconds test
        {
            try {
                result = instance.delay();
                assertEquals(0, result);
            } catch (RuntimeException ex) {
                if (!ex.getMessage().equals("Immediate stop")) {
                    throw ex;
                }
            }
        }
    }

    /**
     * Test of setData method, of class VariableThroughputTimer.
     */
    @Test
    public void testSetData() {
        System.out.println("setData");
        CollectionProperty rows = new CollectionProperty();
        VariableThroughputTimer instance = new VariableThroughputTimer();
        instance.setData(rows);
    }

    /**
     * Test of getData method, of class VariableThroughputTimer.
     */
    @Test
    public void testGetData() {
        System.out.println("getData");
        VariableThroughputTimer instance = new VariableThroughputTimer();
        instance.setData(new CollectionProperty(VariableThroughputTimer.DATA_PROPERTY, new LinkedList()));
        JMeterProperty result = instance.getData();
        //System.err.println(result.getClass().getCanonicalName());
        assertTrue(result instanceof CollectionProperty);
    }

    /**
     * Test of getRPSForSecond method, of class VariableThroughputTimer.
     */
    @Test
    public void testGetRPSForSecond() {
        System.out.println("getRPSForSecond");
        long sec = 0L;
        VariableThroughputTimer instance = new VariableThroughputTimer();
        int expResult = -1;
        int result = instance.getRPSForSecond(sec);
        assertEquals(expResult, result);
    }

    /**
     * Test of testStarted method, of class VariableThroughputTimer.
     */
    @Test
    public void testTestStarted_0args() {
        System.out.println("testStarted");
        VariableThroughputTimer instance = new VariableThroughputTimer();
        instance.testStarted();
    }

    /**
     * Test of testStarted method, of class VariableThroughputTimer.
     */
    @Test
    public void testTestStarted_String() {
        System.out.println("testStarted");
        String string = "";
        VariableThroughputTimer instance = new VariableThroughputTimer();
        instance.testStarted(string);
    }

    /**
     * Test of testEnded method, of class VariableThroughputTimer.
     */
    @Test
    public void testTestEnded_0args() {
        System.out.println("testEnded");
        VariableThroughputTimer instance = new VariableThroughputTimer();
        instance.testEnded();
    }

    /**
     * Test of testEnded method, of class VariableThroughputTimer.
     */
    @Test
    public void testTestEnded_String() {
        System.out.println("testEnded");
        String string = "";
        VariableThroughputTimer instance = new VariableThroughputTimer();
        instance.testEnded(string);
    }

    /**
     * Test of testIterationStart method, of class VariableThroughputTimer.
     */
    @Test
    public void testTestIterationStart() {
        System.out.println("testIterationStart");
        LoopIterationEvent lie = null;
        VariableThroughputTimer instance = new VariableThroughputTimer();
        instance.testIterationStart(lie);
    }

    /**
     * Test of stopTest method, of class VariableThroughputTimer.
     */
    @Test
    public void testStopTest() {
        System.out.println("stopTest");
        VariableThroughputTimer instance = new VariableThroughputTimer();
        instance.stopTest();
    }
}
