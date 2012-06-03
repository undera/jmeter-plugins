package kg.apc.jmeter.vizualizers;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.graphs.AbstractOverTimeVisualizer;
import kg.apc.jmeter.gui.ButtonPanelAddCopyRemove;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author Stephane Hoblingre
 */
public class PageDataExtractorOverTimeGui extends AbstractOverTimeVisualizer {

   private static final String REGEXPS_PROPERTY = "regexps";

   private static final Logger log = LoggingManager.getLoggerForClass();
   private PowerTableModel tableModel;
   private JTable grid;
   public static final String[] columnIdentifiers = new String[]{
      "Regular Expression for Key", "Regular Expression for Value"
   };
   public static final Class[] columnClasses = new Class[]{
      String.class, String.class
   };
   private static String[] defaultValues = new String[]{
      "", ""
   };

   public PageDataExtractorOverTimeGui() {
      setGranulation(1000);
      graphPanel.getGraphObject().setYAxisLabel("Metrics Extracted");
      graphPanel.getGraphObject().getChartSettings().setExpendRows(true);
   }

   @Override
   protected JSettingsPanel createSettingsPanel() {
      return new JSettingsPanel(this,
              JSettingsPanel.GRADIENT_OPTION
              | JSettingsPanel.MAXY_OPTION
              | JSettingsPanel.RELATIVE_TIME_OPTION
              | JSettingsPanel.AUTO_EXPAND_OPTION
              | JSettingsPanel.MARKERS_OPTION_DISABLED);
   }

   @Override
   public String getStaticLabel() {
      return JMeterPluginsUtils.prefixLabel("Page Data Extractor");
   }

   @Override
   public String getLabelResource() {
      return getClass().getSimpleName();
   }

   @Override
   public String getWikiPage() {
      return "PageDataExtractor";
   }
   
   private JTable createGrid() {
        grid = new JTable();
        createTableModel();
        grid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        grid.setMinimumSize(new Dimension(200, 100));

        return grid;
    }

    private Component createRegExpPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Regular Expressions to Apply"));
        panel.setPreferredSize(new Dimension(150, 150));

        JScrollPane scroll = new JScrollPane(createGrid());
        scroll.setPreferredSize(scroll.getMinimumSize());
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(new ButtonPanelAddCopyRemove(grid, tableModel, defaultValues), BorderLayout.SOUTH);

        return panel;
    }

    @Override
    protected JPanel getGraphPanelContainer() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel innerTopPanel = new JPanel(new BorderLayout());

        innerTopPanel.add(createRegExpPanel(), BorderLayout.CENTER);
        innerTopPanel.add(getFilePanel(), BorderLayout.SOUTH);

        panel.add(innerTopPanel, BorderLayout.NORTH);

        return panel;
    }

    private void createTableModel() {
        tableModel = new PowerTableModel(columnIdentifiers, columnClasses);
        grid.setModel(tableModel);
    }

    @Override
    public TestElement createTestElement() {
        TestElement te = new ResultCollector();
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

        if (te instanceof ResultCollector) {
            ResultCollector rc = (ResultCollector) te;
            CollectionProperty rows = JMeterPluginsUtils.tableModelRowsToCollectionProperty(tableModel, REGEXPS_PROPERTY);
            rc.setProperty(rows);
        }
        super.configureTestElement(te);
    }

    @Override
    public void configure(TestElement te) {
        super.configure(te);
        ResultCollector rc = (ResultCollector) te;
        JMeterProperty regexpValues = rc.getProperty(REGEXPS_PROPERTY);
        if (!(regexpValues instanceof NullProperty)) {
            JMeterPluginsUtils.collectionPropertyToTableModelRows((CollectionProperty) regexpValues, tableModel);
        } else {
            log.warn("Received null property instead of collection");
        }
    }
}
