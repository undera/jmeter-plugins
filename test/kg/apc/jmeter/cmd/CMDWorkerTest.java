package kg.apc.jmeter.cmd;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.tree.JMeterTreeListener;
import org.apache.jmeter.gui.tree.JMeterTreeModel;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import kg.apc.emulators.EmulatorJmeterEngine;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.threads.JMeterContextService;
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
public class CMDWorkerTest {

    public CMDWorkerTest() {
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
        CMDWorker instance = new CMDWorker();
        instance.addExportMode(mode);
    }

    /**
     * Test of setInputFile method, of class CMDWorker.
     */
    @Test
    public void testSetInputFile() {
        System.out.println("setInputFile");
        String string = "";
        CMDWorker instance = new CMDWorker();
        instance.setInputFile(string);
    }

    /**
     * Test of setOutputCSVFile method, of class CMDWorker.
     */
    @Test
    public void testSetOutputCSVFile() {
        System.out.println("setOutputCSVFile");
        String string = "";
        CMDWorker instance = new CMDWorker();
        instance.setOutputCSVFile(string);
    }

    /**
     * Test of setOutputPNGFile method, of class CMDWorker.
     */
    @Test
    public void testSetOutputPNGFile() {
        System.out.println("setOutputPNGFile");
        String string = "";
        CMDWorker instance = new CMDWorker();
        instance.setOutputPNGFile(string);
    }

    /**
     * Test of setPluginType method, of class CMDWorker.
     */
    @Test
    public void testSetPluginType() {
        System.out.println("setPluginType");
        String string = "";
        CMDWorker instance = new CMDWorker();
        instance.setPluginType(string);
    }

    /**
     * Test of doJob method, of class CMDWorker.
     */
    @Test
    public void testDoJob() {
        System.out.println("doJob");
        CMDWorker instance = new CMDWorker();
        int expResult = 0;
        instance.setInputFile("/home/undera/bench.jtl");
        int result = instance.doJob();
        assertEquals(expResult, result);
    }

}