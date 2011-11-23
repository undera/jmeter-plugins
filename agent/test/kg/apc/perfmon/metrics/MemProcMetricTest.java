/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
        StringBuilder res = new StringBuilder();
        SigarProxy sigar = SigarProxyCache.newInstance(new Sigar(), 500);
        MemProcMetric instance = new MemProcMetric(sigar, MetricParams.createFromString("", sigar));
        instance.getValue(res);
    }
}
