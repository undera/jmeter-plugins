package org.jmeterplugins.repository;

import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PluginManagerMenuItem extends JMenuItem implements ActionListener {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final PluginManagerDialog dialog = new PluginManagerDialog();

    public PluginManagerMenuItem() {
        super("Plugins Manager", JMeterPluginsUtils.getIcon());
        addActionListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String className = "org.apache.jmeter.protocol.ldap.sampler.LDAPSampler";
        try {
            Class c = Thread.currentThread().getContextClassLoader().loadClass(className);
            log.info("Class " + c);
            log.info("JAR " + c.getProtectionDomain().getCodeSource().getLocation());
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

        dialog.setVisible(true);
    }
}
