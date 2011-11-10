package kg.apc.jmeter.config;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.gui.BrowseAction;
import kg.apc.jmeter.gui.GuiBuilderHelper;
import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.testelement.TestElement;

/**
 *
 * @author Stephane Hoblingre
 */
public class VariablesFromCSVGui extends AbstractConfigGui {

    public static final String WIKIPAGE = "VariablesFromCSV";

    private JTextField fileName;
    private JTextField variablePrefix;
    private JTextField separator;
    private JCheckBox storeSysProp;
    
    private JButton browseButton;
    private JButton checkButton;
    private JTextArea checkInfo;

    public VariablesFromCSVGui() {
        super();
        init();
        initFields();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Variables From CSV File");
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        fileName.setText(element.getPropertyAsString(VariablesFromCSV.FILENAME));
        variablePrefix.setText(element.getPropertyAsString(VariablesFromCSV.VARIABLE_PREFIX));
        separator.setText(element.getPropertyAsString(VariablesFromCSV.SEPARATOR));
        storeSysProp.setSelected(element.getPropertyAsBoolean(VariablesFromCSV.STORE_SYS_PROP));
    }

    @Override
    public TestElement createTestElement() {
        VariablesFromCSV varsCsv = new VariablesFromCSV();
        modifyTestElement(varsCsv);
        varsCsv.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return varsCsv;
    }

    @Override
    public void modifyTestElement(TestElement te) {
        configureTestElement(te);
        if (te instanceof VariablesFromCSV) {
            VariablesFromCSV varsCsv = (VariablesFromCSV) te;
            varsCsv.setFileName(fileName.getText());
            varsCsv.setVariablePrefix(variablePrefix.getText());
            varsCsv.setSeparator(separator.getText());
            varsCsv.setStoreAsSystemProperty(storeSysProp.isSelected());
        }
    }

    @Override
    public void clearGui() {
        super.clearGui();
        initFields();
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

        add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), WIKIPAGE), BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.anchor = GridBagConstraints.FIRST_LINE_END;

        GridBagConstraints editConstraints = new GridBagConstraints();
        editConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        editConstraints.weightx = 1.0;
        editConstraints.fill = GridBagConstraints.HORIZONTAL;

        addToPanel(mainPanel, labelConstraints, 0, 0, new JLabel("CSV File: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 0, fileName = new JTextField(20));
        addToPanel(mainPanel, labelConstraints, 2, 0, browseButton = new JButton("Browse..."));

        GuiBuilderHelper.strechButtonToComponent(fileName, browseButton);

        editConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(2, 0, 0, 0);

        browseButton.addActionListener(new BrowseAction(fileName));

        addToPanel(mainPanel, labelConstraints, 0, 1, new JLabel(" Variable prefix: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 1, variablePrefix = new JTextField(20));

        addToPanel(mainPanel, labelConstraints, 0, 2, new JLabel("Separator (use '\\t' for tab): ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 2, separator = new JTextField(20));

        addToPanel(mainPanel, labelConstraints, 0, 3, new JLabel("Store variables also in System Properties: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 3, storeSysProp = new JCheckBox());

        editConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(4, 0, 0, 2);

        addToPanel(mainPanel, labelConstraints, 0, 4, checkButton = new JButton("Test CSV File"));

        labelConstraints.insets = new java.awt.Insets(4, 0, 0, 0);

        checkInfo = new JTextArea();
        addToPanel(mainPanel, editConstraints, 1, 4, GuiBuilderHelper.getTextAreaScrollPaneContainer(checkInfo, 10));
        checkButton.addActionListener(new TestCsvFileAction(fileName, variablePrefix, separator, checkInfo));
        checkInfo.setEditable(false);
        checkInfo.setOpaque(false);

        JPanel container = new JPanel(new BorderLayout());
        container.add(mainPanel, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);
    }

    private void addToPanel(JPanel panel, GridBagConstraints constraints, int col, int row, JComponent component) {
        constraints.gridx = col;
        constraints.gridy = row;
        panel.add(component, constraints);
    }

    private void initFields() {
        variablePrefix.setText("");
        fileName.setText("");
        checkInfo.setText("");
        storeSysProp.setSelected(false);
    }
}
