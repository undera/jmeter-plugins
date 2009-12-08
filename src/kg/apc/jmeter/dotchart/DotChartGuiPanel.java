// TODO сделать вертикальный лимит отображения
// TODO сделать отключение отображения столбцов
package kg.apc.jmeter.dotchart;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DotChartGuiPanel
   extends JPanel
   implements ItemListener, KeyListener
{

   private final DotChart chart;
   private final JCheckBox drawSamples;
   private final JCheckBox drawThreadAverages;
   private final JCheckBox drawAverages;
   private final JTextField yAxisLimit;

   public DotChartGuiPanel(DotChart achart)
   {
      super();

      chart = achart;

      JLabel selectGraphsLabel = new JLabel("Draw: ");
      drawSamples = createChooseCheckBox("Response Times", chart.isDrawSamples());
      drawThreadAverages = createChooseCheckBox("Response Time Averages vs Active Threads", chart.isDrawThreadAverages());
      drawAverages = createChooseCheckBox("Overall Response Time Averages", chart.isDrawAverages());

      JLabel yAxisLimitLabel = new JLabel("  Y axis limit: ");
      yAxisLimit = createTextField(5);

      add(selectGraphsLabel, BorderLayout.CENTER);
      add(drawSamples, BorderLayout.CENTER);
      add(drawThreadAverages, BorderLayout.CENTER);
      add(drawAverages, BorderLayout.CENTER);

      add(yAxisLimitLabel, BorderLayout.CENTER);
      add(yAxisLimit, BorderLayout.CENTER);

      validate();
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
      {
         chart.setDrawSamples(e.getStateChange() == ItemEvent.SELECTED);
      }

      if (e.getItem() == drawThreadAverages)
      {
         chart.setDrawThreadAverages(e.getStateChange() == ItemEvent.SELECTED);
      }

      if (e.getItem() == drawAverages)
      {
         chart.setDrawAverages(e.getStateChange() == ItemEvent.SELECTED);
      }
   }

   private JTextField createTextField(int initialSize)
   {
      JTextField result = new JTextField(initialSize);
      result.addKeyListener(this);
      return result;
   }

   public void keyTyped(KeyEvent event)
   {
      if (event.getComponent() == yAxisLimit)
      {
         int newLimit;
         try
         {
            newLimit = Integer.parseInt(yAxisLimit.getText());
         }
         catch (NumberFormatException e)
         {
            newLimit = 0;
         }

         chart.setYAxisLimit(newLimit);
         chart.repaint();
      }
   }

   public void keyPressed(KeyEvent e)
   {
   }

   public void keyReleased(KeyEvent e)
   {
   }
}
