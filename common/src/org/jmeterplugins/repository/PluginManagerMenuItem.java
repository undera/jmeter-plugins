package org.jmeterplugins.repository;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PluginManagerMenuItem extends JMenuItem implements ActionListener {
    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String MENU_LABEL = "Plugins Manager";

    public PluginManagerMenuItem() {
        super(MENU_LABEL, new ImageIcon(PluginManagerMenuCreator.class.getResource("/org/jmeterplugins/logo.png")));
        addActionListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        log.info("Action " + e);
    }
}
