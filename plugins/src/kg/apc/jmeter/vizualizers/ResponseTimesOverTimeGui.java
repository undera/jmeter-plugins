package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.graphs.AbstractOverTimeVisualizer;
import java.awt.Color;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.charting.AbstractGraphRow;
import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author apc
 */
public class ResponseTimesOverTimeGui
      extends AbstractOverTimeVisualizer {
   /**
    *
    */
   public ResponseTimesOverTimeGui() {
      super();
      graphPanel.getGraphObject().setYAxisLabel("Response times in ms");
   }

   private void addThreadGroupRecord(String threadGroupName, long time,
         long numThreads) {
      String labelAgg = "Overall Response Times";
      AbstractGraphRow row = model.get(threadGroupName);
      AbstractGraphRow rowAgg = modelAggregate.get(labelAgg);
      if (row == null) {
         row = getNewRow(model, AbstractGraphRow.ROW_AVERAGES, threadGroupName, AbstractGraphRow.MARKER_SIZE_SMALL, false, false, false, true, true);
      }
      if (rowAgg == null) {
         rowAgg = getNewRow(modelAggregate, AbstractGraphRow.ROW_AVERAGES, labelAgg, AbstractGraphRow.MARKER_SIZE_SMALL, false, false, false, true, Color.RED, true);
      }

      row.add(time, numThreads);
      rowAgg.add(time, numThreads);
   }

   public String getLabelResource() {
      return this.getClass().getSimpleName();
   }

   @Override
   public String getStaticLabel() {
      return JMeterPluginsUtils.prefixLabel("Response Times Over Time");
   }

   @Override
   public void add(SampleResult res) {
      super.add(res);
      addThreadGroupRecord(res.getSampleLabel(),
            normalizeTime(res.getEndTime()), res.getTime());
      updateGui(null);
   }

   @Override
   protected JSettingsPanel createSettingsPanel() {
      return new JSettingsPanel(this,
            JSettingsPanel.TIMELINE_OPTION
            | JSettingsPanel.GRADIENT_OPTION
            | JSettingsPanel.FINAL_ZEROING_OPTION
            | JSettingsPanel.LIMIT_POINT_OPTION
            | JSettingsPanel.AGGREGATE_OPTION
            | JSettingsPanel.MAXY_OPTION
            | JSettingsPanel.RELATIVE_TIME_OPTION);
   }

   @Override
   public String getWikiPage() {
      return "ResponseTimesOverTime";
   }
}
