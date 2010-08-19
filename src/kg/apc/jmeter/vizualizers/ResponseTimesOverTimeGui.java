package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.charting.GraphRowAverages;
import kg.apc.jmeter.charting.AbstractGraphRow;
import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author apc
 */
public class ResponseTimesOverTimeGui
      extends AbstractGraphPanelVisualizer
{
   //private static final Logger log = LoggingManager.getLoggerForClass();
   /**
    *
    */
   public ResponseTimesOverTimeGui()
   {
      super();
      graphPanel.getGraphObject().setxAxisLabelRenderer(new DateTimeRenderer(
            "HH:mm:ss"));
      graphPanel.getGraphObject().setDrawFinalZeroingLines(false);
   }

   private void addThreadGroupRecord(String threadGroupName, long time,
         long numThreads)
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

      row.add(time, numThreads);
   }

   public String getLabelResource()
   {
      return this.getClass().getSimpleName();
   }

   @Override
   public String getStaticLabel()
   {
      return "Response Times Over Time";
   }

   public void add(SampleResult res)
   {
      addThreadGroupRecord(res.getSampleLabel(),
            res.getEndTime() - res.getEndTime() % delay, res.getTime());
      updateGui(null);
   }
}
