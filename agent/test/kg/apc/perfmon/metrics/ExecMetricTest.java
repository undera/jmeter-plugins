package kg.apc.perfmon.metrics;

import junit.framework.TestCase;

/**
 *
 * @author undera
 */
public class ExecMetricTest extends TestCase {

    public ExecMetricTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getValue method, of class RunMetric.
     */
    public void testGetValue() throws Exception {
        System.out.println("getValue");
        StringBuilder res = new StringBuilder();
        ExecMetric instance = new ExecMetric();
        instance.setParams("echo 123");
        instance.getValue(res);
        assertTrue(Double.parseDouble(res.toString()) > 0);
    }
}
