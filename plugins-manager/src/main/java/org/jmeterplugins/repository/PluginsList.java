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
    private ChangeListener changeNotifier;

    public PluginsList(Set<Plugin> plugins, ChangeListener notifier) {
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

        for (Plugin plugin : plugins) {
            add(plugin);
        }
    }

    private void add(Plugin plugin) {
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
