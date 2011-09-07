package kg.apc.jmeter.samplers;

import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
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
public class DummySamplerTest {

    /**
     *
     */
    public DummySamplerTest() {
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
     * Test of sample method, of class DummySampler.
     */
    @Test
    public void testSample() {
        System.out.println("sample");
        Entry e = null;
        String data = "test";
        DummySampler instance = new DummySampler();
        instance.setResponseData(data);
        SampleResult result = instance.sample(e);
        assertEquals(data, result.getResponseDataAsString());
    }

    /**
     * Test of setSuccessful method, of class DummySampler.
     */
    @Test
    public void testSetSuccessful() {
        System.out.println("setSuccessful");
        boolean selected = false;
        DummySampler instance = new DummySampler();
        instance.setSuccessful(selected);
    }

    /**
     * Test of setResponseCode method, of class DummySampler.
     */
    @Test
    public void testSetResponseCode() {
        System.out.println("setResponseCode");
        String text = "";
        DummySampler instance = new DummySampler();
        instance.setResponseCode(text);
    }

    /**
     * Test of setResponseMessage method, of class DummySampler.
     */
    @Test
    public void testSetResponseMessage() {
        System.out.println("setResponseMessage");
        String text = "";
        DummySampler instance = new DummySampler();
        instance.setResponseMessage(text);
    }

    /**
     * Test of setResponseData method, of class DummySampler.
     */
    @Test
    public void testSetResponseData() {
        System.out.println("setResponseData");
        String text = "";
        DummySampler instance = new DummySampler();
        instance.setResponseData(text);
    }

    /**
     * Test of isSuccessfull method, of class DummySampler.
     */
    @Test
    public void testIsSuccessfull() {
        System.out.println("isSuccessfull");
        DummySampler instance = new DummySampler();
        boolean expResult = false;
        boolean result = instance.isSuccessfull();
        assertEquals(expResult, result);
    }

    /**
     * Test of getResponseCode method, of class DummySampler.
     */
    @Test
    public void testGetResponseCode() {
        System.out.println("getResponseCode");
        DummySampler instance = new DummySampler();
        String expResult = "";
        String result = instance.getResponseCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of getResponseMessage method, of class DummySampler.
     */
    @Test
    public void testGetResponseMessage() {
        System.out.println("getResponseMessage");
        DummySampler instance = new DummySampler();
        String expResult = "";
        String result = instance.getResponseMessage();
        assertEquals(expResult, result);
    }

    /**
     * Test of getResponseData method, of class DummySampler.
     */
    @Test
    public void testGetResponseData() {
        System.out.println("getResponseData");
        DummySampler instance = new DummySampler();
        String expResult = "";
        String result = instance.getResponseData();
        assertEquals(expResult, result);
    }

    /**
     * Test of getResponseTime method, of class DummySampler.
     */
    @Test
    public void testGetResponseTime() {
        System.out.println("getResponseTime");
        DummySampler instance = new DummySampler();
        int expResult = 0;
        int result = instance.getResponseTime();
        assertEquals(expResult, result);
    }

    /**
     * Test of setResponseTime method, of class DummySampler.
     */
    @Test
    public void testSetResponseTime() {
        System.out.println("setResponseTime");
        DummySampler instance = new DummySampler();
        instance.setResponseTime("10");
    }

    /**
     * Test of setRequestData method, of class DummySampler.
     */
    @Test
    public void testSetRequestData() {
        System.out.println("setRequestData");
        String text = "";
        DummySampler instance = new DummySampler();
        instance.setRequestData(text);
    }

    /**
     * Test of getRequestData method, of class DummySampler.
     */
    @Test
    public void testGetRequestData() {
        System.out.println("getRequestData");
        DummySampler instance = new DummySampler();
        String expResult = "";
        String result = instance.getRequestData();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLatency method, of class DummySampler.
     */
    @Test
    public void testGetLatency() {
        System.out.println("getLatency");
        DummySampler instance = new DummySampler();
        int expResult = 0;
        int result = instance.getLatency();
        assertEquals(expResult, result);
    }

    /**
     * Test of setSimulateWaiting method, of class DummySampler.
     */
    @Test
    public void testSetSimulateWaiting() {
        System.out.println("setSimulateWaiting");
        boolean selected = false;
        DummySampler instance = new DummySampler();
        instance.setSimulateWaiting(selected);
    }

    /**
     * Test of isSimulateWaiting method, of class DummySampler.
     */
    @Test
    public void testIsSimulateWaiting() {
        System.out.println("isSimulateWaiting");
        DummySampler instance = new DummySampler();
        boolean expResult = false;
        boolean result = instance.isSimulateWaiting();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLatency method, of class DummySampler.
     */
    @Test
    public void testSetLatency() {
        System.out.println("setLatency");
        String time = "";
        DummySampler instance = new DummySampler();
        instance.setLatency(time);
    }
}
