// TODO: сделать чтобы при переходе между контролами обновлялся график
package kg.apc.jmeter.threads;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.charting.GraphPanelChart;
import kg.apc.jmeter.charting.GraphRowSumValues;
import kg.apc.jmeter.charting.ColorsDispatcher;
import kg.apc.jmeter.charting.DateTimeRenderer;

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
      initGui();
   }

   /**
    *
    */
   protected final void init()
   {
      JPanel containerPanel = new JPanel(new BorderLayout());

      containerPanel.add(createParamsPanel(), BorderLayout.NORTH);

      chart = new GraphPanelChart(false);
      model = new ConcurrentHashMap<String, AbstractGraphRow>();
      chart.setRows(model);
      chart.setDrawFinalZeroingLines(true);

      chart.setxAxisLabel("Elapsed time");
      chart.setyAxisLabel("Number of active threads");

      containerPanel.add(chart, BorderLayout.CENTER);

      add(containerPanel, BorderLayout.CENTER);

      // this magic LoopPanel provides functionality for thread loops
      // TODO: find a way without magic
      createControllerPanel();
   }

    @Override
    public void clearGui(){
        super.clearGui();
        initGui();
    }

   // Initialise the gui field values
    private void initGui(){
        totalThreads.setText("100");
        initialDelay.setText("0");
        incUserCount.setText("10");
        incUserPeriod.setText("30");
        flightTime.setText("60");
        decUserCount.setText("5");
        decUserPeriod.setText("1");
    }

   private JPanel createParamsPanel()
   {
      JPanel panel = new JPanel(new GridLayout(0, 5, 5, 5));
      panel.setBorder(BorderFactory.createTitledBorder("Threads Scheduling Parameters"));

      panel.add(new JLabel());
      panel.add(new JLabel("This group will start", JLabel.RIGHT));
      totalThreads = new JTextField(5);
      panel.add(totalThreads);
      panel.add(new JLabel("threads:", JLabel.LEFT));
      panel.add(new JLabel());

      panel.add(new JLabel("First, wait for", JLabel.RIGHT));
      initialDelay = new JTextField(5);
      panel.add(initialDelay);
      panel.add(new JLabel("seconds.", JLabel.LEFT));
      panel.add(new JLabel());
      panel.add(new JLabel());

      panel.add(new JLabel("Then start", JLabel.RIGHT));
      incUserCount = new JTextField(5);
      panel.add(incUserCount);
      panel.add(new JLabel("threads every", JLabel.CENTER));
      incUserPeriod = new JTextField(5);
      panel.add(incUserPeriod);
      panel.add(new JLabel("seconds.", JLabel.LEFT));

      panel.add(new JLabel("Then work for", JLabel.RIGHT));
      flightTime = new JTextField(5);
      panel.add(flightTime);
      panel.add(new JLabel("seconds.", JLabel.LEFT));
      panel.add(new JLabel());
      panel.add(new JLabel());

      panel.add(new JLabel("Finally, stop", JLabel.RIGHT));
      decUserCount = new JTextField(5);
      panel.add(decUserCount);
      panel.add(new JLabel("threads every", JLabel.CENTER));
      decUserPeriod = new JTextField(5);
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
      totalThreads.setText(tg.getPropertyAsString(SteppingThreadGroup.NUM_THREADS));
      initialDelay.setText(tg.getPropertyAsString(SteppingThreadGroup.THREAD_GROUP_DELAY));
      incUserCount.setText(tg.getPropertyAsString(SteppingThreadGroup.INC_USER_COUNT));
      incUserPeriod.setText(tg.getPropertyAsString(SteppingThreadGroup.INC_USER_PERIOD));
      decUserCount.setText(tg.getPropertyAsString(SteppingThreadGroup.DEC_USER_COUNT));
      decUserPeriod.setText(tg.getPropertyAsString(SteppingThreadGroup.DEC_USER_PERIOD));
      flightTime.setText(tg.getPropertyAsString(SteppingThreadGroup.FLIGHT_TIME));

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
      row.setColor(ColorsDispatcher.RED);
      row.setDrawLine(true);
      row.setMarkerSize(AbstractGraphRow.MARKER_SIZE_NONE);
      row.setDrawThickLines(true);

      final HashTree hashTree = new HashTree();
      hashTree.add(new LoopController());
      JMeterThread thread = new JMeterThread(hashTree, null, null);

      long now = System.currentTimeMillis();

      // test start
      chart.setxAxisLabelRenderer(new DateTimeRenderer(DateTimeRenderer.HHMMSS, now-1));
      row.add(now, 0);
      row.add(now + tg.getThreadGroupDelay(), 0);

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

   private void registerJTextfieldForGraphRefresh(final JTextField tf)
   {
      tf.getDocument().addDocumentListener(new DocumentListener()
      {
         @Override
         public void changedUpdate(DocumentEvent arg0)
         {
            if(tf.hasFocus()) updateChart();
         }

         @Override
         public void insertUpdate(DocumentEvent arg0)
         {
            if(tf.hasFocus()) updateChart();
         }

         @Override
         public void removeUpdate(DocumentEvent arg0)
         {
            if(tf.hasFocus()) updateChart();
         }
      });
   }
}
