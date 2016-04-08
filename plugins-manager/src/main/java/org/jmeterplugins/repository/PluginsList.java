package org.jmeterplugins.repository;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PluginsList extends JPanel implements ListSelectionListener, HyperlinkListener {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private final JTextPane description = new JTextPane();
    private JList<PluginCheckbox> list = new CheckBoxList<>(5);
    private DefaultListModel<PluginCheckbox> listModel = new DefaultListModel<>();
    private final JComboBox<String> version = new JComboBox<>();

    public PluginsList(Set<Plugin> plugins, ChangeListener checkboxNotifier) {
        super(new BorderLayout(5, 0));

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
            PluginCheckbox item = list.getSelectedValue();
            Plugin plugin = item.getPlugin();
            description.setText(getDescriptionHTML(plugin));

            setUpVersionsList(plugin);

            // TODO: preselect newer version for upgrade
            // TODO: mark upgradable plugins in the list
        }
    }

    private void setUpVersionsList(Plugin plugin) {
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
    }

    private String getDescriptionHTML(Plugin plugin) {
        String txt = "<h1>" + plugin.getName() + "</h1>";
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
            txt += "<p>Location: " + plugin.getInstalledPath() + "</p>";
        }
        return txt;
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

    private Map<Plugin, Boolean> getPlugins() {
        Map<Plugin, Boolean> map = new HashMap<>();
        for (int n = 0; n < listModel.getSize(); n++) {
            map.put(listModel.get(n).getPlugin(), listModel.get(n).isSelected());
        }
        return map;
    }


}
