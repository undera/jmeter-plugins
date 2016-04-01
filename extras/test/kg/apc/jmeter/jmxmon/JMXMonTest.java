package kg.apc.jmeter.jmxmon;

import kg.apc.emulators.TestJMeterUtils;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.vizualizers.JMXMonGui;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JMXMonTest {
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

    public void setThreadStoped(boolean threadStoped) {
        this.threadStoped = threadStoped;
    }

    private boolean threadStoped = false;

    public Map<String, Double> getLatestSamples() {
        return latestSamples;
    }

    private Map<String, Double> latestSamples = new HashMap<String, Double>();

    public Map<String, Double> getQueryResults() {
        return queryResults;
    }

    private Map<String, Double> queryResults = new HashMap<String, Double>();

    @Before
    public void setUp() {
        TestJMeterUtils.createJmeterEnv();
        dataModel = new PowerTableModel(JMXMonGui.columnIdentifiers, JMXMonGui.columnClasses);
        dataModel.addRow(new Object[]{PROBE1, URL, USERNAME, PASSWORD, OBJ_NAME1, ATTRIBUTE1, "", false, false});
        dataModel.addRow(new Object[]{PROBE2, URL, USERNAME, PASSWORD, OBJ_NAME2, ATTRIBUTE2, "", true, false});
    }

    @Test
    public void testRun() throws InterruptedException {
        JMXMonCollector instance = new TestJMXMonCollector(this);
        instance.setData(JMeterPluginsUtils.tableModelRowsToCollectionProperty(dataModel, JMXMonCollector.DATA_PROPERTY));
        instance.testStarted();

        setQueryResult(ATTRIBUTE1, 1);
        setQueryResult(ATTRIBUTE2, 1);
        instance.processConnectors();
        assertLastSample(PROBE1, 1);
        assertEquals(null, latestSamples.get(PROBE2)); // Delta can not produce values at first loop

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

}
