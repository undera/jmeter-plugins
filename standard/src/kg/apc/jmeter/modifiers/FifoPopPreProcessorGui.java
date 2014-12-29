package kg.apc.jmeter.modifiers;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.*;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.processor.gui.AbstractPreProcessorGui;
import org.apache.jmeter.testelement.TestElement;

public class FifoPopPreProcessorGui extends AbstractPreProcessorGui {

    public static final String WIKIPAGE = "InterThreadCommunication";
    private JTextField queueName;
    private JTextField variableName;
    private JTextField timeout;

    public FifoPopPreProcessorGui() {
        super();
        init();
        initFields();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Inter-Thread Communication PreProcessor");
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        if (element instanceof FifoPopPreProcessor) {
            FifoPopPreProcessor el = (FifoPopPreProcessor) element;
            queueName.setText(el.getQueueName());
            variableName.setText(el.getVarName());
            timeout.setText(el.getTimeout());
        }
    }

    @Override
    public TestElement createTestElement() {
        FifoPopPreProcessor preproc = new FifoPopPreProcessor();
        modifyTestElement(preproc);
        preproc.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return preproc;
    }

    @Override
    public void modifyTestElement(TestElement te) {
        configureTestElement(te);
        if (te instanceof FifoPopPreProcessor) {
            FifoPopPreProcessor preproc = (FifoPopPreProcessor) te;
            preproc.setTimeout(timeout.getText());
            preproc.setVarName(variableName.getText());
            preproc.setQueueName(queueName.getText());
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

        editConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(2, 0, 0, 0);

        addToPanel(mainPanel, labelConstraints, 0, 0, new JLabel("FIFO Queue Name to Get Data From: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 0, queueName = new JTextField(20));

        addToPanel(mainPanel, labelConstraints, 0, 1, new JLabel("Variable Name to Store Data: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 1, variableName = new JTextField(20));

        addToPanel(mainPanel, labelConstraints, 0, 2, new JLabel("Timeout: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 2, timeout = new JTextField(20));

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
        queueName.setText("SYNC_FIFO");
        variableName.setText("gotData");
        timeout.setText("");
    }
}
