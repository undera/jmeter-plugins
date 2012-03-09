package kg.apc.perfmon.metrics;

import javax.management.MBeanServerConnection;
import junit.framework.TestCase;
import kg.apc.perfmon.metrics.jmx.EmulatorMBeanServerConnection;
import kg.apc.perfmon.metrics.jmx.JMXConnectorHelper;

/**
 *
 * @author undera
 */
public class JMXMetricTest extends TestCase {

    public JMXMetricTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getValue method, of class JMXMetric.
     */
    public void testGetValue() throws Exception {
        System.out.println("getValue");
        StringBuffer res = new StringBuffer();
        MetricParams params = MetricParams.createFromString("url=127.0.0.1\\:4711:type=gc-count");
        JMXMetric instance = new JMXMetric(params, new EmulatorJMXConnector());
        instance.getValue(res);
        System.out.println(res.toString());
        System.gc();
        res.append('/');
        instance.getValue(res);
        System.out.println(res.toString());
        assertEquals(3, res.length());
    }

    public void testGetValue_class() throws Exception {
        System.out.println("getValue class");
        StringBuffer res = new StringBuffer();
        MetricParams params = MetricParams.createFromString("url=127.0.0.1\\:4711:type=class-count");
        JMXMetric instance = new JMXMetric(params, new EmulatorJMXConnector());
        instance.getValue(res);
        System.out.println(res.toString());
        System.gc();
        res.append('/');
        instance.getValue(res);
        System.out.println(res.toString());
        assertTrue(3 <= res.length());
    }

    private static class EmulatorJMXConnector extends JMXConnectorHelper {

        public MBeanServerConnection getServerConnection(String url, String user, String pwd) {
            return new EmulatorMBeanServerConnection();
        }
    }
}
