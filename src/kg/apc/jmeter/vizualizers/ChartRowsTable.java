package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.charting.AbstractGraphRow;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import org.apache.jorphan.gui.ObjectTableModel;
import org.apache.jorphan.reflect.Functor;

/**
 *
 * @author apc
 */
public class ChartRowsTable
     extends JTable
{
   TableCellRenderer colorRenderer = new ColorRenderer(false);

   /**
    *
    */
   public ChartRowsTable()
   {
      super();
      initializeTableModel();
      setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      getTableHeader().setDefaultRenderer(new HeaderAsTextRenderer());
      getTableHeader().addMouseListener(new HeaderClickCheckAllListener());
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
   }
}
