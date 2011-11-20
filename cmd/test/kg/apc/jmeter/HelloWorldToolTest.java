package kg.apc.jmeter;

import java.io.PrintStream;
import java.util.ListIterator;
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
public class HelloWorldToolTest {

    public HelloWorldToolTest() {
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
     * Test of processParams method, of class HelloWorldTool.
     */
    @Test
    public void testProcessParams() {
        System.out.println("processParams");
        ListIterator<String> args = null;
        HelloWorldTool instance = new HelloWorldTool();
        int expResult = 0;
        int result = instance.processParams(args);
        assertEquals(expResult, result);
    }

    /**
     * Test of showHelp method, of class HelloWorldTool.
     */
    @Test
    public void testShowHelp() {
        System.out.println("showHelp");
        PrintStream os = new PrintStream(System.out);
        HelloWorldTool instance = new HelloWorldTool();
        instance.showHelp(os);
    }
}
