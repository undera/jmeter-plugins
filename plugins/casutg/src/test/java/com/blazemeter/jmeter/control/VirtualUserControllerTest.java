package com.blazemeter.jmeter.control;

import com.blazemeter.jmeter.threads.arrivals.ArrivalsThreadGroup;
import com.blazemeter.jmeter.threads.arrivals.ArrivalsThreadGroupTest;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.sampler.DebugSampler;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class VirtualUserControllerTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        ArrivalsThreadGroupTest.setUpClass();
        TestJMeterUtils.createJmeterEnv();
        // Workaround for new JMeters
        JMeterUtils.setProperty("saveservice_properties", JMeterUtils.getJMeterHome() + "/ss.props");
        JMeterUtils.setProperty("upgrade_properties", JMeterUtils.getJMeterHome() + "/ss.props");
    }

    @Test
    public void testThreadGroupInterrupted() {
        VirtualUserController vuc = new VirtualUserController();
        vuc.addTestElement(new DebugSampler());

        ArrivalsThreadGroup tg = new ArrivalsThreadGroup() {
            @Override
            public boolean isRunning() {
                return false;
            }
        };
        tg.addTestElement(vuc);
        vuc.setOwner(tg);
        assertNull(vuc.next());
    }
}