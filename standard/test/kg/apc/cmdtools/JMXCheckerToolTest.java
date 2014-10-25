package kg.apc.cmdtools;

import junit.framework.TestCase;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.File;
import java.util.ArrayList;
import java.util.ListIterator;

public class JMXCheckerToolTest extends TestCase {

    private final String basedir;

    public JMXCheckerToolTest() {
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

    public void testShowHelp() throws Exception {
        JMXCheckerTool obj = new JMXCheckerTool();
        obj.showHelp(System.out);
    }

    public void testProcessParamsWrong() throws Exception {
        JMXCheckerTool obj = new JMXCheckerTool();
        ArrayList<String> al = new ArrayList<String>();
        al.add("--wrong");
        ListIterator args = al.listIterator();
        try {
            obj.processParams(args);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }

    public void testProcessParamsValid() throws Exception {
        JMXCheckerTool obj = new JMXCheckerTool();
        ArrayList<String> al = new ArrayList<String>();
        al.add("--jmx");
        al.add(basedir + "/Valid.jmx");
        al.add("--tree-dump");
        al.add("--stats");
        ListIterator args = al.listIterator();
        assertEquals(0, obj.processParams(args));
    }

    public void testProcessParamsInvalid() throws Exception {
        JMXCheckerTool obj = new JMXCheckerTool();
        ArrayList<String> al = new ArrayList<String>();
        al.add("--jmx");
        al.add(basedir + "/Invalid.jmx");
        ListIterator args = al.listIterator();
        try {
            assertEquals(1, obj.processParams(args));
            fail();
        } catch (JMXCheckerTool.TestPlanBrokenException ignored) {
        }
    }
}