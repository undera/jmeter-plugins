package kg.apc.jmeter.gui;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import org.apache.jmeter.gui.util.PowerTableModel;

/**
 *
 * @author undera
 */
public class ButtonPanelAddCopyRemove extends JPanel{
    private final JButton addRowButton;
    private final JButton copyRowButton;
    private final JButton deleteRowButton;

    private final PowerTableModel tableModel;

    public ButtonPanelAddCopyRemove(JTable grid, PowerTableModel tableModel, Object[] defaultValues) {
      setLayout(new GridLayout(1, 2));

      addRowButton = new JButton("Add Row");
      copyRowButton = new JButton("Copy Row");
      deleteRowButton = new JButton("Delete Row");

      addRowButton.addActionListener(new AddRowAction(this, grid, tableModel, deleteRowButton, defaultValues));
      copyRowButton.addActionListener(new CopyRowAction(this, grid, tableModel, deleteRowButton));
      deleteRowButton.addActionListener(new DeleteRowAction(this, grid, tableModel, deleteRowButton));

      add(addRowButton);
      add(copyRowButton);
      add(deleteRowButton);
      this.tableModel = tableModel;
    }

    public void checkDeleteButtonStatus() {
       deleteRowButton.setEnabled(tableModel != null && tableModel.getRowCount() > 0);
    }
}
