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
        StringBuffer res = new StringBuffer("");
        SigarProxy sigar = SigarProxyCache.newInstance(new Sigar(), 500);
        MetricParamsSigar params = MetricParamsSigar.createFromString("idle", sigar);
        CPUTotalMetric instance = new CPUTotalMetric(sigar, params);
        instance.getValue(res);
    }

    public void testGetValue_core() throws Exception {
        System.out.println("getValue");
        StringBuffer res = new StringBuffer("");
        SigarProxy sigar = SigarProxyCache.newInstance(new Sigar(), 500);
        MetricParamsSigar params = MetricParamsSigar.createFromString("core=0:idle", sigar);
        CPUTotalMetric instance = new CPUTotalMetric(sigar, params);
        instance.getValue(res);
    }

    public void testGetValue_all() throws Exception {
        System.out.println("getValue");
        SigarProxy sigar = SigarProxyCache.newInstance(new Sigar(), 500);
        for (int n = 0; n < CPUTotalMetric.types.length; n++) {
            MetricParamsSigar params = MetricParamsSigar.createFromString("pid=" + sigar.getPid() + ":" + CPUTotalMetric.types[n], sigar);
            CPUTotalMetric instance = new CPUTotalMetric(sigar, params);
            StringBuffer res = new StringBuffer("");
            instance.getValue(res);
            assertTrue(!res.toString().equals("NaN"));
            System.out.println(CPUTotalMetric.types[n] + "=" + res.toString());
        }
    }
}
