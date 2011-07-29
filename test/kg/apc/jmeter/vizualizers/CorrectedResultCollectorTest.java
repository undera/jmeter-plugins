
package kg.apc.jmeter.vizualizers;

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
public class CorrectedResultCollectorTest {

    public CorrectedResultCollectorTest() {
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
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of testStarted method, of class CorrectedResultCollector.
     */
    @Test
    public void testTestStarted() {
        System.out.println("testStarted");
        CorrectedResultCollector instance = new CorrectedResultCollector();
        instance.testStarted();
    }
}
