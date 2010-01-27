package kg.apc.jmeter.vizualizers;

import org.apache.jmeter.samplers.SampleResult;

public class ThreadsStateOverTimeGui
     extends AbstractGraphPanelVisualizer
{
   public ThreadsStateOverTimeGui()
   {
      super();
      graphPanel.getGraphObject().setxAxisLabelRenderer(new DateTimeRenderer("HH:mm:ss"));
      graphPanel.getGraphObject().setDrawFinalZeroingLines(true);
   }

   private void addThreadGroupRecord(String threadGroupName, long time, int numThreads)
   {
      GraphPanelChartRow row;
      if (!model.containsKey(threadGroupName))
      {
         row = new GraphPanelChartRow(threadGroupName, colors.getNextColor(), true, GraphPanelChartRow.MARKER_SIZE_SMALL);
         model.put(threadGroupName, row);
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
      threadName = threadName.substring(0, threadName.lastIndexOf(" "));

      addThreadGroupRecord(threadName, res.getStartTime() - res.getStartTime() % delay, res.getGroupThreads());
      addThreadGroupRecord(threadName, res.getEndTime() - res.getEndTime() % delay, res.getGroupThreads());
      updateGui(null);
   }
}
