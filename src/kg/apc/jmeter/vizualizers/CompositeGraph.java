package kg.apc.jmeter.vizualizers;

import javax.swing.ImageIcon;
import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.charting.RowsCollector;
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

        graphPanel.getGraphObject().setxAxisLabelRenderer(new DateTimeRenderer("HH:mm:ss"));
        graphPanel.getGraphObject().setReSetColors(true);
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
        
        if(time > lastUpdate + 1000) {
            //System.out.println("---------------Models Dump----------------");
            lastUpdate = time;
            compositeRowsSelectorPanel.updateTree();

            String viz1 = "Active Threads Over Time";
            String rowName1 = "Test all Graphs";

            String viz2 = "Response Times Over Time";
            String rowName2 = "Aggregated Response Times";

            AbstractGraphRow row1 = RowsCollector.getInstance().getRow(viz1, rowName1);
            if(row1 != null)
            {
                if(!model.containsKey("[" + viz1 + "]" + rowName1)) model.put("[" + viz1 + "]" + rowName1, row1);
            }

            AbstractGraphRow row2 = RowsCollector.getInstance().getRow(viz2, rowName2);
            if(row2 != null)
            {
                if(!model.containsKey("[" + viz2 + "]" + rowName2)) model.put("[" + viz2 + "]" + rowName2, row2);
            }

            updateGui();

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
