package kg.apc.jmeter.threads;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
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
import kg.apc.jmeter.gui.ButtonPanelAddCopyRemove;
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
public class UltimateThreadGroupGui extends AbstractThreadGroupGui implements
		TableModelListener, CellEditorListener, ActionListener {

	public static final String WIKIPAGE = "UltimateThreadGroup";
	private static final Logger log = LoggingManager.getLoggerForClass();
	/**
     *
     */
	protected ConcurrentHashMap<String, AbstractGraphRow> model;
	private GraphPanelChart chart;
	/**
     *
     */
	public static final String[] columnIdentifiers = new String[] {
			"Start Threads Count", "Initial Delay, sec", "Startup Time, sec",
			"Hold Load For, sec", "Shutdown Time" };
	/**
     *
     */
	public static final Class[] columnClasses = new Class[] { String.class,
			String.class, String.class, String.class, String.class };
	public static final Integer[] defaultValues = new Integer[] { 100, 0, 30,
			60, 10 };
	private LoopControlPanel loopPanel;
	protected PowerTableModel tableModel;
	protected JTable grid;
	protected ButtonPanelAddCopyRemove buttons;

	private Clipboard system;
	private String rowstring;
	private String value;

	/**
     *
     */
	public UltimateThreadGroupGui() {
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
		containerPanel.add(GuiBuilderHelper.getComponentWithMargin(
				createChart(), 2, 2, 0, 2), BorderLayout.CENTER);
		add(containerPanel, BorderLayout.CENTER);

		// this magic LoopPanel provides functionality for thread loops
		createControllerPanel();
	}

	private JPanel createParamsPanel() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.setBorder(BorderFactory.createTitledBorder("Threads Schedule"));
		panel.setPreferredSize(new Dimension(200, 200));

		JScrollPane scroll = new JScrollPane(createGrid());
		scroll.setPreferredSize(scroll.getMinimumSize());
		panel.add(scroll, BorderLayout.CENTER);
		buttons = new ButtonPanelAddCopyRemove(grid, tableModel, defaultValues);
		panel.add(buttons, BorderLayout.SOUTH);

		return panel;
	}

	private JTable createGrid() {
		grid = new JTable();
		grid.getDefaultEditor(String.class).addCellEditorListener(this);
		createTableModel();
		grid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		grid.setMinimumSize(new Dimension(200, 100));

		/**
		 * Changed in order to enable the copy of cells from Excel.
		 * 
		 * @author renato ochando
		 */
		KeyStroke paste = KeyStroke.getKeyStroke(86, Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask());
		this.grid.registerKeyboardAction(this, "Paste", paste, 0);
		this.system = Toolkit.getDefaultToolkit().getSystemClipboard();

		return grid;
	}

	@Override
	public String getLabelResource() {
		return this.getClass().getSimpleName();
	}

	@Override
	public String getStaticLabel() {
		return JMeterPluginsUtils.prefixLabel("Ultimate Thread Group");
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
			CollectionProperty rows = JMeterPluginsUtils
					.tableModelRowsToCollectionProperty(tableModel,
							UltimateThreadGroup.DATA_PROPERTY);
			utg.setData(rows);
			utg.setSamplerController((LoopController) loopPanel
					.createTestElement());
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
			JMeterPluginsUtils.collectionPropertyToTableModelRows(columns,
					tableModel);
			tableModel.addTableModelListener(this);
			updateUI();
		} else {
			log.warn("Received null property instead of collection");
		}

		TestElement te = (TestElement) tg.getProperty(
				AbstractThreadGroup.MAIN_CONTROLLER).getObjectValue();
		if (te != null) {
			loopPanel.configure(te);
		}
		buttons.checkDeleteButtonStatus();
	}

	@Override
	public void updateUI() {
		super.updateUI();

		if (tableModel != null) {
			UltimateThreadGroup utgForPreview = new UltimateThreadGroup();
			utgForPreview.setData(JMeterPluginsUtils
					.tableModelRowsToCollectionPropertyEval(tableModel,
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

		chart.setxAxisLabelRenderer(new DateTimeRenderer(
				DateTimeRenderer.HHMMSS, now - 1)); // -1 because
													// row.add(thread.getStartTime()
													// - 1, 0)
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
		chart.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
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

	/**
	 * Changed in order to enable the copy of cells from Excel.
	 * 
	 * @author renato ochando
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().compareTo("Paste") == 0) {
			int startRow = this.grid.getSelectedRows()[0];

			int nrCols = this.grid.getColumnCount();
			int nrRows = this.grid.getRowCount();
			try {
				String trstring = (String) this.system.getContents(this)
						.getTransferData(DataFlavor.stringFlavor);
				StringTokenizer st1 = new StringTokenizer(trstring, "\r\n");
				int nrPasteRows = st1.countTokens();
				if (nrPasteRows > nrRows - startRow) {
					for (int i = 0; i < nrPasteRows - nrRows + startRow; i++) {
						JButton button = (JButton) this.buttons.getComponent(0);
						button.doClick();
					}
				}
				for (int i = 0; st1.hasMoreTokens(); i++) {
					this.rowstring = st1.nextToken();
					StringTokenizer st2 = new StringTokenizer(this.rowstring,
							"\t");
					int nrPasteCols = st2.countTokens();
					int startCol;
					if (nrPasteCols == nrCols)
						startCol = 0;
					else
						startCol = this.grid.getSelectedColumns()[0];
					for (int j = 0; st2.hasMoreTokens(); j++) {
						this.value = st2.nextToken();
						if ((startRow + i < this.grid.getRowCount())
								&& (startCol + j < this.grid.getColumnCount())) {
							this.grid.setValueAt(this.value, startRow + i,
									startCol + j);
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		updateUI();
	}

	@Override
	public void clearGui() {
		super.clearGui();
		tableModel.clearData();
	}
}
