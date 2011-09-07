package kg.apc.jmeter.samplers;

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
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author apc
 */
public class HTTPRawSamplerGui
        extends AbstractSamplerGui {

    public static final String WIKIPAGE = "RawRequest";
    private static final Logger log = LoggingManager.getLoggerForClass();
    private JTextField hostName;
    private JTextField port;
    private JTextField timeout;
    private JCheckBox keepAlive;
    private JCheckBox parseResult;
    private JTextArea requestData;
    private JTextField fileName;
    private JButton browseButton;

    /**
     *
     */
    public HTTPRawSamplerGui() {
        init();
        initFields();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("HTTP Raw Request");
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);

        if (element instanceof HTTPRawSampler) {
            HTTPRawSampler rawSampler = (HTTPRawSampler) element;
            hostName.setText(rawSampler.getHostName());
            port.setText(rawSampler.getPort());
            timeout.setText(rawSampler.getTimeout());
            keepAlive.setSelected(rawSampler.isUseKeepAlive());
            requestData.setText(rawSampler.getRequestData());
            parseResult.setSelected(rawSampler.isParseResult());
            fileName.setText(rawSampler.getFileToSend());
        }
    }

    @Override
    public TestElement createTestElement() {
        HTTPRawSampler sampler = new HTTPRawSampler();
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
    public void modifyTestElement(TestElement sampler) {
        super.configureTestElement(sampler);

        if (sampler instanceof HTTPRawSampler) {
            HTTPRawSampler rawSampler = (HTTPRawSampler) sampler;
            rawSampler.setHostName(hostName.getText());
            rawSampler.setPort(port.getText());
            rawSampler.setUseKeepAlive(keepAlive.isSelected());
            rawSampler.setTimeout(timeout.getText());
            rawSampler.setRequestData(transformCRLF(requestData.getText()));
            rawSampler.setFileToSend(fileName.getText());
            rawSampler.setParseResult(parseResult.isSelected());
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

        addToPanel(mainPanel, labelConstraints, 0, 1, new JLabel("Hostname: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 1, hostName = new JTextField(20));

        editConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(2, 0, 0, 0);

        addToPanel(mainPanel, labelConstraints, 0, 2, new JLabel("TCP Port: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 2, port = new JTextField(20));

        addToPanel(mainPanel, labelConstraints, 0, 3, new JLabel("Timeout: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 3, timeout = new JTextField(20));

        addToPanel(mainPanel, labelConstraints, 0, 4, new JLabel("Keep connection open: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 4, keepAlive = new JCheckBox());

        addToPanel(mainPanel, labelConstraints, 0, 5, new JLabel("Request Data: ", JLabel.RIGHT));

        editConstraints.fill = GridBagConstraints.BOTH;
        requestData = new JTextArea();
        addToPanel(mainPanel, editConstraints, 1, 5, GuiBuilderHelper.getTextAreaScrollPaneContainer(requestData, 20));


        editConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(4, 0, 0, 0);

        addToPanel(mainPanel, labelConstraints, 0, 6, new JLabel("Data file path: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 6, fileName = new JTextField(20));
        addToPanel(mainPanel, labelConstraints, 2, 6, browseButton = new JButton("Browse..."));

        GuiBuilderHelper.strechButtonToComponent(fileName, browseButton);

        browseButton.addActionListener(new BrowseAction(fileName));

        editConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(2, 0, 0, 0);

        addToPanel(mainPanel, labelConstraints, 0, 7, new JLabel("Parse result as HTTP: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 7, parseResult = new JCheckBox());

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
        hostName.setText("localhost");
        port.setText("80");
        timeout.setText("0");
        keepAlive.setSelected(false);
        requestData.setText("GET / HTTP/1.0\r\n"
                + "Host: localhost\r\n"
                + "Connection: close\r\n"
                + "\r\n");
        parseResult.setSelected(true);
        fileName.setText("");
    }
}
