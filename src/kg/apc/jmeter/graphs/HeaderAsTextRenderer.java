package kg.apc.jmeter.graphs;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import org.apache.jmeter.gui.util.HeaderAsPropertyRenderer;

/**
 *
 * @author apc
 */
class HeaderAsTextRenderer
     extends HeaderAsPropertyRenderer
{
   @Override
   public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column)
   {
      if (table != null)
      {
         JTableHeader header = table.getTableHeader();
         if (header != null)
         {
            setForeground(header.getForeground());
            setBackground(header.getBackground());
            setFont(header.getFont());
         }
         setText(getText(value, row, column));
         setBorder(UIManager.getBorder("TableHeader.cellBorder"));
         setHorizontalAlignment(JLabel.CENTER);
      }
      return this;
   }

   @Override
   protected String getText(Object value, int row, int column)
   {
      if (value == null)
      {
         return "";
      }
      return value.toString();
   }
}
