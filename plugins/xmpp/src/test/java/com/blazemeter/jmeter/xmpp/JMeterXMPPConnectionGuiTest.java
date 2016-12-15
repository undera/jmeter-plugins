package com.blazemeter.jmeter.xmpp;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.net.URLClassLoader;

import static org.junit.Assert.assertTrue;

public class JMeterXMPPConnectionGuiTest {
    @BeforeClass
    public static void setUpClass()
            throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @Test
    public void testCreateTestElement() throws Exception {
        JMeterXMPPConnectionGui obj = new JMeterXMPPConnectionGui();
        assertTrue(obj.createTestElement() instanceof JMeterXMPPConnection);
    }

    @Test
    public void testConfigure() throws Exception {
        JMeterXMPPConnectionGui obj = new JMeterXMPPConnectionGui();
        obj.configure(obj.createTestElement());
    }

    @Test
    public void testClearGui() throws Exception {
        JMeterXMPPConnectionGui obj = new JMeterXMPPConnectionGui();
        obj.clearGui();
    }

    @Test
    public void testModifyTestElement() throws Exception {
        JMeterXMPPConnectionGui obj = new JMeterXMPPConnectionGui();
        obj.modifyTestElement(obj.createTestElement());
    }

    //@Test
    public void displayGUI() throws Throwable {
        setSearchPaths();

        if (!GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance()) {
            Component gui = new JMeterXMPPConnectionGui();
            JDialog dialog = new JDialog();
            dialog.add(gui);

            dialog.setPreferredSize(new Dimension(800, 600));
            dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            dialog.pack();
            dialog.setVisible(true);
            while (dialog.isVisible()) {
                Thread.sleep(1000);
            }
        }
    }

    private void setSearchPaths() {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        URL[] urls = ((URLClassLoader) cl).getURLs();
        String classes = ".";
        for (URL url : urls) {
            if (url.getFile().contains("bouncy") || !url.getFile().contains("jmeter")) {
                continue;
            }

            classes += ";" + url.getFile();
        }
        JMeterUtils.setProperty("search_paths", classes);
    }


}