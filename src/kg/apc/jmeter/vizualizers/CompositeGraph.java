package kg.apc.jmeter.vizualizers;

import javax.swing.ImageIcon;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class CompositeGraph extends AbstractGraphPanelVisualizer {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private long lastUpdate = 0;

    private JCompositeRowsSelectorPanel compositeRowsSelectorPanel;

    public CompositeGraph()
    {
        ImageIcon rowsIcon = new ImageIcon(CompositeGraph.class.getResource("checks.png"));
        graphPanel.remove(1);
        compositeRowsSelectorPanel = new JCompositeRowsSelectorPanel();
        graphPanel.insertTab("Graphs", rowsIcon, compositeRowsSelectorPanel, "Select graphs/rows to display", 1);
    }

    @Override
    protected JSettingsPanel getSettingsPanel() {
        return new JSettingsPanel(this, true, true, true, true, true, true, true, true);
    }

    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel() {
        return "Composite Graph";
    }

    @Override
    public void clearData()
    {
        super.clearData();
        compositeRowsSelectorPanel.clearData();
    }

    @Override
    public void add(SampleResult sr) {
        /*
        GuiPackage gui = GuiPackage.getInstance();
        JMeterTreeModel testTree = gui.getTreeModel();

        Iterator it = testTree.getNodesOfType(RowsProviderResultCollector.class).iterator();
        while (it.hasNext()) {
            Object obj=it.next();
            System.out.println(obj.getClass().getCanonicalName());
            if (((JMeterTreeNode) obj).getTestElement() instanceof RowsProviderResultCollector)
            {
                RowsProviderResultCollector provider = (RowsProviderResultCollector)((JMeterTreeNode) obj).getTestElement();
                System.out.println("Size: "+provider.getRowNames().size());

                Iterator it2=provider.getRowNames().iterator();
                while(it2.hasNext())
                {
                    String rowName=(String) it2.next();
                    System.out.println(rowName);
                }
            }
        }
        */
        long time = System.currentTimeMillis();
        
        if(time > lastUpdate + 5000) {
            //System.out.println("---------------Models Dump----------------");
            lastUpdate = time;
            compositeRowsSelectorPanel.updateTrees();
            /*
            Iterator<String> testNames = RowsCollector.getInstance().getThreadSafeVizualizerNamesIterator();
            int i=1;
            while(testNames.hasNext())
            {
                String testName = testNames.next();
                System.out.println("Vizualiser (" + i++ + "): " + testName);
                Iterator<AbstractGraphRow> rows = RowsCollector.getInstance().getThreadSafeRowsIterator(testName);
                while(rows.hasNext())
                {
                    AbstractGraphRow row = rows.next();
                    System.out.println(row.getLabel());
                }
                System.out.println("------------------------------------------");
            }
             
             */
        }
        
    }
}
