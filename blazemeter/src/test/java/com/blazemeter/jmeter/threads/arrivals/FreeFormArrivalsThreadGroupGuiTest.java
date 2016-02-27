package com.blazemeter.jmeter.threads.arrivals;

import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class FreeFormArrivalsThreadGroupGuiTest {
    @BeforeClass
    public static void setUpClass() throws IOException {
        JMeterContextService.getContext().setVariables(new JMeterVariables());
        File propsFile = File.createTempFile("jmeter-plugins", ".properties");
        propsFile.deleteOnExit();

        JMeterUtils.loadJMeterProperties(propsFile.getAbsolutePath());
        JMeterUtils.setJMeterHome(propsFile.getParent());
        JMeterUtils.setLocale(new Locale("ignoreResources"));
    }

    @Test
    public void testFlow() {
        FreeFormArrivalsThreadGroupGui obj = new FreeFormArrivalsThreadGroupGui();
        TestElement te = obj.createTestElement();
        obj.configure(te);
        obj.clearGui();
        obj.modifyTestElement(te);
    }
}