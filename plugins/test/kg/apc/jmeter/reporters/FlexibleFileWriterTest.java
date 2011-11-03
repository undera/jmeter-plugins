package kg.apc.jmeter.reporters;

import kg.apc.emulators.FileChannelEmul;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import kg.apc.emulators.TestJMeterUtils;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterVariables;
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
public class FlexibleFileWriterTest {

    private class FlexibleFileWriter extends kg.apc.jmeter.reporters.FlexibleFileWriter {

        private FileChannelEmul fileEmul = new FileChannelEmul();

        @Override
        protected void openFile() throws FileNotFoundException {
            fileChannel = fileEmul;
        }
    }

    public FlexibleFileWriterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
        JMeterUtils.setProperty("sample_variables", "TEST1,TEST2,TEST3");
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
        SampleResult res = new SampleResult();
        res.setResponseData("test".getBytes());
        SampleEvent e = new SampleEvent(res, "Test");
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.setColumns("isSuccsessful|\\t||\\t|latency");
        instance.testStarted();
        for (int n = 0; n < 10; n++) {
            String exp = "0\t|\t" + n;
            System.out.println(exp);
            res.setLatency(n);
            res.setSampleLabel("n" + n);
            instance.sampleOccurred(e);
            ByteBuffer written = instance.fileEmul.getWrittenBytes();
            assertEquals(exp, JMeterPluginsUtils.byteBufferToString(written));
        }
        instance.testEnded();
    }

    @Test
    public void testSampleOccurred_var() {
        System.out.println("sampleOccurred");
        SampleResult res = new SampleResult();
        res.setResponseData("test".getBytes());
        JMeterVariables vars = new JMeterVariables();
        vars.put("TEST1", "TEST");
        SampleEvent e = new SampleEvent(res, "Test", vars);
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.setColumns("variable#0| |variable#| |variable#4t");
        instance.testStarted();
        for (int n = 0; n < 10; n++) {
            String exp = "TEST variable# variable#4t";
            System.out.println(exp);
            instance.sampleOccurred(e);
            ByteBuffer written = instance.fileEmul.getWrittenBytes();
            assertEquals(exp, JMeterPluginsUtils.byteBufferToString(written));
        }
        instance.testEnded();
    }

    /**
     * Test of sampleOccurred method, of class FlexibleCSVWriter.
     */
    @Test
    public void testSampleOccurred_phout() {
        System.out.println("sampleOccurred_phout");

        SampleResult res = new SampleResult();
        res.sampleStart();
        res.setResponseData("test".getBytes());
        res.setResponseCode("200");
        res.setLatency(4);
        res.setSuccessful(true);
        res.sampleEnd();
        SampleEvent e = new SampleEvent(res, "Test");

        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.setColumns("endTimeMillis|\\t\\t|responseTimeMicros|\\t|latencyMicros|\\t|sentBytes|\\t|receivedBytes|\\t|isSuccsessful|\\t|responseCode|\\r\\n");
        instance.testStarted();
        instance.sampleOccurred(e);
        String written = JMeterPluginsUtils.byteBufferToString(instance.fileEmul.getWrittenBytes());
        System.out.println(written);
        assertEquals(8, written.split("\t").length);
        instance.testEnded();
    }

    @Test
    public void testSampleOccurred_labels() {
        System.out.println("sampleOccurred_labels");
        SampleResult res = new SampleResult();
        res.setResponseData("test".getBytes());
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.setColumns("threadName|\\t|sampleLabel");
        instance.testStarted();

        res.setSampleLabel("SAMPLELBL");
        res.setThreadName("THRDNAME");
        String exp = "THRDNAME\tSAMPLELBL";
        SampleEvent e = new SampleEvent(res, "Test");
        instance.sampleOccurred(e);
        ByteBuffer written = instance.fileEmul.getWrittenBytes();
        assertEquals(exp, JMeterPluginsUtils.byteBufferToString(written));

        instance.testEnded();
    }

    /**
     * Test of sampleStarted method, of class FlexibleCSVWriter.
     */
    @Test
    public void testSampleStarted() {
        System.out.println("sampleStarted");
        SampleEvent e = null;
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.sampleStarted(e);
    }

    /**
     * Test of sampleStopped method, of class FlexibleCSVWriter.
     */
    @Test
    public void testSampleStopped() {
        System.out.println("sampleStopped");
        SampleEvent e = null;
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.sampleStopped(e);
    }

    /**
     * Test of testStarted method, of class FlexibleCSVWriter.
     */
    @Test
    public void testTestStarted_0args() {
        System.out.println("testStarted");
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.testStarted();
    }

    /**
     * Test of testStarted method, of class FlexibleCSVWriter.
     */
    @Test
    public void testTestStarted_String() {
        System.out.println("testStarted");
        String host = "";
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.testStarted(host);
    }

    /**
     * Test of testEnded method, of class FlexibleCSVWriter.
     */
    @Test
    public void testTestEnded_0args() {
        System.out.println("testEnded");
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.testEnded();
    }

    /**
     * Test of testEnded method, of class FlexibleCSVWriter.
     */
    @Test
    public void testTestEnded_String() {
        System.out.println("testEnded");
        String host = "";
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.testEnded(host);
    }

    /**
     * Test of testIterationStart method, of class FlexibleCSVWriter.
     */
    @Test
    public void testTestIterationStart() {
        System.out.println("testIterationStart");
        LoopIterationEvent event = null;
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.testIterationStart(event);
    }

    /**
     * Test of setFilename method, of class FlexibleCSVWriter.
     */
    @Test
    public void testSetFilename() {
        System.out.println("setFilename");
        String name = "";
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.setFilename(name);
    }

    /**
     * Test of getFilename method, of class FlexibleCSVWriter.
     */
    @Test
    public void testGetFilename() {
        System.out.println("getFilename");
        FlexibleFileWriter instance = new FlexibleFileWriter();
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
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.setColumns(cols);
    }

    /**
     * Test of getColumns method, of class FlexibleCSVWriter.
     */
    @Test
    public void testGetColumns() {
        System.out.println("getColumns");
        FlexibleFileWriter instance = new FlexibleFileWriter();
        String expResult = "";
        String result = instance.getColumns();
        assertEquals(expResult, result);
    }

    /**
     * Test of openFile method, of class FlexibleFileWriter.
     */
    @Test
    public void testOpenFile() throws Exception {
        System.out.println("openFile");
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.openFile();
    }

    /**
     * Test of isOverwrite method, of class FlexibleFileWriter.
     */
    @Test
    public void testIsOverwrite() {
        System.out.println("isOverwrite");
        FlexibleFileWriter instance = new FlexibleFileWriter();
        boolean expResult = false;
        boolean result = instance.isOverwrite();
        assertEquals(expResult, result);
    }

    /**
     * Test of setOverwrite method, of class FlexibleFileWriter.
     */
    @Test
    public void testSetOverwrite() {
        System.out.println("setOverwrite");
        boolean ov = false;
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.setOverwrite(ov);
    }
}
