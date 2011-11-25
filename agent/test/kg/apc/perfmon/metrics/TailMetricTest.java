package kg.apc.perfmon.metrics;

import junit.framework.Test;
import junit.framework.TestSuite;
import java.io.File;
import java.io.PrintWriter;
import junit.framework.TestCase;

/**
 *
 * @author undera
 */
public class TailMetricTest extends TestCase {

    public TailMetricTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(TailMetricTest.class);
        return suite;
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
        TailMetric instance;

        StringBuilder res = new StringBuilder();
        instance = new TailMetric(MetricParams.createFromString("/notexists"));
        instance.getValue(res);

        File f = File.createTempFile("plugins-tail-", ".log");
        PrintWriter w = new PrintWriter(f);
        instance = new TailMetric(MetricParams.createFromString(f.getAbsolutePath()));

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
