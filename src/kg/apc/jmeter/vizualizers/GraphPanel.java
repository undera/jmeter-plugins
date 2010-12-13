// TODO: add slider to zoom Y axis
package kg.apc.jmeter.vizualizers;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.charting.GraphPanelChart;

/**
 *
 * @author apc
 */
public class GraphPanel
      extends JTabbedPane
      implements ChangeListener
{
   private GraphPanelChart graphTab;
   private JComponent rowsTab;
   private JComponent settingsTab;
   private ChartRowsTable table;

   /**
    *
    */
   public GraphPanel()
   {
      super();
      addGraphTab();
      addRowsTab();
      addOptionsTab();
      addChangeListener(this);
   }

   private void addRowsTab()
   {
      ImageIcon rowsIcon = createImageIcon("checks.png");
      rowsTab = new JPanel(new BorderLayout());
      rowsTab.add(makeTable(), BorderLayout.CENTER);

      JPanel logoPanel = new JPanel();
      JLabel logoLabel = new JLabel();
      logoPanel.setLayout(new java.awt.GridLayout(1, 0));
      logoLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
      logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kg/apc/jmeter/vizualizers/logoSimple.png"))); // NOI18N
      logoPanel.add(logoLabel);

      rowsTab.add(logoPanel, BorderLayout.SOUTH);
      rowsTab.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

      addTab("Rows", rowsIcon, rowsTab, "Select rows to display");
   }

   private void addOptionsTab()
   {
      ImageIcon icon = createImageIcon("settings.png");
      settingsTab = new JPanel(new BorderLayout());
      addTab("Settings", icon, settingsTab, "Chart plot settings");
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
      pane.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
      return pane;
   }

   private void addGraphTab()
   {
      ImageIcon graphIcon = createImageIcon("graph.png");
      graphTab = new GraphPanelChart();
      graphTab.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
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

   /**
    *
    */
   public void updateGui()
   {
      JComponent selectedTab = (JComponent) getSelectedComponent();
      selectedTab.updateUI();
      selectedTab.repaint();
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

   /**
    * check if the row bellows to the selected model and add it to the table
    * @param row
    */
   public void addRow(AbstractGraphRow row)
   {
      if(getGraphObject().isModelContainsRow(row))
      {
          table.addRow(row);
      }
   }

   public void stateChanged(ChangeEvent e)
   {
      updateGui();
   }

   /**
    * 
    */
   public void clearRowsTab()
   {
      table.clear();
   }

   public JComponent getSettingsTab()
   {
      return settingsTab;
   }
}
