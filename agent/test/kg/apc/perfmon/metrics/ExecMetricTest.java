package kg.apc.perfmon.metrics;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author undera
 */
public class ExecMetricTest extends TestCase {

    public ExecMetricTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ExecMetricTest.class);
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
        StringBuffer res = new StringBuffer();
        //default is linux os
        String cmd = "echo:123";
        if(System.getProperty("os.name").toLowerCase().indexOf("windows") != -1) {
            cmd = "cmd:/C:echo:123";
        }
        ExecMetric instance = new ExecMetric(MetricParams.createFromString(cmd));
        instance.getValue(res);
        assertTrue(Double.parseDouble(res.toString()) == 123);
    }

    /*
    public void testGetValue2() throws Exception {
    System.out.println("getValue");
    StringBuffer res = new StringBuffer();
    ExecMetric instance = new ExecMetric(MetricParams.createFromString("/usr/bin/perl:-e:\"print 100*rand(1);\""));
    instance.getValue(res);
    assertTrue(Double.parseDouble(res.toString()) > 0);
    }
     */
}
