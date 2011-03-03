package kg.apc.jmeter.reporters;

import kg.apc.jmeter.util.FileChannelEmul;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.samplers.SampleEvent;
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
public class FlexibleCSVWriterTest {
    private class FlexibleCSVWriter extends kg.apc.jmeter.reporters.FlexibleCSVWriter
    {
        private FileChannelEmul fileEmul=new FileChannelEmul();
        @Override
        protected void openFile() throws FileNotFoundException {
            fileChannel=fileEmul;
        }
    }

    public FlexibleCSVWriterTest() {
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
     * Test of sampleOccurred method, of class FlexibleCSVWriter.
     */
    @Test
    public void testSampleOccurred() {
        System.out.println("sampleOccurred");
        String exp="4";
        SampleResult res=new SampleResult();
        res.setResponseData("test".getBytes());
        SampleEvent e = new SampleEvent(res, "Test");
        FlexibleCSVWriter instance = new FlexibleCSVWriter();
        instance.sampleOccurred(e);
        assertEquals(ByteBuffer.wrap(exp.getBytes()), instance.fileEmul.getWrittenBytes());
    }

    /**
     * Test of sampleStarted method, of class FlexibleCSVWriter.
     */
    @Test
    public void testSampleStarted() {
        System.out.println("sampleStarted");
        SampleEvent e = null;
        FlexibleCSVWriter instance = new FlexibleCSVWriter();
        instance.sampleStarted(e);
    }

    /**
     * Test of sampleStopped method, of class FlexibleCSVWriter.
     */
    @Test
    public void testSampleStopped() {
        System.out.println("sampleStopped");
        SampleEvent e = null;
        FlexibleCSVWriter instance = new FlexibleCSVWriter();
        instance.sampleStopped(e);
    }

    /**
     * Test of testStarted method, of class FlexibleCSVWriter.
     */
    @Test
    public void testTestStarted_0args() {
        System.out.println("testStarted");
        FlexibleCSVWriter instance = new FlexibleCSVWriter();
        instance.testStarted();
    }

    /**
     * Test of testStarted method, of class FlexibleCSVWriter.
     */
    @Test
    public void testTestStarted_String() {
        System.out.println("testStarted");
        String host = "";
        FlexibleCSVWriter instance = new FlexibleCSVWriter();
        instance.testStarted(host);
    }

    /**
     * Test of testEnded method, of class FlexibleCSVWriter.
     */
    @Test
    public void testTestEnded_0args() {
        System.out.println("testEnded");
        FlexibleCSVWriter instance = new FlexibleCSVWriter();
        instance.testEnded();
    }

    /**
     * Test of testEnded method, of class FlexibleCSVWriter.
     */
    @Test
    public void testTestEnded_String() {
        System.out.println("testEnded");
        String host = "";
        FlexibleCSVWriter instance = new FlexibleCSVWriter();
        instance.testEnded(host);
    }

    /**
     * Test of testIterationStart method, of class FlexibleCSVWriter.
     */
    @Test
    public void testTestIterationStart() {
        System.out.println("testIterationStart");
        LoopIterationEvent event = null;
        FlexibleCSVWriter instance = new FlexibleCSVWriter();
        instance.testIterationStart(event);
    }

    /**
     * Test of setFilename method, of class FlexibleCSVWriter.
     */
    @Test
    public void testSetFilename() {
        System.out.println("setFilename");
        String name = "";
        FlexibleCSVWriter instance = new FlexibleCSVWriter();
        instance.setFilename(name);
    }

    /**
     * Test of getFilename method, of class FlexibleCSVWriter.
     */
    @Test
    public void testGetFilename() {
        System.out.println("getFilename");
        FlexibleCSVWriter instance = new FlexibleCSVWriter();
        String expResult = "";
        String result = instance.getFilename();
        assertEquals(expResult, result);
    }

    /**
     * Test of setColumns method, of class FlexibleCSVWriter.
     */
    @Test
    public void testSetColumns() {
        System.out.println("setColumns");
        String cols = "";
        FlexibleCSVWriter instance = new FlexibleCSVWriter();
        instance.setColumns(cols);
    }

    /**
     * Test of getColumns method, of class FlexibleCSVWriter.
     */
    @Test
    public void testGetColumns() {
        System.out.println("getColumns");
        FlexibleCSVWriter instance = new FlexibleCSVWriter();
        String expResult = "";
        String result = instance.getColumns();
        assertEquals(expResult, result);
    }
}