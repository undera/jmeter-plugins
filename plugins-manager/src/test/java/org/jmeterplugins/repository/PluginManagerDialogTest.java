package org.jmeterplugins.repository;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

public class PluginManagerDialogTest {
    @Test
    public void displayGUI() throws InterruptedException {
        if (!GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance()) {
            TestJMeterUtils.createJmeterEnv();
            JMeterUtils.setProperty("jpgc.repo.address", "http://localhost:8003");
            PluginManagerDialog frame = new PluginManagerDialog(new PluginManager());

            frame.setPreferredSize(new Dimension(800, 600));
            frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
            Thread.sleep(60000);
        }
    }
}