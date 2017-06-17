package nz.co.breakpoint.jmeter.vizualizers.sshmon;

import java.util.List;
import kg.apc.emulators.TestJMeterUtils;
import kg.apc.jmeter.JMeterPluginsUtils;
import static kg.apc.jmeter.vizualizers.MonitoringResultsCollector.DATA_PROPERTY;
import kg.apc.jmeter.vizualizers.MonitoringSampler;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SSHMonCollectorTest {

    private PowerTableModel dataModel;

    @BeforeClass
    public static void setUpClass() {
        TestJMeterUtils.createJmeterEnv();        
    }

    @Before
    public void setUp() {
        dataModel = new PowerTableModel(SSHMonGui.columnIdentifiers, SSHMonGui.columnClasses);
        dataModel.addRow(new Object[]{
            "label", "host", 123, "username", "key", "password", "command", false
        });
    }

    public class TestSSHMonCollector extends SSHMonCollector {
        // Make protected attribute accessible:
        public List<MonitoringSampler> getSamplers() { return samplers; }
    }
    
    @Test
    public void testInitiateConnectors() {
        TestSSHMonCollector instance = new TestSSHMonCollector();
        instance.setData(JMeterPluginsUtils.tableModelRowsToCollectionProperty(dataModel, DATA_PROPERTY));
        
        instance.initiateConnectors();

        assertEquals(instance.getSamplers().size(), 1);
        
        SSHMonSampler sampler = (SSHMonSampler)instance.getSamplers().get(0);
        assertEquals(sampler.getMetricName(), dataModel.getValueAt(0, 0));
        assertEquals(sampler.getConnectionDetails(), new ConnectionDetails(
            (String)dataModel.getValueAt(0, 3), // username
            (String)dataModel.getValueAt(0, 1), // host
            (Integer)dataModel.getValueAt(0, 2), // port
            (String)dataModel.getValueAt(0, 5), // password
            ((String)dataModel.getValueAt(0, 4)).getBytes())); // privateKey
        assertEquals(sampler.getRemoteCommand(), dataModel.getValueAt(0, 6));
        assertEquals(sampler.isSampleDeltaValue(), dataModel.getValueAt(0, 7));
    }
}
