package kg.apc.jmeter.threads;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.DateTimeRenderer;
import kg.apc.charting.GraphPanelChart;
import kg.apc.charting.rows.GraphRowSumValues;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.gui.ButtonPanelLoadClean;
import kg.apc.jmeter.gui.GuiBuilderHelper;

import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
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
public class AutomaticUltimateThreadGroupGui extends AbstractThreadGroupGui implements TableModelListener,
		CellEditorListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String WIKIPAGE = "AutomaticUltimateThreadGroup";
	private static final Logger log = LoggingManager.getLoggerForClass();
	/**
     *
     */
	protected ConcurrentHashMap<String, AbstractGraphRow> model;
	private GraphPanelChart chart;
	/**
     *
     */
	public static final String[] columnIdentifiers = new String[] { "Start Threads Count", "Initial Delay, sec",
			"Startup Time, sec", "Hold Load For, sec", "Shutdown Time" };
	/**
     *
     */
	@SuppressWarnings("rawtypes")
	public static final Class[] columnClasses = new Class[] { String.class, String.class, String.class, String.class,
			String.class };

	private LoopControlPanel loopPanel;
	protected PowerTableModel tableModel;
	protected JTable grid;
	protected ButtonPanelLoadClean buttonPanel;

	private JTextField virtualUsersTF;
	private JTextField shutdownTimeTF;
	private JTextField rampUpTF;
	private JTextField durationCycleTF;
	private JTextField avgResponseTimeTF;
	private JTextField avgThinkTimeTF;

	private Object[][] values = new Object[100][5];

	/**
     *
     */
	public AutomaticUltimateThreadGroupGui() {
		super();
		init();
	}

	/**
     *
     */
	protected final void init() {
		JMeterPluginsUtils.addHelpLinkToPanel(this, WIKIPAGE);
		JPanel containerPanel = new VerticalPanel();

		containerPanel.add(createParamsPanel(), BorderLayout.NORTH);
		containerPanel.add(GuiBuilderHelper.getComponentWithMargin(createChart(), 2, 2, 0, 2), BorderLayout.CENTER);
		add(containerPanel, BorderLayout.CENTER);

		// this magic LoopPanel provides functionality for thread loops
		createControllerPanel();
	}

	private JPanel createParamsPanel() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.setBorder(BorderFactory.createTitledBorder("Threads Schedule"));
		panel.setPreferredSize(new Dimension(200, 300));

		panel.add(createFieldsPanel(), BorderLayout.NORTH);

		JScrollPane scroll = new JScrollPane(createGrid());
		scroll.setPreferredSize(scroll.getMinimumSize());
		panel.add(scroll, BorderLayout.CENTER);

		ActionListener actionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Integer[] startThreadsCount;
				Integer[] initialDelay;
				Integer[] startupTime;
				Integer[] holdLoad;
				Integer[] shutdownTime;

				String[] virtualUsers = virtualUsersTF.getText().split(";");

				for (int c = virtualUsers.length; c < values.length; c++) {
					values[c] = null;
				}

				startThreadsCount = new Integer[virtualUsers.length];
				initialDelay = new Integer[virtualUsers.length];
				startupTime = new Integer[virtualUsers.length];
				holdLoad = new Integer[virtualUsers.length];
				shutdownTime = new Integer[virtualUsers.length];

				for (int c = 0; c < virtualUsers.length; c++) {
					startThreadsCount[c] = c == 0 ? Integer.valueOf(virtualUsers[c]) : Integer.valueOf(virtualUsers[c])
							- Integer.valueOf(virtualUsers[c - 1]);
				}

				int rampUp = Integer.valueOf(rampUpTF.getText());
				int durationPerCycle = Integer.valueOf(durationCycleTF.getText()) * 60;

				for (int c = 0; c < startThreadsCount.length; c++) {
					startupTime[c] = startThreadsCount[c] * rampUp;
				}

				for (int c = 0; c < startupTime.length; c++) {
					initialDelay[c] = c == 0 ? 0 : startupTime[c - 1] + initialDelay[c - 1] + durationPerCycle;
				}

				int sumOfStartupTimes = 0;
				for (int currentStartupTime : startupTime) {
					sumOfStartupTimes += currentStartupTime;
				}

				int shutdownTimeMin = Integer.valueOf(shutdownTimeTF.getText()) * 60;
				for (int c = 0; c < startupTime.length; c++) {
					shutdownTime[c] = (shutdownTimeMin * startupTime[c]) / sumOfStartupTimes;
				}

				for (int c = 0; c < initialDelay.length; c++) {
					holdLoad[c] = c == 0 ? initialDelay[initialDelay.length - 1] + startupTime[startupTime.length - 1]
							+ durationPerCycle : initialDelay[c - 1] + startupTime[c - 1] + holdLoad[c - 1]
							+ shutdownTime[c - 1] - initialDelay[c] - startupTime[c];
				}

				for (int c = 0; c < virtualUsers.length; c++) {
					values[c] = new Integer[] { startThreadsCount[c], initialDelay[c], startupTime[c], holdLoad[c],
							shutdownTime[c] };
				}
			}
		};

		buttonPanel = new ButtonPanelLoadClean(grid, tableModel, actionListener, values);

		panel.add(buttonPanel, BorderLayout.SOUTH);

		return panel;
	}

	private JTable createGrid() {
		grid = new JTable() {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		grid.getDefaultEditor(String.class).addCellEditorListener(this);
		createTableModel();
		grid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		grid.setMinimumSize(new Dimension(200, 100));

		return grid;
	}

	private JPanel createFieldsPanel() {
		VerticalPanel parentPanel = new VerticalPanel();
		JPanel panel0 = new JPanel();
		JPanel panel1 = new JPanel();

		JLabel virtualUsersL = new JLabel("Virtual User Step per Clycle [csv]");
		JLabel rampUpL = new JLabel("Ramp-up per Real User [sec]");
		JLabel shutdownTimeL = new JLabel("Shutdown Time [min]");
		JLabel durationCycleL = new JLabel("Duration per Cycle [min]");
		JLabel avgResponseTimeL = new JLabel("Avg Response Time [sec]");
		JLabel avgThinkTimeL = new JLabel("Avg Think Time [sec]");

		virtualUsersTF = new JTextField(36);
		shutdownTimeTF = new JTextField(2);
		rampUpTF = new JTextField(2);
		durationCycleTF = new JTextField(2);
		avgResponseTimeTF = new JTextField(2);
		avgThinkTimeTF = new JTextField(2);

		panel0.add(virtualUsersL);
		panel0.add(virtualUsersTF);
		panel0.add(rampUpL);
		panel0.add(rampUpTF);
		panel1.add(shutdownTimeL);
		panel1.add(shutdownTimeTF);
		panel1.add(durationCycleL);
		panel1.add(durationCycleTF);
		panel1.add(avgResponseTimeL);
		panel1.add(avgResponseTimeTF);
		panel1.add(avgThinkTimeL);
		panel1.add(avgThinkTimeTF);

		parentPanel.add(panel0, BorderLayout.NORTH);
		parentPanel.add(panel1, BorderLayout.SOUTH);

		return parentPanel;
	}

	@Override
	public String getLabelResource() {
		return this.getClass().getSimpleName();
	}

	@Override
	public String getStaticLabel() {
		return JMeterPluginsUtils.prefixLabel("Automatic Ultimate Thread Group");
	}

	@Override
	public TestElement createTestElement() {
		// log.info("Create test element");
		UltimateThreadGroup tg = new UltimateThreadGroup();
		modifyTestElement(tg);
		tg.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));

		return tg;
	}

	@Override
	public void modifyTestElement(TestElement tg) {
		// log.info("Modify test element");
		if (grid.isEditing()) {
			grid.getCellEditor().stopCellEditing();
		}

		if (tg instanceof UltimateThreadGroup) {
			UltimateThreadGroup utg = (UltimateThreadGroup) tg;
			CollectionProperty rows = JMeterPluginsUtils.tableModelRowsToCollectionProperty(tableModel,
					UltimateThreadGroup.DATA_PROPERTY);
			utg.setData(rows);
			utg.setSamplerController((LoopController) loopPanel.createTestElement());
		}
		super.configureTestElement(tg);
	}

	@Override
	public void configure(TestElement tg) {
		// log.info("Configure");
		super.configure(tg);
		UltimateThreadGroup utg = (UltimateThreadGroup) tg;
		// log.info("Configure "+utg.getName());
		JMeterProperty threadValues = utg.getData();
		if (!(threadValues instanceof NullProperty)) {
			CollectionProperty columns = (CollectionProperty) threadValues;

			tableModel.removeTableModelListener(this);
			JMeterPluginsUtils.collectionPropertyToTableModelRows(columns, tableModel);
			tableModel.addTableModelListener(this);
			updateUI();
		} else {
			log.warn("Received null property instead of collection");
		}

		TestElement te = (TestElement) tg.getProperty(AbstractThreadGroup.MAIN_CONTROLLER).getObjectValue();
		if (te != null) {
			loopPanel.configure(te);
		}
	}

	@Override
	public void updateUI() {
		super.updateUI();

		if (tableModel != null) {
			UltimateThreadGroup utgForPreview = new UltimateThreadGroup();
			utgForPreview.setData(JMeterPluginsUtils.tableModelRowsToCollectionPropertyEval(tableModel,
					UltimateThreadGroup.DATA_PROPERTY));
			updateChart(utgForPreview);
		}
	}

	private void updateChart(UltimateThreadGroup tg) {
		tg.testStarted();
		model.clear();
		GraphRowSumValues row = new GraphRowSumValues();
		row.setColor(Color.RED);
		row.setDrawLine(true);
		row.setMarkerSize(AbstractGraphRow.MARKER_SIZE_NONE);
		row.setDrawThickLines(true);

		final HashTree hashTree = new HashTree();
		hashTree.add(new LoopController());
		JMeterThread thread = new JMeterThread(hashTree, null, null);

		long now = System.currentTimeMillis();

		chart.setxAxisLabelRenderer(new DateTimeRenderer(DateTimeRenderer.HHMMSS, now - 1)); // -1
																								// because
																								// row.add(thread.getStartTime()
																								// -
																								// 1,
																								// 0)
		chart.setForcedMinX(now);

		row.add(now, 0);

		// users in
		int numThreads = tg.getNumThreads();
		log.debug("Num Threads: " + numThreads);
		for (int n = 0; n < numThreads; n++) {
			thread.setThreadNum(n);
			thread.setThreadName(Integer.toString(n));
			tg.scheduleThread(thread, now);
			row.add(thread.getStartTime() - 1, 0);
			row.add(thread.getStartTime(), 1);
		}

		tg.testStarted();
		// users out
		for (int n = 0; n < tg.getNumThreads(); n++) {
			thread.setThreadNum(n);
			thread.setThreadName(Integer.toString(n));
			tg.scheduleThread(thread, now);
			row.add(thread.getEndTime() - 1, 0);
			row.add(thread.getEndTime(), -1);
		}

		model.put("Expected parallel users count", row);
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

	private Component createChart() {
		chart = new GraphPanelChart(false, true);
		model = new ConcurrentHashMap<String, AbstractGraphRow>();
		chart.setRows(model);
		chart.getChartSettings().setDrawFinalZeroingLines(true);
		chart.setxAxisLabel("Elapsed time");
		chart.setYAxisLabel("Number of active threads");
		chart.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
		return chart;
	}

	public void tableChanged(TableModelEvent e) {
		// log.info("Model changed");
		updateUI();
	}

	private void createTableModel() {
		tableModel = new PowerTableModel(columnIdentifiers, columnClasses);
		tableModel.addTableModelListener(this);
		grid.setModel(tableModel);
	}

	public void editingStopped(ChangeEvent e) {
		// log.info("Editing stopped");
		updateUI();
	}

	public void editingCanceled(ChangeEvent e) {
		// no action needed
	}

	@Override
	public void clearGui() {
		super.clearGui();
		tableModel.clearData();
	}
}
