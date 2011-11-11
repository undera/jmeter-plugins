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
     * Test of setMetricType method, of class PerfMonAgentConnector.
     */
    @Test
    public void testSetMetricType() {
        System.out.println("setMetricType");
        String metric = "";
        PerfMonAgentConnector instance = new PerfMonAgentConnectorImpl();
        instance.setMetricType(metric);
    }

    /**
     * Test of setParams method, of class PerfMonAgentConnector.
     */
    @Test
    public void testSetParams() {
        System.out.println("setParams");
        String params = "";
        PerfMonAgentConnector instance = new PerfMonAgentConnectorImpl();
        instance.setParams(params);
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
    }
}
