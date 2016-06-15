package org.jmeterplugins.repository;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.gui.plugin.MenuCreator;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.assertEquals;


public class PluginManagerMenuCreatorTest {
    @BeforeClass
    public static void setup() {
        TestJMeterUtils.createJmeterEnv();
    }

    @Test
    public void getMenuItemsAtLocation() throws Exception {
        PluginManagerMenuCreator creator = new PluginManagerMenuCreator();
        assertEquals(0, creator.getTopLevelMenus().length);
        creator.localeChanged();
        JMenuItem[] menuItemsAtLocation = creator.getMenuItemsAtLocation(MenuCreator.MENU_LOCATION.OPTIONS);
        assertEquals(1, menuItemsAtLocation.length);
        assertEquals(0, creator.getMenuItemsAtLocation(MenuCreator.MENU_LOCATION.RUN).length);
        creator.localeChanged(menuItemsAtLocation[0]);
    }

}