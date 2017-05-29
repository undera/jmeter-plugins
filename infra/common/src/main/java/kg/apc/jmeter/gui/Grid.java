package kg.apc.jmeter.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import kg.apc.jmeter.gui.ButtonPanelAddCopyRemove;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.gui.util.VerticalPanel;


public class Grid extends VerticalPanel {
    private JTable grid;
    private PowerTableModel tableModel;

    public Grid(String title, String[] columnIdentifiers, Class<?>[] columnClasses, Object[] defaultValues) {
        this(title, columnIdentifiers, columnClasses, defaultValues, new JTable());
    }

    public Grid(String title, String[] columnIdentifiers, Class<?>[] columnClasses, Object[] defaultValues, JTable grid) {
        super();

        this.setBorder(BorderFactory.createTitledBorder(title));
        this.setPreferredSize(new Dimension(150, 150));
        this.grid = grid;
        JScrollPane scroll = new JScrollPane(createGrid(columnIdentifiers, columnClasses));
        scroll.setPreferredSize(scroll.getMinimumSize());
        this.add(scroll, BorderLayout.CENTER);
        this.add(new ButtonPanelAddCopyRemove(grid, tableModel, defaultValues), BorderLayout.SOUTH);

        grid.getTableHeader().setReorderingAllowed(false);
    }

    private JTable createGrid(String[] columnIdentifiers, Class<?>[] columnClasses) {
        tableModel = new PowerTableModel(columnIdentifiers, columnClasses);
        grid.setModel(tableModel);
        grid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        grid.setMinimumSize(new Dimension(200, 100));

        return grid;
    }

    public PowerTableModel getModel() {
        return tableModel;
    }

    public JTable getGrid() {
        return grid;
    }
}
