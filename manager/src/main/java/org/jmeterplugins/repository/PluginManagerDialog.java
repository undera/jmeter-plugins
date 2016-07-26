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
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class PluginManagerDialog extends JDialog implements ActionListener, ComponentListener {
    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final Border SPACING = BorderFactory.createEmptyBorder(5, 5, 5, 5);
    private final PluginManager manager;
    private final JTextPane modifs = new JTextPane();
    private final JButton apply = new JButton("Apply Changes and Restart JMeter");
    private final PluginsList installed;
    private final PluginsList available;
    private final PluginUpgradesList upgrades;
    private final JSplitPane topAndDown = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    private JLabel statusLabel = new JLabel("");
    private JTextArea failureLabel = new JTextArea();


    public PluginManagerDialog(PluginManager aManager) {
        super((JFrame) null, "JMeter Plugins Manager", true);
        setLayout(new BorderLayout());
        addComponentListener(this);
        manager = aManager;
        Dimension size = new Dimension(1024, 768);
        setSize(size);
        setPreferredSize(size);
        setIconImage(PluginManagerMenuItem.getPluginsIcon().getImage());
        ComponentUtil.centerComponentInWindow(this);

        try {
            manager.load();
        } catch (IOException e) {
            log.error("Failed to load plugins manager", e);
            ByteArrayOutputStream text = new ByteArrayOutputStream(4096);
            e.printStackTrace(new PrintStream(text));
            failureLabel.setText(text.toString());
        }
        failureLabel.setForeground(Color.RED);
        failureLabel.setEditable(false);
        add(failureLabel, BorderLayout.NORTH);

        final GenericCallback<Object> statusRefresh = new GenericCallback<Object>() {
            @Override
            public void notify(Object ignored) {
                String changeText = manager.getChangesAsText();
                modifs.setText(changeText);
                apply.setEnabled(!changeText.isEmpty() && installed.isEnabled());
            }
        };

        ChangeListener cbNotifier = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() instanceof PluginCheckbox) {
                    PluginCheckbox checkbox = (PluginCheckbox) e.getSource();
                    Plugin plugin = checkbox.getPlugin();
                    manager.toggleInstalled(plugin, checkbox.isSelected());
                    statusRefresh.notify(this);
                }
            }
        };

        ChangeListener cbUpgradeNotifier = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() instanceof PluginCheckbox) {
                    PluginCheckbox checkbox = (PluginCheckbox) e.getSource();
                    Plugin plugin = checkbox.getPlugin();
                    if (checkbox.isSelected()) {
                        plugin.setCandidateVersion(checkbox.getPlugin().getMaxVersion());
                    } else {
                        plugin.setCandidateVersion(checkbox.getPlugin().getInstalledVersion());
                    }
                    statusRefresh.notify(this);
                }
            }
        };


        installed = new PluginsList(manager.getInstalledPlugins(), cbNotifier, statusRefresh);
        available = new PluginsList(manager.getAvailablePlugins(), cbNotifier, statusRefresh);
        upgrades = new PluginUpgradesList(manager.getUpgradablePlugins(), cbUpgradeNotifier, statusRefresh);

        topAndDown.setResizeWeight(.75);
        topAndDown.setDividerSize(5);
        topAndDown.setTopComponent(getTabsPanel());
        topAndDown.setBottomComponent(getBottomPanel());
        add(topAndDown, BorderLayout.CENTER);
        statusRefresh.notify(this); // to reflect upgrades
    }

    private Component getTabsPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Installed Plugins", installed);
        tabbedPane.addTab("Available Plugins", available);
        tabbedPane.addTab("Upgrades", upgrades);
        return tabbedPane;
    }

    private JPanel getBottomPanel() {
        apply.setEnabled(false);
        modifs.setEditable(false);
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.ITALIC));

        JPanel panel = new JPanel(new BorderLayout());

        JPanel modifsPanel = new JPanel(new BorderLayout());
        modifsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, getHeight() / 3));
        modifsPanel.setPreferredSize(new Dimension(getWidth(), getHeight() / 3));
        modifsPanel.setBorder(SPACING);
        modifsPanel.setBorder(BorderFactory.createTitledBorder("Review Changes"));

        modifs.setEditable(false);
        modifsPanel.add(new JScrollPane(modifs), BorderLayout.CENTER);

        panel.add(modifsPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new BorderLayout());
        btnPanel.setBorder(SPACING);
        btnPanel.add(apply, BorderLayout.EAST);
        btnPanel.add(statusLabel, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        apply.addActionListener(this);
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new Thread() {
            @Override
            public void run() {
                installed.setEnabled(false);
                available.setEnabled(false);
                upgrades.setEnabled(false);
                apply.setEnabled(false);
                // FIXME: what to do when user presses "cancel" on save test plan dialog?
                GenericCallback<String> statusChanged = new GenericCallback<String>() {
                    @Override
                    public void notify(String s) {
                        statusLabel.setText(s);
                        repaint();
                    }
                };
                try {
                    manager.applyChanges(statusChanged);
                    ActionRouter.getInstance().actionPerformed(new ActionEvent(this, 0, ActionNames.EXIT));
                } catch (Exception ex) {
                    statusChanged.notify("Failed to apply changes: " + ex.getMessage());
                    throw ex;
                }
            }
        }.start();
    }

    @Override
    public void componentResized(ComponentEvent e) {

    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {
        topAndDown.setVisible(!manager.allPlugins.isEmpty());
        failureLabel.setVisible(manager.allPlugins.isEmpty());
        pack();
    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
