package kg.apc.perfmon.metrics;

import junit.framework.TestCase;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;

/**
 *
 * @author undera
 */
public class MemTotalMetricTest extends TestCase {

    public MemTotalMetricTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getValue method, of class MemTotalMetric.
     */
    public void testGetValue() throws Exception {
        System.out.println("getValue");
        StringBuilder res = new StringBuilder();
        SigarProxy sigar = SigarProxyCache.newInstance(new Sigar(), 500);
        MemTotalMetric instance = new MemTotalMetric(sigar, MetricParams.createFromString("", sigar));
        instance.getValue(res);
    }
}
