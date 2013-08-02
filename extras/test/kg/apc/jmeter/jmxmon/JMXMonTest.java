package kg.apc.jmeter.jmxmon;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXServiceURL;
import kg.apc.emulators.TestJMeterUtils;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.vizualizers.JMXMonGui;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.samplers.SampleEvent;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author cyberw
 */
public class JMXMonTest {
    public static final String URL = "service:jmx:rmi:///jndi/rmi://secc2273.ssnhm.com:6969/jmxrmi";//"service:jmx:rmi:///jndi/rmi://localhost:6969/jmxrmi";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String OBJ_NAME1 = "Something:name=objectName1";
    public static final String OBJ_NAME2 = "Something:name=objectName1";
    public static final String ATTRIBUTE1 = "attribute1";
    public static final String ATTRIBUTE2 = "attribute2";
    
    public static final String PROBE1 = "probe1";
    public static final String PROBE2 = "probe2";

    private PowerTableModel dataModel;
    private boolean threadStoped = false;
    private Map<String, Double> latestSamples = new HashMap<String, Double>();
    private Map<String, Double> queryResults = new HashMap<String, Double>();

    @Before
    public void setUp() {
        TestJMeterUtils.createJmeterEnv();
        dataModel = new PowerTableModel(JMXMonGui.columnIdentifiers, JMXMonGui.columnClasses);
        dataModel.addRow(new Object[]{ PROBE1, URL, USERNAME, PASSWORD, OBJ_NAME1, ATTRIBUTE1, "", false });
        dataModel.addRow(new Object[]{ PROBE2, URL, USERNAME, PASSWORD, OBJ_NAME1, ATTRIBUTE2, "", true });
    }
    
    @Test
    public void testRun() throws InterruptedException {
        JMXMonCollector instance = new TestJMXMonCollector();
        instance.setData(JMeterPluginsUtils.tableModelRowsToCollectionProperty(dataModel, JMXMonCollector.DATA_PROPERTY));
        instance.testStarted();
        
        setQueryResult(ATTRIBUTE1, 1);
        setQueryResult(ATTRIBUTE2, 1);
        instance.processConnectors();
        assertLastSample(PROBE1, 1);
        assertNull(latestSamples.get(PROBE2)); // Delta can not produce values at first loop
        
        setQueryResult(ATTRIBUTE1, -2);
        setQueryResult(ATTRIBUTE2, 2);
        instance.processConnectors();
        assertLastSample(PROBE1, -2);
        assertLastSample(PROBE2, 1);
        
        setQueryResult(ATTRIBUTE1, 13);
        setQueryResult(ATTRIBUTE2, 1);
        instance.processConnectors();
        assertLastSample(PROBE1, 13);
        assertLastSample(PROBE2, -1);

        instance.testEnded();
        assertSampleGeneratorThreadIsStoped();
                
    }

    public void setQueryResult(String attribute, double value) {
        queryResults.put(attribute, value);
    }
    
//    @Override
//    public ResultSet getQueryResult(String probeName) {
//        return TestConnection.resultSet(queryResults.get(probeName));
//    }

    private void assertSampleGeneratorThreadIsStoped() throws InterruptedException {
        synchronized (this) {
            if (!threadStoped) {
                wait(1000);
                assertTrue(threadStoped);
            }
        }
    }

    private void assertLastSample(String probeName, double expected) {
        final Double actual = latestSamples.get(probeName);
        assertEquals(expected, actual, 0.0001);
    }

    private class TestJMXMonCollector extends JMXMonCollector {
        @Override
        public void run() {
            try {
                // Override run to controll the entire flow from the test
                Thread.sleep(24*60*60*1000);
            } catch (InterruptedException ex) {
                synchronized (JMXMonTest.this) {
                    threadStoped = true;
                    JMXMonTest.this.notifyAll();
                }
            }
        }
        
        @Override
        protected void initiateConnector(JMXServiceURL u, Hashtable attributes, String name, boolean delta, String objectName, String attribute, String key) throws MalformedURLException, IOException {
            MBeanServerConnection conn = new MBeanServerConnectionEmul(queryResults);
            jmxMonSamplers.add(new JMXMonSampler(conn, name, objectName, attribute, key, delta));
        }
        
        @Override
        public void jmxMonSampleOccurred(SampleEvent event) {
            super.sampleOccurred(event);
            double value = JMXMonSampleResult.getValue(event.getResult());
            latestSamples.put(event.getResult().getSampleLabel(), value);
        }

        @Override
        public void testEnded() {
            super.testEnded(); //To change body of generated methods, choose Tools | Templates.
        }
        
        
    }
}
