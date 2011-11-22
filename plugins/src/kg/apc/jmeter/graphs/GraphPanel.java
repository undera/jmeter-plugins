package kg.apc.jmeter.graphs;

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
import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.GraphPanelChart;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author apc
 */
public class GraphPanel
        extends JTabbedPane
{

    private static final Logger log = LoggingManager.getLoggerForClass();
    private GraphPanelChart graphPanelObject;
    private JRowsSelectorPanel rowsTab;
    private JComponent settingsTab;
    private ChartRowsTable table;
    private JGraphPanel graphTab;
    
    /**
     *
     */
    public GraphPanel()
    {
        super();
        addGraphTab();
        addRowsTab();
        addOptionsTab();
        addChangeListener(new TabsChangeListener());
    }

    private void addRowsTab()
    {
        ImageIcon rowsIcon = createImageIcon("/kg/apc/jmeter/img/checks.png");
        rowsTab = new JRowsSelectorPanel(this);
        rowsTab.setTable(makeTable(rowsTab));

        addTab("Rows", rowsIcon, rowsTab, "Select rows to display");
    }

    /**
     * Method used only for change row selector tab in Composite Chart
     * @param tabLabelnew the new tab label
     * @param object the new Panel to add in the tab
     * @param toolTipText the new tooltip text
     */
    public void replaceRowTab(String tabLabel, Component object, String toolTipText) {
       int index = indexOfComponent(rowsTab);
       remove(index);
       ImageIcon rowsIcon = createImageIcon("/kg/apc/jmeter/img/checks.png");
       insertTab(tabLabel, rowsIcon, object, toolTipText, index);
    }

    private void addOptionsTab()
    {
        ImageIcon icon = createImageIcon("/kg/apc/jmeter/img/settings.png");
        settingsTab = new JPanel(new BorderLayout());
        addTab("Settings", icon, settingsTab, "Chart plot settings");
    }

    private Component makeTable(JRowsSelectorPanel rowsTab)
    {
        table = new ChartRowsTable(rowsTab);
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
        ImageIcon graphIcon = createImageIcon("/kg/apc/jmeter/img/graph.png");
        graphPanelObject = new GraphPanelChart(GuiPackage.getInstance() != null);
        graphPanelObject.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        graphTab = new JGraphPanel();
        graphTab.add(graphPanelObject, BorderLayout.CENTER);
        addTab("Chart", graphIcon, graphTab, "View chart");
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    private static ImageIcon createImageIcon(String path)
    {
        java.net.URL imgURL = GraphPanel.class.getResource(path);
        if (imgURL != null)
            return new ImageIcon(imgURL);
        else
        {
            throw new RuntimeException("Couldn't find file: " + path);
            //log.error("Couldn't find file: " + path);
            //return null;
        }
    }

    /**
     *
     */
    public void updateGui()
    {
        graphPanelObject.invalidateCache();
        JComponent selectedTab = (JComponent) getSelectedComponent();
        selectedTab.updateUI();
        selectedTab.repaint();
    }

    public Image getGraphImage()
    {
        Image result = graphPanelObject.createImage(graphPanelObject.getWidth(), graphPanelObject.getHeight());

        if (result != null)
            graphPanelObject.paintComponent(result.getGraphics());

        return result;
    }

    /**
     * @return the graphTab
     */
    public GraphPanelChart getGraphObject()
    {
        return graphPanelObject;
    }

    /**
     * check if the row bellows to the selected model and add it to the table
     * @param row
     */
    public void addRow(AbstractGraphRow row)
    {
        if (getGraphObject().isModelContainsRow(row))
            table.addRow(row);
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

    private class JGraphPanel extends JPanel implements GraphRendererInterface
    {
        public JGraphPanel()
        {
            super();
            setLayout(new BorderLayout());
        }
        @Override
        public JPanel getGraphDisplayPanel()
        {
            return this;
        }

        @Override
        public boolean isPreview()
        {
            return false;
        }
    }

    private class TabsChangeListener
            implements ChangeListener
    {

        @Override
        public void stateChanged(ChangeEvent e)
        {
            updateGui();

            JComponent selectedComponent = (JComponent) getSelectedComponent();
            //settings panel is added to a top container panel, so we get it
            //as selected component
            if (selectedComponent == settingsTab)
            {
                selectedComponent = (JComponent) selectedComponent.getComponent(0);
            }

            GraphRendererInterface renderer = (GraphRendererInterface) (selectedComponent);
            renderer.getGraphDisplayPanel().add(graphPanelObject, BorderLayout.CENTER);
            graphPanelObject.setIsPreview(renderer.isPreview());
        }
    }
}
