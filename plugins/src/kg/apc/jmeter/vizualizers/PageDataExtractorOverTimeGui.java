package kg.apc.jmeter.vizualizers;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import kg.apc.charting.AbstractGraphRow;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.graphs.AbstractOverTimeVisualizer;
import kg.apc.jmeter.gui.ButtonPanelAddCopyRemove;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
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
      "Regular Expression for Key", "Regular Expression for Value", "Delta"
   };
   public static final Class[] columnClasses = new Class[]{
      String.class, String.class, Boolean.class
   };
   private static Object[] defaultValues = new Object[]{
      "", "", false
   };
   private CollectionProperty regExps = null;
   private HashMap<String, Pattern> patterns = new HashMap<String, Pattern>();

   private ArrayList<Object> cmdRegExps = null;

   private HashMap<String, Double> oldValues = new HashMap<String, Double>();

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
      grid.getColumnModel().getColumn(0).setPreferredWidth(350);
      grid.getColumnModel().getColumn(1).setPreferredWidth(350);
      grid.getColumnModel().getColumn(2).setPreferredWidth(50);

      return grid;
   }

   private Component createRegExpPanel() {
      JPanel panel = new JPanel(new BorderLayout(5, 5));
      panel.setBorder(BorderFactory.createTitledBorder("Regular Expressions Data Extractors"));
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
      TestElement te = super.createTestElement();
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

      if (te instanceof CorrectedResultCollector) {
         CorrectedResultCollector rc = (CorrectedResultCollector) te;
         CollectionProperty rows = JMeterPluginsUtils.tableModelRowsToCollectionProperty(tableModel, REGEXPS_PROPERTY);
         rc.setProperty(rows);
      }
   }

   @Override
   public void configure(TestElement te) {
      super.configure(te);
      CorrectedResultCollector rc = (CorrectedResultCollector) te;
      JMeterProperty regexpValues = rc.getProperty(REGEXPS_PROPERTY);
      if (!(regexpValues instanceof NullProperty)) {
         JMeterPluginsUtils.collectionPropertyToTableModelRows((CollectionProperty) regexpValues, tableModel, columnClasses);
         regExps = (CollectionProperty) regexpValues;
      } else {
         log.warn("Received null property instead of collection");
      }
   }

   private void processPage(String pageBody, String regExpKey, String regExpValue, boolean isDelta, long time) {
      Pattern patternKey = patterns.get(regExpKey);
      if (patternKey == null) {
         try {
            patternKey = Pattern.compile(regExpKey);
            patterns.put(regExpKey, patternKey);
         } catch (PatternSyntaxException ex) {
            log.error("Error compiling pattern: " + regExpKey);
         }
      }
      Pattern patternValue = patterns.get(regExpValue);
      if (patternValue == null) {
         try {
            patternValue = Pattern.compile(regExpValue);
            patterns.put(regExpValue, patternValue);
         } catch (PatternSyntaxException ex) {
            log.error("Error compiling pattern: " + regExpValue);
         }
      }

      if (patternKey != null && patternValue != null) {
         Matcher mKey = patternKey.matcher(pageBody);
         Matcher mValue = patternValue.matcher(pageBody);

         boolean found = false;

         while (mKey.find() && mValue.find()) {
            found = true;
            String key = mKey.group(1);
            String sValue = mValue.group(1);

            try {
               double value = Double.parseDouble(sValue);
               if(isDelta) {
                   if(oldValues.containsKey(key)) {
                       double tmp = oldValues.get(key) - value;
                       addRecord(key, time, tmp);
                   }
                   oldValues.put(key, value);
               } else {
                   addRecord(key, time, value);
               }
            } catch (NumberFormatException ex) {
               log.error("Value extracted is not a number: " + sValue);
            }
         }
         if (!found) {
            log.warn("No data found for regExpKey: " + regExpKey + " and regExpValue: " + regExpValue);
         }
      }
   }

   private void addRecord(String keyName, long time, double value) {

      if (!isSampleIncluded(keyName)) {
         return;
      }

      AbstractGraphRow row = model.get(keyName);

      if (row == null) {
         row = getNewRow(model, AbstractGraphRow.ROW_AVERAGES, keyName, AbstractGraphRow.MARKER_SIZE_SMALL, false, false, false, true, true);
      }
      row.add(time, value);
   }

   @Override
   public void add(SampleResult res) {
      super.add(res);

      if (regExps == null && cmdRegExps == null) {
         return;
      }

      String pageBody = res.getResponseDataAsString();
      long time = normalizeTime(res.getEndTime());

      if(cmdRegExps == null) {
         PropertyIterator iter = regExps.iterator();
         while (iter.hasNext()) {
            CollectionProperty props = (CollectionProperty) iter.next();
            String regExpKey = props.get(0).getStringValue();
            String regExpValue = props.get(1).getStringValue();
            Boolean isDelta = props.get(2).getBooleanValue();

            processPage(pageBody, regExpKey, regExpValue, isDelta, time);
         }
      } else {
         Iterator<Object> regExpIter = cmdRegExps.iterator();
         while (regExpIter.hasNext()) {
            String regExpKey = (String)regExpIter.next();
            String regExpValue = (String)regExpIter.next();
            boolean isDelta = (Boolean)regExpIter.next();

            processPage(pageBody, regExpKey, regExpValue, isDelta, time);
         }
      }

      updateGui(null);
   }

    @Override
    public void clearData() {
        oldValues.clear();
        super.clearData();
    }

   //for cmdLine tool only
   public void setCmdRegExps (ArrayList<Object> cmdRegExps) {
      this.cmdRegExps = cmdRegExps;
   }
}
