package kg.apc.perfmon.metrics;

import junit.framework.TestCase;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;

/**
 *
 * @author undera
 */
public class MetricParamsTest extends TestCase {
    
    public MetricParamsTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of createFromString method, of class MetricParams.
     */
    public void testCreateFromString_ptql() {
        System.out.println("createFromString");
        String metricParams = "ptql=Exe.Name.ct=java,Args.*.ct=junit";
        String defaultType = "";
        final SigarProxy sigar = SigarProxyCache.newInstance(new Sigar(), 500);
        MetricParams result = MetricParams.createFromString(metricParams, defaultType, sigar);
        assertTrue(result.PID > 0);
    }
    
    public void testCreateFromString_name() {
        System.out.println("createFromString");
        String defaultType = "";
        final SigarProxy sigar = SigarProxyCache.newInstance(new Sigar(), 500);
        MetricParams resultLinux = MetricParams.createFromString("name=java#1", defaultType, sigar);
        MetricParams resultWin = MetricParams.createFromString("name=java.exe#1", defaultType, sigar);
        assertTrue(resultLinux.PID > 0 || resultWin.PID > 0);
    }
    
    public void testCreateFromString_pid() {
        System.out.println("createFromString");
        String defaultType = "";
        final SigarProxy sigar = SigarProxyCache.newInstance(new Sigar(), 500);
        String metricParams = "pid=" + sigar.getPid();
        MetricParams result = MetricParams.createFromString(metricParams, defaultType, sigar);
        assertTrue(result.PID > 0);
    }
}
