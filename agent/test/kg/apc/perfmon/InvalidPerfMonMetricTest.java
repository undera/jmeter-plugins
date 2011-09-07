package kg.apc.perfmon;

import junit.framework.TestCase;

/**
 *
 * @author undera
 */
public class InvalidPerfMonMetricTest extends TestCase {

    public InvalidPerfMonMetricTest() {
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
