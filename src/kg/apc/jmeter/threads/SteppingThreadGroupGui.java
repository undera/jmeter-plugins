// TODO: fix start time when test was started
package kg.apc.jmeter.threads;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.vizualizers.DateTimeRenderer;
import kg.apc.jmeter.charting.GraphPanelChart;
import kg.apc.jmeter.charting.GraphRowExactValues;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.threads.AbstractThreadGroup;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.threads.gui.AbstractThreadGroupGui;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.gui.layout.VerticalLayout;

public class SteppingThreadGroupGui
      extends AbstractThreadGroupGui
{
   protected ConcurrentHashMap<String, AbstractGraphRow> model;
   private GraphPanelChart chart;
   private JTextField initialDelay;
   private JTextField incUserCount;
   private JTextField incUserPeriod;
   private JTextField flightTime;
   private JTextField decUserCount;
   private JTextField decUserPeriod;
   private JTextField totalThreads;
   private LoopControlPanel loopPanel;

   public SteppingThreadGroupGui()
   {
      super();
      init();
   }

   @Override
   protected void init()
   {
      super.init();

      JPanel containerPanel = new JPanel(new BorderLayout());

      containerPanel.add(createParamsPanel(), BorderLayout.NORTH);

      chart = new GraphPanelChart();
      model = new ConcurrentHashMap<String, AbstractGraphRow>();
      chart.setRows(model);
      chart.setDrawFinalZeroingLines(true);
      chart.setxAxisLabelRenderer(new DateTimeRenderer("HH:mm:ss"));
      containerPanel.add(chart, BorderLayout.CENTER);

      add(containerPanel, BorderLayout.CENTER);

      // this magic LoopPanel provides functionality for thread loops
      // TODO: find a way without magic
      createControllerPanel();
   }

   private JPanel createParamsPanel()
   {
      // TODO arrange controls in better way
      JPanel panel = new JPanel(new VerticalLayout(0, VerticalLayout.LEFT));
      panel.setBorder(BorderFactory.createTitledBorder("Threads Scheduling Parameters"));

      JPanel panel0 = new JPanel();
      panel0.add(new JLabel("This group will start ", JLabel.LEFT));
      totalThreads = new JTextField("1", 5);
      panel0.add(totalThreads);
      panel0.add(new JLabel(" threads by following schedule: ", JLabel.RIGHT));
      panel.add(panel0);

      final FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT, 50, 0);

      JPanel panel1 = new JPanel(flowLayout);
      panel1.add(new JLabel("First, wait for ", JLabel.LEFT));
      initialDelay = new JTextField("0", 5);
      panel1.add(initialDelay);
      panel1.add(new JLabel(" seconds.", JLabel.RIGHT));
      panel.add(panel1);

      JPanel panel2 = new JPanel(flowLayout);
      panel2.add(new JLabel("Then start ", JLabel.LEFT));
      incUserCount = new JTextField("1", 5);
      panel2.add(incUserCount);
      panel2.add(new JLabel("threads every ", JLabel.RIGHT));
      incUserPeriod = new JTextField("1", 5);
      panel2.add(incUserPeriod);
      panel2.add(new JLabel(" seconds.", JLabel.RIGHT));
      panel.add(panel2);


      JPanel panel3 = new JPanel(flowLayout);
      panel3.add(new JLabel("Then work for ", JLabel.LEFT));
      flightTime = new JTextField("60", 5);
      panel3.add(flightTime);
      panel3.add(new JLabel(" seconds. ", JLabel.RIGHT));
      panel.add(panel3);

      JPanel panel4 = new JPanel(flowLayout);
      panel4.add(new JLabel("Finally, stop ", JLabel.LEFT));
      decUserCount = new JTextField("1", 5);
      panel4.add(decUserCount);
      panel4.add(new JLabel(" threads every ", JLabel.RIGHT));
      decUserPeriod = new JTextField("1", 5);
      panel4.add(decUserPeriod);
      panel4.add(new JLabel(" seconds.", JLabel.RIGHT));
      panel.add(panel4);

      return panel;
   }

   public String getLabelResource()
   {
      return this.getClass().getSimpleName();
   }

   @Override
   public String getStaticLabel()
   {
      return "Stepping Thread Group";
   }

   public TestElement createTestElement()
   {
      SteppingThreadGroup tg = new SteppingThreadGroup();
      modifyTestElement(tg);
      return tg;
   }

   public void modifyTestElement(TestElement tg)
   {
      super.configureTestElement(tg);

      tg.setProperty(SteppingThreadGroup.NUM_THREADS, totalThreads.getText());
      tg.setProperty(SteppingThreadGroup.THREAD_GROUP_DELAY, initialDelay.getText());
      tg.setProperty(SteppingThreadGroup.INC_USER_COUNT, incUserCount.getText());
      tg.setProperty(SteppingThreadGroup.INC_USER_PERIOD, incUserPeriod.getText());
      tg.setProperty(SteppingThreadGroup.DEC_USER_COUNT, decUserCount.getText());
      tg.setProperty(SteppingThreadGroup.DEC_USER_PERIOD, decUserPeriod.getText());
      tg.setProperty(SteppingThreadGroup.FLIGHT_TIME, flightTime.getText());
      if (tg instanceof SteppingThreadGroup)
      {
         updateChart((SteppingThreadGroup) tg);
         ((AbstractThreadGroup) tg).setSamplerController((LoopController) loopPanel.createTestElement());
      }
   }

   @Override
   public void configure(TestElement tg)
   {
      super.configure(tg);
      totalThreads.setText(Integer.toString(tg.getPropertyAsInt(SteppingThreadGroup.NUM_THREADS)));
      initialDelay.setText(Integer.toString(tg.getPropertyAsInt(SteppingThreadGroup.THREAD_GROUP_DELAY)));
      incUserCount.setText(Integer.toString(tg.getPropertyAsInt(SteppingThreadGroup.INC_USER_COUNT)));
      incUserPeriod.setText(Integer.toString(tg.getPropertyAsInt(SteppingThreadGroup.INC_USER_PERIOD)));
      decUserCount.setText(Integer.toString(tg.getPropertyAsInt(SteppingThreadGroup.DEC_USER_COUNT)));
      decUserPeriod.setText(Integer.toString(tg.getPropertyAsInt(SteppingThreadGroup.DEC_USER_PERIOD)));
      flightTime.setText(Integer.toString(tg.getPropertyAsInt(SteppingThreadGroup.FLIGHT_TIME)));

      TestElement te = (TestElement) tg.getProperty(AbstractThreadGroup.MAIN_CONTROLLER).getObjectValue();
      if (te != null)
      {
         loopPanel.configure(te);
      }
   }

   private void updateChart(SteppingThreadGroup tg)
   {
      model.clear();
      GraphRowExactValues row = new GraphRowExactValues();
      row.setColor(Color.RED);
      row.setDrawLine(true);
      row.setMarkerSize(AbstractGraphRow.MARKER_SIZE_SMALL);

      final HashTree hashTree = new HashTree();
      hashTree.add(new LoopController());
      JMeterThread thread = new JMeterThread(hashTree, null, null);

      // test start
      row.add(System.currentTimeMillis(), 0);
      row.add(System.currentTimeMillis() + tg.getThreadGroupDelay(), 0);

      // users in
      for (int n = 0; n < tg.getNumThreads(); n++)
      {
         thread.setThreadNum(n);
         tg.scheduleThread(thread);
         row.add(thread.getStartTime(), n + 1);
      }

      // users out
      for (int n = 0; n < tg.getNumThreads(); n++)
      {
         thread.setThreadNum(n);
         tg.scheduleThread(thread);
         row.add(thread.getEndTime(), tg.getNumThreads() - n);
      }

      // final point
      row.add(thread.getEndTime() + tg.getOutUserPeriod() * 1000, 0);

      model.put("Expected parallel users count", row);
      chart.repaint();
   }

   private JPanel createControllerPanel()
   {
      loopPanel = new LoopControlPanel(false);
      LoopController looper = (LoopController) loopPanel.createTestElement();
      looper.setLoops(-1);
      looper.setContinueForever(true);
      loopPanel.configure(looper);
      return loopPanel;
   }
}
