package kg.apc.jmeter.perfmon;

import java.io.IOException;
import kg.apc.emulators.SocketEmulator;
import org.apache.jmeter.gui.util.PowerTableModel;
import kg.apc.emulators.TestJMeterUtils;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.vizualizers.PerfMonGui;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author APC
 */
public class PerfMonCollectorTest {

    private class PerfMonCollectorEmul
            extends PerfMonCollector {

        SocketEmulator sock = new SocketEmulator();

        @Override
        protected PerfMonAgentConnector getConnector(String host, int port) throws IOException {
            return new UnavailableAgentConnector(new IOException("Unit test"));
        }
    }
    private PowerTableModel dataModel;

    public PerfMonCollectorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        dataModel = new PowerTableModel(PerfMonGui.columnIdentifiers, PerfMonGui.columnClasses);
        dataModel.addRow(new String[]{
                    "localhost", "4444", "CPU", ""
                });
        dataModel.addRow(new String[]{
                    "localhost", "4444", "Memory", ""
                });
        dataModel.addRow(new String[]{
                    "localhost", "4444", "Swap", ""
                });
        dataModel.addRow(new String[]{
                    "localhost", "4444", "Disks I/O", ""
                });
        dataModel.addRow(new String[]{
                    "localhost", "4444", "Network I/O", ""
                });
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSetData() {
        System.out.println("setData");
        CollectionProperty rows = new CollectionProperty();
        PerfMonCollector instance = new PerfMonCollector();
        instance.setData(rows);
    }

    @Test
    public void testGetData() {
        System.out.println("getData");
        PerfMonCollector instance = new PerfMonCollector();
        JMeterProperty result = instance.getMetricSettings();
        assertNotNull(result);
    }

    @Test
    public void testSampleOccurred() {
        System.out.println("sampleOccurred");
        SampleEvent event = null;
        PerfMonCollector instance = new PerfMonCollector();
        instance.sampleOccurred(event);
    }

    @Test
    public void testRun() throws InterruptedException {
        System.out.println("run");
        PerfMonCollector instance = new PerfMonCollectorEmul();
        instance.setData(JMeterPluginsUtils.tableModelRowsToCollectionProperty(dataModel, PerfMonCollector.DATA_PROPERTY));
        instance.testStarted();
        Thread.sleep(1500);
        instance.testEnded();
    }

    @Test
    public void testTestStarted() {
        System.out.println("testStarted");
        PerfMonCollector instance = new PerfMonCollector();
        instance.testStarted();
    }

    @Test
    public void testTestEnded() {
        System.out.println("testEnded");
        PerfMonCollector instance = new PerfMonCollector();
        instance.testStarted();
        instance.testEnded();
    }

}
