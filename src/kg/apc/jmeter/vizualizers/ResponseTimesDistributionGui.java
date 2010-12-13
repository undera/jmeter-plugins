package kg.apc.jmeter.vizualizers;

import java.awt.Color;
import java.util.concurrent.ConcurrentSkipListMap;
import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.charting.GraphRowSumValues;
import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author apc
 */
public class ResponseTimesDistributionGui
      extends AbstractGraphPanelVisualizer
{
   /**
    *
    */
   public ResponseTimesDistributionGui()
   {
      super();
      setGranulation(100);
      //graphPanel.getGraphObject().setChartType(GraphPanelChart.CHART_PERCENTAGE);
   }

   private synchronized AbstractGraphRow getNewRow(ConcurrentSkipListMap<String, AbstractGraphRow> model, String label)
   {
      AbstractGraphRow row = null;
      
      if (!model.containsKey(label))
      {
         row = new GraphRowSumValues(false);
         row.setLabel(label);
         row.setDrawLine(false);
         row.setDrawBar(true);
         row.setMarkerSize(AbstractGraphRow.MARKER_SIZE_NONE);
         model.put(label, row);
         graphPanel.addRow(row);
      }
      else
      {
         row = model.get(label);
      }
      //for testing of X Axis out of range prevention, uncomment following line
      //row.add(30000, 1);
      return row;
   }

   private void addThreadGroupRecord(String threadGroupName, long time, int granulation)
   {
      String aggLabel = "Aggregated Response Times";
      AbstractGraphRow row = model.get(threadGroupName);
      AbstractGraphRow rowAgg = modelAggregate.get(aggLabel);
      if (row == null)
      {
         row = getNewRow(model, threadGroupName);
         row.setColor(colors.getNextColor());
      }
      if (rowAgg == null)
      {
         rowAgg = getNewRow(modelAggregate, aggLabel);
         rowAgg.setColor(Color.RED);
      }

      row.add(time, 1);
      row.setGranulationValue(granulation);
      rowAgg.add(time, 1);
      rowAgg.setGranulationValue(granulation);
   }

   public String getLabelResource()
   {
      return this.getClass().getSimpleName();
   }

   @Override
   public String getStaticLabel()
   {
      return "Response Times Distribution";
   }

   public void add(SampleResult res)
   {
      int granulation = getGranulation();
      addThreadGroupRecord(res.getSampleLabel(), res.getTime() - res.getTime() % granulation, granulation);
      updateGui(null);
   }

    @Override
    protected JSettingsPanel getSettingsPanel()
    {
        return new JSettingsPanel(this, true, true, false, false, false, true, false, true);
    }
}
