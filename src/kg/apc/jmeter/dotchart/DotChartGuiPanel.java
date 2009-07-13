// TODO сделать вертикальный лимит отображения
// TODO сделать отключение отображения столбцов
package kg.apc.jmeter.dotchart;

import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import org.apache.jmeter.gui.util.HorizontalPanel;

public class DotChartGuiPanel
     extends HorizontalPanel
     implements ItemListener
{
   private final DotChart chart;
   private final JCheckBox drawSamples;
   private final JCheckBox drawThreadAverages;
   private final JCheckBox drawAverages;

   public DotChartGuiPanel(DotChart achart)
   {
      super();

      chart = achart;

      setLayout(new FlowLayout());
      JLabel selectGraphsLabel = new JLabel("Draw: ");
      drawSamples = createChooseCheckBox("Response Times", chart.isDrawSamples());
      drawThreadAverages = createChooseCheckBox("Response Time Averages vs Active Threads", chart.isDrawThreadAverages());
      drawAverages = createChooseCheckBox("Overall Response Time Averages", chart.isDrawAverages());

      add(selectGraphsLabel);
      add(drawSamples);
      add(drawThreadAverages);
      add(drawAverages);
   }

   private JCheckBox createChooseCheckBox(String labelResourceName, boolean state)
   {
      JCheckBox checkBox = new JCheckBox(labelResourceName);
      checkBox.setSelected(state);
      checkBox.addItemListener(this);
      return checkBox;
   }

   public void itemStateChanged(ItemEvent e)
   {
      if (e.getItem() == drawSamples)
         chart.setDrawSamples(e.getStateChange() == ItemEvent.SELECTED);

      if (e.getItem() == drawThreadAverages)
         chart.setDrawThreadAverages(e.getStateChange() == ItemEvent.SELECTED);

      if (e.getItem() == drawAverages)
         chart.setDrawAverages(e.getStateChange() == ItemEvent.SELECTED);
   }
}
