package kg.apc.jmeter.vizualizers;

import java.util.List;
import kg.apc.emulators.TestJMeterUtils;
import org.junit.*;
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

    /**
     * Test of getList method, of class CorrectedResultCollector.
     */
    @Test
    public void testGetList() {
        System.out.println("getList");
        String prop = "";
        CorrectedResultCollector instance = new CorrectedResultCollector();
        List result = instance.getList(prop);
        assertNotNull(result);
    }

    /**
     * Test of setExcludeLabels method, of class CorrectedResultCollector.
     */
    @Test
    public void testSetExcludeLabels() {
        System.out.println("setExcludeLabels");
        String excludeLabels = "";
        CorrectedResultCollector instance = new CorrectedResultCollector();
        instance.setExcludeLabels(excludeLabels);
    }

    /**
     * Test of setIncludeLabels method, of class CorrectedResultCollector.
     */
    @Test
    public void testSetIncludeLabels() {
        System.out.println("setIncludeLabels");
        String labels = "";
        CorrectedResultCollector instance = new CorrectedResultCollector();
        instance.setIncludeLabels(labels);
    }
}
