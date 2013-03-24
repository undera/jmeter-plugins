package kg.apc.jmeter.reporters;

import java.io.IOException;
import java.util.LinkedList;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.util.JMeterUtils;
import kg.apc.emulators.TestJMeterUtils;
import kg.apc.io.FileSystem;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.loadosophia.jmeter.LoadosophiaAPIClient;
import org.loadosophia.jmeter.StatusNotifierCallback;

/**
 *
 * @author undera
 */
public class LoadosophiaUploaderTest {

    private final String basedir;

    public LoadosophiaUploaderTest() {
        String file = this.getClass().getResource("anchor.txt").getPath();
        basedir = file.substring(0, file.lastIndexOf("/"));
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
        JMeterUtils.setJMeterHome(basedir);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of testStarted method, of class LoadosophiaUploader.
     */
    @Test
    public void testTestStarted() {
        System.out.println("testStarted");
        LoadosophiaUploader instance = new LoadosophiaUploader();
        instance.setTitle("UnitTest");
        instance.setStoreDir(TestJMeterUtils.getTempDir());
        instance.testStarted();
    }

    /**
     * Test of testEnded method, of class LoadosophiaUploader.
     */
    @Test
    public void testTestEnded() throws IOException {
        System.out.println("testEnded");
        JMeterUtils.setProperty("loadosophia.address", "http://localhost/");
        LoadosophiaUploader instance = new LoadosophiaUploaderEmul();
        instance.setStoreDir(TestJMeterUtils.getTempDir());
        instance.setTitle("UnitTest");
        instance.setColorFlag("gray");
        instance.setProject("DEFAULT");
        instance.setUploadToken("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        instance.testStarted();

        SampleResult res = new SampleResult();
        res.sampleStart();
        res.sampleEnd();
        SampleEvent event = new SampleEvent(res, "test");
        instance.sampleOccurred(event);

        FileSystem.copyFile(instance.getFilename(), instance.getFilename() + ".perfmon1");
        LoadosophiaUploadingNotifier.getInstance().addFile(instance.getFilename() + ".perfmon1");

        FileSystem.copyFile(instance.getFilename(), instance.getFilename() + ".perfmon2");
        LoadosophiaUploadingNotifier.getInstance().addFile(instance.getFilename() + ".perfmon2");

        instance.testEnded();
    }

    /**
     * Test of setFilePrefix method, of class LoadosophiaUploader.
     */
    @Test
    public void testSetFilePrefix() {
        System.out.println("setFilePrefix");
        String prefix = "";
        LoadosophiaUploader instance = new LoadosophiaUploader();
        instance.setTitle(prefix);
    }

    /**
     * Test of setProject method, of class LoadosophiaUploader.
     */
    @Test
    public void testSetProject() {
        System.out.println("setProject");
        String proj = "";
        LoadosophiaUploader instance = new LoadosophiaUploader();
        instance.setProject(proj);
    }

    /**
     * Test of setUploadToken method, of class LoadosophiaUploader.
     */
    @Test
    public void testSetUploadToken() {
        System.out.println("setUploadToken");
        String token = "";
        LoadosophiaUploader instance = new LoadosophiaUploader();
        instance.setUploadToken(token);
    }

    /**
     * Test of getProject method, of class LoadosophiaUploader.
     */
    @Test
    public void testGetProject() {
        System.out.println("getProject");
        LoadosophiaUploader instance = new LoadosophiaUploader();
        String expResult = "";
        String result = instance.getProject();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetUploadToken() {
        System.out.println("getUploadToken");
        LoadosophiaUploader instance = new LoadosophiaUploader();
        String expResult = "";
        String result = instance.getUploadToken();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetFilePrefix() {
        System.out.println("getFilePrefix");
        LoadosophiaUploader instance = new LoadosophiaUploader();
        String expResult = "";
        String result = instance.getTitle();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetStoreDir() {
        System.out.println("getStoreDir");
        LoadosophiaUploader instance = new LoadosophiaUploader();
        String expResult = "";
        String result = instance.getStoreDir();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetStoreDir() {
        System.out.println("setStoreDir");
        String prefix = "";
        LoadosophiaUploader instance = new LoadosophiaUploader();
        instance.setStoreDir(prefix);
    }

    @Test
    public void testTestIterationStart() {
        System.out.println("testIterationStart");
        LoopIterationEvent lie = null;
        LoadosophiaUploader instance = new LoadosophiaUploader();
        instance.testIterationStart(lie);
    }

    private static class LoadosophiaUploaderEmul extends LoadosophiaUploader {

        @Override
        protected LoadosophiaAPIClient getAPIClient() {
            return new FakeAPIClient(this);
        }
    }

    private static class FakeAPIClient extends LoadosophiaAPIClient {

        public FakeAPIClient(StatusNotifierCallback informer) {
            super(informer, "TEST", "TEST", COLOR_NONE, "TEST", "TEST");
        }

        @Override
        protected String[] getUploadStatus(int queueID) throws IOException {
            String[] str = {"0", "4"};
            return str;
        }

        @Override
        protected String[] multipartPost(LinkedList<Part> parts, String URL, int expectedSC) throws IOException {
            String[] str = {"0", "4"};
            return str;
        }
    }
}
