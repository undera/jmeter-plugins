package org.jmeterplugins.repository;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

public class PluginManagerDialogTest {
    @BeforeClass
    public static void setup() {
        TestJMeterUtils.createJmeterEnv();
    }

    @Test
    public void displayGUI() throws InterruptedException {
        if (!GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance()) {
            //JMeterUtils.setProperty("jpgc.repo.address", "http://localhost:80");
            System.setProperty("http.proxyHost", "localhost");
            System.setProperty("http.proxyPort", "81");
            PluginManagerDialog frame = new PluginManagerDialog(new PluginManager());

            frame.setPreferredSize(new Dimension(800, 600));
            frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
            while (frame.isVisible()) {
                Thread.sleep(1000);
            }
        }
    }
}