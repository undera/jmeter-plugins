package nz.co.breakpoint.jmeter.vizualizers.sshmon;

import java.io.IOException;

import kg.apc.jmeter.vizualizers.MonitoringSampler;
import kg.apc.jmeter.vizualizers.MonitoringSampleGenerator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Integration test requires an SSH server to be started externally 
 * (currently via process-exec-maven-plugin)
 */
public class SSHMonSamplerIT {
    public static ConnectionDetails localConnection = new ConnectionDetails("0.0.0.0", Integer.valueOf(System.getProperty("sshmon.sshd.port")));

    public class MockSampleGenerator implements MonitoringSampleGenerator {
        protected String metric = "";
        protected double value = Double.NaN;
        protected boolean collected = false;
        
        @Override
        public void generateSample(double d, String string) {
            this.metric = string;
            this.value = d;
            this.collected = true;
        }
    }

    @Test
    public void testGenerateSamples() {
        SSHMonSampler instance = new SSHMonSampler("test", localConnection, "echo 123", false);
        MockSampleGenerator collector = new MockSampleGenerator();
        instance.generateSamples(collector);
        assertTrue(collector.collected);
        assertEquals(collector.value, 123.0, 0.0);
        assertEquals(collector.metric, "test");
    }
}
