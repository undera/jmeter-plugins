package kg.apc.perfmon.metrics;

import junit.framework.TestCase;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;

/**
 *
 * @author undera
 */
public class NetworkIOMetricTest extends TestCase {
    
    public NetworkIOMetricTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of logAvailableInterfaces method, of class NetworkIOMetric.
     */
    public void testLogAvailableInterfaces() {
        System.out.println("logAvailableInterfaces");
        SigarProxy sigar = SigarProxyCache.newInstance(new Sigar(), 500);
        NetworkIOMetric.logAvailableInterfaces(sigar);
    }

    /**
     * Test of getValue method, of class NetworkIOMetric.
     */
    public void testGetValue() throws Exception {
        System.out.println("getValue");
        SigarProxy sigar = SigarProxyCache.newInstance(new Sigar(), 500);
        for (int n = 0; n < NetworkIOMetric.types.length; n++) {
            NetworkIOMetric instance = new NetworkIOMetric(sigar, NetworkIOMetric.types[n]);
            StringBuilder res = new StringBuilder();
            instance.getValue(res);
            System.out.println(NetworkIOMetric.types[n] + "=" + res.toString());
            Thread.sleep(100);
            res = new StringBuilder();
            instance.getValue(res);
            System.out.println(NetworkIOMetric.types[n] + "=" + res.toString());
        }
    }
}
