package org.jmeterplugins.repository;

import org.junit.Test;

import javax.swing.*;
import java.awt.*;

public class PluginManagerDialogTest {
    @Test
    public void displayGUI() throws InterruptedException {
        PluginManagerDialog frame = new PluginManagerDialog(new PluginManager());

        frame.setPreferredSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        Thread.sleep(60000);
    }
}