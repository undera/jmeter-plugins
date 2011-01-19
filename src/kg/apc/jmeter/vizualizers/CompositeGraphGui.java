package kg.apc.jmeter.vizualizers;

import java.util.Iterator;
import javax.swing.ImageIcon;
import kg.apc.jmeter.charting.AbstractGraphRow;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class CompositeGraphGui extends AbstractGraphPanelVisualizer
{

    private static final Logger log = LoggingManager.getLoggerForClass();
    private long lastUpdate = 0;
    private JCompositeRowsSelectorPanel compositeRowsSelectorPanel;
    public CompositeModel compositeModel;

    public CompositeGraphGui()
    {
        compositeModel = new CompositeModel();
        ImageIcon rowsIcon = new ImageIcon(CompositeGraphGui.class.getResource("checks.png"));
        graphPanel.remove(1);
        compositeRowsSelectorPanel = new JCompositeRowsSelectorPanel(compositeModel);
        graphPanel.insertTab("Graphs", rowsIcon, compositeRowsSelectorPanel, "Select graphs/rows to display", 1);

        graphPanel.getGraphObject().setxAxisLabelRenderer(new DateTimeRenderer("HH:mm:ss"));
        graphPanel.getGraphObject().setReSetColors(true);

        graphPanel.getGraphObject().setxAxisLabel("Elapsed time");
        graphPanel.getGraphObject().setyAxisLabel("Scaled values");
        
        CompositeResultCollector compositeResultCollector = new CompositeResultCollector();
        compositeResultCollector.setCompositeModel(compositeModel);
        setModel(compositeResultCollector);
    }

    @Override
    protected JSettingsPanel getSettingsPanel()
    {
        return new JSettingsPanel(this, false, true, false, true, true, false, false, false);
    }

    @Override
    public String getLabelResource()
    {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel()
    {
        return "Composite Graph";
    }

    @Override
    public void clearData()
    {
        super.clearData();
        compositeRowsSelectorPanel.clearData();
    }

    @Override
   public TestElement createTestElement()
   {
        ResultCollector modelNew = getModel();
        if (modelNew == null) {
            modelNew = new CompositeResultCollector();
            ((CompositeResultCollector)modelNew).setCompositeModel(compositeModel);
            setModel(modelNew);
        }
        modifyTestElement(modelNew);
        return modelNew;
   }

    @Override
    public void configure(TestElement te)
    {
        //log.info("Configure");
        super.configure(te);
        ((CompositeResultCollector)te).setCompositeModel(compositeModel);
    }

    @Override
    public void updateGui()
    {
        model.clear();

        Iterator<String[]> iter = compositeRowsSelectorPanel.getItems();
        while (iter.hasNext())
        {
            String[] item = iter.next();
            AbstractGraphRow row = compositeModel.getRow(item[0], item[1]);
            if (row != null)
            {
                if (!model.containsKey(item[0] + ">" + item[1]))
                {
                    model.put(item[0] + ">" + item[1], row);
                }
            }
        }
        super.updateGui();
    }

    @Override
    public void add(SampleResult sr)
    {
        long time = System.currentTimeMillis();

        if (time > lastUpdate + 1000)
        {
            compositeRowsSelectorPanel.updateTree();
            lastUpdate = time;
            updateGui();
        }
    }
}
