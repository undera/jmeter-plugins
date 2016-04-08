package org.jmeterplugins.repository;

import org.apache.jmeter.gui.action.ActionNames;
import org.apache.jmeter.gui.action.ActionRouter;
import org.apache.jorphan.gui.ComponentUtil;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class PluginManagerDialog extends JDialog {
    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final Border SPACING = BorderFactory.createEmptyBorder(5, 5, 5, 5);
    private final PluginManager manager;
    private final JTextArea modifs = new JTextArea();
    private final JButton apply = new JButton("Apply Changes and Restart JMeter");
    private final PluginsList installed;
    private final PluginsList available;


    public PluginManagerDialog(PluginManager aManager) {
        super((JFrame) null, "Plugins Manager", true);
        setLayout(new BorderLayout());
        manager = aManager;
        setSize(new Dimension(640, 480));
        setIconImage(PluginManagerMenuItem.getPluginsIcon().getImage());
        ComponentUtil.centerComponentInWindow(this, 30);

        try {
            manager.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ChangeListener notifier = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() instanceof PluginCheckbox) {
                    PluginCheckbox checbox = (PluginCheckbox) e.getSource();
                    Plugin plugin = checbox.getPlugin();
                    manager.toggleInstalled(plugin);
                    modifs.setText(manager.getChangesAsText());
                }
            }
        };
        installed = new PluginsList(manager.getInstalledPlugins(), notifier);
        available = new PluginsList(manager.getAvailablePlugins(), notifier);

        add(getTabsPanel(), BorderLayout.CENTER);
        add(getBottomPanel(), BorderLayout.SOUTH);
    }


    private Component getTabsPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Installed Plugins", installed);
        tabbedPane.addTab("Available Plugins", available);
        return tabbedPane;
    }

    private JPanel getBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel modifsPanel = new JPanel(new BorderLayout());
        modifsPanel.setMinimumSize(new Dimension(200, 200));
        modifsPanel.setBorder(SPACING);
        modifsPanel.setBorder(BorderFactory.createTitledBorder("Review Changes"));

        modifs.setEditable(false);
        modifsPanel.add(modifs, BorderLayout.CENTER);

        panel.add(modifsPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new BorderLayout());
        btnPanel.setBorder(SPACING);
        btnPanel.add(apply, BorderLayout.EAST);
        btnPanel.add(new JPanel(), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        apply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // FIXME: what to do when user presses "cancel" on save test plan dialog?
                manager.applyChanges();
                ActionRouter.getInstance().actionPerformed(new ActionEvent(this, 0, ActionNames.EXIT));
            }
        });
        return panel;
    }
}
