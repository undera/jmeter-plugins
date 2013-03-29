package kg.apc.jmeter.reporters;

import net.sf.json.JSONArray;
import org.apache.jmeter.samplers.SampleResult;
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
public class LoadosophiaAggregatorTest {

    public LoadosophiaAggregatorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAddSample() {
        System.out.println("addSample");
        SampleResult res = new SampleResult();
        LoadosophiaAggregator instance = new LoadosophiaAggregator();
        instance.addSample(res);
    }

    @Test
    public void testHaveDataToSend() {
        System.out.println("haveDataToSend");
        LoadosophiaAggregator instance = new LoadosophiaAggregator();
        boolean expResult = false;
        boolean result = instance.haveDataToSend();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetDataToSend() {
        System.out.println("getDataToSend");
        LoadosophiaAggregator instance = new LoadosophiaAggregator();
        String expResult = "[]";
        JSONArray result = instance.getDataToSend();
        assertEquals(expResult, result.toString());
    }
}
