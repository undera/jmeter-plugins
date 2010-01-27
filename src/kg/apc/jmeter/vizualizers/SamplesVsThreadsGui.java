package kg.apc.jmeter.vizualizers;

import java.awt.Color;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class SamplesVsThreadsGui
     extends AbstractGraphPanelVisualizer
{
   private static final Logger log = LoggingManager.getLoggerForClass();

   public SamplesVsThreadsGui()
   {
      super();
      graphPanel.getGraphObject().setDrawCurrentX(true);
   }

   public String getLabelResource()
   {
      return this.getClass().getSimpleName();
   }

   @Override
   public String getStaticLabel()
   {
      return "Samples vs Threads";
   }

   public void add(SampleResult res)
   {
      String label = res.getSampleLabel();
      String averageLabel = "Average " + res.getSampleLabel();
      GraphRowAverages row;
      GraphRowOverallAverages avgRow;
      if (!model.containsKey(label))
      {
         final Color nextColor = colors.getNextColor();
         row = getNewRow(label, nextColor);
         avgRow = getNewAveragesRow(averageLabel, nextColor);
      }
      else
      {
         row = (GraphRowAverages) model.get(label);
         avgRow = (GraphRowOverallAverages) model.get(averageLabel);
      }

      row.add(res.getAllThreads(), res.getTime());
      //avgRow.add(res.getAllThreads(), res.getTime());

      graphPanel.getGraphObject().setCurrentX(res.getAllThreads());
      updateGui(null);
   }

   private GraphRowOverallAverages getNewAveragesRow(String averageLabel, final Color nextColor)
   {
      GraphRowOverallAverages avgRow = new GraphRowOverallAverages();
      avgRow.setLabel(averageLabel);
      avgRow.setColor(nextColor);
      avgRow.setMarkerSize(AbstractGraphRow.MARKER_SIZE_BIG);
      avgRow.setDrawValueLabel(true);
      avgRow.setShowInLegend(false);
      model.put(averageLabel, avgRow);
      return avgRow;
   }

   private GraphRowAverages getNewRow(String label, final Color nextColor)
   {
      GraphRowAverages row = new GraphRowAverages();
      row.setLabel(label);
      row.setColor(nextColor);
      row.setDrawLine(true);
      row.setMarkerSize(AbstractGraphRow.MARKER_SIZE_SMALL);
      model.put(label, row);
      return row;
   }
}
