package com.blazemeter.jmeter.threads.concurrency;

import com.blazemeter.jmeter.threads.arrivals.ArrivalsThreadGroupGuiTest;
import org.apache.jmeter.testelement.TestElement;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class ConcurrencyThreadGroupGuiTest {
    @BeforeClass
    public static void setUpClass() throws IOException {
        ArrivalsThreadGroupGuiTest.setUpClass();
    }

    @Test
    public void testFLow() throws Exception {
        ConcurrencyThreadGroupGui obj = new ConcurrencyThreadGroupGui();
        TestElement te = obj.createTestElement();
        obj.configure(te);
        obj.clearGui();
        obj.modifyTestElement(te);
    }


    public void displayGUI() throws InterruptedException {
        ConcurrencyThreadGroupGui obj = new ConcurrencyThreadGroupGui();
        ConcurrencyThreadGroup te = (ConcurrencyThreadGroup) obj.createTestElement();
        te.setTargetLevel("10");
        te.setRampUp("60");
        obj.configure(te);
        obj.clearGui();
        obj.modifyTestElement(te);

        JFrame frame = new JFrame("FrameDemo");
        frame.setSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(obj, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        Thread.sleep(60000);
    }
}