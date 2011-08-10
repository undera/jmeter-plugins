package kg.apc.jmeter.samplers;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.gui.GuiBuilderHelper;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;

/**
 *
 * @author undera
 */
public class UDPSamplerGui extends AbstractSamplerGui {

    private static final String WIKIPAGE = "UDPRequest";
    private JTextField hostName;
    private JTextField port;
    private JCheckBox waitResponse;
    private JTextField timeout;
    private JTextField messageEncodeClass;
    private JTextArea requestData;

    public UDPSamplerGui() {
        init();
        initFields();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("UDP Request");
    }

   @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);

        if (element instanceof UDPSampler) {
            UDPSampler sampler = (UDPSampler) element;
            hostName.setText(sampler.getHostName());
            port.setText(sampler.getPort());
            timeout.setText(sampler.getTimeout());
            waitResponse.setSelected(sampler.isWaitResponse());
            messageEncodeClass.setText(sampler.getEncoderClass());
            requestData.setText(sampler.getRequestData());
        }
    }

   @Override
    public TestElement createTestElement() {
        UDPSampler sampler = new UDPSampler();
        modifyTestElement(sampler);
        sampler.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return sampler;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     *
     * @param sampler
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
   @Override
    public void modifyTestElement(TestElement el) {
        super.configureTestElement(el);

        if (el instanceof UDPSampler) {
            UDPSampler sampler = (UDPSampler) el;
            sampler.setHostName(hostName.getText());
            sampler.setPort(port.getText());
            sampler.setWaitResponse(waitResponse.isSelected());
            sampler.setTimeout(timeout.getText());
            sampler.setRequestData(transformCRLF(requestData.getText()));
            sampler.setEncoderClass(messageEncodeClass.getText());
        }
    }

    /**
     * first replace removes old \r\n
     * second eliminates orphan \r
     * third make all newlines - old and new  like \r\n
     */
    private String transformCRLF(String str) {
        return str.replace("\r\n", "\n").replace("\r", "").replace("\n", "\r\n");
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

        addToPanel(mainPanel, labelConstraints, 0, 1, new JLabel("Hostname/IP: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 1, hostName = new JTextField(20));

        editConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(2, 0, 0, 0);

        addToPanel(mainPanel, labelConstraints, 0, 2, new JLabel("UDP Port: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 2, port = new JTextField(20));

        addToPanel(mainPanel, labelConstraints, 0, 3, new JLabel("Wait for Response: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 3, waitResponse = new JCheckBox());

        addToPanel(mainPanel, labelConstraints, 0, 4, new JLabel("Response Timeout: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 4, timeout = new JTextField(20));

        addToPanel(mainPanel, labelConstraints, 0, 5, new JLabel("Data Encode/Decode class: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 5, messageEncodeClass = new JTextField(20));

        editConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(4, 0, 0, 0);

        addToPanel(mainPanel, labelConstraints, 0, 6, new JLabel("Request Data: ", JLabel.RIGHT));

        editConstraints.fill = GridBagConstraints.BOTH;
        requestData = new JTextArea();
        addToPanel(mainPanel, editConstraints, 1, 6, GuiBuilderHelper.getTextAreaScrollPaneContainer(requestData, 10));

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
        hostName.setText("");
        port.setText("");
        timeout.setText("1000");
        waitResponse.setSelected(true);
        messageEncodeClass.setText(HexStringUDPDecoder.class.getCanonicalName());
        requestData.setText("");
    }

}
