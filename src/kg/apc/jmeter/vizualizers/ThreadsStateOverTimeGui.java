package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.charting.AbstractGraphRow;
import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author apc
 */
public class ThreadsStateOverTimeGui
      extends AbstractOverTimeVisualizer
{
   //private static final Logger log = LoggingManager.getLoggerForClass();
   /**
    *
    */
   public ThreadsStateOverTimeGui()
   {
      super();
      graphPanel.getGraphObject().setyAxisLabel("Number of active threads");
   }

   private void addThreadGroupRecord(String threadGroupName, long time, int numThreads)
   {
      AbstractGraphRow row = model.get(threadGroupName);
      if (row == null)
      {
        row = getNewRow(model, AbstractGraphRow.ROW_AVERAGES, threadGroupName, AbstractGraphRow.MARKER_SIZE_SMALL, false, false, false, true, true);
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

   @Override
   public void add(SampleResult res)
   {
        super.add(res);
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
