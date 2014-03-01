package kg.apc.jmeter.gui;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.apache.jmeter.gui.util.PowerTableModel;

/**
 * @author renato ochando
 * 
 */
public class ButtonPanelLoadClean extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final JButton loadTableButton;
	private final JButton cleanTableButton;

	private final PowerTableModel tableModel;

	public ButtonPanelLoadClean(JTable grid, PowerTableModel tableModel, ActionListener populateValuesActionListener, Object[][] values) {
		setLayout(new GridLayout(1, 2));

		loadTableButton = new JButton("Load");
		cleanTableButton = new JButton("Clean");
		
		loadTableButton.addActionListener(new LoadTableAction(this, grid, tableModel, cleanTableButton, values));
		loadTableButton.addActionListener(populateValuesActionListener);
		
		cleanTableButton.addActionListener(new CleanTableAction(this, grid, tableModel, loadTableButton,
				cleanTableButton));

		add(loadTableButton);
		add(cleanTableButton);
		this.tableModel = tableModel;
	}

	public void checkCleanLoadButtonStatus() {
		cleanTableButton.setEnabled(tableModel != null && tableModel.getRowCount() > 0);
		loadTableButton.setEnabled(!cleanTableButton.isEnabled());
	}
}
