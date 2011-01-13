// todo: add spacing around panel
// fixme: perhaps we should replace model via setModel and use ONE model...
package kg.apc.jmeter.vizualizers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListMap;
import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.charting.GraphPanelChart;
import kg.apc.jmeter.charting.GraphRowAverages;
import kg.apc.jmeter.charting.GraphRowExactValues;
import kg.apc.jmeter.charting.GraphRowOverallAverages;
import kg.apc.jmeter.charting.GraphRowPercentiles;
import kg.apc.jmeter.charting.GraphRowSumValues;
import kg.apc.jmeter.charting.RowsCollector;
import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.BooleanProperty;
import org.apache.jmeter.testelement.property.LongProperty;
import org.apache.jmeter.visualizers.GraphListener;
import org.apache.jmeter.visualizers.ImageVisualizer;
import org.apache.jmeter.visualizers.Sample;
import org.apache.jmeter.visualizers.gui.AbstractVisualizer;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author apc
 */
public abstract class AbstractGraphPanelVisualizer
        extends AbstractVisualizer
        implements Clearable,
        GraphListener,
        ImageVisualizer,
        SettingsInterface
{

    private static final Logger log = LoggingManager.getLoggerForClass();
    /**
     *
     */
    protected ConcurrentSkipListMap<String, AbstractGraphRow> model;
    protected ConcurrentSkipListMap<String, AbstractGraphRow> modelAggregate;
    /**
     *
     */
    protected long lastRepaint = 0;
    /**
     *
     */
    private int interval = 500;
    private boolean isAggregate = false;
    /**
     *
     */
    protected GraphPanel graphPanel;
    /**
     *
     */
    protected ColorsDispatcher colors;
    private static final long REPAINT_INTERVAL = 500;
    public static final String INTERVAL_PROPERTY = "interval_grouping";
    public static final String GRAPH_AGGREGATED = "graph_aggregated";
    public JSettingsPanel settingsPanel = null;

    /**
     *
     */
    public AbstractGraphPanelVisualizer()
    {
        super();
        model = new ConcurrentSkipListMap<String, AbstractGraphRow>();
        modelAggregate = new ConcurrentSkipListMap<String, AbstractGraphRow>();
        colors = new ColorsDispatcher();
        //RowsProviderResultCollector resCollector = new RowsProviderResultCollector();
        //setModel(resCollector);
        initGui();
    }

    protected abstract JSettingsPanel getSettingsPanel();

    private void initGui()
    {
        setLayout(new BorderLayout());
        add(makeTitlePanel(), BorderLayout.NORTH);
        add(createGraphPanel(), BorderLayout.CENTER);
    }

    /**
     *
     * @return
     */
    protected GraphPanel createGraphPanel()
    {
        graphPanel = new GraphPanel();
        graphPanel.getGraphObject().setRows(model);
        // should be placed after creating graph panel
        settingsPanel = getSettingsPanel();
        graphPanel.getSettingsTab().add(settingsPanel, BorderLayout.CENTER);
        //graphPanel.setSettingsTabPanel(settingsPanel);
        return graphPanel;
    }

    /**
     *
     * @param sample
     */
    @Override
    public void updateGui(Sample sample)
    {
        long time = System.currentTimeMillis();
        if ((time - lastRepaint) >= REPAINT_INTERVAL)
        {
            updateGui();
            repaint();
            lastRepaint = time;
        }
    }

    /**
     *
     */
    @Override
    public void updateGui()
    {
        graphPanel.updateGui();
    }

    @Override
    public void clearData()
    {
        model.clear();
        modelAggregate.clear();
        colors.reset();
        graphPanel.clearRowsTab();
        //disbaled for now
        RowsCollector.getInstance().clearRows(getModel().getName());
        updateGui();
        repaint();
    }

    /**
     *
     * @return
     */
    @Override
    public Image getImage()
    {
        return graphPanel.getGraphImage();
    }

    @Override
    public int getGranulation()
    {
        return interval;
    }

    @Override
    public void setGranulation(int granulation)
    {
        if (granulation < 1)
        {
            throw new IllegalArgumentException("Interval cannot be less than 1");
        }
        interval = granulation;
        settingsPanel.setGranulationValue(granulation);
    }

    @Override
    public TestElement createTestElement()
    {
        TestElement el = super.createTestElement();
        return el;
    }

    @Override
    public void modifyTestElement(TestElement c)
    {
        super.modifyTestElement(c);
        c.setProperty(new LongProperty(INTERVAL_PROPERTY, interval));
        c.setProperty(new BooleanProperty(GRAPH_AGGREGATED, isAggregate));
    }

    @Override
    public void configure(TestElement el)
    {
        super.configure(el);
        int intervalProp = el.getPropertyAsInt(INTERVAL_PROPERTY);
        boolean aggregatedProp = el.getPropertyAsBoolean(GRAPH_AGGREGATED, false);
        if (intervalProp > 0)
        {
            setGranulation(intervalProp);
        }
        switchModel(aggregatedProp);
    }

    @Override
    public GraphPanelChart getGraphPanelChart()
    {
        return graphPanel.getGraphObject();
    }

    @Override
    public void switchModel(boolean aggregate)
    {

        ConcurrentSkipListMap<String, AbstractGraphRow> selectedModel;
        if (aggregate)
        {
            selectedModel = modelAggregate;
        } else
        {
            selectedModel = model;
        }

        graphPanel.getGraphObject().setRows(selectedModel);
        graphPanel.clearRowsTab();

        Iterator<AbstractGraphRow> rowsIter = selectedModel.values().iterator();
        while (rowsIter.hasNext())
        {
            graphPanel.addRow(rowsIter.next());
        }

        isAggregate = aggregate;
        settingsPanel.setAggregateMode(aggregate);
    }

    private AbstractGraphRow instanciateNewRow(int rowType)
    {
        switch (rowType)
        {
            case AbstractGraphRow.ROW_AVERAGES:
                return new GraphRowAverages();
            case AbstractGraphRow.ROW_EXACT_VALUES:
                return new GraphRowExactValues();
            case AbstractGraphRow.ROW_OVERALL_AVERAGES:
                return new GraphRowOverallAverages();
            case AbstractGraphRow.ROW_PERCENTILES:
                return new GraphRowPercentiles();
            case AbstractGraphRow.ROW_SUM_VALUES:
                return new GraphRowSumValues(false);
            case AbstractGraphRow.ROW_ROLLING_SUM_VALUES:
                return new GraphRowSumValues(true);
            default:
                return null;
        }
    }

    protected synchronized AbstractGraphRow getNewRow(
            ConcurrentSkipListMap<String, AbstractGraphRow> model,
            int rowType,
            String label,
            int markerSize,
            boolean isBarRow,
            boolean displayLabel,
            boolean thickLines,
            boolean showInLegend,
            Color color,
            boolean canCompose)
    {
        AbstractGraphRow row = null;
        if (!model.containsKey(label))
        {
            row = instanciateNewRow(rowType);
            row.setLabel(label);
            row.setMarkerSize(markerSize);
            row.setDrawBar(isBarRow);
            row.setDrawLine(!isBarRow);
            row.setDrawValueLabel(displayLabel);
            row.setDrawThickLines(thickLines);
            row.setShowInLegend(showInLegend);
            if(color == null)
            {
                row.setColor(colors.getNextColor());
            } else {
                row.setColor(color);
            }
            model.put(label, row);
            graphPanel.addRow(row);
            if(canCompose)
            {
                RowsCollector.getInstance().addRow(getModel().getName(), row);
            }
        } else
        {
            row = model.get(label);
        }

        return row;
    }

    protected synchronized AbstractGraphRow getNewRow(
            ConcurrentSkipListMap<String, AbstractGraphRow> model,
            int rowType,
            String label,
            int markerSize,
            boolean isBarRow,
            boolean displayLabel,
            boolean thickLines,
            boolean showInLegend,
            boolean canCompose)
    {
        return getNewRow(model, rowType, label, markerSize, isBarRow, displayLabel, thickLines, showInLegend, null, canCompose);
    }
}
