package kg.apc.perfmon.metrics;

import junit.framework.TestCase;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;

/**
 *
 * @author undera
 */
public class CPUProcMetricTest extends TestCase {

    public CPUProcMetricTest() {
    }

    /**
     * Test of getValue method, of class CPUPerfMetric.
     */
    public void testGetValue() throws Exception {
        System.out.println("getValue");
        StringBuilder res = new StringBuilder();
        final SigarProxy sigar = SigarProxyCache.newInstance(new Sigar(), 500);
        MetricParams params = MetricParams.createFromString("pid="+sigar.getPid(), sigar);
        CPUProcMetric instance = new CPUProcMetric(sigar, params);
        instance.getValue(res);
        assertTrue(Double.parseDouble(res.toString()) >= 0);
    }
}
