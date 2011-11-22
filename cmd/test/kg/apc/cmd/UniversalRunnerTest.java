package kg.apc.cmd;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author undera
 */
public class UniversalRunnerTest extends TestCase {

    public UniversalRunnerTest() {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(UniversalRunnerTest.class);
        return suite;
    }

    public static void setUpClass() throws Exception {
    }

    public static void tearDownClass() throws Exception {
    }

    public void setUp() {
        // JMeterUtils.setJMeterHome("");
    }

    public void tearDown() {
    }

    /**
     * Test of getJMeterDir method, of class NewDriver.
     */
    public void testGetJMeterDir() {
        System.out.println("getJMeterDir");
        String result = UniversalRunner.getJMeterDir();
        assertTrue(result.length() > 0);
    }

    /**
     * Test of main method, of class NewDriver.
     */
    public void testMain() throws Throwable {
        System.out.println("main");
        String[] args = "--help".split(" ");
        UniversalRunner.main(args);
    }
}