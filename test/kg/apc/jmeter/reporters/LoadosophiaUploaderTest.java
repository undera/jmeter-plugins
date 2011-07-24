package kg.apc.jmeter.reporters;

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

    public LoadosophiaUploaderTest() {
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
     * Test of testStarted method, of class LoadosophiaUploader.
     */
    @Test
    public void testTestStarted() {
        System.out.println("testStarted");
        LoadosophiaUploader instance = new LoadosophiaUploader();
        instance.setFilePrefix("UnitTest");
        instance.testStarted();
    }

    /**
     * Test of testEnded method, of class LoadosophiaUploader.
     */
    @Test
    public void testTestEnded() {
        System.out.println("testEnded");
        LoadosophiaUploader instance = new LoadosophiaUploader();
        instance.setFilePrefix("UnitTest");
        instance.setUploaderURI("http://localhost/service/upload/");
        instance.testStarted();
        SampleResult res=new SampleResult();
        SampleEvent event=new SampleEvent(res, "test");
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
}
