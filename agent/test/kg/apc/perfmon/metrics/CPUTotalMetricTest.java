package kg.apc.perfmon.metrics;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;

/**
 *
 * @author undera
 */
public class CPUTotalMetricTest extends TestCase {

    public CPUTotalMetricTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(CPUTotalMetricTest.class);
        return suite;
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getValue method, of class CPUTotalMetric.
     */
    public void testGetValue() throws Exception {
        System.out.println("getValue");
        StringBuilder res = new StringBuilder();
        SigarProxy sigar = SigarProxyCache.newInstance(new Sigar(), 500);
        MetricParams params=MetricParams.createFromString("idle", sigar);
        CPUTotalMetric instance = new CPUTotalMetric(sigar, params);
        instance.getValue(res);
        assertTrue(!res.toString().isEmpty());
    }
}
