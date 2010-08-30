package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.charting.GraphRowSumValues;
import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author Stephane Hoblingre
 */
public class TotalTransactionsPerSecondGui
      extends AbstractGraphPanelVisualizer
{
   //private static final Logger log = LoggingManager.getLoggerForClass();
   /**
    *
    */

   public TotalTransactionsPerSecondGui()
   {
      super();
      graphPanel.getGraphObject().setxAxisLabelRenderer(new DateTimeRenderer(
            "HH:mm:ss"));
      graphPanel.getGraphObject().setDrawFinalZeroingLines(false);
      delay = 1000;
   }

   private void addTransaction(String threadGroupName, long time, int count)
   {
      AbstractGraphRow row = model.get(threadGroupName);
      if (row == null)
      {
         row = new GraphRowSumValues(false);
         row.setLabel(threadGroupName);
         row.setColor(colors.getNextColor());
         row.setDrawLine(true);
         row.setMarkerSize(AbstractGraphRow.MARKER_SIZE_SMALL);
         model.put(threadGroupName, row);
         graphPanel.addRow(row);
      }

      row.add(time, count);
   }

   public String getLabelResource()
   {
      return this.getClass().getSimpleName();
   }

   @Override
   public String getStaticLabel()
   {
      return "Total Transactions Per Second";
   }

   public void add(SampleResult res)
   {
       //always add 0 failed transactions
       if(res.isSuccessful()) {
        addTransaction("Total Failed Transactions", res.getEndTime() - res.getEndTime() % delay, 0);
        addTransaction("Total Successful Transactions", res.getEndTime() - res.getEndTime() % delay, 1);
       } else {
        addTransaction("Total Failed Transactions", res.getEndTime() - res.getEndTime() % delay, 1);
       }
      updateGui(null);
   }
}
