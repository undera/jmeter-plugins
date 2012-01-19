package kg.apc.cmdtools;

import java.text.DecimalFormatSymbols;
import java.io.File;
import java.io.IOException;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.util.JMeterUtils;
import java.io.PrintStream;
import java.util.ListIterator;
import kg.apc.emulators.FilesTestTools;
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
public class ReporterToolTest {

    private final String basedir;

    public ReporterToolTest() {
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
        ReporterTool instance = new ReporterTool();
        instance.showHelp(os);
    }

    /**
     * Test of processParams method, of class CMDReporterTool.
     */
    @Test
    public void testProcessParams() {
        System.out.println("processParams");
        ListIterator<String> args = PluginsCMD.argsArrayToListIterator("--help".split(" "));
        ReporterTool instance = new ReporterTool();
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
        ReporterTool instance = new ReporterTool();
        int expResult = 0;
        int result = instance.processParams(PluginsCMD.argsArrayToListIterator(args));
        assertEquals(expResult, result);
        assertTrue(65 == f.length() || 68 == f.length()); // 65 at linux, 68 at windows because of \r\n
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
        ReporterTool instance = new ReporterTool();
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
        ReporterTool instance = new ReporterTool();
        try {
            int result = instance.processParams(PluginsCMD.argsArrayToListIterator(args));
            fail("HitsPerSec don't handle aggregates");
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    // issue 96
    public void testProcessParams_issue96() throws IOException {
        System.out.println("processParams outliers");
        File f = File.createTempFile("test", ".csv");
        String str =
                "--plugin-type TimesVsThreads  "
                + "--hide-low-counts 5 "
                + "--generate-csv " + f.getAbsolutePath() + " "
                + "--input-jtl " + basedir + "/issue96.jtl";
        String[] args = str.split(" +");
        ReporterTool instance = new ReporterTool();
        int result = instance.processParams(PluginsCMD.argsArrayToListIterator(args));
        assertEquals(0, result);
        
        if (new DecimalFormatSymbols().getDecimalSeparator() == '.') {
            FilesTestTools.compareFiles(f, new File(basedir + "/issue96.comma.txt"));
        } else {
            FilesTestTools.compareFiles(new File(basedir + "/issue96.semicolon.txt"), f);
        }
    }
}
