package kg.apc.jmeter.reporters;

import java.io.File;
import java.io.IOException;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author undera
 */
public class FlexibleFileWriterTest {

    public FlexibleFileWriterTest() {
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
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of sampleOccurred method, of class FlexibleCSVWriter.
     */
    @Test
    public void testSampleOccurred() throws IOException {
        System.out.println("sampleOccurred");
        SampleResult res = new SampleResult();
        res.setResponseData("test".getBytes());
        SampleEvent e = new SampleEvent(res, "Test");
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.setColumns("isSuccsessful|\\t||\\t|latency");
        String tmpFile = File.createTempFile("ffw_test_", ".txt").getAbsolutePath();
        instance.setFilename(tmpFile);
        instance.testStarted();
        for (int n = 0; n < 10; n++) {
            String exp = "0\t|\t" + n;
            System.out.println(exp);
            res.setLatency(n);
            res.setSampleLabel("n" + n);
            instance.sampleOccurred(e);
            //ByteBuffer written = instance.fileEmul.getWrittenBytes();
            // assertEquals(exp, JMeterPluginsUtils.byteBufferToString(written));
        }
        instance.testEnded();
        assertTrue(tmpFile.length()>0);
    }

    @Test
    public void testSampleOccurred_var() throws IOException {
        System.out.println("sampleOccurred-var");
        SampleResult res = new SampleResult();
        res.setResponseData("test".getBytes());
        JMeterVariables vars = new JMeterVariables();
        vars.put("TEST1", "TEST");
        SampleEvent e = new SampleEvent(res, "Test", vars);
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.setFilename(File.createTempFile("ffw_test_", ".txt").getAbsolutePath());
        System.out.println("prop: " + JMeterUtils.getProperty("sample_variables"));
        System.out.println("count: " + SampleEvent.getVarCount());
        instance.setColumns("variable#0| |variable#| |variable#4t");
        instance.testStarted();
        for (int n = 0; n < 10; n++) {
            String exp = "TEST variable# variable#4t";
            System.out.println(exp);
            instance.sampleOccurred(e);
            //ByteBuffer written = instance.fileEmul.getWrittenBytes();
            //assertEquals(exp, JMeterPluginsUtils.byteBufferToString(written));
        }
        instance.testEnded();
    }

    /**
     * Test of sampleOccurred method, of class FlexibleCSVWriter.
     */
    @Test
    public void testSampleOccurred_phout() throws IOException {
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
        instance.setFilename(File.createTempFile("ffw_test_", ".txt").getAbsolutePath());
        instance.setColumns("endTimeMillis|\\t\\t|responseTimeMicros|\\t|latencyMicros|\\t|sentBytes|\\t|receivedBytes|\\t|isSuccsessful|\\t|responseCode|\\r\\n");
        instance.testStarted();
        instance.sampleOccurred(e);
        //String written = JMeterPluginsUtils.byteBufferToString(instance.fileEmul.getWrittenBytes());
        //System.out.println(written);
        //assertEquals(8, written.split("\t").length);
        instance.testEnded();
    }

    @Test
    public void testSampleOccurred_labels() throws IOException {
        System.out.println("sampleOccurred_labels");
        SampleResult res = new SampleResult();
        res.setResponseData("test".getBytes());
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.setFilename(File.createTempFile("ffw_test_", ".txt").getAbsolutePath());
        instance.setColumns("threadName|\\t|sampleLabel");
        instance.testStarted();

        res.setSampleLabel("SAMPLELBL");
        res.setThreadName("THRDNAME");
        String exp = "THRDNAME\tSAMPLELBL";
        SampleEvent e = new SampleEvent(res, "Test");
        instance.sampleOccurred(e);
        //ByteBuffer written = instance.fileEmul.getWrittenBytes();
        //assertEquals(exp, JMeterPluginsUtils.byteBufferToString(written));

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
    public void testTestEnded_0args() throws IOException {
        System.out.println("testEnded");
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.setFilename(File.createTempFile("ffw_test_", ".txt").getAbsolutePath());
        instance.testStarted();
        instance.testEnded();
    }

    /**
     * Test of testEnded method, of class FlexibleCSVWriter.
     */
    @Test
    public void testTestEnded_String() throws IOException {
        System.out.println("testEnded");
        String host = "";
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.setFilename(File.createTempFile("ffw_test_", ".txt").getAbsolutePath());
        instance.testStarted(host);
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

    /**
     * Test of setFileHeader method, of class FlexibleFileWriter.
     */
    @Test
    public void testSetFileHeader() {
        System.out.println("setFileHeader");
        String str = "";
        kg.apc.jmeter.reporters.FlexibleFileWriter instance = new kg.apc.jmeter.reporters.FlexibleFileWriter();
        instance.setFileHeader(str);
    }

    /**
     * Test of getFileHeader method, of class FlexibleFileWriter.
     */
    @Test
    public void testGetFileHeader() {
        System.out.println("getFileHeader");
        kg.apc.jmeter.reporters.FlexibleFileWriter instance = new kg.apc.jmeter.reporters.FlexibleFileWriter();
        String expResult = "";
        String result = instance.getFileHeader();
        assertEquals(expResult, result);
    }

    /**
     * Test of setFileFooter method, of class FlexibleFileWriter.
     */
    @Test
    public void testSetFileFooter() {
        System.out.println("setFileFooter");
        String str = "";
        kg.apc.jmeter.reporters.FlexibleFileWriter instance = new kg.apc.jmeter.reporters.FlexibleFileWriter();
        instance.setFileFooter(str);
    }

    /**
     * Test of getFileFooter method, of class FlexibleFileWriter.
     */
    @Test
    public void testGetFileFooter() {
        System.out.println("getFileFooter");
        kg.apc.jmeter.reporters.FlexibleFileWriter instance = new kg.apc.jmeter.reporters.FlexibleFileWriter();
        String expResult = "";
        String result = instance.getFileFooter();
        assertEquals(expResult, result);
    }

    /**
     * Test of openFile method, of class FlexibleFileWriter.
     */
    @Test
    public void testOpenFile() throws Exception {
        System.out.println("openFile");
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.setFilename(File.createTempFile(".tmp", "ffw").getAbsolutePath());
        instance.openFile();
    }
}
