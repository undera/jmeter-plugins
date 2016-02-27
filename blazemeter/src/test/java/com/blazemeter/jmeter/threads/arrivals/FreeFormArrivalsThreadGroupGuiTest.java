package com.blazemeter.jmeter.threads.arrivals;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.testelement.TestElement;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class FreeFormArrivalsThreadGroupGuiTest {
    @BeforeClass
    public static void setUpClass() throws IOException {
        TestJMeterUtils.createJmeterEnv();
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