package kg.apc.jmeter.timers;

import org.apache.jmeter.gui.util.PowerTableModel;
import java.util.LinkedList;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.util.TestJMeterUtils;
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
        String load="const(10,10s) line(10,100,1m) step(5,25,5,1h)";
        JMeterUtils.setProperty(VariableThroughputTimer.DATA_PROPERTY, load);
        VariableThroughputTimer instance = new VariableThroughputTimer();
        JMeterUtils.setProperty(VariableThroughputTimer.DATA_PROPERTY, ""); // clear!
        JMeterProperty result = instance.getData();
        System.err.println(result);
        assertEquals("[[10, 10, 5, 10, 15, 20, 25], [10, 100, 5, 10, 15, 20, 25], [10, 60, 3600, 3600, 3600, 3600, 3600]]", result.toString());
    }

    @Test
    public void testDelay1000() throws InterruptedException {
        System.out.println("delay 1000");
        VariableThroughputTimer instance = new VariableThroughputTimer();
        CollectionProperty prop = JMeterPluginsUtils.tableModelToCollectionProperty(dataModel, VariableThroughputTimer.DATA_PROPERTY);
        instance.setData(prop);
        //fail("temp");
        long start = System.currentTimeMillis();
        while ((System.currentTimeMillis() - start) < 10 * 1000) // 10 seconds test
        {
            long result = instance.delay();
            if (result > 0) {
                synchronized (this) {
                    wait(result / 1000);
                }
                //System.out.println(result);
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
        int expResult = 0;
        int result = instance.getRPSForSecond(sec);
        assertEquals(expResult, result);
    }
}
