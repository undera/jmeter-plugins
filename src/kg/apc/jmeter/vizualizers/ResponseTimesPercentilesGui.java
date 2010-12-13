package kg.apc.jmeter.vizualizers;

import java.awt.Color;
import java.util.concurrent.ConcurrentSkipListMap;
import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.charting.GraphRowPercentiles;
import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author Stephane Hoblingre
 */
public class ResponseTimesPercentilesGui
        extends AbstractGraphPanelVisualizer
{

    /**
     *
     */
    public ResponseTimesPercentilesGui()
    {
        super();
        graphPanel.getGraphObject().setForcedMinX(0);
    }

    @Override
    public String getLabelResource()
    {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel()
    {
        return "Response Times Percentiles";
    }

    @Override
    public void add(SampleResult res)
    {
        String label = res.getSampleLabel();
        String aggregateLabel = "Aggregated Response Times";
        GraphRowPercentiles row;
        GraphRowPercentiles rowAgg;
        if (!model.containsKey(label))
        {
            row = getNewRow(model, label);
            row.setColor(colors.getNextColor());
        } else
        {
            row = (GraphRowPercentiles) model.get(label);
        }

        if (!modelAggregate.containsKey(aggregateLabel))
        {
            rowAgg = getNewRow(modelAggregate, aggregateLabel);
            rowAgg.setColor(Color.RED);
        } else
        {
            rowAgg = (GraphRowPercentiles) modelAggregate.get(aggregateLabel);
        }

        row.addResponseTime(res.getTime());
        rowAgg.addResponseTime(res.getTime());
        updateGui(null);
    }

    private synchronized GraphRowPercentiles getNewRow(ConcurrentSkipListMap<String, AbstractGraphRow> model, String label)
    {
        GraphRowPercentiles row = null;
        if (!model.containsKey(label))
        {
            row = new GraphRowPercentiles();
            row.setLabel(label);
            row.setDrawLine(true);
            row.setMarkerSize(AbstractGraphRow.MARKER_SIZE_SMALL);
            model.put(label, row);
            graphPanel.addRow(row);
        } else
        {
            row = (GraphRowPercentiles) model.get(label);
        }
        return row;
    }

    @Override
    protected JSettingsPanel getSettingsPanel()
    {
        return new JSettingsPanel(this, false, true, false, false, false, false, false, true);
    }
}
