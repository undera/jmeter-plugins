package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.charting.GraphRowAverages;
import kg.apc.jmeter.charting.AbstractGraphRow;
import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author apc
 */
public class ThreadsStateOverTimeGui
      extends AbstractGraphPanelVisualizer
{
   //private static final Logger log = LoggingManager.getLoggerForClass();
   /**
    *
    */
   public ThreadsStateOverTimeGui()
   {
      super();
      graphPanel.getGraphObject().setxAxisLabelRenderer(new DateTimeRenderer("HH:mm:ss"));
      graphPanel.getGraphObject().setDrawFinalZeroingLines(true);
   }

   private synchronized AbstractGraphRow getNewRow(String label)
   {
      AbstractGraphRow row = null;
      if (!model.containsKey(label))
      {
         row = new GraphRowAverages();
         row.setLabel(label);
         row.setColor(colors.getNextColor());
         row.setDrawLine(true);
         row.setMarkerSize(AbstractGraphRow.MARKER_SIZE_SMALL);
         model.put(label, row);
         graphPanel.addRow(row);
      }
      else
      {
         row = model.get(label);
      }

      return row;
   }

   private void addThreadGroupRecord(String threadGroupName, long time, int numThreads)
   {
      AbstractGraphRow row = model.get(threadGroupName);
      if (row == null)
      {
         row = getNewRow(threadGroupName);
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
      return "Active Threads Over Time";
   }

   public void add(SampleResult res)
   {
      String threadName = res.getThreadName();
      threadName = threadName.lastIndexOf(" ") >= 0 ? threadName.substring(0, threadName.lastIndexOf(" ")) : threadName;

      //addThreadGroupRecord(threadName, res.getStartTime() - res.getStartTime() % delay, res.getGroupThreads());
      addThreadGroupRecord(threadName, res.getEndTime() - res.getEndTime() % getGranulation(), res.getGroupThreads());
      updateGui(null);
   }

    @Override
    protected JSettingsPanel getSettingsPanel()
    {
        return new JSettingsPanel(this, true, true, false, true, true);
    }
}
