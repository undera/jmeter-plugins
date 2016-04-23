package com.blazemeter.jmeter.xmpp;

import com.blazemeter.jmeter.xmpp.actions.AbstractXMPPAction;
import kg.apc.emulators.TestJMeterUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;


public class JMeterXMPPSamplerGuiTest {
    @BeforeClass
    public static void setUpClass()
            throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @Test
    public void testCreateTestElement() throws Exception {
        JMeterXMPPSamplerGui obj = new JMeterXMPPSamplerGuiEmul();
        assertTrue(obj.createTestElement() instanceof JMeterXMPPSampler);
    }

    @Test
    public void testConfigure() throws Exception {
        JMeterXMPPSamplerGui obj = new JMeterXMPPSamplerGuiEmul();
        obj.configure(obj.createTestElement());
    }

    @Test
    public void testModifyTestElement() throws Exception {
        JMeterXMPPSamplerGui obj = new JMeterXMPPSamplerGuiEmul();
        obj.modifyTestElement(obj.createTestElement());
    }

    @Test
    public void testClearGui() throws Exception {
        JMeterXMPPSamplerGui obj = new JMeterXMPPSamplerGuiEmul();
        obj.clearGui();
    }

    private class JMeterXMPPSamplerGuiEmul extends JMeterXMPPSamplerGui {
        @Override
        protected Map<String, AbstractXMPPAction> getActions() {
            Map<String, AbstractXMPPAction> list = new HashMap<>();
            JMeterXMPPSamplerTest.fillActionClasses(list);
            return list;
        }
    }
}