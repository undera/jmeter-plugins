package kg.apc.jmeter.reporters;

import kg.apc.emulators.TestJMeterUtils;
import kg.apc.io.FileSystem;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.junit.*;
import org.loadosophia.jmeter.LoadosophiaAPIClient;
import org.loadosophia.jmeter.StatusNotifierCallback;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
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
        LinkedList<String[]> response = new LinkedList<String[]>();
        String[] v1 = {"0", "4"};
        response.push(v1);
        String[] v2 = {"0", "4"};
        response.push(v2);
        String[] v3 = {"0", "4"};
        response.push(v3);
        String[] v4 = {"0", "4"};
        response.push(v4);
        LoadosophiaUploader instance = new LoadosophiaUploaderEmul(response);
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
        LoadosophiaUploader instance = new LoadosophiaUploader();
        instance.testIterationStart(null);
    }

    private static class LoadosophiaUploaderEmul extends LoadosophiaUploader {

        private final LinkedList<String[]> response;

        private LoadosophiaUploaderEmul(LinkedList<String[]> aresponse) {
            response = aresponse;
        }

        @Override
        protected LoadosophiaAPIClient getAPIClient() {
            return new FakeAPIClient(this, response);
        }
    }

    private static class FakeAPIClient extends LoadosophiaAPIClient {
        private static final Logger log = LoggingManager.getLoggerForClass();

        private LinkedList<String[]> response;

        private FakeAPIClient(StatusNotifierCallback aThis, LinkedList<String[]> aresponse) {
            super(aThis, "TEST", "TEST", COLOR_NONE, "TEST", "TEST");
            response = aresponse;
        }

        @Override
        protected String[] getUploadStatus(int queueID) throws IOException {
            String[] resp = response.pop();
            log.info("Simulating response: " + Arrays.toString(resp));
            return resp;
        }

        @Override
        protected String[] multipartPost(LinkedList<Part> parts, String URL, int expectedSC) throws IOException {
            return getUploadStatus(0);
        }
    }

    @Test
    public void testSetTitle() {
        System.out.println("setTitle");
        String prefix = "";
        LoadosophiaUploader instance = new LoadosophiaUploader();
        instance.setTitle(prefix);
    }

    @Test
    public void testGetTitle() {
        System.out.println("getTitle");
        LoadosophiaUploader instance = new LoadosophiaUploader();
        String expResult = "";
        String result = instance.getTitle();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetColorFlag() {
        System.out.println("setColorFlag");
        String color = "";
        LoadosophiaUploader instance = new LoadosophiaUploader();
        instance.setColorFlag(color);
    }

    @Test
    public void testGetColorFlag() {
        System.out.println("getColorFlag");
        LoadosophiaUploader instance = new LoadosophiaUploader();
        String expResult = "";
        String result = instance.getColorFlag();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetAPIClient() {
        System.out.println("getAPIClient");
        LoadosophiaUploader instance = new LoadosophiaUploader();
        LoadosophiaAPIClient result = instance.getAPIClient();
        assertNotNull(result);
    }

    @Test
    public void testNotifyAbout() {
        System.out.println("notifyAbout");
        String info = "";
        LoadosophiaUploader instance = new LoadosophiaUploader();
        instance.notifyAbout(info);
    }

    @Test
    public void testIsUseOnline() {
        System.out.println("isUseOnline");
        LoadosophiaUploader instance = new LoadosophiaUploader();
        boolean expResult = false;
        boolean result = instance.isUseOnline();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetUseOnline() {
        System.out.println("setUseOnline");
        boolean selected = false;
        LoadosophiaUploader instance = new LoadosophiaUploader();
        instance.setUseOnline(selected);
    }

    @Test
    public void testSampleOccurred() {
        System.out.println("sampleOccurred");
        SampleResult res = new SampleResult();
        SampleEvent event = new SampleEvent(res, "test");
        LoadosophiaUploader instance = new LoadosophiaUploader();
        instance.sampleOccurred(event);
    }

    @Test
    public void testRun() {
        System.out.println("run");
        LoadosophiaUploader instance = new LoadosophiaUploader();
        instance.run();
    }

    @Test
    public void testOnlineProcessor() throws InterruptedException {
        System.out.println("onlineProcessor");
        LinkedList<String[]> response = new LinkedList<String[]>();
        String[] v1 = {"0", "4"};
        response.push(v1);
        response.push(v1);
        response.push(v1);
        response.push(v1);
        response.push(v1);
        String[] v2 = {"{}", "4"};
        response.push(v2);
        LoadosophiaUploader instance = new LoadosophiaUploaderEmul(response);
        instance.setUseOnline(true);
        instance.setStoreDir(TestJMeterUtils.getTempDir());
        instance.testStarted("");
        for (int i = 0; i < 100; i++) {
            SampleResult res = new SampleResult();
            res.setThreadName("test " + i);
            SampleEvent event = new SampleEvent(res, "test " + i);
            instance.sampleOccurred(event);
        }
        Thread.sleep(10);
        instance.testEnded("");
    }
}
