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
        MetricParamsSigar result = MetricParamsSigar.createFromString(metricParams, sigar);
        assertTrue(result.PID > 0);
    }

    public void testCreateFromString_name() {
        System.out.println("createFromString");
        final SigarProxy sigar = SigarProxyCache.newInstance(new Sigar(), 500);
        MetricParamsSigar resultLinux = MetricParamsSigar.createFromString("name=java#1", sigar);
        MetricParamsSigar resultWin = MetricParamsSigar.createFromString("name=java.exe#1", sigar);
        assertTrue(resultLinux.PID > 0 || resultWin.PID > 0);
    }

    public void testCreateFromString_escaping() {
        System.out.println("createFromString");
        final SigarProxy sigar = SigarProxyCache.newInstance(new Sigar(), 500);
        MetricParamsSigar result = MetricParamsSigar.createFromString("exec:testcolon\\:attr:next", sigar);
        assertEquals("exec", result.params[0]);
        assertEquals("testcolon:attr", result.params[1]);
        assertEquals("next", result.params[2]);
    }

    public void testCreateFromString_escaping_issue101() {
        System.out.println("createFromString");
        final SigarProxy sigar = SigarProxyCache.newInstance(new Sigar(), 500);
        MetricParamsSigar result = MetricParamsSigar.createFromString("exec:/bin/sh:-c:wget http\\://localhost\\:8000", sigar);
        assertEquals("exec", result.params[0]);
        assertEquals("/bin/sh", result.params[1]);
        assertEquals("-c", result.params[2]);
        assertEquals("wget http://localhost:8000", result.params[3]);
    }

    public void testCreateFromString_multicore() {
        System.out.println("createFromString");
        final SigarProxy sigar = SigarProxyCache.newInstance(new Sigar(), 500);
        MetricParamsSigar result = MetricParamsSigar.createFromString("core=1", sigar);
        assertEquals(1, result.coreID);
    }

    public void testCreateFromString() {
        System.out.println("createFromString");
        final SigarProxy sigar = SigarProxyCache.newInstance(new Sigar(), 500);
        String metricParams = "pid=" + sigar.getPid();
        MetricParamsSigar result = MetricParamsSigar.createFromString(metricParams, sigar);
        assertTrue(result.PID > 0);
    }

    /**
     * Test of logAvailableProcesses method, of class MetricParams.
     */
    public void testLogAvailableProcesses() {
        System.out.println("logAvailableProcesses");
        final SigarProxy sigar = SigarProxyCache.newInstance(new Sigar(), 500);
        MetricParamsSigar.logAvailableProcesses(sigar);
    }
}
