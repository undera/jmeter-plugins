package kg.apc.perfmon.metrics;

import junit.framework.TestCase;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;

/**
 *
 * @author undera
 */
public class DiskIOMetricTest extends TestCase {

    public DiskIOMetricTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getValue method, of class DiskIOMetric.
     */
    public void testGetValue() throws Exception {
        System.out.println("getValue");
        StringBuilder res = new StringBuilder();
        SigarProxy sigar = SigarProxyCache.newInstance(new Sigar(), 500);
        DiskIOMetric instance = new DiskIOMetric(sigar, "used");
        instance.getValue(res);
        System.err.println(res.toString());
    }

    public void testGetValue_concrete() throws Exception {
        System.out.println("getValue_c");
        StringBuilder res = new StringBuilder();
        SigarProxy sigar = SigarProxyCache.newInstance(new Sigar(), 500);
        DiskIOMetric instance = new DiskIOMetric(sigar, "fs=/");
        instance.getValue(res);
        assertTrue(!res.toString().isEmpty());
        System.err.append(res.toString());
    }

    /**
     * Test of logAllAvailableFilesystems method, of class DiskIOMetric.
     */
    public void testLogAllAvailableFilesystems() {
        System.out.println("logAllAvailableFilesystems");
        SigarProxy sigar = SigarProxyCache.newInstance(new Sigar(), 500);
        DiskIOMetric.logAllAvailableFilesystems(sigar);
    }
}
