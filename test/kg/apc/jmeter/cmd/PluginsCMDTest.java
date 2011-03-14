package kg.apc.jmeter.cmd;

import kg.apc.emulators.PluginsCMDEmul;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author undera
 */
public class PluginsCMDTest {

    public PluginsCMDTest() {
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
     * Test of main method, of class PluginsCMD.
     */
    @Test
    public void testMain() {
        System.out.println("main noparams");
        String[] args = null;
        PluginsCMDEmul.main(args);
    }

    @Test
    public void testMainErr() {
        System.out.println("mainerr");
        String[] args = new String[] {"err"};
        PluginsCMDEmul.main(args);
    }

    @Test
    public void test_Image() {
        System.out.println("png");
        String[] args = new String[] {
            "--export-png",
            "test.png",
            "--jtl-file",
            "/tmp/test.jtl"
        };
        PluginsCMDEmul.main(args);
    }
}