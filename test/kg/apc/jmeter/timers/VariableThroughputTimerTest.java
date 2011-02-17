package kg.apc.jmeter.timers;

import java.util.LinkedList;
import kg.apc.jmeter.threads.UltimateThreadGroup;
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

    public VariableThroughputTimerTest() {
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
        System.out.println("delay");
        VariableThroughputTimer instance = new VariableThroughputTimer();
        for (int n=0; n<1500; n++)
        {
            long result = instance.delay();
            //Thread.sleep(result/2);
            //assertEquals(0, result);
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
        instance.setData(new CollectionProperty(UltimateThreadGroup.DATA_PROPERTY, new LinkedList()));
        JMeterProperty result = instance.getData();
        //System.err.println(result.getClass().getCanonicalName());
        assertTrue(result instanceof CollectionProperty);
    }

}