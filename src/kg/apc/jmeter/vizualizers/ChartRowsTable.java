package kg.apc.jmeter.vizualizers;

import java.awt.Color;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import org.apache.jorphan.gui.ObjectTableModel;
import org.apache.jorphan.reflect.Functor;

public class ChartRowsTable
     extends JTable
{
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
              "(un)check all",
              "Row name"
           }, AbstractGraphRow.class, new Functor[]
           {
              new Functor("isDrawOnChart"),
              new Functor("getLabel")
           }, new Functor[]
           {
              new Functor("setDrawOnChart"),
              null
           }, new Class[]
           {
              Boolean.class,
              String.class
           });
      setModel(model);
   }

   public void addRow(AbstractGraphRow row)
   {
      ((ObjectTableModel) dataModel).addRow(row);
   }
}
