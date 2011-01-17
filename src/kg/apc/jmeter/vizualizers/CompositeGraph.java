package kg.apc.jmeter.vizualizers;

import java.util.Iterator;
import javax.swing.ImageIcon;
import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.charting.RowsCollector;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class CompositeGraph extends AbstractGraphPanelVisualizer
{

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

        graphPanel.getGraphObject().setxAxisLabel("Elapsed time");
        graphPanel.getGraphObject().setyAxisLabel("Scaled values");
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
    public void updateGui()
    {
        model.clear();

        Iterator<String[]> iter = compositeRowsSelectorPanel.getItems();
        while (iter.hasNext())
        {
            String[] item = iter.next();
            AbstractGraphRow row = RowsCollector.getInstance().getRow(item[0], item[1]);
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
