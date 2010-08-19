// TODO: сделать чтобы при переходе между контролами обновлялся график
package kg.apc.jmeter.threads;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.vizualizers.DateTimeRenderer;
import kg.apc.jmeter.charting.AbstractGraphPanelChartElement;
import kg.apc.jmeter.charting.GraphPanelChart;
import kg.apc.jmeter.charting.GraphRowSumValues;

import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.threads.AbstractThreadGroup;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.threads.gui.AbstractThreadGroupGui;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author apc
 */
public class SteppingThreadGroupGui
      extends AbstractThreadGroupGui
{
   /**
    *
    */
   private static final Logger log = LoggingManager.getLoggerForClass();
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

   /**
    *
    */
   public SteppingThreadGroupGui()
   {
      super();
      init();
   }

   /**
    *
    */
   protected final void init()
   {
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
      JPanel panel = new JPanel(new GridLayout(0, 5, 5, 5));
      panel.setBorder(BorderFactory.createTitledBorder("Threads Scheduling Parameters"));

      panel.add(new JLabel());
      panel.add(new JLabel("This group will start", JLabel.RIGHT));
      totalThreads = new JTextField("1", 5);
      panel.add(totalThreads);
      panel.add(new JLabel("threads:", JLabel.LEFT));
      panel.add(new JLabel());

      panel.add(new JLabel("First, wait for", JLabel.RIGHT));
      initialDelay = new JTextField("0", 5);
      panel.add(initialDelay);
      panel.add(new JLabel("seconds.", JLabel.LEFT));
      panel.add(new JLabel());
      panel.add(new JLabel());

      panel.add(new JLabel("Then start", JLabel.RIGHT));
      incUserCount = new JTextField("1", 5);
      panel.add(incUserCount);
      panel.add(new JLabel("threads every", JLabel.CENTER));
      incUserPeriod = new JTextField("1", 5);
      panel.add(incUserPeriod);
      panel.add(new JLabel("seconds.", JLabel.LEFT));

      panel.add(new JLabel("Then work for", JLabel.RIGHT));
      flightTime = new JTextField("60", 5);
      panel.add(flightTime);
      panel.add(new JLabel("seconds.", JLabel.LEFT));
      panel.add(new JLabel());
      panel.add(new JLabel());

      panel.add(new JLabel("Finally, stop", JLabel.RIGHT));
      decUserCount = new JTextField("1", 5);
      panel.add(decUserCount);
      panel.add(new JLabel("threads every", JLabel.CENTER));
      decUserPeriod = new JTextField("1", 5);
      panel.add(decUserPeriod);
      panel.add(new JLabel("seconds.", JLabel.LEFT));

      registerJTextfieldForGraphRefresh(totalThreads);
      registerJTextfieldForGraphRefresh(initialDelay);
      registerJTextfieldForGraphRefresh(incUserCount);
      registerJTextfieldForGraphRefresh(incUserPeriod);
      registerJTextfieldForGraphRefresh(flightTime);
      registerJTextfieldForGraphRefresh(decUserCount);
      registerJTextfieldForGraphRefresh(decUserPeriod);

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

   private void updateChart()
   {
      log.debug("Updating chart...");
      TestElement tg = createTestElement();
      updateChart((SteppingThreadGroup) tg);
   }

   private void updateChart(SteppingThreadGroup tg)
   {
      model.clear();

      GraphRowSumValues row = new GraphRowSumValues();
      row.setColor(Color.RED);
      row.setDrawLine(true);
      row.setMarkerSize(AbstractGraphRow.MARKER_SIZE_NONE);
      row.setDrawThickLines(true);

      final HashTree hashTree = new HashTree();
      hashTree.add(new LoopController());
      JMeterThread thread = new JMeterThread(hashTree, null, null);

      // test start
      row.add(System.currentTimeMillis(), 0);
      row.add(System.currentTimeMillis() + tg.getThreadGroupDelay(), 0);

      int numThreads = tg.getNumThreads();

      // users in
      for (int n = 0; n < numThreads; n++)
      {
         thread.setThreadNum(n);
         tg.scheduleThread(thread);
         row.add(thread.getStartTime() - 1, 0);
         row.add(thread.getStartTime(), 1);
      }

      // users out
      for (int n = 0; n < numThreads; n++)
      {
         thread.setThreadNum(n);
         tg.scheduleThread(thread);
         row.add(thread.getEndTime() - 1, 0);
         row.add(thread.getEndTime(), -1);
      }

      calculateRowMaxY(row);

      model.put("Expected parallel users count", row);
      chart.repaint();
   }

   private void calculateRowMaxY(GraphRowSumValues row)
   {
      double max = Double.MIN_VALUE;
      Iterator<Entry<Long, AbstractGraphPanelChartElement>> it = row.iterator();
      while (it.hasNext())
      {
         double el = it.next().getValue().getValue();
         if (el > max)
         {
            max = el;
         }
      }
      row.setMaxY(max);
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

   private void registerJTextfieldForGraphRefresh(JTextField tf)
   {
      tf.getDocument().addDocumentListener(new DocumentListener()
      {
         @Override
         public void changedUpdate(DocumentEvent arg0)
         {
            updateChart();
         }

         @Override
         public void insertUpdate(DocumentEvent arg0)
         {
            updateChart();
         }

         @Override
         public void removeUpdate(DocumentEvent arg0)
         {
            updateChart();
         }
      });
   }
}
