package com.blazemeter.jmeter.xmpp;

import kg.apc.emulators.TestJMeterUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class JMeterXMPPConnectionGuiTest {
    @BeforeClass
    public static void setUpClass()
            throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @Test
    public void testCreateTestElement() throws Exception {
        JMeterXMPPConnectionGui obj = new JMeterXMPPConnectionGui();
        assertTrue(obj.createTestElement() instanceof JMeterXMPPConnection);
    }

    @Test
    public void testConfigure() throws Exception {
        JMeterXMPPConnectionGui obj = new JMeterXMPPConnectionGui();
        obj.configure(obj.createTestElement());
    }

    @Test
    public void testClearGui() throws Exception {
        JMeterXMPPConnectionGui obj = new JMeterXMPPConnectionGui();
        obj.clearGui();
    }

    @Test
    public void testModifyTestElement() throws Exception {
        JMeterXMPPConnectionGui obj = new JMeterXMPPConnectionGui();
        obj.modifyTestElement(obj.createTestElement());
    }
}