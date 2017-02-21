package com.blazemeter.jmeter;

import kg.apc.emulators.TestJMeterUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class RotatingResultCollectorGuiTest {

    @BeforeClass
    public static void setUpClass()
            throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    //@Test
    public void testGui() throws Exception {
        if (!GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance()) {
            RotatingResultCollectorGui gui = new RotatingResultCollectorGui();
            RotatingResultCollector te1 = (RotatingResultCollector) gui.createTestElement();
            RotatingResultCollector te2 = (RotatingResultCollector) gui.createTestElement();
            assertNotEquals("", te1.getName());

            te1.setMaxSamplesCount("7");
            gui.configure(te1);
            gui.modifyTestElement(te2);

            assertTrue(te1.equals(te2));
            assertEquals(7, te2.getMaxSamplesCountAsInt());

            JDialog frame = new JDialog();
            frame.add(gui);

            frame.setPreferredSize(new Dimension(800, 600));
            frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
            while (frame.isVisible()) {
                Thread.sleep(100);
            }
        }
    }

    @Test
    public void testGuiForm() throws Exception {
        RotatingResultCollectorGui gui = new RotatingResultCollectorGui();
        RotatingResultCollector te1 = (RotatingResultCollector) gui.createTestElement();
        RotatingResultCollector te2 = (RotatingResultCollector) gui.createTestElement();
        assertNotEquals("", te1.getName());

        te1.setMaxSamplesCount("7");
        gui.configure(te1);
        gui.modifyTestElement(te2);

        assertTrue(te1.equals(te2));
        assertEquals(7, te2.getMaxSamplesCountAsInt());

        gui.clearGui();
        gui.modifyTestElement(te1);
        assertEquals("", te1.getMaxSamplesCount());
    }

}