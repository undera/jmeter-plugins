package kg.apc.perfmon;

import junit.framework.TestCase;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarProxyCache;

/**
 *
 * @author undera
 */
public class RunMetricTest extends TestCase {

    public RunMetricTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getValue method, of class RunMetric.
     */
    public void testGetValue() throws Exception {
        System.out.println("getValue");
        StringBuilder res = new StringBuilder();
        RunMetric instance = new RunMetric(SigarProxyCache.newInstance(new Sigar(), 500));
        instance.setParams("echo 123");
        instance.getValue(res);
        assertTrue(Double.parseDouble(res.toString()) > 0);
    }
}
