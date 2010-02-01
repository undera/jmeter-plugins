package kg.apc.jmeter.vizualizers;

import org.apache.jmeter.samplers.SampleResult;

public class ThreadsStateOverTimeGui
     extends AbstractGraphPanelVisualizer
{
   //private static final Logger log = LoggingManager.getLoggerForClass();
   public ThreadsStateOverTimeGui()
   {
      super();
      graphPanel.getGraphObject().setxAxisLabelRenderer(new DateTimeRenderer("HH:mm:ss"));
      graphPanel.getGraphObject().setDrawFinalZeroingLines(true);
   }

   private void addThreadGroupRecord(String threadGroupName, long time, int numThreads)
   {
      AbstractGraphRow row;
      if (!model.containsKey(threadGroupName))
      {
         row = new GraphRowAverages();
         row.setLabel(threadGroupName);
         row.setColor(colors.getNextColor());
         row.setDrawLine(true);
         row.setMarkerSize(AbstractGraphRow.MARKER_SIZE_SMALL);
         model.put(threadGroupName, row);
         graphPanel.addRow(row);
      }
      else
      {
         row = model.get(threadGroupName);
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
      addThreadGroupRecord(threadName, res.getEndTime() - res.getEndTime() % delay, res.getGroupThreads());
      updateGui(null);
   }
}
