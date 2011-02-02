package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.charting.ColorsDispatcher;
import kg.apc.jmeter.charting.AbstractGraphRow;
import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author apc
 */
public class LatenciesOverTimeGui
      extends AbstractOverTimeVisualizer
{
   /**
    *
    */
   public LatenciesOverTimeGui()
   {
      super();
      graphPanel.getGraphObject().setyAxisLabel("Response latencies in ms");
   }

   private void addThreadGroupRecord(String threadGroupName, long time,
         long numThreads)
   {
      String labelAgg = "Aggregated Responses Latencies";
      AbstractGraphRow row = model.get(threadGroupName);
      AbstractGraphRow rowAgg = modelAggregate.get(labelAgg);
      if (row == null)
      {
         row = getNewRow(model, AbstractGraphRow.ROW_AVERAGES, threadGroupName, AbstractGraphRow.MARKER_SIZE_SMALL, false, false, false, true, true);
      }
      if (rowAgg == null)
      {
         rowAgg = getNewRow(modelAggregate, AbstractGraphRow.ROW_AVERAGES, labelAgg, AbstractGraphRow.MARKER_SIZE_SMALL, false, false, false, true, ColorsDispatcher.RED, true);
      }

      row.add(time, numThreads);
      rowAgg.add(time, numThreads);
   }

   public String getLabelResource()
   {
      return this.getClass().getSimpleName();
   }

   @Override
   public String getStaticLabel()
   {
      return "Response Latencies Over Time";
   }

   @Override
   public void add(SampleResult res)
   {
        super.add(res);
      addThreadGroupRecord(res.getSampleLabel(),
            normalizeTime(res.getEndTime()), res.getLatency());
      updateGui(null);
   }

    @Override
    protected JSettingsPanel getSettingsPanel()
    {
        return new JSettingsPanel(this, true, true, false, true, true, false, false, true, true);
    }
}
