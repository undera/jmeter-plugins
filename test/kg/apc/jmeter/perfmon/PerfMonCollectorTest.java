package kg.apc.jmeter.perfmon;

import java.io.IOException;
import kg.apc.emulators.SocketEmulator;
import kg.apc.emulators.TestJMeterUtils;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.vizualizers.PerfMonGui;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import static org.junit.Assert.*;
import org.junit.*;

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
                    "localhost", "4444", "CPU", "label=testLabel"
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
        dataModel.addRow(new String[]{
                    "localhost", "4444", "EXEC", "label=test:echo 123"
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

    /**
     * Test of getMetricSettings method, of class PerfMonCollector.
     */
    @Test
    public void testGetMetricSettings() {
        System.out.println("getMetricSettings");
        PerfMonCollector instance = new PerfMonCollector();
        JMeterProperty expResult = new NullProperty();
        JMeterProperty result = instance.getMetricSettings();
        assertEquals(expResult.toString(), result.toString());
    }

    /**
     * Test of generateSample method, of class PerfMonCollector.
     */
    @Test
    public void testGenerateSample() {
        System.out.println("generateSample");
        double value = 0.0;
        String label = "";
        PerfMonCollector instance = new PerfMonCollector();
        instance.generateSample(value, label);
    }

    /**
     * Test of generateErrorSample method, of class PerfMonCollector.
     */
    @Test
    public void testGenerateErrorSample() {
        System.out.println("generateErrorSample");
        String label = "";
        String errorMsg = "";
        PerfMonCollector instance = new PerfMonCollector();
        instance.generateErrorSample(label, errorMsg);
    }

    /**
     * Test of generate2Samples method, of class PerfMonCollector.
     */
    @Test
    public void testGenerate2Samples_3args() {
        System.out.println("generate2Samples");
        long[] values = {0, 0};
        String label1 = "";
        String label2 = "";
        PerfMonCollector instance = new PerfMonCollector();
        instance.generate2Samples(values, label1, label2);
    }

    /**
     * Test of generate2Samples method, of class PerfMonCollector.
     */
    @Test
    public void testGenerate2Samples_4args() {
        System.out.println("generate2Samples");
        long[] values = {0, 0};
        String label1 = "";
        String label2 = "";
        double dividingFactor = 0.0;
        PerfMonCollector instance = new PerfMonCollector();
        instance.generate2Samples(values, label1, label2, dividingFactor);
    }

    /**
     * Test of getConnector method, of class PerfMonCollector.
     */
    @Test
    public void testGetConnector() throws Exception {
        System.out.println("getConnector");
        String host = "";
        int port = 0;
        PerfMonCollector instance = new PerfMonCollector();
        PerfMonAgentConnector result = instance.getConnector(host, port);
        assertTrue(result instanceof OldAgentConnector);
    }
}
