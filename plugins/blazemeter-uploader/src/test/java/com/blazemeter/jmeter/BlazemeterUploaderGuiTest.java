package com.blazemeter.jmeter;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.testelement.TestElement;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.Assert.*;

public class BlazemeterUploaderGuiTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

//    @Test
    public void displayGUI() throws InterruptedException {
        if (!GraphicsEnvironment.isHeadless()) {
            BlazemeterUploaderGui obj = new BlazemeterUploaderGui();
            TestElement te = obj.createTestElement();
            obj.configure(te);
            obj.clearGui();
            obj.modifyTestElement(te);

            JFrame frame = new JFrame(obj.getStaticLabel());
            frame.setPreferredSize(new Dimension(800, 600));
            frame.getContentPane().add(obj, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);

            while (frame.isVisible()) {
                Thread.sleep(1000);
            }
        }
    }

}