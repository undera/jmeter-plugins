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
public class TCPStatMetricTest extends TestCase {

    public TCPStatMetricTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(TCPStatMetricTest.class);
        return suite;
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getValue method, of class TCPStatMetric.
     */
    public void testGetValue() throws Exception {
        System.out.println("getValue");
        SigarProxy sigar = SigarProxyCache.newInstance(new Sigar(), 500);
        for (int n = 0; n < TCPStatMetric.types.length; n++) {
            TCPStatMetric instance = new TCPStatMetric(sigar, MetricParams.createFromString(TCPStatMetric.types[n]));
            StringBuffer res = new StringBuffer();
            instance.getValue(res);
            System.out.println(TCPStatMetric.types[n] + '=' + res.toString());
        }
    }
}
