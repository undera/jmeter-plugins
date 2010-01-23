package kg.apc.jmeter.vizualizers;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
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
      addTab("Rows", rowsIcon, rowsTab, "Select rows to display");
   }

   private void addGraphTab()
   {
      ImageIcon graphIcon = createImageIcon("graph.png");
      graphTab = new GraphPanelChart();

//      JPanel panel = new JPanel(new BorderLayout());
//      panel.add(graphTab, BorderLayout.CENTER);

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

   private void autoSizeGraph()
   {
      final Container p = graphTab.getParent();
      int newWidth = p.getWidth() - 50;
      int newHeight = p.getHeight() - 50;
      if (graphTab.getWidth() != newWidth || graphTab.getHeight() != newHeight)
      {
         graphTab.setSize(new Dimension(newWidth, newHeight));
         log.info("Autosize dimensions: "
              + Integer.toString(p.getWidth()) + " "
              + Integer.toString(p.getHeight()) + " ");
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
