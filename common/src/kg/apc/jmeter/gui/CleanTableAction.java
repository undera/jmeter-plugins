package kg.apc.jmeter.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import org.apache.jmeter.gui.util.PowerTableModel;

/**
 * @author renato ochando
 * 
 */
public class CleanTableAction implements ActionListener {

	private JTable grid;
	private PowerTableModel tableModel;
	private JButton loadTableButton;
	private JButton cleanTableButton;
	private final JComponent sender;

	public CleanTableAction(JComponent aSender, JTable grid, PowerTableModel tableModel, JButton loadTableButton,
			JButton cleanTableButton) {
		this.grid = grid;
		this.tableModel = tableModel;
		this.loadTableButton = loadTableButton;
		this.cleanTableButton = cleanTableButton;
		this.sender = aSender;
	}

	public void actionPerformed(ActionEvent e) {
		if (grid.isEditing()) {
			TableCellEditor cellEditor = grid.getCellEditor(grid.getEditingRow(), grid.getEditingColumn());
			cellEditor.cancelCellEditing();
		}

		tableModel.clearData();
		tableModel.fireTableDataChanged();

		loadTableButton.setEnabled(true);
		cleanTableButton.setEnabled(false);

		sender.updateUI();
	}

}
