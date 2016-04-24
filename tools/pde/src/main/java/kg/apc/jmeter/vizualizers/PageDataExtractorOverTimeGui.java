package kg.apc.jmeter.vizualizers;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import kg.apc.charting.AbstractGraphRow;
import kg.apc.jmeter.CMDLineArgumentsProcessor;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.graphs.AbstractOverTimeVisualizer;
import kg.apc.jmeter.gui.ButtonPanelAddCopyRemove;
import kg.apc.jmeter.vizualizers.CorrectedResultCollector;
import kg.apc.jmeter.vizualizers.JSettingsPanel;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;


public class PageDataExtractorOverTimeGui extends AbstractOverTimeVisualizer implements CMDLineArgumentsProcessor {

    private static final String REGEXPS_PROPERTY = "regexps";
    private static final Logger log = LoggingManager.getLoggerForClass();
    private PowerTableModel tableModel;
    private JTable grid;
    public static final String[] columnIdentifiers = new String[]{
        "Chart label", "Regular expression value extractor", "Delta", "RegExp label"
    };
    public static final Class[] columnClasses = new Class[]{
        String.class, String.class, Boolean.class, Boolean.class
    };
    private static Object[] defaultValues = new Object[]{
        "", "", false, false
    };
    private CollectionProperty regExps = null;
    private HashMap<String, Pattern> patterns = new HashMap<>();
    private ArrayList<Object> cmdRegExps = null;
    private HashMap<String, Double> oldValues = new HashMap<>();

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
                | JSettingsPanel.MARKERS_OPTION);
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
        grid.getColumnModel().getColumn(3).setPreferredWidth(110);

        return grid;
    }

    private Component createRegExpPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Regular Expressions Data Extractor"));
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

    private void processPageRegExpLabel(String pageBody, String regExpKey, String regExpValue, boolean isDelta, long time) {
        //handle multiple keys with same name found with the regexp
        ArrayList<String> labels = new ArrayList<>();
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

                if (labels.contains(key)) {
                    int i = 2;
                    while (labels.contains(key + "_" + i)) {
                        i++;
                    }
                    key = key + "_" + i;
                }

                labels.add(key);

                String sValue = mValue.group(1);

                try {
                    double value = Double.parseDouble(sValue);
                    if (isDelta) {
                        if (oldValues.containsKey(key)) {
                            double delta = value - oldValues.get(key);
                            addRecord(key, time, delta);
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

    private void processPageLabel(String pageBody, String label, String regExpValue, boolean isDelta, long time) {
        Pattern patternValue = patterns.get(regExpValue);
        if (patternValue == null) {
            try {
                patternValue = Pattern.compile(regExpValue);
                patterns.put(regExpValue, patternValue);
            } catch (PatternSyntaxException ex) {
                log.error("Error compiling pattern: " + regExpValue);
            }
        }

        if (patternValue != null) {
            Matcher mValue = patternValue.matcher(pageBody);

            int counter = 0;

            while (mValue.find()) {
                counter++;
                String key;
                if (counter > 1) {
                    key = label + " (" + counter + ")";
                } else {
                    key = label;
                }

                String sValue = mValue.group(1);

                try {
                    double value = Double.parseDouble(sValue);
                    if (isDelta) {
                        if (oldValues.containsKey(key)) {
                            double delta = value - oldValues.get(key);
                            addRecord(key, time, delta);
                        }
                        oldValues.put(key, value);
                    } else {
                        addRecord(key, time, value);
                    }
                } catch (NumberFormatException ex) {
                    log.error("Value extracted is not a number: " + sValue);
                }
            }
            if (counter == 0) {
                log.warn("No data found for regExpValue: " + regExpValue);
            }
        }
    }

    private void addRecord(String label, long time, double value) {

        if (!isSampleIncluded(label)) {
            return;
        }

        AbstractGraphRow row = model.get(label);

        if (row == null) {
            row = getNewRow(model, AbstractGraphRow.ROW_AVERAGES, label, AbstractGraphRow.MARKER_SIZE_SMALL, false, false, false, true, true);
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

        if (cmdRegExps == null) {
            PropertyIterator iter = regExps.iterator();
            while (iter.hasNext()) {
                CollectionProperty props = (CollectionProperty) iter.next();
                String label = props.get(0).getStringValue();
                String regExpValue = props.get(1).getStringValue();
                Boolean isDelta = props.get(2).getBooleanValue();
                Boolean isLabelRegExp = props.get(3).getBooleanValue();

                if (isLabelRegExp) {
                    processPageRegExpLabel(pageBody, label, regExpValue, isDelta, time);
                } else {
                    processPageLabel(pageBody, label, regExpValue, isDelta, time);
                }
            }
        } else {
            Iterator<Object> regExpIter = cmdRegExps.iterator();
            while (regExpIter.hasNext()) {
                String label = (String) regExpIter.next();
                String regExpValue = (String) regExpIter.next();
                boolean isDelta = (Boolean) regExpIter.next();
                boolean isLabelRegExp = (Boolean) regExpIter.next();

                if (isLabelRegExp) {
                    processPageRegExpLabel(pageBody, label, regExpValue, isDelta, time);
                } else {
                    processPageLabel(pageBody, label, regExpValue, isDelta, time);
                }
            }
        }

        updateGui(null);
    }

    @Override
    public void clearData() {
        oldValues.clear();
        super.clearData();
    }

    private void storeRegExps(String regExps) {
        String[] regStrings = regExps.split("\\{;\\}");

        if (regStrings.length % 4 != 0) {
            throw new IllegalArgumentException("Regular expressions must be succession of key/value/isDelta(true or false)/isRegExpLabel(true or false) separated by {;}");
        }

        ArrayList<Object> data = new ArrayList<>();
        for (int i = 0; i < regStrings.length; i = i + 4) {
            data.add(regStrings[i]);
            data.add(regStrings[i + 1]);
            data.add("true".equalsIgnoreCase(regStrings[i + 2]));
            data.add("true".equalsIgnoreCase(regStrings[i + 3]));
        }

        this.cmdRegExps = data;
    }

    public void processCMDOption(String nextArg, ListIterator args) {
        if (nextArg.equalsIgnoreCase("--extractor-regexps")) {
            // 
            if (!args.hasNext()) {
                throw new IllegalArgumentException("Missing regular expressions");
            }

            storeRegExps((String) args.next());
        } else {
            throw new UnsupportedOperationException("Unknown option: " + nextArg);
        }
    }
}
