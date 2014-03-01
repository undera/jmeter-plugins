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
 * @author renato ochando
 */
public class LoadTableAction implements ActionListener {

	private JTable grid;
	private PowerTableModel tableModel;
	private JButton cleanTableButton;
	private Object[][] values;
	private JComponent sender;

	public LoadTableAction(JComponent aSender, JTable grid, PowerTableModel tableModel, JButton deleteRowButton,
			Object[][] values) {
		this.grid = grid;
		this.tableModel = tableModel;
		this.cleanTableButton = deleteRowButton;
		this.values = values;
		this.sender = aSender;
	}

	public void actionPerformed(ActionEvent e) {
		if (values == null || values[0] == null || values[0].length == 0)
			return;
		
		if (grid.isEditing()) {
			TableCellEditor cellEditor = grid.getCellEditor(grid.getEditingRow(), grid.getEditingColumn());
			cellEditor.stopCellEditing();
		}
		
		tableModel.clearData();
		for (int c = 0; c < values.length; c++) {
			if (values[c] == null)
				continue;
			tableModel.addRow(values[c]);
		}

		tableModel.fireTableDataChanged();

		// Enable CLEAN (which may already be enabled, but it won't hurt)
		cleanTableButton.setEnabled(true);

		// Highlight (select) the appropriate row.
		int rowToSelect = tableModel.getRowCount() - 1;
		if (rowToSelect < grid.getRowCount()) {
			grid.setRowSelectionInterval(rowToSelect, rowToSelect);
		}
		sender.updateUI();
	}
}
