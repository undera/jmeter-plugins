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
public class MemProcMetricTest extends TestCase {

    public MemProcMetricTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(MemProcMetricTest.class);
        return suite;
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getValue method, of class MemProcMetric.
     */
    public void testGetValue() throws Exception {
        System.out.println("getValue");
        StringBuffer res = new StringBuffer();
        SigarProxy sigar = SigarProxyCache.newInstance(new Sigar(), 500);
        MemProcMetric instance = new MemProcMetric(sigar, MetricParams.createFromString("pid=" + sigar.getPid(), sigar));
        instance.getValue(res);
    }

    public void testGetValue_all() throws Exception {
        System.out.println("getValue");
        SigarProxy sigar = SigarProxyCache.newInstance(new Sigar(), 500);
        for (int n = 0; n < MemProcMetric.types.length; n++) {
            MetricParams params = MetricParams.createFromString("pid=" + sigar.getPid() + ":" + MemProcMetric.types[n], sigar);
            MemProcMetric instance = new MemProcMetric(sigar, params);
            StringBuffer res = new StringBuffer();
            instance.getValue(res);
            System.out.println(MemProcMetric.types[n] + "=" + res.toString());
        }
    }
}
