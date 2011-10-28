package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.graphs.AbstractGraphPanelVisualizer;
import java.awt.Color;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.charting.AbstractGraphRow;
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
      graphPanel.getGraphObject().setxAxisLabel("Response times in ms");
      graphPanel.getGraphObject().setYAxisLabel("Number of responses");
   }

   private void addThreadGroupRecord(String threadGroupName, long time, int granulation)
   {
      String aggLabel = "Overall Response Times";
      AbstractGraphRow row = model.get(threadGroupName);
      AbstractGraphRow rowAgg = modelAggregate.get(aggLabel);

      if (row == null)
      {
         row = getNewRow(model, AbstractGraphRow.ROW_SUM_VALUES, threadGroupName, AbstractGraphRow.MARKER_SIZE_NONE, true, false, false, true, false);
      }
      if (rowAgg == null)
      {
         rowAgg = getNewRow(modelAggregate, AbstractGraphRow.ROW_SUM_VALUES, aggLabel, AbstractGraphRow.MARKER_SIZE_NONE, true, false, false, true, Color.RED, false);
      }

      row.add(time, 1);
      row.setGranulationValue(granulation);
      rowAgg.add(time, 1);
      rowAgg.setGranulationValue(granulation);
   }

    @Override
   public String getLabelResource()
   {
      return this.getClass().getSimpleName();
   }

   @Override
   public String getStaticLabel()
   {
      return JMeterPluginsUtils.prefixLabel("Response Times Distribution");
   }

    @Override
   public void add(SampleResult res)
   {
      int granulation = getGranulation();
      addThreadGroupRecord(res.getSampleLabel(), res.getTime() - res.getTime() % granulation, granulation);
      updateGui(null);
   }

    @Override
    protected JSettingsPanel createSettingsPanel()
    {
        return new JSettingsPanel(this,
                JSettingsPanel.TIMELINE_OPTION |
                JSettingsPanel.GRADIENT_OPTION |
                JSettingsPanel.BAR_CHART_X_LIMIT_OPTION |
                JSettingsPanel.AGGREGATE_OPTION |
                JSettingsPanel.MARKERS_OPTION |
                JSettingsPanel.CHART_TYPE_OPTION);
    }

    @Override
    public String getWikiPage() {
        return "RespTimesDistribution";
    }
}
