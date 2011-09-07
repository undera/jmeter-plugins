package kg.apc.jmeter.modifiers;

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
import org.apache.jmeter.processor.gui.AbstractPreProcessorGui;
import org.apache.jmeter.testelement.TestElement;

/**
 *
 * @author undera
 */
public class RawRequestSourcePreProcessorGui extends AbstractPreProcessorGui {

    public static final String WIKIPAGE = "RawDataSource";
    private JCheckBox rewindOnEOF;
    private JTextField variableName;
    private JTextField fileName;
    private JButton browseButton;
    private JButton checkButton;
    private JTextArea checkInfo;

    public RawRequestSourcePreProcessorGui() {
        super();
        init();
        initFields();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Raw Data Source PreProcessor");
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        rewindOnEOF.setSelected(element.getPropertyAsBoolean(RawRequestSourcePreProcessor.REWIND));
        variableName.setText(element.getPropertyAsString(RawRequestSourcePreProcessor.VARIABLE_NAME));
        fileName.setText(element.getPropertyAsString(RawRequestSourcePreProcessor.FILENAME));
    }

    @Override
    public TestElement createTestElement() {
        RawRequestSourcePreProcessor preproc = new RawRequestSourcePreProcessor();
        modifyTestElement(preproc);
        preproc.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return preproc;
    }

    @Override
    public void modifyTestElement(TestElement te) {
        configureTestElement(te);
        if (te instanceof RawRequestSourcePreProcessor) {
            RawRequestSourcePreProcessor preproc = (RawRequestSourcePreProcessor) te;
            preproc.setRewindOnEOF(rewindOnEOF.isSelected());
            preproc.setVarName(variableName.getText());
            preproc.setFileName(fileName.getText());
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

        addToPanel(mainPanel, labelConstraints, 0, 0, new JLabel("Rewind on end of file: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 0, rewindOnEOF = new JCheckBox());
        addToPanel(mainPanel, labelConstraints, 0, 1, new JLabel("Data file path: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 1, fileName = new JTextField(20));
        addToPanel(mainPanel, labelConstraints, 2, 1, browseButton = new JButton("Browse..."));

        GuiBuilderHelper.strechButtonToComponent(fileName, browseButton);

        editConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(2, 0, 0, 0);

        browseButton.addActionListener(new BrowseAction(fileName));

        addToPanel(mainPanel, labelConstraints, 0, 2, new JLabel("Variable name: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 2, variableName = new JTextField(20));

        editConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(4, 0, 0, 2);

        addToPanel(mainPanel, labelConstraints, 0, 3, checkButton = new JButton("Check File Consistency"));

        labelConstraints.insets = new java.awt.Insets(4, 0, 0, 0);

        checkInfo = new JTextArea();
        addToPanel(mainPanel, editConstraints, 1, 3, GuiBuilderHelper.getTextAreaScrollPaneContainer(checkInfo, 10));
        checkButton.addActionListener(new CheckConsistencyAction(fileName, checkInfo));
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
        rewindOnEOF.setSelected(true);
        variableName.setText("rawData");
        fileName.setText("");
        checkInfo.setText("");
    }
}
