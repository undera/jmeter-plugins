package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.charting.GraphRowAverages;
import kg.apc.jmeter.charting.AbstractGraphRow;
import org.apache.jmeter.samplers.SampleResult;

public class ThroughputOverTimeGui
      extends AbstractGraphPanelVisualizer
{
   //private static final Logger log = LoggingManager.getLoggerForClass();
   public ThroughputOverTimeGui()
   {
      super();
      graphPanel.getGraphObject().setxAxisLabelRenderer(new DateTimeRenderer(
            "HH:mm:ss"));
      graphPanel.getGraphObject().setDrawFinalZeroingLines(true);
   }

   private void addThreadGroupRecord(String threadGroupName, long time,
         double rTime)
   {
      AbstractGraphRow row = model.get(threadGroupName);
      if (row == null)
      {
         row = new GraphRowAverages();
         row.setLabel(threadGroupName);
         row.setColor(colors.getNextColor());
         row.setDrawLine(true);
         row.setMarkerSize(AbstractGraphRow.MARKER_SIZE_SMALL);
         model.put(threadGroupName, row);
         graphPanel.addRow(row);
      }

      row.add(time, rTime);
   }

   public String getLabelResource()
   {
      return this.getClass().getSimpleName();
   }

   @Override
   public String getStaticLabel()
   {
      return "Transaction Throughput Over Time";
   }

   public void add(SampleResult res)
   {
      addThreadGroupRecord(res.getSampleLabel(),
            res.getEndTime() - res.getEndTime() % delay, 1000 / res.getTime());
      updateGui(null);
   }
}
