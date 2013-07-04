package kg.apc.jmeter.graphs;

import javax.swing.event.TableModelEvent;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import kg.apc.charting.AbstractGraphRow;
import org.apache.jorphan.gui.ObjectTableModel;
import org.apache.jorphan.reflect.Functor;

/**
 *
 * @author apc
 */
class ChartRowsTable
     extends JTable
{
   TableCellRenderer colorRenderer = new ColorRenderer(false);
   JRowsSelectorPanel parentContainer = null;

   /**
    *
    */
   public ChartRowsTable(JRowsSelectorPanel parent)
   {
      super();
      parentContainer = parent;
      initializeTableModel();
      setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      getTableHeader().setDefaultRenderer(new HeaderAsTextRenderer());
      getTableHeader().addMouseListener(new HeaderClickCheckAllListener());
      getTableHeader().setReorderingAllowed(false);
      getTableHeader().setResizingAllowed(false);
      setCollumnsSize();
   }

   private void setCollumnsSize() {
      getColumnModel().getColumn(0).setPreferredWidth(100);
      getColumnModel().getColumn(1).setPreferredWidth(100);
      getColumnModel().getColumn(2).setPreferredWidth(500);
   }

   private void initializeTableModel()
   {
      ObjectTableModel model = new ObjectTableModel(new String[]
           {
              "(Un)Check All",
              "Legend Color",
              "Row Name"
           }, AbstractGraphRow.class, new Functor[]
           {
              new Functor("isDrawOnChart"),
              new Functor("getColor"),
              new Functor("getLabel")
           }, new Functor[]
           {
              new Functor("setDrawOnChart"),
              null,
              null
           }, new Class[]
           {
              Boolean.class,
              Color.class,
              String.class
           });

      model.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e)
            {
                if(parentContainer != null)
                {
                    parentContainer.refreshPreview();
                }
            }
        });

      setModel(model);
   }

   /**
    *
    * @param row
    */
   public void addRow(AbstractGraphRow row)
   {
      ((ObjectTableModel) dataModel).addRow(row);
   }

   @Override
   public TableCellRenderer getCellRenderer(int row, int column)
   {
      if ((column == 1))
      {
         return colorRenderer;
      }
      return super.getCellRenderer(row, column);
   }

   /**
    * 
    */
   public void clear()
   {
      initializeTableModel();
      setCollumnsSize();
   }
}
