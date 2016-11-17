package org.jmeterplugins.repository;

import javax.swing.JMenuItem;

import org.apache.jmeter.gui.plugin.MenuCreator;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class PluginManagerMenuCreator implements MenuCreator {
    private static final Logger log = LoggingManager.getLoggerForClass();

    @Override
    public JMenuItem[] getMenuItemsAtLocation(MENU_LOCATION location) {
        if (location == MENU_LOCATION.OPTIONS) {
            try {
                PluginManagerCMDInstaller.main(new String[0]);
            } catch (Throwable e) {
                log.warn("Was unable to install pmgr cmdline tool", e);
            }

            try {
                return new JMenuItem[]{new PluginManagerMenuItem()};
            } catch (Throwable e) {
                log.error("Failed to load Plugins Manager", e);
                return new JMenuItem[0];
            }
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
