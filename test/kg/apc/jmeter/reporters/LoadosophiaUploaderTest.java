package kg.apc.jmeter.reporters;

import org.apache.jmeter.util.JMeterUtils;
import kg.apc.emulators.TestJMeterUtils;
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
        instance.setFilePrefix("UnitTest");
        instance.setStoreDir(TestJMeterUtils.getTempDir());
        instance.testStarted();
    }

    /**
     * Test of testEnded method, of class LoadosophiaUploader.
     */
    @Test
    public void testTestEnded() {
        System.out.println("testEnded");
        LoadosophiaUploader instance = new LoadosophiaUploader();
        instance.setStoreDir(TestJMeterUtils.getTempDir());
        instance.setFilePrefix("UnitTest");
        instance.setUploaderURI("http://localhost/uploader/");
        instance.setProject("DEFAULT");
        instance.setUploadToken("0000000000000000000000000000000000000000000000000000000000000000");
        instance.testStarted();
        SampleResult res = new SampleResult();
        res.sampleStart();
        res.sampleEnd();
        SampleEvent event = new SampleEvent(res, "test");
        instance.sampleOccurred(event);
        instance.testEnded();
    }

    /**
     * Test of getUploaderURI method, of class LoadosophiaUploader.
     */
    @Test
    public void testGetUploaderURI() {
        System.out.println("getUploaderURI");
        LoadosophiaUploader instance = new LoadosophiaUploader();
        String expResult = "";
        String result = instance.getUploaderURI();
        assertEquals(expResult, result);
    }

    /**
     * Test of setUploaderURI method, of class LoadosophiaUploader.
     */
    @Test
    public void testSetUploaderURI() {
        System.out.println("setUploaderURI");
        String uri = "";
        LoadosophiaUploader instance = new LoadosophiaUploader();
        instance.setUploaderURI(uri);
    }

    /**
     * Test of setFilePrefix method, of class LoadosophiaUploader.
     */
    @Test
    public void testSetFilePrefix() {
        System.out.println("setFilePrefix");
        String prefix = "";
        LoadosophiaUploader instance = new LoadosophiaUploader();
        instance.setFilePrefix(prefix);
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

    /**
     * Test of getUploadToken method, of class LoadosophiaUploader.
     */
    @Test
    public void testGetUploadToken() {
        System.out.println("getUploadToken");
        LoadosophiaUploader instance = new LoadosophiaUploader();
        String expResult = "";
        String result = instance.getUploadToken();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFilePrefix method, of class LoadosophiaUploader.
     */
    @Test
    public void testGetFilePrefix() {
        System.out.println("getFilePrefix");
        LoadosophiaUploader instance = new LoadosophiaUploader();
        String expResult = "";
        String result = instance.getFilePrefix();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStoreDir method, of class LoadosophiaUploader.
     */
    @Test
    public void testGetStoreDir() {
        System.out.println("getStoreDir");
        LoadosophiaUploader instance = new LoadosophiaUploader();
        String expResult = "";
        String result = instance.getStoreDir();
        assertEquals(expResult, result);
    }

    /**
     * Test of setStoreDir method, of class LoadosophiaUploader.
     */
    @Test
    public void testSetStoreDir() {
        System.out.println("setStoreDir");
        String prefix = "";
        LoadosophiaUploader instance = new LoadosophiaUploader();
        instance.setStoreDir(prefix);
    }
}
