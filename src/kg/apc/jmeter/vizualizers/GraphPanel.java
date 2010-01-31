// TODO: add slider to zoom Y axis
package kg.apc.jmeter.vizualizers;

import java.awt.Component;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import org.apache.jmeter.config.Argument;
import org.apache.jmeter.gui.util.HeaderAsPropertyRenderer;
import org.apache.jorphan.gui.ObjectTableModel;
import org.apache.jorphan.reflect.Functor;
import sun.swing.table.DefaultTableCellHeaderRenderer;

public class GraphPanel
     extends JTabbedPane
{
   private GraphPanelChart graphTab;
   private JComponent rowsTab;
   private JTable table;
   private ObjectTableModel tableModel;

   public GraphPanel()
   {
      super();
      addGraphTab();
      addRowsTab();
   }

   private void addRowsTab()
   {
      ImageIcon rowsIcon = createImageIcon("checks.png");
      rowsTab = new JPanel();
      rowsTab.add(makeTable());
      addTab("Rows", rowsIcon, rowsTab, "Select rows to display");
   }

   private Component makeTable()
   {
      initializeTableModel();
      table = new JTable(tableModel);
      table.getTableHeader().setDefaultRenderer(new DefaultTableCellHeaderRenderer());
      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      return makeScrollPane(table);
   }

   private JScrollPane makeScrollPane(Component comp)
   {
      JScrollPane pane = new JScrollPane(comp);
      pane.setPreferredSize(pane.getMinimumSize());
      return pane;
   }

   private void initializeTableModel()
   {
      tableModel = new ObjectTableModel(new String[]
           {
              "Row name",
              "display?"
           },
           AbstractGraphRow.class,
           new Functor[]
           {
              new Functor("getName"), // $NON-NLS-1$
              new Functor("getValue")
           }, // $NON-NLS-1$
           new Functor[]
           {
              null, // $NON-NLS-1$
              new Functor("setValue")
           }, // $NON-NLS-1$
           new Class[]
           {
              String.class,
              String.class
           });
   }

   private void addGraphTab()
   {
      ImageIcon graphIcon = createImageIcon("graph.png");
      graphTab = new GraphPanelChart();
      addTab("Chart", graphIcon, graphTab, "View chart");
   }

   /** Returns an ImageIcon, or null if the path was invalid. */
   private static ImageIcon createImageIcon(String path)
   {
      java.net.URL imgURL = GraphPanel.class.getResource(path);
      if (imgURL != null)
      {
         return new ImageIcon(imgURL);
      }
      else
      {
         System.err.println("Couldn't find file: " + path);
         return null;
      }
   }

   public void updateGui()
   {
      if (getSelectedComponent() == graphTab)
      {
         graphTab.updateUI();
         graphTab.repaint();
      }
   }

   Image getGraphImage()
   {
      Image result = graphTab.createImage(graphTab.getWidth(), graphTab.getHeight());

      if (result != null)
      {
         graphTab.paintComponent(result.getGraphics());
      }

      return result;
   }

   /**
    * @return the graphTab
    */
   public GraphPanelChart getGraphObject()
   {
      return graphTab;
   }
}
