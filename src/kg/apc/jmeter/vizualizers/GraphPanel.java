// TODO: add slider to zoom Y axis
package kg.apc.jmeter.vizualizers;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.charting.GraphPanelChart;

public class GraphPanel
     extends JTabbedPane
     implements ChangeListener
{
   private GraphPanelChart graphTab;
   private JComponent rowsTab;
   private ChartRowsTable table;

   public GraphPanel()
   {
      super();
      addGraphTab();
      addRowsTab();
      addChangeListener(this);
   }

   private void addRowsTab()
   {
      ImageIcon rowsIcon = createImageIcon("checks.png");
      rowsTab = new JPanel(new BorderLayout());
      rowsTab.add(makeTable(), BorderLayout.CENTER);
      addTab("Rows", rowsIcon, rowsTab, "Select rows to display");
   }

   private Component makeTable()
   {
      table = new ChartRowsTable();
      return makeScrollPane(table);
   }

   private JScrollPane makeScrollPane(Component comp)
   {
      JScrollPane pane = new JScrollPane(comp);
      pane.setPreferredSize(pane.getMinimumSize());
      return pane;
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

      if (getSelectedComponent() == rowsTab)
      {
         rowsTab.updateUI();
         rowsTab.repaint();
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

   public void addRow(AbstractGraphRow row)
   {
      table.addRow(row);
   }

   public void stateChanged(ChangeEvent e)
   {
      updateGui();
   }

   public void clearRowsTab()
   {
      table.clear();
   }
}
