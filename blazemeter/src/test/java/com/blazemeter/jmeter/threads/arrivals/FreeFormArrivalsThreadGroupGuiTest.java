package com.blazemeter.jmeter.threads.arrivals;

import kg.apc.emulators.TestJMeterUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class FreeFormArrivalsThreadGroupGuiTest {
    @BeforeClass
    public static void setUpClass() throws IOException {
        TestJMeterUtils.createJmeterEnv();
    }

    @Test
    public void testFlow() {
        FreeFormArrivalsThreadGroupGui obj = new FreeFormArrivalsThreadGroupGui();
        FreeFormArrivalsThreadGroup te = (FreeFormArrivalsThreadGroup) obj.createTestElement();
        // TODO: add couple of rows into schedule
        obj.configure(te);
        obj.clearGui();
        obj.modifyTestElement(te);
    }

    public void displayGUI() throws InterruptedException {
        FreeFormArrivalsThreadGroupGui obj = new FreeFormArrivalsThreadGroupGui();
        ArrivalsThreadGroup te = (ArrivalsThreadGroup) obj.createTestElement();
        te.setTargetLevel("10");
        te.setRampUp("60");
        obj.configure(te);
        obj.clearGui();
        obj.modifyTestElement(te);

        JFrame frame = new JFrame("FrameDemo");
        frame.setPreferredSize(new Dimension(1024, 768));
        frame.setMinimumSize(new Dimension(640, 480));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(obj, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        Thread.sleep(60000);
    }
}