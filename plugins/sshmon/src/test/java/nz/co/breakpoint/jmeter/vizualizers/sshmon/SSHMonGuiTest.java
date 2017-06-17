package nz.co.breakpoint.jmeter.vizualizers.sshmon;

import kg.apc.emulators.TestJMeterUtils;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class SSHMonGuiTest {

    @BeforeClass
    public static void setUpClass() {
        TestJMeterUtils.createJmeterEnv();
    }

    @Test
    public void testCreateMonitoringResultsCollector() {
        SSHMonGui subject = new SSHMonGui();
        assertTrue(subject.createMonitoringResultsCollector() instanceof SSHMonCollector);
    }
}
