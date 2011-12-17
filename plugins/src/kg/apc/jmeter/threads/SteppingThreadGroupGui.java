package kg.apc.jmeter.threads;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import kg.apc.jmeter.JMeterPluginsUtils;

import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.GraphPanelChart;
import kg.apc.charting.rows.GraphRowSumValues;
import kg.apc.charting.DateTimeRenderer;
import kg.apc.jmeter.gui.GuiBuilderHelper;

import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.engine.util.CompoundVariable;
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
        extends AbstractThreadGroupGui {

    private class FieldChangesListener implements DocumentListener {

        private final JTextField tf;

        public FieldChangesListener(JTextField field) {
            tf = field;
        }

        private void update() {
            refreshPreview();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
           if(tf.hasFocus()) update();
        }
        
        @Override
        public void removeUpdate(DocumentEvent e) {
           if(tf.hasFocus()) update();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
           if(tf.hasFocus()) update();
        }
    }

    public static final String WIKIPAGE = "SteppingThreadGroup";
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
    private JTextField rampUp;

    /**
     *
     */
    public SteppingThreadGroupGui() {
        super();
        init();
        initGui();
    }

    protected final void init() {
        JMeterPluginsUtils.addHelpLinkToPanel(this, WIKIPAGE);
        JPanel containerPanel = new JPanel(new BorderLayout());

        containerPanel.add(createParamsPanel(), BorderLayout.NORTH);

        chart = new GraphPanelChart(false, true);
        model = new ConcurrentHashMap<String, AbstractGraphRow>();
        chart.setRows(model);
        chart.getChartSettings().setDrawFinalZeroingLines(true);

        chart.setxAxisLabel("Elapsed time");
        chart.setYAxisLabel("Number of active threads");

        chart.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        containerPanel.add(GuiBuilderHelper.getComponentWithMargin(chart, 2, 2, 0, 2), BorderLayout.CENTER);

        add(containerPanel, BorderLayout.CENTER);

        // this magic LoopPanel provides functionality for thread loops
        createControllerPanel();
    }

    @Override
    public void clearGui() {
        super.clearGui();
        initGui();
    }

    // Initialise the gui field values
    private void initGui() {
        totalThreads.setText("100");
        initialDelay.setText("0");
        incUserCount.setText("10");
        incUserPeriod.setText("30");
        flightTime.setText("60");
        decUserCount.setText("5");
        decUserPeriod.setText("1");
        rampUp.setText("5");
    }

    private JPanel createParamsPanel() {
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
        panel.add(new JLabel("seconds, ", JLabel.LEFT));

        panel.add(new JLabel());
        panel.add(new JLabel("using ramp-up", JLabel.RIGHT));
        rampUp = new JTextField(5);
        panel.add(rampUp);
        panel.add(new JLabel("seconds.", JLabel.LEFT));
        panel.add(new JLabel());

        panel.add(new JLabel("Then hold load for", JLabel.RIGHT));
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
        registerJTextfieldForGraphRefresh(rampUp);

        return panel;
    }

    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Stepping Thread Group");
    }

    @Override
    public TestElement createTestElement() {
        SteppingThreadGroup tg = new SteppingThreadGroup();
        modifyTestElement(tg);
        tg.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return tg;
    }

    private void refreshPreview() {
        SteppingThreadGroup tgForPreview = new SteppingThreadGroup();
        tgForPreview.setNumThreads(new CompoundVariable(totalThreads.getText()).execute());
        tgForPreview.setThreadGroupDelay(new CompoundVariable(initialDelay.getText()).execute());
        tgForPreview.setInUserCount(new CompoundVariable(incUserCount.getText()).execute());
        tgForPreview.setInUserPeriod(new CompoundVariable(incUserPeriod.getText()).execute());
        tgForPreview.setOutUserCount(new CompoundVariable(decUserCount.getText()).execute());
        tgForPreview.setOutUserPeriod(new CompoundVariable(decUserPeriod.getText()).execute());
        tgForPreview.setFlightTime(new CompoundVariable(flightTime.getText()).execute());
        tgForPreview.setRampUp(new CompoundVariable(rampUp.getText()).execute());

        if(tgForPreview.getInUserCountAsInt() == 0) tgForPreview.setInUserCount(new CompoundVariable(totalThreads.getText()).execute());
        if(tgForPreview.getOutUserCountAsInt() == 0) tgForPreview.setOutUserCount(new CompoundVariable(totalThreads.getText()).execute());

        updateChart(tgForPreview);
    }

    @Override
    public void modifyTestElement(TestElement te) {
        super.configureTestElement(te);

        if (te instanceof SteppingThreadGroup) {
            SteppingThreadGroup tg = (SteppingThreadGroup) te;
            tg.setProperty(SteppingThreadGroup.NUM_THREADS, totalThreads.getText());
            tg.setThreadGroupDelay(initialDelay.getText());
            tg.setInUserCount(incUserCount.getText());
            tg.setInUserPeriod(incUserPeriod.getText());
            tg.setOutUserCount(decUserCount.getText());
            tg.setOutUserPeriod(decUserPeriod.getText());
            tg.setFlightTime(flightTime.getText());
            tg.setRampUp(rampUp.getText());
            ((AbstractThreadGroup) tg).setSamplerController((LoopController) loopPanel.createTestElement());

            refreshPreview();
        }
    }

    @Override
    public void configure(TestElement te) {
        super.configure(te);
        SteppingThreadGroup tg = (SteppingThreadGroup) te;
        totalThreads.setText(tg.getNumThreadsAsString());
        initialDelay.setText(tg.getThreadGroupDelay());
        incUserCount.setText(tg.getInUserCount());
        incUserPeriod.setText(tg.getInUserPeriod());
        decUserCount.setText(tg.getOutUserCount());
        decUserPeriod.setText(tg.getOutUserPeriod());
        flightTime.setText(tg.getFlightTime());
        rampUp.setText(tg.getRampUp());

        TestElement controller = (TestElement) tg.getProperty(AbstractThreadGroup.MAIN_CONTROLLER).getObjectValue();
        if (controller != null) {
            loopPanel.configure(controller);
        }
    }

    private void updateChart(SteppingThreadGroup tg) {
        model.clear();

        GraphRowSumValues row = new GraphRowSumValues();
        row.setColor(Color.RED);
        row.setDrawLine(true);
        row.setMarkerSize(AbstractGraphRow.MARKER_SIZE_NONE);
        row.setDrawThickLines(true);

        final HashTree hashTree = new HashTree();
        hashTree.add(new LoopController());
        JMeterThread thread = new JMeterThread(hashTree, null, null);

        tg.testStarted();
        long now = System.currentTimeMillis();

        // test start
        chart.setxAxisLabelRenderer(new DateTimeRenderer(DateTimeRenderer.HHMMSS, now - 1)); //-1 because row.add(thread.getStartTime() - 1, 0)
        row.add(now, 0);
        row.add(now + tg.getThreadGroupDelayAsInt(), 0);

        int numThreads = tg.getNumThreads();

        // users in
        for (int n = 0; n < numThreads; n++) {
            thread.setThreadNum(n);
            tg.scheduleThread(thread);
            row.add(thread.getStartTime() - 1, 0);
            row.add(thread.getStartTime(), 1);
        }

        // users out
        for (int n = 0; n < numThreads; n++) {
            thread.setThreadNum(n);
            tg.scheduleThread(thread);
            row.add(thread.getEndTime() - 1, 0);
            row.add(thread.getEndTime(), -1);
        }

        model.put("Expected Active Users Count", row);
        chart.invalidateCache();
        chart.repaint();
    }

    private JPanel createControllerPanel() {
        loopPanel = new LoopControlPanel(false);
        LoopController looper = (LoopController) loopPanel.createTestElement();
        looper.setLoops(-1);
        looper.setContinueForever(true);
        loopPanel.configure(looper);
        return loopPanel;
    }

    private void registerJTextfieldForGraphRefresh(final JTextField tf) {
        tf.addActionListener(loopPanel);
        tf.getDocument().addDocumentListener(new FieldChangesListener(tf));
    }
}
