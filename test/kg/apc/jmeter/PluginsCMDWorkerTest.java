package kg.apc.jmeter;

import java.io.File;
import java.io.IOException;
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
public class PluginsCMDWorkerTest {

    public PluginsCMDWorkerTest() {
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
     * Test of addExportMode method, of class CMDWorker.
     */
    @Test
    public void testAddExportMode() {
        System.out.println("addExportMode");
        int mode = 0;
        PluginsCMDWorker instance = new PluginsCMDWorker();
        instance.addExportMode(mode);
    }

    /**
     * Test of setInputFile method, of class CMDWorker.
     */
    @Test
    public void testSetInputFile() {
        System.out.println("setInputFile");
        String string = "";
        PluginsCMDWorker instance = new PluginsCMDWorker();
        instance.setInputFile(string);
    }

    /**
     * Test of setOutputCSVFile method, of class CMDWorker.
     */
    @Test
    public void testSetOutputCSVFile() {
        System.out.println("setOutputCSVFile");
        String string = "";
        PluginsCMDWorker instance = new PluginsCMDWorker();
        instance.setOutputCSVFile(string);
    }

    /**
     * Test of setOutputPNGFile method, of class CMDWorker.
     */
    @Test
    public void testSetOutputPNGFile() {
        System.out.println("setOutputPNGFile");
        String string = "";
        PluginsCMDWorker instance = new PluginsCMDWorker();
        instance.setOutputPNGFile(string);
    }

    /**
     * Test of setPluginType method, of class CMDWorker.
     */
    @Test
    public void testSetPluginType() {
        System.out.println("setPluginType");
        String string = "";
        PluginsCMDWorker instance = new PluginsCMDWorker();
        instance.setPluginType(string);
    }

    /**
     * Test of doJob method, of class CMDWorker.
     */
    @Test
    public void testDoJob() throws IOException {
        System.out.println("doJob");
        PluginsCMDWorker instance = new PluginsCMDWorker();
        instance.setInputFile("/home/undera/short.jtl");
        instance.setOutputPNGFile(File.createTempFile("test", ".png").getAbsolutePath());
        instance.setOutputCSVFile(File.createTempFile("test", ".csv").getAbsolutePath());
        instance.setPluginType("ResponseTimesDistribution");
        instance.addExportMode(PluginsCMDWorker.EXPORT_PNG);
        instance.addExportMode(PluginsCMDWorker.EXPORT_CSV);
        int result = instance.doJob();
        int expResult = 0;
        assertEquals(expResult, result);
    }

}