package kg.apc.perfmon.metrics;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.hyperic.sigar.SigarProxy;

/**
 *
 * @author undera
 */
public class AbstractCPUMetricTest extends TestCase {

    public AbstractCPUMetricTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(AbstractCPUMetricTest.class);
        return suite;
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getMetric method, of class AbstractCPUMetric.
     */
    public void testGetMetric() {
        System.out.println("getMetric");
        SigarProxy sigar = null;
        MetricParams metricParams = MetricParams.createFromString("", sigar);
        AbstractCPUMetric result = AbstractCPUMetric.getMetric(sigar, metricParams);
        assertNotNull(result);
    }
}
