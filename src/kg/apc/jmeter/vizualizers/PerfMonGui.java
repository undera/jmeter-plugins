package kg.apc.jmeter.vizualizers;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.gui.ButtonPanelAddCopyRemove;
import kg.apc.jmeter.perfmon.AgentConnector;
import kg.apc.jmeter.perfmon.PerfMonCollector;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author APC
 */
public class PerfMonGui
      extends AbstractOverTimeVisualizer {
   private static final Logger log = LoggingManager.getLoggerForClass();
   private PowerTableModel tableModel;
   private JTable grid;
   private JComboBox metricTypesBox;
   public static final String[] columnIdentifiers = new String[]{
      "Host / IP", "Port", "Metric to collect", "Metric parameter (see help)"
   };
   public static final Class[] columnClasses = new Class[]{
      String.class, String.class, String.class, String.class
   };
   private static Object[] defaultValues = new Object[]{
      "localhost", "4444", "CPU", ""
   };

   public PerfMonGui() {
      super();
      graphPanel.getGraphObject().setYAxisLabel("Performance Metrics");
      initGui();
   }

   @Override
   protected JSettingsPanel createSettingsPanel() {
      return new JSettingsPanel(this,
            JSettingsPanel.GRADIENT_OPTION
            | JSettingsPanel.LIMIT_POINT_OPTION
            | JSettingsPanel.MAXY_OPTION
            | JSettingsPanel.RELATIVE_TIME_OPTION);
   }

   @Override
   public String getWikiPage() {
      return "PerfMon";
   }

   public String getLabelResource() {
      return getClass().getSimpleName();
   }

   @Override
   public String getStaticLabel() {
      return JMeterPluginsUtils.prefixLabel("PerfMon Metrics Collector");
   }

   private void initGui() {
      add(createConnectionsPanel(), BorderLayout.SOUTH);
   }

   private Component createConnectionsPanel() {
      JPanel panel = new JPanel(new BorderLayout(5, 5));
      panel.setBorder(BorderFactory.createTitledBorder("Servers to Monitor (ServerAgent must be started, see help)"));
      panel.setPreferredSize(new Dimension(150, 150));

      JScrollPane scroll = new JScrollPane(createGrid());
      scroll.setPreferredSize(scroll.getMinimumSize());
      panel.add(scroll, BorderLayout.CENTER);
      panel.add(new ButtonPanelAddCopyRemove(grid, tableModel, defaultValues), BorderLayout.SOUTH);

      metricTypesBox=new JComboBox(AgentConnector.metrics.toArray());
      grid.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(metricTypesBox));

      return panel;
   }

   private JTable createGrid() {
      grid = new JTable();
      createTableModel();
      grid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      grid.setMinimumSize(new Dimension(200, 100));

      return grid;
   }

   private void createTableModel() {
      tableModel = new PowerTableModel(columnIdentifiers, columnClasses);
      grid.setModel(tableModel);
   }

   @Override
   public TestElement createTestElement() {
      TestElement te = new PerfMonCollector();
      modifyTestElement(te);
      te.setComment(JMeterPluginsUtils.getWikiLinkText(getWikiPage()));
      return te;
   }

   @Override
   public void modifyTestElement(TestElement te) {
      super.modifyTestElement(te);
      if (grid.isEditing()) {
         grid.getCellEditor().stopCellEditing();
      }

      if (te instanceof PerfMonCollector) {
         PerfMonCollector pmte = (PerfMonCollector) te;
         CollectionProperty rows = JMeterPluginsUtils.tableModelRowsToCollectionProperty(tableModel, PerfMonCollector.DATA_PROPERTY);
         pmte.setData(rows);
      }
      super.configureTestElement(te);
   }

   @Override
   public void configure(TestElement te) {
      super.configure(te);
      PerfMonCollector pmte = (PerfMonCollector) te;
      JMeterProperty perfmonValues = pmte.getData();
      if (!(perfmonValues instanceof NullProperty)) {
         JMeterPluginsUtils.collectionPropertyToTableModelRows((CollectionProperty) perfmonValues, tableModel);
      }
      else {
         log.warn("Received null property instead of collection");
      }
   }

   @Override
   public void add(SampleResult res) {
      super.add(res);
      addThreadGroupRecord(res.getSampleLabel(), normalizeTime(res.getEndTime()), res.getLatency());
      updateGui(null);
   }

   private void addThreadGroupRecord(String threadGroupName, long time, long value) {
      AbstractGraphRow row = model.get(threadGroupName);
      if (row == null) {
         row = getNewRow(model, AbstractGraphRow.ROW_AVERAGES, threadGroupName,
               AbstractGraphRow.MARKER_SIZE_SMALL, false, false, false, true, true);
      }

      log.info("Got value: "+value);
      row.add(time, value);
   }
}
