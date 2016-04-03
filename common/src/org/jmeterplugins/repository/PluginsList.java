package org.jmeterplugins.repository;

import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class PluginsList extends JPanel implements ListSelectionListener, HyperlinkListener {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private final JTextPane description = new JTextPane();
    private JList<JCheckBox> list = new CheckBoxList(5);
    private DefaultListModel<JCheckBox> listModel = new DefaultListModel<>();

    public PluginsList() {
        super(new BorderLayout(5, 0));

        description.setContentType("text/html");
        description.setEditable(false);
        description.addHyperlinkListener(this);

        list.setModel(listModel);
        list.setBorder(PluginManagerDialog.SPACING);
        list.addListSelectionListener(this);

        add(new JScrollPane(description), BorderLayout.CENTER);
        add(list, BorderLayout.WEST);
    }

    public void add(Plugin plugin) {
        PluginCheckbox element = new PluginCheckbox(plugin.getName());
        element.setSelected(plugin.isInstalled());
        element.setPlugin(plugin);
        listModel.addElement(element);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && list.getSelectedIndex() >= 0) {
            PluginCheckbox item = (PluginCheckbox) list.getSelectedValue(); // FIXME: not very nice to cast it
            Plugin plugin = item.getPlugin();
            String txt = "<h1>" + plugin.getName() + "</h1>";
            txt += "<p>" + plugin.getDescription() + "</p>";
            txt += "<p style='float: right'><a href='" + plugin.getHelpLink() + "'>More info...</a></p>";
            txt += "<p><img src='" + plugin.getScreenshot() + "'/></p>";
            description.setText(txt);
        }
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            JMeterPluginsUtils.openInBrowser(e.getURL().toString());
        }
    }

    private class PluginCheckbox extends JCheckBox {

        private Plugin plugin;

        public PluginCheckbox(String name) {
            super(name);
        }

        public void setPlugin(Plugin plugin) {
            this.plugin = plugin;
        }

        public Plugin getPlugin() {
            return plugin;
        }
    }
}
