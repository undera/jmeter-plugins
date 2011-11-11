package kg.apc.perfmon.metrics;

import junit.framework.TestCase;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarProxyCache;

/**
 *
 * @author undera
 */
public class CPUPerfMetricTest extends TestCase {

    public CPUPerfMetricTest() {
    }

    /**
     * Test of getValue method, of class CPUPerfMetric.
     */
    public void testGetValue() throws Exception {
        System.out.println("getValue");
        StringBuilder res = new StringBuilder();
        CPUTotalMetric instance = new CPUTotalMetric(SigarProxyCache.newInstance(new Sigar(), 500));
        instance.getValue(res);
        assertTrue(Double.parseDouble(res.toString()) > 0);
    }
}
