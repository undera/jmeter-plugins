package org.jmeterplugins.repository;

import javax.swing.*;
import java.awt.*;

public class PluginsList extends JPanel {
    private final JTextPane description = new JTextPane();
    private JList<JCheckBox> list = new CheckBoxList(5);
    private DefaultListModel<JCheckBox> listModel = new DefaultListModel<>();

    public PluginsList() {
        super(new BorderLayout(5, 0));

        description.setContentType("text/html");
        description.setEditable(false);

        list.setModel(listModel);
        list.setBorder(PluginManagerDialog.SPACING);

        add(description, BorderLayout.EAST);
        add(list, BorderLayout.WEST);
    }

    public void add(Plugin plugin) {
        listModel.addElement(new JCheckBox(plugin.getName()));
    }
}
