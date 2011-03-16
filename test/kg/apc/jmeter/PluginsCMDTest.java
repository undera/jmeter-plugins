package kg.apc.jmeter;

import kg.apc.emulators.PluginsCMDEmul;
import kg.apc.emulators.TestJMeterUtils;
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
        String str="--help";
        PluginsCMDEmul.main(str.split(" "));
    }

    /**
     * Test of exitWithCode method, of class PluginsCMD.
     */
    @Test
    public void testExitWithCode() {
        System.out.println("exitWithCode");
        int i = 0;
        PluginsCMD instance = new PluginsCMDEmul();
        instance.exitWithCode(i);
    }

    /**
     * Test of processParams method, of class PluginsCMD.
     */
    @Test
    public void testProcessParams() {
        System.out.println("processParams");
        String[] args = null;
        PluginsCMD instance = new PluginsCMDEmul();
        int expResult = 0;
        int result = instance.processParams(args);
        assertEquals(expResult, result);
    }
}
