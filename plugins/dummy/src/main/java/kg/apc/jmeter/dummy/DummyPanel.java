package kg.apc.jmeter.dummy;

import kg.apc.jmeter.gui.GuiBuilderHelper;
import kg.apc.jmeter.modifiers.DummySubPostProcessor;
import kg.apc.jmeter.samplers.DummySampler;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.reflect.ClassFinder;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class DummyPanel extends JPanel {
    private JCheckBox isSuccessful;
    private JTextField responseCode;
    private JTextField responseMessage;
    private JTextField responseTime;
    private JTextArea responseData;
    private JTextArea requestData;
    private JCheckBox isWaiting;
    private JTextField latency;
    private JTextField connect;
    private JTextField url;
    private JComboBox<String> resultClass;

    public DummyPanel() {
        super(new GridBagLayout());
    }

    public void configure(TestElement element) {
        isSuccessful.setSelected(element.getPropertyAsBoolean(DummyElement.IS_SUCCESSFUL));
        isWaiting.setSelected(element.getPropertyAsBoolean(DummyElement.IS_WAITING));
        responseCode.setText(element.getPropertyAsString(DummyElement.RESPONSE_CODE));
        responseMessage.setText(element.getPropertyAsString(DummyElement.RESPONSE_MESSAGE));
        requestData.setText(element.getPropertyAsString(DummyElement.REQUEST_DATA));
        responseData.setText(element.getPropertyAsString(DummyElement.RESPONSE_DATA));
        responseTime.setText(element.getPropertyAsString(DummyElement.RESPONSE_TIME));
        latency.setText(element.getPropertyAsString(DummyElement.LATENCY));
        connect.setText(element.getPropertyAsString(DummyElement.CONNECT));
        url.setText(element.getPropertyAsString(DummyElement.URL));
        resultClass.setSelectedItem(element.getPropertyAsString(DummyElement.RESULT_CLASS));
    }

    public void modifyTestElement(TestElement te) {
        DummyElement dummy;
        if (te instanceof DummySampler) {
            dummy = ((DummySampler) te).getDummy();
        } else if (te instanceof DummySubPostProcessor) {
            dummy = ((DummySubPostProcessor) te).getDummy();
        } else {
            return;
        }

        dummy.setSimulateWaiting(isWaiting.isSelected());
        dummy.setSuccessful(isSuccessful.isSelected());
        dummy.setResponseCode(responseCode.getText());
        dummy.setResponseMessage(responseMessage.getText());
        dummy.setRequestData(requestData.getText());
        dummy.setResponseData(responseData.getText());
        dummy.setResponseTime(responseTime.getText());
        dummy.setLatency(latency.getText());
        dummy.setConnectTime(connect.getText());
        dummy.setURL(url.getText());
        dummy.setResultClass(Objects.requireNonNull(resultClass.getSelectedItem()).toString());
    }

    public void initFields() {
        isSuccessful.setSelected(true);
        isWaiting.setSelected(true);
        responseCode.setText("200");
        responseMessage.setText("OK");
        requestData.setText("Dummy Sampler used to simulate requests and responses\nwithout actual network activity. This helps debugging tests.");
        responseData.setText("Dummy Sampler used to simulate requests and responses\nwithout actual network activity. This helps debugging tests.");
        responseTime.setText("${__Random(50,500)}");
        latency.setText("${__Random(1,50)}");
        connect.setText("${__Random(1,5)}");
        url.setText("");
        resultClass.setSelectedItem(SampleResult.class.getCanonicalName());
    }

    public JPanel init() {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.anchor = GridBagConstraints.FIRST_LINE_END;

        GridBagConstraints editConstraints = new GridBagConstraints();
        editConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        editConstraints.weightx = 1.0;
        editConstraints.fill = GridBagConstraints.HORIZONTAL;


        addToPanel(this, labelConstraints, 0, 0, new JLabel("Successful sample: ", JLabel.RIGHT));
        addToPanel(this, editConstraints, 1, 0, isSuccessful = new JCheckBox());
        addToPanel(this, labelConstraints, 0, 1, new JLabel("Response Code (eg 200): ", JLabel.RIGHT));
        addToPanel(this, editConstraints, 1, 1, responseCode = new JTextField(20));

        editConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(2, 0, 0, 0);

        addToPanel(this, labelConstraints, 0, 2, new JLabel("Response Message (eg OK): ", JLabel.RIGHT));
        addToPanel(this, editConstraints, 1, 2, responseMessage = new JTextField(20));
        addToPanel(this, labelConstraints, 0, 3, new JLabel("Connect Time (milliseconds): ", JLabel.RIGHT));
        addToPanel(this, editConstraints, 1, 3, connect = new JTextField(20));
        addToPanel(this, labelConstraints, 0, 4, new JLabel("Latency (milliseconds): ", JLabel.RIGHT));
        addToPanel(this, editConstraints, 1, 4, latency = new JTextField(20));
        addToPanel(this, labelConstraints, 0, 5, new JLabel("Response Time (milliseconds): ", JLabel.RIGHT));
        addToPanel(this, editConstraints, 1, 5, responseTime = new JTextField(20));
        addToPanel(this, labelConstraints, 0, 6, new JLabel("Simulate Response Time (sleep): ", JLabel.RIGHT));
        addToPanel(this, editConstraints, 1, 6, isWaiting = new JCheckBox());

        editConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(4, 0, 0, 0);

        addToPanel(this, labelConstraints, 0, 7, new JLabel("Request Data: ", JLabel.RIGHT));
        editConstraints.fill = GridBagConstraints.BOTH;
        requestData = new JTextArea();
        addToPanel(this, editConstraints, 1, 7, GuiBuilderHelper.getTextAreaScrollPaneContainer(requestData, 10));

        addToPanel(this, labelConstraints, 0, 8, new JLabel("Response Data: ", JLabel.RIGHT));
        editConstraints.fill = GridBagConstraints.BOTH;

        responseData = new JTextArea();
        addToPanel(this, editConstraints, 1, 8, GuiBuilderHelper.getTextAreaScrollPaneContainer(responseData, 10));

        addToPanel(this, labelConstraints, 0, 9, new JLabel("URL: ", JLabel.RIGHT));
        addToPanel(this, editConstraints, 1, 9, url = new JTextField());

        String[] classesThatExtend;
        try {
            String[] paths = JMeterUtils.getSearchPaths();
            Class[] lookup = {SampleResult.class};
            classesThatExtend = ClassFinder.findClassesThatExtend(paths, lookup).toArray(new String[0]);
            if (classesThatExtend.length < 1) {
                throw new IOException("Go to default");
            }
        } catch (IOException e) {
            classesThatExtend = new String[]{SampleResult.class.getCanonicalName()};
        }
        resultClass = new JComboBox<>(classesThatExtend);
        addToPanel(this, labelConstraints, 0, 10, new JLabel("SampleResult class: ", JLabel.RIGHT));
        addToPanel(this, editConstraints, 1, 10, resultClass);

        JPanel container = new JPanel(new BorderLayout());
        container.add(this, BorderLayout.NORTH);
        return container;
    }

    private void addToPanel(JPanel panel, GridBagConstraints constraints, int col, int row, JComponent component) {
        constraints.gridx = col;
        constraints.gridy = row;
        panel.add(component, constraints);
    }
}
