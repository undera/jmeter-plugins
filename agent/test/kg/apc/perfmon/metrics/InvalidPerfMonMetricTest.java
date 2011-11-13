package kg.apc.perfmon.metrics;

import junit.framework.Test;
import junit.framework.TestSuite;
import kg.apc.perfmon.metrics.InvalidPerfMonMetric;
import junit.framework.TestCase;

/**
 *
 * @author undera
 */
public class InvalidPerfMonMetricTest extends TestCase {

    public InvalidPerfMonMetricTest() {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(InvalidPerfMonMetricTest.class);
        return suite;
    }

    /**
     * Test of getValue method, of class InvalidPerfMonMetric.
     */
    public void testGetValue() {
        System.out.println("getValue");
        StringBuilder res = new StringBuilder();
        InvalidPerfMonMetric instance = new InvalidPerfMonMetric();
        instance.getValue(res);
    }
}
