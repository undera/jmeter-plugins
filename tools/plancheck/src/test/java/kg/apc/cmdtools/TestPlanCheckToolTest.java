package kg.apc.cmdtools;

import kg.apc.emulators.TestJMeterUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.ListIterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestPlanCheckToolTest {

    private final String basedir;

    public TestPlanCheckToolTest() {
        File file = new File(this.getClass().getResource("/kg/apc/cmdtools/Valid.jmx").getFile());
        basedir = TestJMeterUtils.fixWinPath(file.getParentFile().getAbsolutePath());
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @Test
    public void testShowHelp() throws Exception {
        TestPlanCheckTool obj = new TestPlanCheckTool();
        obj.showHelp(System.out);
    }

    @Test
    public void testProcessParamsWrong() throws Exception {
        TestPlanCheckTool obj = new TestPlanCheckTool();
        ArrayList<String> al = new ArrayList<>();
        al.add("--wrong");
        ListIterator args = al.listIterator();
        try {
            obj.processParams(args);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testProcessParamsValid() throws Exception {
        TestPlanCheckTool obj = new TestPlanCheckTool();
        ArrayList<String> al = new ArrayList<>();
        al.add("--jmx");
        al.add(basedir + "/Valid.jmx");
        al.add("--tree-dump");
        al.add("--stats");
        ListIterator args = al.listIterator();
        assertEquals(0, obj.processParams(args));
    }

    @Test
    public void testProcessParamsInvalid() throws Exception {
        TestPlanCheckTool obj = new TestPlanCheckTool();
        ArrayList<String> al = new ArrayList<>();
        al.add("--jmx");
        al.add(basedir + "/Invalid.jmx");
        ListIterator args = al.listIterator();
        try {
            assertEquals(1, obj.processParams(args));
        } catch (TestPlanCheckTool.TestPlanBrokenException ignored) {
        }
    }
}