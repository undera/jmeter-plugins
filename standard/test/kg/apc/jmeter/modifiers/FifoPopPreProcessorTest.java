package kg.apc.jmeter.modifiers;

import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FifoPopPreProcessorTest {

    public FifoPopPreProcessorTest() {
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
     * Test of testStarted method, of class FifoPopPreProcessor.
     */
    @Test
    public void testTestStarted_0args() {
        System.out.println("testStarted");
        FifoPopPreProcessor instance = new FifoPopPreProcessor();
        instance.testStarted();
    }

    /**
     * Test of testStarted method, of class FifoPopPreProcessor.
     */
    @Test
    public void testTestStarted_String() {
        System.out.println("testStarted");
        String host = "";
        FifoPopPreProcessor instance = new FifoPopPreProcessor();
        instance.testStarted(host);
    }

    /**
     * Test of testEnded method, of class FifoPopPreProcessor.
     */
    @Test
    public void testTestEnded_0args() {
        System.out.println("testEnded");
        FifoPopPreProcessor instance = new FifoPopPreProcessor();
        instance.testEnded();
    }

    /**
     * Test of testEnded method, of class FifoPopPreProcessor.
     */
    @Test
    public void testTestEnded_String() {
        System.out.println("testEnded");
        String host = "";
        FifoPopPreProcessor instance = new FifoPopPreProcessor();
        instance.testEnded(host);
    }

    /**
     * Test of process method, of class FifoPopPreProcessor.
     */
    @Test
    public void testProcess() {
        System.out.println("process");
        FifoPopPreProcessor instance = new FifoPopPreProcessor();
        instance.setTimeout("1");
        instance.process();
    }

    /**
     * Test of getVarName method, of class FifoPopPreProcessor.
     */
    @Test
    public void testGetVarName() {
        System.out.println("getVarName");
        FifoPopPreProcessor instance = new FifoPopPreProcessor();
        String expResult = "";
        String result = instance.getVarName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTimeout method, of class FifoPopPreProcessor.
     */
    @Test
    public void testGetTimeout() {
        System.out.println("getTimeout");
        FifoPopPreProcessor instance = new FifoPopPreProcessor();
        String result = instance.getTimeout();
        assertTrue(!"0".equals(result));
    }

    /**
     * Test of getQueueName method, of class FifoPopPreProcessor.
     */
    @Test
    public void testGetQueueName() {
        System.out.println("getQueueName");
        FifoPopPreProcessor instance = new FifoPopPreProcessor();
        String expResult = "";
        String result = instance.getQueueName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTimeout method, of class FifoPopPreProcessor.
     */
    @Test
    public void testSetTimeout() {
        System.out.println("setTimeout");
        String atimeout = "";
        FifoPopPreProcessor instance = new FifoPopPreProcessor();
        instance.setTimeout(atimeout);
    }

    /**
     * Test of setVarName method, of class FifoPopPreProcessor.
     */
    @Test
    public void testSetVarName() {
        System.out.println("setVarName");
        String text = "";
        FifoPopPreProcessor instance = new FifoPopPreProcessor();
        instance.setVarName(text);
    }

    /**
     * Test of setQueueName method, of class FifoPopPreProcessor.
     */
    @Test
    public void testSetQueueName() {
        System.out.println("setQueueName");
        String text = "";
        FifoPopPreProcessor instance = new FifoPopPreProcessor();
        instance.setQueueName(text);
    }
}
