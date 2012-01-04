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
        StringBuffer res = new StringBuffer();
        final SigarProxy sigar = SigarProxyCache.newInstance(new Sigar(), 500);
        MetricParamsSigar params = MetricParamsSigar.createFromString("pid=" + sigar.getPid(), sigar);
        CPUProcMetric instance = new CPUProcMetric(sigar, params);
        instance.getValue(res);
        assertTrue(Double.parseDouble(res.toString()) >= 0);
    }

    public void testGetValue_all() throws Exception {
        System.out.println("getValue");
        SigarProxy sigar = SigarProxyCache.newInstance(new Sigar(), 500);
        for (int n = 0; n < CPUProcMetric.types.length; n++) {
            MetricParamsSigar params = MetricParamsSigar.createFromString("pid=" + sigar.getPid() + ":" + CPUProcMetric.types[n], sigar);
            CPUProcMetric instance = new CPUProcMetric(sigar, params);
            StringBuffer res = new StringBuffer();
            instance.getValue(res);
            System.out.println(CPUProcMetric.types[n] + "=" + res.toString());
            Thread.sleep(100);
            res = new StringBuffer();
            instance.getValue(res);
            System.out.println(CPUProcMetric.types[n] + "=" + res.toString());
        }
    }
}
