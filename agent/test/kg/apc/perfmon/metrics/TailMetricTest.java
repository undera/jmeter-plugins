package kg.apc.perfmon.metrics;

import kg.apc.perfmon.metrics.TailMetric;
import java.io.File;
import java.io.PrintWriter;
import junit.framework.TestCase;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarProxyCache;

/**
 *
 * @author undera
 */
public class TailMetricTest extends TestCase {

    public TailMetricTest(String testName) {
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
        TailMetric instance = new TailMetric();

        StringBuilder res = new StringBuilder();
        instance.setParams("/notexists");
        instance.getValue(res);

        File f = File.createTempFile("plugins-tail-", ".log");
        PrintWriter w = new PrintWriter(f);
        instance.setParams(f.getAbsolutePath());

        w.write("1");
        w.flush();
        res = new StringBuilder();
        instance.getValue(res);
        assertEquals("1", res.toString());

        w.write("15");
        w.flush();
        res = new StringBuilder();
        instance.getValue(res);
        assertEquals("15", res.toString());
    }
}
