package kg.apc.jmeter.vizualizers;

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
      //graphPanel.getGraphObject().setChartType(GraphPanelChart.CHART_PERCENTAGE);
   }

   private synchronized AbstractGraphRow getNewRow(String label)
   {
      AbstractGraphRow row = null;
      if (!model.containsKey(label))
      {
         row = new GraphRowSumValues(false);
         row.setLabel(label);
         row.setColor(colors.getNextColor());
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

      return row;
   }

   private void addThreadGroupRecord(String threadGroupName, long time, int granulation)
   {
      AbstractGraphRow row = model.get(threadGroupName);
      if (row == null)
      {
         row = getNewRow(threadGroupName);
      }

      row.add(time, 1);
      row.setGranulationValue(granulation);
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
        return new JSettingsPanel(this, true, true, true, true, true);
    }
}
