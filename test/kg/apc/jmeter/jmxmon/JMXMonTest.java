package kg.apc.jmeter.jmxmon;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import kg.apc.emulators.TestJMeterUtils;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.vizualizers.JMXMonGui;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.threads.JMeterContextService;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author mabo
 */
public class JMXMonTest implements TestConnection.TestConnectionDataProvider {
    public static final String URL = "service:jmx:rmi:///jndi/rmi://localhost:6969/jmxrmi";
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
        // JMeterContextService.getContext().getVariables().putObject(TEST_CONNECTION, new TestDataSource());

        dataModel = new PowerTableModel(JMXMonGui.columnIdentifiers, JMXMonGui.columnClasses);
        dataModel.addRow(new Object[]{ PROBE1, URL, USERNAME, PASSWORD, OBJ_NAME1, ATTRIBUTE1, false });
        dataModel.addRow(new Object[]{ PROBE2, URL, USERNAME, PASSWORD, OBJ_NAME1, ATTRIBUTE1, false });
    }
    
    @Test
    public void testRun() throws InterruptedException {
        JMXMonCollector instance = new TestJMXMonCollector();
        instance.setData(JMeterPluginsUtils.tableModelRowsToCollectionProperty(dataModel, JMXMonCollector.DATA_PROPERTY));
        instance.testStarted();
        
        setQueryResult(PROBE1, 1);
        setQueryResult(PROBE2, 1);
        instance.processConnectors();
        assertLastSample(PROBE1, 1);
        assertNull(latestSamples.get(PROBE2)); // Deleta can not produce values at first loop
        
        setQueryResult(PROBE1, -2);
        setQueryResult(PROBE2, 2);
        instance.processConnectors();
        assertLastSample(PROBE1, -2);
        assertLastSample(PROBE2, 1);
        
        setQueryResult(PROBE1, 13);
        setQueryResult(PROBE2, 1);
        instance.processConnectors();
        assertLastSample(PROBE1, 13);
        assertLastSample(PROBE2, -1);

        instance.testEnded();
        assertSampleGeneratorThreadIsStoped();        
    }

    public void setQueryResult(String probeName, double value) {
        queryResults.put(probeName, value);
    }
    
//    @Override
    public ResultSet getQueryResult(String probeName) {
        return TestConnection.resultSet(queryResults.get(probeName));
    }

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
        if (actual == null) {
            for (String s : latestSamples.keySet())
                System.out.println("Key: " + s);
            System.exit(-1);
            assertTrue(false);
        }
        assertEquals(expected, actual, 0.0001);
    }
/*
    private class TestDataSource implements DataSourceComponent {
        @Override
        public Connection getConnection() throws SQLException {
            return new TestConnection(JMXMonTest.this);
        }

        @Override
        public void configure(Configuration c) throws ConfigurationException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
  */  
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
        public void jmxMonSampleOccurred(SampleEvent event) {
            super.sampleOccurred(event);
            double value = JMXMonSampleResult.getValue(event.getResult());
            latestSamples.put(event.getResult().getSampleLabel(), value);
        }
    }
}
