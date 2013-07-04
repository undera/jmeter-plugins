package kg.apc.jmeter.modifiers;

import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author undera
 */
public class FifoPutPostProcessorTest {

    public FifoPutPostProcessorTest() {
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
     * Test of testStarted method, of class FifoPutPostProcessor.
     */
    @Test
    public void testTestStarted_0args() {
        System.out.println("testStarted");
        FifoPutPostProcessor instance = new FifoPutPostProcessor();
        instance.testStarted();
    }

    /**
     * Test of testStarted method, of class FifoPutPostProcessor.
     */
    @Test
    public void testTestStarted_String() {
        System.out.println("testStarted");
        String host = "";
        FifoPutPostProcessor instance = new FifoPutPostProcessor();
        instance.testStarted(host);
    }

    /**
     * Test of testEnded method, of class FifoPutPostProcessor.
     */
    @Test
    public void testTestEnded_0args() {
        System.out.println("testEnded");
        FifoPutPostProcessor instance = new FifoPutPostProcessor();
        instance.testEnded();
    }

    /**
     * Test of testEnded method, of class FifoPutPostProcessor.
     */
    @Test
    public void testTestEnded_String() {
        System.out.println("testEnded");
        String host = "";
        FifoPutPostProcessor instance = new FifoPutPostProcessor();
        instance.testEnded(host);
    }

    /**
     * Test of testIterationStart method, of class FifoPutPostProcessor.
     */
    @Test
    public void testTestIterationStart() {
        System.out.println("testIterationStart");
        LoopIterationEvent event = null;
        FifoPutPostProcessor instance = new FifoPutPostProcessor();
        instance.testIterationStart(event);
    }

    /**
     * Test of process method, of class FifoPutPostProcessor.
     */
    @Test
    public void testProcess() {
        System.out.println("process");
        FifoPutPostProcessor instance = new FifoPutPostProcessor();
        instance.process();
    }

    /**
     * Test of getValue method, of class FifoPutPostProcessor.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        FifoPutPostProcessor instance = new FifoPutPostProcessor();
        String expResult = "";
        String result = instance.getValue();
        assertEquals(expResult, result);
    }

    /**
     * Test of getQueueName method, of class FifoPutPostProcessor.
     */
    @Test
    public void testGetQueueName() {
        System.out.println("getQueueName");
        FifoPutPostProcessor instance = new FifoPutPostProcessor();
        String expResult = "";
        String result = instance.getQueueName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setQueueName method, of class FifoPutPostProcessor.
     */
    @Test
    public void testSetQueueName() {
        System.out.println("setQueueName");
        String text = "";
        FifoPutPostProcessor instance = new FifoPutPostProcessor();
        instance.setQueueName(text);
    }

    /**
     * Test of setValue method, of class FifoPutPostProcessor.
     */
    @Test
    public void testSetValue() {
        System.out.println("setValue");
        String text = "";
        FifoPutPostProcessor instance = new FifoPutPostProcessor();
        instance.setValue(text);
    }
}
