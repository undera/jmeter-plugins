package kg.apc.jmeter.timers;

import org.apache.jmeter.gui.util.PowerTableModel;
import java.util.LinkedList;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.util.TestJMeterUtils;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
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
      dataModel = new PowerTableModel(VariableThroughputTimerGui.columnIdentifiers, VariableThroughputTimerGui.columnClasses);
      dataModel.addRow(new Integer[]
            {
               1, 10, 3
            });
      dataModel.addRow(new Integer[]
            {
               15, 15, 3
            });
      dataModel.addRow(new Integer[]
            {
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
    public void testDelay1000() throws InterruptedException {
        System.out.println("delay 1000");
        VariableThroughputTimer instance = new VariableThroughputTimer();
        CollectionProperty prop = JMeterPluginsUtils.tableModelToCollectionProperty(dataModel, VariableThroughputTimer.DATA_PROPERTY);
        instance.setData(prop);

        long start=System.currentTimeMillis();
        while ((System.currentTimeMillis()-start)<10*1000) // 10 seconds test
        {
            long result = instance.delay();
            if (result>0)
            {
                Thread.sleep(result/1000);
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

}