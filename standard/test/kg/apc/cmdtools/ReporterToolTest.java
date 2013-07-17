package kg.apc.cmdtools;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ListIterator;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.util.JMeterUtils;
import static org.junit.Assert.*;
import org.junit.*;

public class ReporterToolTest {

    private final String basedir;

    public ReporterToolTest() {
        File file = new File(this.getClass().getResource("short.jtl").getFile());
        basedir = TestJMeterUtils.fixWinPath(file.getParentFile().getAbsolutePath());
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
        System.out.println(f.length());
        assertTrue(66 == f.length() || 68 == f.length()); // 66 at linux, 68 at windows because of \r\n
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
}
