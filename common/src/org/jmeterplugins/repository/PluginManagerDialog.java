package org.jmeterplugins.repository;

import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jorphan.gui.ComponentUtil;

import javax.swing.*;
import java.awt.*;

public class PluginManagerDialog extends JDialog {
    private final PluginManager manager;

    public PluginManagerDialog(PluginManager aManager) {
        super((JFrame) null, "Plugins Manager", true);
        this.manager = aManager;
        this.setSize(new Dimension(640, 480));
        this.setIconImage(JMeterPluginsUtils.getIcon().getImage());
        ComponentUtil.centerComponentInWindow(this, 50);
    }

}
