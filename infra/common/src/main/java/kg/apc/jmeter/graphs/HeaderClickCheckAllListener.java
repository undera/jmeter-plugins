package kg.apc.jmeter.graphs;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

class HeaderClickCheckAllListener
     extends MouseAdapter
{
   private boolean checked = true;
   private static final int CHECK_COL_INDEX = 0;

   @Override
   public void mouseClicked(MouseEvent evt)
   {
      JTable table = ((JTableHeader) evt.getSource()).getTable();
      TableColumnModel colModel = table.getColumnModel();

      // The index of the column whose header was clicked
      int vColIndex = colModel.getColumnIndexAtX(evt.getX());
      //int mColIndex = table.convertColumnIndexToModel(vColIndex);

      // Return if not clicked on any column header
      if (vColIndex == -1)
      {
         return;
      }

      // Determine if mouse was clicked between column heads
      Rectangle headerRect = table.getTableHeader().getHeaderRect(vColIndex);
      if (vColIndex == 0)
      {
         headerRect.width -= 3;    // Hard-coded constant
      }
      else
      {
         headerRect.grow(-3, 0);   // Hard-coded constant
      }

      if (!headerRect.contains(evt.getX(), evt.getY()))
      {
         // Mouse was clicked between column heads
         // vColIndex is the column head closest to the click

         // vLeftColIndex is the column head to the left of the click
         int vLeftColIndex = vColIndex;
         if (evt.getX() < headerRect.x)
         {
            vLeftColIndex--;
         }
      }
      else
      {
         // click was on column
         if (vColIndex == CHECK_COL_INDEX)
         {
            checked = !checked;
            for (int n = 0; n < table.getModel().getRowCount(); n++)
            {
               table.getModel().setValueAt(checked, n, CHECK_COL_INDEX);
            }
         }
      }
   }
}
