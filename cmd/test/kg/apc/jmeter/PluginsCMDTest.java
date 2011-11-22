package kg.apc.jmeter;

import java.io.PrintStream;
import java.util.ListIterator;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author undera
 */
public class PluginsCMDTest extends TestCase {

    public PluginsCMDTest() {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(PluginsCMDTest.class);
        return suite;
    }

    public static void setUpClass() throws Exception {
        // TestJMeterUtils.createJmeterEnv();
    }

    public static void tearDownClass() throws Exception {
    }

    public void setUp() {
        //JMeterUtils.setJMeterHome(basedir);
    }

    public void tearDown() {
    }

    /**
     * Test of processParams method, of class PluginsCMD.
     */
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
    public void testProcessParams_ListIterator() {
        System.out.println("processParams");
        ListIterator args = PluginsCMD.argsArrayToListIterator("-?".split(" "));
        PluginsCMD instance = new PluginsCMD();
        int expResult = 0;
        int result = instance.processParams(args);
        assertEquals(expResult, result);
    }

    /**
     * Test of showHelp method, of class PluginsCMD.
     */
    public void testShowHelp() {
        System.out.println("showHelp");
        PrintStream os = System.out;
        PluginsCMD instance = new PluginsCMD();
        instance.showHelp(os);
    }

    /**
     * Test of argsArrayToListIterator method, of class PluginsCMD.
     */
    public void testArgsArrayToListIterator() {
        System.out.println("argsArrayToListIterator");
        String[] args = "".split(" ");
        ListIterator result = PluginsCMD.argsArrayToListIterator(args);
        assertNotNull(result);
    }
}
