// TODO: cover all parameters
package kg.apc.jmeter;

import java.io.PrintStream;
import java.util.ListIterator;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.util.JMeterUtils;
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

    private String basedir;

    public PluginsCMDTest() {
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
     * Test of processParams method, of class PluginsCMD.
     */
    @Test
    public void testProcessParams() {
        System.out.println("processParams");
        String[] args = null;
        PluginsCMD instance = new PluginsCMD();
        int expResult = 0;
        int result = instance.processParams(args);
        assertEquals(expResult, result);
    }

    /**
     * Test of processParams method, of class PluginsCMD.
     */
    @Test
    public void testProcessParams_StringArr() {
        System.out.println("processParams");
        String[] args = null;
        PluginsCMD instance = new PluginsCMD();
        int expResult = 0;
        int result = instance.processParams(args);
        assertEquals(expResult, result);
    }

    /**
     * Test of processParams method, of class PluginsCMD.
     */
    @Test
    public void testProcessParams_ListIterator() {
        System.out.println("processParams");
        ListIterator<String> args = PluginsCMD.argsArrayToListIterator("-?".split(" "));
        PluginsCMD instance = new PluginsCMD();
        int expResult = 0;
        int result = instance.processParams(args);
        assertEquals(expResult, result);
    }

    /**
     * Test of showHelp method, of class PluginsCMD.
     */
    @Test
    public void testShowHelp() {
        System.out.println("showHelp");
        PrintStream os = System.out;
        PluginsCMD instance = new PluginsCMD();
        instance.showHelp(os);
    }

    /**
     * Test of argsArrayToListIterator method, of class PluginsCMD.
     */
    @Test
    public void testArgsArrayToListIterator() {
        System.out.println("argsArrayToListIterator");
        String[] args = "".split(" ");
        ListIterator result = PluginsCMD.argsArrayToListIterator(args);
        assertNotNull(result);
    }
}
