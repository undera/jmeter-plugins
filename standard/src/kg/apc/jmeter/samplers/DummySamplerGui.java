// TODO: resolve scrolling issue here and in all other samplers
package kg.apc.jmeter.samplers;

import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.gui.GuiBuilderHelper;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;
import java.awt.*;

public class DummySamplerGui
        extends AbstractSamplerGui {

    public static final String WIKIPAGE = "DummySampler";
    private JCheckBox isSuccessful;
    private JTextField responseCode;
    private JTextField responseMessage;
    private JTextField responseTime;
    private JTextArea responseData;
    private JTextArea requestData;
    private JCheckBox isWaiting;
    private JTextField latency;
    private JTextField connect;


    public DummySamplerGui() {
        init();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Dummy Sampler");
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);

        isSuccessful.setSelected(element.getPropertyAsBoolean(DummySampler.IS_SUCCESSFUL));
        isWaiting.setSelected(element.getPropertyAsBoolean(DummySampler.IS_WAITING));
        responseCode.setText(element.getPropertyAsString(DummySampler.RESPONSE_CODE));
        responseMessage.setText(element.getPropertyAsString(DummySampler.RESPONSE_MESSAGE));
        requestData.setText(element.getPropertyAsString(DummySampler.REQUEST_DATA));
        responseData.setText(element.getPropertyAsString(DummySampler.RESPONSE_DATA));
        responseTime.setText(element.getPropertyAsString(DummySampler.RESPONSE_TIME));
        latency.setText(element.getPropertyAsString(DummySampler.LATENCY));
        connect.setText(element.getPropertyAsString(DummySampler.CONNECT));
    }

    @Override
    public TestElement createTestElement() {
        DummySampler sampler = new DummySampler();
        modifyTestElement(sampler);
        sampler.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return sampler;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     *
     * @param sampler Sampler
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    @Override
    public void modifyTestElement(TestElement sampler) {
        super.configureTestElement(sampler);

        if (sampler instanceof DummySampler) {
            DummySampler dummySampler = (DummySampler) sampler;
            dummySampler.setSimulateWaiting(isWaiting.isSelected());
            dummySampler.setSuccessful(isSuccessful.isSelected());
            dummySampler.setResponseCode(responseCode.getText());
            dummySampler.setResponseMessage(responseMessage.getText());
            dummySampler.setRequestData(requestData.getText());
            dummySampler.setResponseData(responseData.getText());
            dummySampler.setResponseTime(responseTime.getText());
            dummySampler.setLatency(latency.getText());
            dummySampler.setConnectTime(connect.getText());
        }
    }

    @Override
    public void clearGui() {
        super.clearGui();
        initFields();
    }

    private void initFields() {
        isSuccessful.setSelected(true);
        isWaiting.setSelected(true);
        responseCode.setText("200");
        responseMessage.setText("OK");
        requestData.setText("Dummy Sampler used to simulate requests and responses\nwithout actual network activity. This helps debugging tests.");
        responseData.setText("Dummy Sampler used to simulate requests and responses\nwithout actual network activity. This helps debugging tests.");
        responseTime.setText("${__Random(50,500)}");
        latency.setText("${__Random(1,50)}");
        connect.setText("${__Random(1,5)}");
    }

    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName();
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

        addToPanel(mainPanel, labelConstraints, 0, 0, new JLabel("Successful sample: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 0, isSuccessful = new JCheckBox());
        addToPanel(mainPanel, labelConstraints, 0, 1, new JLabel("Response Code (eg 200): ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 1, responseCode = new JTextField(20));

        editConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(2, 0, 0, 0);

        addToPanel(mainPanel, labelConstraints, 0, 2, new JLabel("Response Message (eg OK): ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 2, responseMessage = new JTextField(20));
        addToPanel(mainPanel, labelConstraints, 0, 3, new JLabel("Connect Time (milliseconds): ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 3, connect = new JTextField(20));
        addToPanel(mainPanel, labelConstraints, 0, 4, new JLabel("Latency (milliseconds): ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 4, latency = new JTextField(20));
        addToPanel(mainPanel, labelConstraints, 0, 5, new JLabel("Response Time (milliseconds): ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 5, responseTime = new JTextField(20));
        addToPanel(mainPanel, labelConstraints, 0, 6, new JLabel("Simulate Response Time (sleep): ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 6, isWaiting = new JCheckBox());

        editConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(4, 0, 0, 0);

        addToPanel(mainPanel, labelConstraints, 0, 7, new JLabel("Request Data: ", JLabel.RIGHT));
        editConstraints.fill = GridBagConstraints.BOTH;
        requestData = new JTextArea();
        addToPanel(mainPanel, editConstraints, 1, 7, GuiBuilderHelper.getTextAreaScrollPaneContainer(requestData, 10));

        addToPanel(mainPanel, labelConstraints, 0, 8, new JLabel("Response Data: ", JLabel.RIGHT));
        editConstraints.fill = GridBagConstraints.BOTH;

        responseData = new JTextArea();
        addToPanel(mainPanel, editConstraints, 1, 8, GuiBuilderHelper.getTextAreaScrollPaneContainer(responseData, 10));

        JPanel container = new JPanel(new BorderLayout());
        container.add(mainPanel, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);
    }

    private void addToPanel(JPanel panel, GridBagConstraints constraints, int col, int row, JComponent component) {
        constraints.gridx = col;
        constraints.gridy = row;
        panel.add(component, constraints);
    }
}
