// TODO сделать вертикальный лимит отображения
// TODO сделать отключение отображения столбцов
package kg.apc.jmeter.dotchart;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class DotChartGuiPanel
   extends JPanel
   implements ItemListener, KeyListener, ActionListener
{

   private final DotChart chart;
   private JCheckBox drawSamples;
   private JCheckBox drawThreadAverages;
   private JCheckBox drawAverages;
   private JTextField yAxisLimit;
   private JComboBox displayMode;

   public DotChartGuiPanel(DotChart achart)
   {
      super();

      chart = achart;

      add(getModePanel());
      add(getSelectionPanel());
      add(getLimitPanel());
   }

   private JPanel getModePanel()
   {
      JPanel modePanel = new JPanel();
      modePanel.setBorder(new TitledBorder("Graph Mode"));
      Vector elements = new Vector(2);
      elements.addElement("Response Times");
      elements.addElement("Throughput");
      displayMode = new JComboBox(elements);
      displayMode.addActionListener(this);
      modePanel.add(displayMode);
      return modePanel;
   }

   private JPanel getLimitPanel()
   {
      JPanel axisLimitPanel = new JPanel();
      axisLimitPanel.setBorder(new TitledBorder("Y axis limit"));
      yAxisLimit = createTextField(5);
      axisLimitPanel.add(yAxisLimit);
      return axisLimitPanel;
   }

   private JPanel getSelectionPanel()
   {
      drawSamples = createChooseCheckBox("Values", chart.isDrawSamples());
      drawThreadAverages = createChooseCheckBox("Averages vs Active Threads", chart.isDrawThreadAverages());
      drawAverages = createChooseCheckBox("Overall Averages", chart.isDrawAverages());

      JPanel drawSelectionPanel = new JPanel();
      drawSelectionPanel.setBorder(new TitledBorder("Draw"));
      drawSelectionPanel.add(drawSamples);
      drawSelectionPanel.add(drawThreadAverages);
      drawSelectionPanel.add(drawAverages);
      return drawSelectionPanel;
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

   public void actionPerformed(ActionEvent e)
   {
      if (e.getSource() == displayMode)
      {
         switch (displayMode.getSelectedIndex())
         {
            case 0:
               chart.setDisplayMode(DotChart.DM_TIMES);
               break;
            case 1:
               chart.setDisplayMode(DotChart.DM_THROUGHPUT);
               break;
         }

         chart.repaint();
      }

   }
}
