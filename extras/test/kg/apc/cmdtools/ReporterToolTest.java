package kg.apc.cmdtools;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormatSymbols;
import kg.apc.emulators.FilesTestTools;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.util.JMeterUtils;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author undera
 */
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
