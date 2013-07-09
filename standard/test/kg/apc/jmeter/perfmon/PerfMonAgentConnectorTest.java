package kg.apc.jmeter.perfmon;

import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author undera
 */
public class PerfMonAgentConnectorTest {

    public PerfMonAgentConnectorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of connect method, of class PerfMonAgentConnector.
     */
    @Test
    public void testConnect() throws Exception {
        System.out.println("connect");
        PerfMonAgentConnector instance = new PerfMonAgentConnectorImpl();
        instance.connect();
    }

    /**
     * Test of disconnect method, of class PerfMonAgentConnector.
     */
    @Test
    public void testDisconnect() {
        System.out.println("disconnect");
        PerfMonAgentConnector instance = new PerfMonAgentConnectorImpl();
        instance.disconnect();
    }

    /**
     * Test of generateSamples method, of class PerfMonAgentConnector.
     */
    @Test
    public void testGenerateSamples() throws Exception {
        System.out.println("generateSamples");
        PerfMonSampleGenerator collector = null;
        PerfMonAgentConnector instance = new PerfMonAgentConnectorImpl();
        instance.generateSamples(collector);
    }

    public class PerfMonAgentConnectorImpl implements PerfMonAgentConnector {

        public void setMetricType(String metric) {
        }

        public void setParams(String params) {
        }

        public void connect() throws IOException {
        }

        public void disconnect() {
        }

        public String getLabel(boolean translateHost) {
            return "";
        }

        public void generateSamples(PerfMonSampleGenerator collector) throws IOException {
        }

        public void addMetric(String metric, String params, java.lang.String label) {
        }
    }

    /**
     * Test of addMetric method, of class PerfMonAgentConnector.
     */
    @Test
    public void testAddMetric() {
        System.out.println("addMetric");
        String metric = "";
        String params = "";
        PerfMonAgentConnector instance = new PerfMonAgentConnectorImpl();
        instance.addMetric(metric, params, null);
    }
}
