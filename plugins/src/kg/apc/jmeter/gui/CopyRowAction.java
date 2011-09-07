package kg.apc.jmeter.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import org.apache.jmeter.gui.util.PowerTableModel;

/**
 *
 * @author undera
 */
public class CopyRowAction
        implements ActionListener {

    private JTable grid;
    private PowerTableModel tableModel;
    private JButton deleteRowButton;
    private JComponent sender;

    public CopyRowAction(JComponent aSender, JTable grid, PowerTableModel tableModel, JButton deleteRowButton) {
        this.grid = grid;
        this.tableModel = tableModel;
        this.deleteRowButton = deleteRowButton;
        this.sender = aSender;
    }

    public void actionPerformed(ActionEvent e) {
        if (grid.isEditing()) {
            TableCellEditor cellEditor = grid.getCellEditor(grid.getEditingRow(), grid.getEditingColumn());
            cellEditor.stopCellEditing();
        }
        final int selectedRow = grid.getSelectedRow();

        if (tableModel.getRowCount() == 0 || selectedRow < 0) {
            return;
        }

        tableModel.addRow(tableModel.getRowData(selectedRow));
        tableModel.fireTableDataChanged();

        // Enable DELETE (which may already be enabled, but it won't hurt)
        deleteRowButton.setEnabled(true);

        // Highlight (select) the appropriate row.
        int rowToSelect = selectedRow + 1;
        grid.setRowSelectionInterval(rowToSelect, rowToSelect);
        sender.updateUI();
    }
}
