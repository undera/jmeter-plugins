package com.blazemeter.jmeter.threads.arrivals;

import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.ListedHashTree;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

public class ArrivalsThreadGroupGuiTest {
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
        ArrivalsThreadGroupGui obj = new ArrivalsThreadGroupGui();
        TestElement te = obj.createTestElement();
        obj.configure(te);
        obj.clearGui();
        obj.modifyTestElement(te);
    }

    public void displayGUI() throws InterruptedException {
        ArrivalsThreadGroupGui obj = new ArrivalsThreadGroupGui();
        ArrivalsThreadGroup te = (ArrivalsThreadGroup) obj.createTestElement();
        te.setTargetLevel("10");
        te.setRampUp("60");
        obj.configure(te);
        obj.clearGui();
        obj.modifyTestElement(te);

        JFrame frame = new JFrame("FrameDemo");
        frame.setSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(obj, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        Thread.sleep(60000);
    }

    /**
     * @deprecated but kept for possible future use
     */
    private class ArrivalsRatePreviewer extends ArrivalsThreadStarter implements Iterator<Double> {
        public ArrivalsRatePreviewer(ArrivalsThreadGroup atg) {
            super(0, null, new ListedHashTree(), null, atg);
            rollingTime = 0;
            startTime = 0;
        }

        @Override
        public boolean hasNext() {
            return getCurrentRate() >= 0;
        }

        @Override
        public Double next() {
            tickRollingTime(getCurrentRate());
            return rollingTime / 1000 - startTime;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        public long getCountedArrivals() {
            return this.scheduledCount;
        }
    }
}