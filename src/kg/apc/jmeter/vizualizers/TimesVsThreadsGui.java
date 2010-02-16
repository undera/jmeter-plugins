package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.charting.GraphRowAverages;
import kg.apc.jmeter.charting.GraphRowOverallAverages;
import kg.apc.jmeter.charting.AbstractGraphRow;
import java.awt.Color;
import org.apache.jmeter.samplers.SampleResult;

public class TimesVsThreadsGui
     extends AbstractGraphPanelVisualizer
{
   public TimesVsThreadsGui()
   {
      super();
      graphPanel.getGraphObject().setDrawCurrentX(true);
      graphPanel.getGraphObject().setForcedMinX(0);
   }

   public String getLabelResource()
   {
      return this.getClass().getSimpleName();
   }

   @Override
   public String getStaticLabel()
   {
      return "Response Times vs Threads";
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
         graphPanel.addRow(row);
         graphPanel.addRow(avgRow);
      }
      else
      {
         row = (GraphRowAverages) model.get(label);
         avgRow = (GraphRowOverallAverages) model.get(averageLabel);
      }

      row.add(res.getAllThreads(), res.getTime());
      avgRow.add(res.getAllThreads(), res.getTime());

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
