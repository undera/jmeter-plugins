package org.jmeterplugins.repository;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class PluginsList extends JPanel implements ListSelectionListener, HyperlinkListener {
    /**
     *
     */
    private static final long serialVersionUID = 295116233618658217L;

    private static final Logger log = LoggingManager.getLoggerForClass();

    private final JTextPane description = new JTextPane();
    protected JList<PluginCheckbox> list = new CheckBoxList<>(5);
    private DefaultListModel<PluginCheckbox> listModel = new DefaultListModel<>();
    protected final JComboBox<String> version = new JComboBox<>();
    private ItemListener itemListener = new VerChoiceChanged();
    private GenericCallback<Object> dialogRefresh;

    public PluginsList(Set<Plugin> plugins, ChangeListener checkboxNotifier, GenericCallback<Object> dialogRefresh) {
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
            listModel.addElement(getCheckboxItem(plugin, checkboxNotifier));
        }

        list.setComponentPopupMenu(new ToggleAllPopupMenu());
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

    protected PluginCheckbox getCheckboxItem(Plugin plugin, ChangeListener changeNotifier) {
        PluginCheckbox element = new PluginCheckbox(plugin.getName());
        element.setSelected(plugin.isInstalled());
        element.setPlugin(plugin);
        element.addChangeListener(changeNotifier);
        return element;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && list.getSelectedIndex() >= 0) {
            Plugin plugin = list.getSelectedValue().getPlugin();
            description.setText(getDescriptionHTML(plugin));
            setUpVersionsList(list.getSelectedValue());
            setToolTipRenderer(plugin);
            cacheImage(plugin);
            description.setCaretPosition(0);
        }
    }

    private void setToolTipRenderer(Plugin plugin) {
        final List<String> tooltips = new ArrayList<>();

        for (String version : plugin.getVersions()) {
            tooltips.add(plugin.getVersionChanges(version));
        }

        version.setRenderer(new ComboboxToolTipRenderer(tooltips));
    }

    protected void setUpVersionsList(PluginCheckbox cb) {
        version.removeItemListener(itemListener);
        version.removeAllItems();
        for (String ver : cb.getPlugin().getVersions()) {
            version.addItem(ver);
        }
        version.setSelectedItem(getCbVersion(cb));

        version.setEnabled(version.getItemCount() > 1);
        version.addItemListener(itemListener);
    }

    protected String getCbVersion(PluginCheckbox cb) {
        Plugin plugin = cb.getPlugin();
        if (plugin.isInstalled()) {
            return plugin.getInstalledVersion();
        } else {
            return plugin.getCandidateVersion();
        }
    }

    private String getDescriptionHTML(Plugin plugin) {
        String txt = "<h1>" + plugin.getName() + "</h1>";
        if (plugin.isUpgradable()) {
            txt += "<p><font color='orange'>This plugin can be upgraded to version " + plugin.getMaxVersion() + "</font></p>";
        }
        if (!plugin.getVendor().isEmpty()) {
            txt += "<p>Vendor: <i>" + plugin.getVendor() + "</i></p>";
        }
        String changes = plugin.getVersionChanges(plugin.getCandidateVersion());
        if (null != changes) {
            txt += "<p>What's new: " + changes + "</p>";
        }
        if (!plugin.getDescription().isEmpty()) {
            txt += "<p>" + plugin.getDescription() + "</p>";
        }
        if (!plugin.getHelpLink().isEmpty()) {
            txt += "<p>Documentation: <a href='" + plugin.getHelpLink() + "'>" + plugin.getHelpLink() + "</a></p>";
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

        Map<String, String> libs = plugin.getLibs(plugin.getCandidateVersion());
        if (!libs.isEmpty()) {
            txt += "<pre>Libraries: " + Arrays.toString(libs.keySet().toArray(new String[0])) + "</pre>";
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

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        list.setEnabled(enabled);
        version.setEnabled(enabled);
    }

    private class VerChoiceChanged implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent event) {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                if (event.getItem() instanceof String) {
                    String item = (String) event.getItem();
                    Plugin plugin = list.getSelectedValue().getPlugin();
                    plugin.setCandidateVersion(item);
                    dialogRefresh.notify(this);
                    description.setText(getDescriptionHTML(plugin));
                    description.setCaretPosition(0);
                }
            }
        }
    }


    private void cacheImage(Plugin plugin) {
        if (!plugin.getScreenshot().isEmpty()) {
            try {
                Dictionary cache = (Dictionary) description.getDocument().getProperty("imageCache");
                if (cache == null) {
                    cache = new Hashtable();
                    description.getDocument().putProperty("imageCache", cache);
                }

                URL url = new URL(plugin.getScreenshot());
                cache.put(url, ImageIO.read(url));
            } catch (IOException e) {
                log.warn("Cannot cached image " + plugin.getScreenshot());
            }
        }
    }

    private class ToggleAllPopupMenu extends JPopupMenu implements ActionListener {
        /**
         *
         */
        private static final long serialVersionUID = -4299203920659842279L;

        public ToggleAllPopupMenu() {
            super("Toggle All");
            JMenuItem menuItem = new JMenuItem("Toggle All");
            menuItem.addActionListener(this);
            add(menuItem);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            for (Object a : listModel.toArray()) {
                if (a instanceof PluginCheckbox) {
                    PluginCheckbox cb = (PluginCheckbox) a;
                    cb.doClick();
                }
            }
            list.repaint();
        }
    }

    private class ComboboxToolTipRenderer extends DefaultListCellRenderer {
        private final List<String> tooltips;

        public ComboboxToolTipRenderer(List<String> tooltips) {
            this.tooltips = tooltips;
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {

            JComponent comp = (JComponent) super.getListCellRendererComponent(list,
                    value, index, isSelected, cellHasFocus);

            if (-1 < index && null != value && null != tooltips && tooltips.size() > index) {
                list.setToolTipText(tooltips.get(index));
            }
            return comp;
        }
    }
}

