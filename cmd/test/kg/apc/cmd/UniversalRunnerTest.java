package kg.apc.cmd;

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
public class UniversalRunnerTest {

    public UniversalRunnerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        // JMeterUtils.setJMeterHome("");
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getJMeterDir method, of class NewDriver.
     */
    @Test
    public void testGetJMeterDir() {
        System.out.println("getJMeterDir");
        String result = UniversalRunner.getJMeterDir();
        assertTrue(result.length() > 0);
    }

    /**
     * Test of main method, of class NewDriver.
     */
    @Test
    public void testMain() throws Throwable {
        System.out.println("main");
        String[] args = "--help".split(" ");
        UniversalRunner.main(args);
    }
}