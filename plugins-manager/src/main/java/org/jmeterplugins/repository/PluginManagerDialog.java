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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PluginManagerDialog extends JDialog implements ChangeListener {
    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final Border SPACING = BorderFactory.createEmptyBorder(5, 5, 5, 5);
    private final PluginManager manager;
    private final JTextArea modifs = new JTextArea();
    private final JButton apply = new JButton("Apply Changes and Restart JMeter");
    private final PluginsList installed = new PluginsList(this);
    private final PluginsList available = new PluginsList(this);
    private Set<Plugin> deletions = new HashSet<>();
    private Set<Plugin> additions = new HashSet<>();


    public PluginManagerDialog(PluginManager aManager) {
        super((JFrame) null, "Plugins Manager", true);
        setLayout(new BorderLayout());
        this.manager = aManager;
        this.setSize(new Dimension(640, 480));
        this.setIconImage(PluginManagerMenuItem.getPluginsIcon().getImage());
        ComponentUtil.centerComponentInWindow(this, 50);

        try {
            this.manager.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        add(getTabsPanel(), BorderLayout.CENTER);
        add(getBottomPanel(), BorderLayout.SOUTH);
    }


    private Component getTabsPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Installed Plugins", getInstalledPane());
        tabbedPane.addTab("Available Plugins", getAvailablePane());
        return tabbedPane;
    }

    private Component getAvailablePane() {
        for (Plugin plugin : manager.getPlugins()) {
            if (!plugin.isInstalled()) {
                available.add(plugin);
            }
        }
        return available;
    }

    private Component getInstalledPane() {
        for (Plugin plugin : manager.getPlugins()) {
            if (plugin.isInstalled()) {
                installed.add(plugin);
            }
        }
        return installed;
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

        apply.addActionListener(new ApplyAction());
        return panel;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        modifs.setText("");
        deletions.clear();
        Map<Plugin, Boolean> pInstalled = installed.getPlugins();
        for (Plugin plugin : pInstalled.keySet()) {
            if (!pInstalled.get(plugin)) {
                deletions.add(plugin);
                modifs.append("Delete '" + plugin.getName() + "'\n");
            }
        }

        additions.clear();
        Map<Plugin, Boolean> pAvail = available.getPlugins();
        for (Plugin plugin : pAvail.keySet()) {
            if (pAvail.get(plugin)) {
                additions.add(plugin);
                modifs.append("Install '" + plugin.getName() + "'\n");
            }
        }
    }

    private class ApplyAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            // FIXME: what to do when user presses "cancel" on save test plan dialog?

            for (Plugin plugin : additions) {
                try {
                    plugin.download("2.13");
                } catch (IOException e) {
                    log.error("Failed to download " + plugin, e);
                    additions.remove(plugin);
                }
            }

            manager.modifierHook(deletions, additions);
            // query updates for installed
            ActionRouter.getInstance().actionPerformed(new ActionEvent(this, 0, ActionNames.EXIT));
        }
    }
}
