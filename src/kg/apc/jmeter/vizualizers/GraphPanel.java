package kg.apc.jmeter.vizualizers;

import java.awt.Image;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class GraphPanel
     extends JTabbedPane
{
   private static final Logger log = LoggingManager.getLoggerForClass();
   private GraphPanelChart graphTab;
   private JComponent rowsTab;

   public GraphPanel(ConcurrentHashMap<String, GraphPanelChartRow> model)
   {
      super();
      addGraphTab(model);
      addRowsTab();
   }

   private void addRowsTab()
   {
      ImageIcon rowsIcon = createImageIcon("checks.png");
      rowsTab = new JPanel();
      addTab("Rows", rowsIcon, rowsTab, "Select rows to display");
   }

   private void addGraphTab(ConcurrentHashMap<String, GraphPanelChartRow> model)
   {
      ImageIcon graphIcon = createImageIcon("graph.png");
      graphTab = new GraphPanelChart();
      graphTab.setRows(model);
      addTab("Chart", graphIcon, graphTab, "View chart");
   }

   /** Returns an ImageIcon, or null if the path was invalid. */
   protected static ImageIcon createImageIcon(String path)
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
      //if (getSelectedComponent() == graphTab)
      {
         //autoSizeGraph();

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
}
