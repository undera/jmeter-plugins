package org.jmeterplugins.repository;

import org.apache.jmeter.gui.plugin.MenuCreator;

import javax.swing.*;

public class PluginManagerMenuCreator implements MenuCreator {
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
