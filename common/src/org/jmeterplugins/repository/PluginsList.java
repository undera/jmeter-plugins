package org.jmeterplugins.repository;

import kg.apc.jmeter.JMeterPluginsUtils;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PluginsList extends JPanel implements ListSelectionListener, HyperlinkListener {
    private final JTextPane description = new JTextPane();
    private JList<PluginCheckbox> list = new CheckBoxList<>(5);
    private DefaultListModel<PluginCheckbox> listModel = new DefaultListModel<>();
    private ChangeListener changeNotifier;

    public PluginsList(ChangeListener notifier) {
        super(new BorderLayout(5, 0));

        changeNotifier = notifier;
        description.setContentType("text/html");
        description.setEditable(false);
        description.addHyperlinkListener(this);

        list.setModel(listModel);
        list.setBorder(PluginManagerDialog.SPACING);
        list.addListSelectionListener(this);

        add(new JScrollPane(description), BorderLayout.CENTER);
        add(new JScrollPane(list), BorderLayout.WEST);
    }

    public void add(Plugin plugin) {
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
            description.setText(txt);
        }
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            JMeterPluginsUtils.openInBrowser(e.getURL().toString());
        }
    }

    public Map<Plugin, Boolean> getPlugins() {
        Map<Plugin, Boolean> map = new HashMap<>();
        for (int n = 0; n < listModel.getSize(); n++) {
            map.put(listModel.get(n).getPlugin(), listModel.get(n).isSelected());
        }
        return map;
    }


    private class PluginCheckbox extends JCheckBox {

        private Plugin plugin;

        public PluginCheckbox(String name) {
            super(name);
        }

        public void setPlugin(Plugin plugin) {
            this.plugin = plugin;
            if (plugin.getID().equals("jpgc-common")) {
                setEnabled(false);
            }
        }

        public Plugin getPlugin() {
            return plugin;
        }
    }
}
