package kg.apc.jmeter.dbmon;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import kg.apc.emulators.TestJMeterUtils;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.vizualizers.DbMonGui;
import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.threads.JMeterContextService;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DbmonTest implements TestConnection.TestConnectionDataProvider {

    public static final String TEST_CONNECTION = "testConnection";
    public static final String QUERY1 = "select value1 from table1";
    public static final String QUERY2 = "select value2 from table1";
    public static final String PROBE1 = "probe1";
    public static final String PROBE2 = "probe2";
    private PowerTableModel dataModel;
    private boolean threadStoped = false;
    private Map<String, Double> latestSamples = new HashMap<String, Double>();
    private Map<String, Double> queryResults = new HashMap<String, Double>();

    @Before
    public void setUp() {
        TestJMeterUtils.createJmeterEnv();
        JMeterContextService.getContext().getVariables().putObject(TEST_CONNECTION, new TestDataSource());

        dataModel = new PowerTableModel(DbMonGui.columnIdentifiers, DbMonGui.columnClasses);
        dataModel.addRow(new Object[]{TEST_CONNECTION, PROBE1, false, QUERY1});
        dataModel.addRow(new Object[]{TEST_CONNECTION, PROBE2, true, QUERY2});
    }

    @Test
    public void testRun() throws InterruptedException {
        DbMonCollector instance = new TestDbMonCollector();
        instance.setData(JMeterPluginsUtils.tableModelRowsToCollectionProperty(dataModel, DbMonCollector.DATA_PROPERTY));
        instance.testStarted("localhost");

        setQueryResult(QUERY1, 1);
        setQueryResult(QUERY2, 1);
        instance.processConnectors();
        assertLastSample(PROBE1, 1);
        assertNull(latestSamples.get(PROBE2)); // Delta can not produce values at first loop

        setQueryResult(QUERY1, -2);
        setQueryResult(QUERY2, 2);
        instance.processConnectors();
        assertLastSample(PROBE1, -2);
        assertLastSample(PROBE2, 1);

        setQueryResult(QUERY1, 13);
        setQueryResult(QUERY2, 1);
        instance.processConnectors();
        assertLastSample(PROBE1, 13);
        assertLastSample(PROBE2, -1);

        instance.testEnded();
        assertSampleGeneratorThreadIsStoped();
    }

    public void setQueryResult(String sql, double value) {
        queryResults.put(sql, value);
    }

    @Override
    public ResultSet getQueryResult(String sql) {
        return TestConnection.resultSet(queryResults.get(sql));
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
        assertEquals(expected, actual, 0.0001);
    }

    private class TestDataSource implements DataSourceComponent {

        @Override
        public Connection getConnection() throws SQLException {
            return new TestConnection(DbmonTest.this);
        }

        @Override
        public void configure(Configuration c) throws ConfigurationException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    private class TestDbMonCollector extends DbMonCollector {

        @Override
        public void run() {
            try {
                // Override run to controll the entire flow from the test
                Thread.sleep(24 * 60 * 60 * 1000);
            } catch (InterruptedException ex) {
                synchronized (DbmonTest.this) {
                    threadStoped = true;
                    DbmonTest.this.notifyAll();
                }
            }
        }

        @Override
        public void dbMonSampleOccurred(SampleEvent event) {
            super.sampleOccurred(event);
            double value = DbMonSampleResult.getValue(event.getResult());
            latestSamples.put(event.getResult().getSampleLabel(), value);
        }
    }
}
