package org.jmeterplugins.repository;

import org.apache.jmeter.gui.plugin.MenuCreator;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;

public class PluginManagerMenuCreator implements MenuCreator {
    private static final Logger log = LoggingManager.getLoggerForClass();

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
        log.debug("Is locale changed?");
        return false;
    }

    @Override
    public void localeChanged() {
    }

}
