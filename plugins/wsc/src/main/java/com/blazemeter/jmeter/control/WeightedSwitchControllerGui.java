package com.blazemeter.jmeter.control;

import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.gui.Grid;
import org.apache.jmeter.control.Controller;
import org.apache.jmeter.control.gui.AbstractControllerGui;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.tree.JMeterTreeModel;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class WeightedSwitchControllerGui extends AbstractControllerGui {
    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String WEIGHTS = "Weight";
    private Grid grid;

    public WeightedSwitchControllerGui() {
        super();
        init();
        clearGui();
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

        add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), getClass().getSimpleName()), BorderLayout.NORTH);
        final JTable table = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return (column != 0) && Boolean.parseBoolean((String) getModel().getValueAt(row, 2));
            }
        };
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, java.lang.Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                try {
                    String val = table.getModel().getValueAt(row, 2).toString();
                    cellComponent.setBackground(Boolean.parseBoolean(val) ? Color.white : Color.lightGray);
                } catch (Exception ex) {
                    cellComponent.setBackground(Color.white);
                }
                return cellComponent;
            }
        });
        grid = new Grid("Child Item Weights",
                new String[]{"Name", WEIGHTS, "Enabled"},
                new Class[]{String.class, String.class, String.class},
                new String[]{"", "100", "true"}, table);
        table.removeColumn(table.getColumn("Enabled"));

        grid.getComponent(2).setVisible(false); // hide grid mgmt buttons
        add(grid, BorderLayout.CENTER);
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public String getStaticLabel() {
        return "bzm - Weighted Switch Controller";
    }

    @Override
    public TestElement createTestElement() {
        WeightedSwitchController te = new WeightedSwitchController();
        modifyTestElement(te);
        return te;
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.configureTestElement(element);
        if (element instanceof WeightedSwitchController) {
            WeightedSwitchController wsc = (WeightedSwitchController) element;
            wsc.setData(grid.getModel());
        }
    }

    @Override
    public void configure(TestElement element) {
        // from model to GUI
        super.configure(element);
        log.debug("Props: " + this.isShowing() + " " + isVisible() + " " + isEnabled() + " " + isFocusOwner());
        GuiPackage gp = GuiPackage.getInstance();

        if (gp != null && element instanceof WeightedSwitchController) {
            WeightedSwitchController wsc = (WeightedSwitchController) element;
            CollectionProperty oldData = wsc.getData();

            grid.getModel().clearData();

            if (isShowing()) {
                fillGridFromTree(wsc, oldData);
            } else {
                JMeterPluginsUtils.collectionPropertyToTableModelRows(oldData, grid.getModel());
            }
        }
    }

    private void fillGridFromTree(WeightedSwitchController wsc, CollectionProperty oldData) {
        JMeterTreeModel treeModel = GuiPackage.getInstance().getTreeModel();
        JMeterTreeNode root = (JMeterTreeNode) treeModel.getRoot();

        Map<JMeterTreeNode, Boolean> childItems = getChildItems(root, wsc);
        for (JMeterTreeNode node : childItems.keySet()) {
            String w = "100";
            JMeterProperty row = getRowByName(node.getTestElement().getName(), oldData);
            if (row != null) {
                w = ((CollectionProperty) row).get(1).getStringValue();
            }
            grid.getModel().addRow(new String[]{node.getTestElement().getName(), w, childItems.get(node).toString()});
        }
    }

    private JMeterProperty getRowByName(String rowName, CollectionProperty oldData) {
        for (int i = 0; i <  oldData.size(); i++) {
            JMeterProperty row = oldData.get(i);
            if (row instanceof CollectionProperty && rowName.equals(((CollectionProperty) row).get(0).getStringValue())) {
                return row;
            }
        }
        return null;
    }

    private Map<JMeterTreeNode, Boolean> getChildItems(JMeterTreeNode root, WeightedSwitchController element) {
        Map<JMeterTreeNode, Boolean> result = new LinkedHashMap<>();
        for (int i = 0; i < root.getChildCount(); i++) {
            JMeterTreeNode child = (JMeterTreeNode) root.getChildAt(i);

            TestElement te = child.getTestElement();
            if (element != root.getTestElement()) {
                result.putAll(getChildItems(child, element));
            } else {
                if (te instanceof Sampler || te instanceof Controller) {
                    result.put(child, te.isEnabled());
                }
            }
        }
        return result;
    }

    @Override
    public void clearGui() {
        super.clearGui();
        grid.getModel().clearData();
    }
}
