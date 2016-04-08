package org.jmeterplugins.repository;

import javax.swing.*;


class PluginCheckbox extends JCheckBox {

    private Plugin plugin;

    public PluginCheckbox(String name) {
        super(name);
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
        if (plugin.getID().equals("jpgc-common")) {
            setEnabled(false);
        }
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
