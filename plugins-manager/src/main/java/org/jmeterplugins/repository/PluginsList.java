package org.jmeterplugins.repository;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Set;

public class PluginsList extends JPanel implements ListSelectionListener, HyperlinkListener {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private final JTextPane description = new JTextPane();
    private JList<PluginCheckbox> list = new CheckBoxList<>(5);
    private DefaultListModel<PluginCheckbox> listModel = new DefaultListModel<>();
    private final JComboBox<String> version = new JComboBox<>();
    private ItemListener itemListener = new VerChoiceChanged();
    private ChangeListener dialogRefresh;

    public PluginsList(Set<Plugin> plugins, ChangeListener checkboxNotifier, ChangeListener dialogRefresh) {
        super(new BorderLayout(5, 0));
        this.dialogRefresh = dialogRefresh;

        description.setContentType("text/html");
        description.setEditable(false);
        description.addHyperlinkListener(this);

        list.setModel(listModel);
        list.setBorder(PluginManagerDialog.SPACING);
        list.addListSelectionListener(this);

        add(new JScrollPane(list), BorderLayout.WEST);
        add(getDetailsPanel(), BorderLayout.CENTER);

        for (Plugin plugin : plugins) {
            add(plugin, checkboxNotifier);
        }
    }

    private JPanel getDetailsPanel() {
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.add(new JScrollPane(description), BorderLayout.CENTER);

        version.setEnabled(false);

        JPanel verPanel = new JPanel(new BorderLayout());
        verPanel.add(new JLabel("Version: "), BorderLayout.WEST);
        verPanel.add(version, BorderLayout.CENTER);
        detailsPanel.add(verPanel, BorderLayout.SOUTH);
        return detailsPanel;
    }

    private void add(Plugin plugin, ChangeListener changeNotifier) {
        PluginCheckbox element = new PluginCheckbox(plugin.getName());
        element.setSelected(plugin.isInstalled());
        element.setPlugin(plugin);
        element.addChangeListener(changeNotifier);
        listModel.addElement(element);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && list.getSelectedIndex() >= 0) {
            Plugin plugin = list.getSelectedValue().getPlugin();
            description.setText(getDescriptionHTML(plugin));
            setUpVersionsList(plugin);
        }
    }

    private void setUpVersionsList(Plugin plugin) {
        version.removeItemListener(itemListener);
        version.removeAllItems();
        for (String ver : plugin.getVersions()) {
            version.addItem(ver);
        }
        String selVersion;
        if (plugin.isInstalled()) {
            selVersion = plugin.getInstalledVersion();
        } else {
            selVersion = plugin.getCandidateVersion();
        }
        version.setSelectedItem(selVersion);

        version.setEnabled(version.getItemCount() > 1);
        version.addItemListener(itemListener);
    }

    private String getDescriptionHTML(Plugin plugin) {
        String txt = "<h1>" + plugin.getName() + "</h1>";
        if (plugin.isUpgradable()) {
            txt += "<p><font color='orange'>This plugin can be upgraded to version " + plugin.getMaxVersion() + "</font></p>";
        }
        if (!plugin.getVendor().isEmpty()) {
            txt += "<p>Vendor: <i>" + plugin.getVendor() + "</i></p>";
        }
        if (!plugin.getDescription().isEmpty()) {
            txt += "<p>" + plugin.getDescription() + "</p>";
        }
        if (!plugin.getHelpLink().isEmpty()) {
            txt += "<p><a href='" + plugin.getHelpLink() + "'>More info...</a></p>";
        }
        if (!plugin.getScreenshot().isEmpty()) {
            txt += "<p><img src='" + plugin.getScreenshot() + "'/></p>";
        }
        if (plugin.getInstalledPath() != null) {
            txt += "<pre>Location: " + plugin.getInstalledPath() + "</pre>";
        }

        Set<String> deps = plugin.getDepends();
        if (!deps.isEmpty()) {
            txt += "<pre>Dependencies: " + Arrays.toString(deps.toArray(new String[0])) + "</pre>";
        }
        return txt + "<br/>";
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            openInBrowser(e.getURL().toString());
        }
    }

    public static void openInBrowser(String string) {
        if (java.awt.Desktop.isDesktopSupported()) {
            try {
                java.awt.Desktop.getDesktop().browse(new URI(string));
            } catch (IOException | URISyntaxException ignored) {
                log.debug("Failed to open in browser", ignored);
            }
        }
    }

    private class VerChoiceChanged implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent event) {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                if (event.getItem() instanceof String) {
                    String item = (String) event.getItem();
                    Plugin plugin = list.getSelectedValue().getPlugin();
                    plugin.setCandidateVersion(item);
                    dialogRefresh.stateChanged(new ChangeEvent(this));
                    // TODO: file description text refresh, because of depends list there
                }
            }
        }
    }
}

