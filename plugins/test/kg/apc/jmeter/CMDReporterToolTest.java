package kg.apc.jmeter;

import java.io.File;
import java.io.IOException;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.util.JMeterUtils;
import java.io.PrintStream;
import java.util.ListIterator;
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
public class CMDReporterToolTest {

    private final String basedir;

    public CMDReporterToolTest() {
        String file = this.getClass().getResource("short.jtl").getPath();
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
     * Test of showHelp method, of class CMDReporterTool.
     */
    @Test
    public void testShowHelp() {
        System.out.println("showHelp");
        PrintStream os = System.out;
        CMDReporterTool instance = new CMDReporterTool();
        instance.showHelp(os);
    }

    /**
     * Test of processParams method, of class CMDReporterTool.
     */
    @Test
    public void testProcessParams() {
        System.out.println("processParams");
        ListIterator<String> args = PluginsCMD.argsArrayToListIterator("--help".split(" "));
        CMDReporterTool instance = new CMDReporterTool();
        try {
            instance.processParams(args);
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    // issue 39
    public void testProcessParams_aggreg() throws IOException {
        System.out.println("processParams aggregate");
        File f = File.createTempFile("test", ".csv");
        String str = "--generate-csv " + f.getAbsolutePath() + " "
                + "--input-jtl " + basedir + "/few.jtl "
                + "--aggregate-rows yes --plugin-type ResponseTimesOverTime";
        String[] args = str.split(" +");
        CMDReporterTool instance = new CMDReporterTool();
        int expResult = 0;
        int result = instance.processParams(PluginsCMD.argsArrayToListIterator(args));
        assertEquals(expResult, result);
        assertTrue(78 == f.length() || 81 == f.length()); // 78 at linux, 81 at windows because or \r\n
    }

    @Test
    // issue 47
    public void testProcessParams_outliers() throws IOException {
        System.out.println("processParams outliers");
        File f = File.createTempFile("test", ".png");
        String str = "--width 1000 --height 300 "
                + "--prevent-outliers yes "
                + "--plugin-type ResponseTimesDistribution"
                + " --generate-png " + f.getAbsolutePath() + " "
                + "--input-jtl " + basedir + "/results_issue_47.jtl";
        String[] args = str.split(" +");
        CMDReporterTool instance = new CMDReporterTool();
        int expResult = 0;
        int result = instance.processParams(PluginsCMD.argsArrayToListIterator(args));
        assertEquals(expResult, result);
        System.out.println(f.length());
        assertTrue(14000 <= f.length());
    }

    @Test
    // issue 64
    public void testProcessParams_issue64() throws IOException {
        System.out.println("processParams outliers");
        File f = File.createTempFile("test", ".png");
        String str = "--width 800 --height 600 "
                + "--plugin-type HitsPerSecond  "
                + "--aggregate-rows yes "
                + "--generate-png " + f.getAbsolutePath() + " "
                + "--input-jtl " + basedir + "/results_issue_47.jtl";
        String[] args = str.split(" +");
        CMDReporterTool instance = new CMDReporterTool();
        try {
            int result = instance.processParams(PluginsCMD.argsArrayToListIterator(args));
            fail("HitsPerSec don't handle aggregates");
        } catch (UnsupportedOperationException e) {
        }
    }
}
