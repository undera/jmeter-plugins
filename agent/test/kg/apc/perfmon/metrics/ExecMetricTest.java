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
        StringBuilder res = new StringBuilder();
        ExecMetric instance = new ExecMetric();
        instance.setParams("echo:123");
        instance.getValue(res);
        assertTrue(Double.parseDouble(res.toString()) > 0);
    }
/*
    public void testGetValue2() throws Exception {
        System.out.println("getValue");
        StringBuilder res = new StringBuilder();
        ExecMetric instance = new ExecMetric();
        instance.setParams("/bin/sh:-c:uptime | cut -d ' ' -f 12 | cut -d ',' -f 1");
        instance.getValue(res);
        assertTrue(Double.parseDouble(res.toString()) > 0);
    }
*/
    /**
     * Test of setParams method, of class ExecMetric.
     */
    public void testSetParams() {
        System.out.println("setParams");
        String string = "";
        ExecMetric instance = new ExecMetric();
        instance.setParams(string);
    }
}
