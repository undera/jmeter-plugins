package kg.apc.jmeter.cmd;

import kg.apc.emulators.PluginsCMDEmul;
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
public class PluginsCMDTest {

    public PluginsCMDTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        String homeDir = "/home/undera/NetBeansProjects/jmeter/trunk";
        System.setProperty("jmeter.home", homeDir);
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
        String[] args = new String[]{"err"};
        try {
            PluginsCMDEmul.main(args);
            fail("Exception expected");
        } catch (RuntimeException e) {
        }
    }

    @Test
    public void test_Image() {
        System.out.println("png");
        PluginsCMDEmul.main("--generate-png test.png --input-jtl /home/undera/short.jtl --plugin-type ResponseTimesOverTime".split(" "));
    }
}
