package org.jmeterplugins.repository;

import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.gui.plugin.MenuCreator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PluginManagerMenuItem extends JMenuItem implements MenuCreator, ActionListener {
    private static final PluginManagerDialog dialog = new PluginManagerDialog(new PluginManager());

    public PluginManagerMenuItem() {
        super("Plugins Manager", JMeterPluginsUtils.getIcon());
        addActionListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        dialog.setVisible(true);
    }

    @Override
    public JMenuItem[] getMenuItemsAtLocation(MENU_LOCATION location) {
        if (location == MENU_LOCATION.OPTIONS) {
            JMenuItem item = new PluginManagerMenuItem();
            JMenuItem[] arr = new JMenuItem[1];
            arr[0] = item;
            return arr;
        } else {
            return new JMenuItem[0];
        }
    }

    @Override
    public javax.swing.JMenu[] getTopLevelMenus() {
        return new javax.swing.JMenu[0];
    }

    @Override
    public boolean localeChanged(javax.swing.MenuElement menu) {
        return false;
    }

    @Override
    public void localeChanged() {
    }

}
