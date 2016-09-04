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
    public void displayGUI() throws Throwable {
        if (!GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance()) {
            JMeterUtils.setProperty("jpgc.repo.address", "/home/undera/Sources/JMeter/jmeter-plugins/target/jpgc-repo/all.json");
            System.setProperty("http.proxyHost", "localhost");
            System.setProperty("http.proxyPort", "81");
            PluginManager aManager = new PluginManager();
            aManager.load();
            PluginManagerDialog frame = new PluginManagerDialog(aManager);

            frame.setPreferredSize(new Dimension(800, 600));
            frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
            while (frame.isVisible()) {
                Thread.sleep(1000);
            }
            JMeterUtils.setProperty("jpgc.repo.address", null);
        }
    }
}