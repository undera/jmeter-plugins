package kg.apc.jmeter.modifiers;

import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.gui.GuiBuilderHelper;
import org.apache.jmeter.processor.gui.AbstractPostProcessorGui;
import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;
import java.awt.*;

public class FifoPutPostProcessorGui extends AbstractPostProcessorGui {

    public static final String WIKIPAGE = "InterThreadCommunication";
    private JTextField queueName;
    private JTextArea value;

    public FifoPutPostProcessorGui() {
        super();
        init();
        initFields();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Inter-Thread Communication PostProcessor");
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        if (element instanceof FifoPutPostProcessor) {
            FifoPutPostProcessor el = (FifoPutPostProcessor) element;
            queueName.setText(el.getQueueName());
            value.setText(el.getValue());
        }
    }

    @Override
    public TestElement createTestElement() {
        FifoPutPostProcessor preproc = new FifoPutPostProcessor();
        modifyTestElement(preproc);
        preproc.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return preproc;
    }

    @Override
    public void modifyTestElement(TestElement te) {
        configureTestElement(te);
        if (te instanceof FifoPutPostProcessor) {
            FifoPutPostProcessor preproc = (FifoPutPostProcessor) te;
            preproc.setValue(value.getText());
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

        addToPanel(mainPanel, labelConstraints, 0, 0, new JLabel("FIFO Queue Name to Put Data Into: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 0, queueName = new JTextField(20));

        editConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(4, 0, 0, 0);

        addToPanel(mainPanel, labelConstraints, 0, 6, new JLabel("Value to Put: ", JLabel.RIGHT));
        editConstraints.fill = GridBagConstraints.BOTH;
        value = new JTextArea();
        addToPanel(mainPanel, editConstraints, 1, 6, GuiBuilderHelper.getTextAreaScrollPaneContainer(value, 10));

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
        value.setText("");
    }
}
