package org.jmeterplugins.repository;

import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jorphan.gui.ComponentUtil;

import javax.swing.*;
import java.awt.*;

public class PluginManagerDialog extends JDialog {
    public PluginManagerDialog() {
        super((JFrame) null, "Plugins Manager", true);
        this.setSize(new Dimension(640, 480));
        this.setIconImage(JMeterPluginsUtils.getIcon().getImage());
        ComponentUtil.centerComponentInWindow(this, 50);
    }

}
