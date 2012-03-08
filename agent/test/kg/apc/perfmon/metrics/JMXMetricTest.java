package kg.apc.perfmon.metrics;

import junit.framework.TestCase;

/**
 *
 * @author undera
 */
public class JMXMetricTest extends TestCase {
    
    public JMXMetricTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getValue method, of class JMXMetric.
     */
    public void testGetValue() throws Exception {
        System.out.println("getValue");
        StringBuffer res = new StringBuffer();
        MetricParams params= MetricParams.createFromString("url=127.0.0.1\\:4711");
        JMXMetric instance = new JMXMetric(params);
        instance.getValue(res);
    }
}
