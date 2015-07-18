package kg.apc.jmeter.reporters;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FlexibleFileWriterTest {

    public FlexibleFileWriterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

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
        assertTrue(tmpFile.length() > 0);
    }

    @Test
    public void testSampleOccurred_null() throws IOException {
        System.out.println("sampleOccurred null");
        SampleResult res = new SampleResult();
        //res.setResponseData("test".getBytes());
        SampleEvent e = new SampleEvent(res, "Test");
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.setColumns(FlexibleFileWriter.AVAILABLE_FIELDS.replace(' ', '|'));
        String tmpFile = File.createTempFile("ffw_test_", ".txt").getAbsolutePath();
        instance.setFilename(tmpFile);
        instance.testStarted();
        for (int n = 0; n < 10; n++) {
            res.sampleStart();
            res.sampleEnd();
            instance.sampleOccurred(e);
        }
        instance.testEnded();
        assertTrue(tmpFile.length() > 0);
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
        instance.setColumns("endTimeMillis|\\t\\t|responseTimeMicros|\\t|latencyMicros|\\t|sentBytes|\\t|receivedBytes|\\t|isSuccsessful|\\t|responseCode|\\t|connectTime|\\r\\n");
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
        SampleEvent e = new SampleEvent(res, "Test");
        instance.sampleOccurred(e);
        //ByteBuffer written = instance.fileEmul.getWrittenBytes();
        //assertEquals(exp, JMeterPluginsUtils.byteBufferToString(written));

        instance.testEnded();
    }

    @Test
    public void testSampleStarted() {
        System.out.println("sampleStarted");
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.sampleStarted(null);
    }

    @Test
    public void testSampleStopped() {
        System.out.println("sampleStopped");
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.sampleStopped(null);
    }

    @Test
    public void testTestStarted_0args() {
        System.out.println("testStarted");
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.testStarted();
    }

    @Test
    public void testTestStarted_String() {
        System.out.println("testStarted");
        String host = "";
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.testStarted(host);
    }

    @Test
    public void testTestEnded_0args() throws IOException {
        System.out.println("testEnded");
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.setFilename(File.createTempFile("ffw_test_", ".txt").getAbsolutePath());
        instance.testStarted();
        instance.testEnded();
    }

    @Test
    public void testTestEnded_String() throws IOException {
        System.out.println("testEnded");
        String host = "";
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.setFilename(File.createTempFile("ffw_test_", ".txt").getAbsolutePath());
        instance.testStarted(host);
        instance.testEnded(host);
    }

    @Test
    public void testSetFilename() {
        System.out.println("setFilename");
        String name = "";
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.setFilename(name);
    }

    @Test
    public void testGetFilename() {
        System.out.println("getFilename");
        FlexibleFileWriter instance = new FlexibleFileWriter();
        String expResult = "";
        String result = instance.getFilename();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetColumns() {
        System.out.println("setColumns");
        String cols = "";
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.setColumns(cols);
    }

    @Test
    public void testGetColumns() {
        System.out.println("getColumns");
        FlexibleFileWriter instance = new FlexibleFileWriter();
        String expResult = "";
        String result = instance.getColumns();
        assertEquals(expResult, result);
    }

    @Test
    public void testIsOverwrite() {
        System.out.println("isOverwrite");
        FlexibleFileWriter instance = new FlexibleFileWriter();
        boolean result = instance.isOverwrite();
        assertEquals(false, result);
    }

    @Test
    public void testSetOverwrite() {
        System.out.println("setOverwrite");
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.setOverwrite(false);
    }

    @Test
    public void testSetFileHeader() throws IOException {
        System.out.println("setFileHeader");
        String str = "Test\\t\\r\\n";
        FlexibleFileWriter instance = new FlexibleFileWriter();
        File f = File.createTempFile("ffw_test_", ".txt");
        instance.setFilename(f.getAbsolutePath());
        instance.setFileHeader(str);
        instance.testStarted();
        instance.testEnded();
        assertEquals(7, f.length());
    }

    @Test
    public void testGetFileHeader() {
        System.out.println("getFileHeader");
        FlexibleFileWriter instance = new FlexibleFileWriter();
        String expResult = "";
        String result = instance.getFileHeader();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetFileFooter() {
        System.out.println("setFileFooter");
        String str = "";
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.setFileFooter(str);
    }

    @Test
    public void testGetFileFooter() {
        System.out.println("getFileFooter");
        FlexibleFileWriter instance = new FlexibleFileWriter();
        String expResult = "";
        String result = instance.getFileFooter();
        assertEquals(expResult, result);
    }

    @Test
    public void testOpenFile() throws Exception {
        System.out.println("openFile");
        FlexibleFileWriter instance = new FlexibleFileWriter();
        instance.setFilename(File.createTempFile(".tmp", "ffw").getAbsolutePath());
        instance.openFile();
    }
}
