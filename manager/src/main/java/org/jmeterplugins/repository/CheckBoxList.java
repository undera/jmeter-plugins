package org.jmeterplugins.repository;

// http://www.devx.com/tips/Tip/5342

import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class CheckBoxList<T extends JCheckBox> extends JList<T> {
    protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    public CheckBoxList(final int xOffset) {
        setCellRenderer(new CellRenderer());

        addMouseListener(new MouseAdapter() {
                             public void mousePressed(MouseEvent e) {
                                 int index = locationToIndex(e.getPoint());

                                 if (index != -1) {
                                     JCheckBox checkbox = getModel().getElementAt(index);
                                     Icon i = UIManager.getIcon("CheckBox.icon");
                                     if (e.getX() <= i.getIconWidth() + xOffset) {
                                         if (checkbox.isEnabled()) {
                                             checkbox.setSelected(!checkbox.isSelected());
                                         }
                                     }
                                     repaint();
                                 }
                             }
                         }
        );

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    protected class CellRenderer implements ListCellRenderer<JCheckBox> {
        @Override
        public Component getListCellRendererComponent(JList<? extends JCheckBox> list, JCheckBox value, int index, boolean isSelected, boolean cellHasFocus) {
            value.setBackground(isSelected ? getSelectionBackground() : getBackground());
            value.setForeground(isSelected ? getSelectionForeground() : getForeground());
            value.setEnabled(isEnabled() && value.isEnabled());
            value.setFont(getFont());
            if (value instanceof PluginCheckbox) {
                Plugin p = ((PluginCheckbox) value).getPlugin();
                if (p.isUpgradable()) {
                    value.setFont(getFont().deriveFont(Font.ITALIC | Font.BOLD));
                }
            }

            value.setFocusPainted(false);
            value.setBorderPainted(true);
            value.setBorder(isSelected ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);
            return value;
        }
    }
}
