package kg.apc.cmdtools;

import java.io.PrintStream;
import java.util.ListIterator;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author undera
 */
public class HelloWorldToolTest extends TestCase {

    public HelloWorldToolTest() {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(HelloWorldToolTest.class);
        return suite;
    }

    public static void setUpClass() throws Exception {
    }

    public static void tearDownClass() throws Exception {
    }

    public void setUp() {
    }

    public void tearDown() {
    }

    /**
     * Test of processParams method, of class HelloWorldTool.
     */
    public void testProcessParams() {
        System.out.println("processParams");
        ListIterator args = null;
        HelloWorldTool instance = new HelloWorldTool();
        int expResult = 0;
        int result = instance.processParams(args);
        assertEquals(expResult, result);
    }

    /**
     * Test of showHelp method, of class HelloWorldTool.
     */
    public void testShowHelp() {
        System.out.println("showHelp");
        PrintStream os = new PrintStream(System.out);
        HelloWorldTool instance = new HelloWorldTool();
        instance.showHelp(os);
    }
}
