package com.googlecode.jmeter.plugins.webdriver.config.gui;

import com.googlecode.jmeter.plugins.webdriver.config.WebDriverConfig;
import com.googlecode.jmeter.plugins.webdriver.proxy.ProxyType;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import java.awt.*;

public class WebDriverConfigGui extends AbstractConfigGui {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final long serialVersionUID = 241L;

    private JTextField server;

    private JTextField port;

    private JTextField remoteFile;

    private JTextField localFile;

    private JTextArea inputData;

    private JCheckBox binaryMode;

    private JCheckBox saveResponseData;

    private JRadioButton getBox;

    private JRadioButton putBox;

    private JRadioButton directProxy; // synonymous with no proxy

    private JRadioButton autoDetectProxy;

    private JRadioButton systemProxy;

    private JRadioButton manualProxy;

    private JRadioButton pacUrlProxy;

    private JTextField pacUrl;

    public WebDriverConfigGui() {
        init();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Web Driver Config");
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }


    @Override
    public void configure(TestElement element) {
        super.configure(element);
        if(element instanceof WebDriverConfig) {
            WebDriverConfig webDriverConfig = (WebDriverConfig)element;
            switch (webDriverConfig.getProxyType()) {
                case DIRECT:
                    directProxy.setSelected(true);
                    break;
                case AUTO_DETECT:
                    autoDetectProxy.setSelected(true);
                    break;
                case MANUAL:
                    manualProxy.setSelected(true);
                    break;
                case PROXY_PAC:
                    pacUrlProxy.setSelected(true);
                    break;
                default:
                    systemProxy.setSelected(true); // fallback to system proxy
            }
        }
    }

    @Override
    public TestElement createTestElement() {
        WebDriverConfig element = new WebDriverConfig();
        modifyTestElement(element);
        return element;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     *
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    @Override
    public void modifyTestElement(TestElement element) {
        configureTestElement(element);
        if(element instanceof WebDriverConfig) {
            WebDriverConfig webDriverConfig = (WebDriverConfig)element;
            if(directProxy.isSelected()) {
                webDriverConfig.setProxyType(ProxyType.DIRECT);
            } else if(autoDetectProxy.isSelected()) {
                webDriverConfig.setProxyType(ProxyType.AUTO_DETECT);
            } else if(pacUrlProxy.isSelected()) {
                webDriverConfig.setProxyType(ProxyType.PROXY_PAC);
            } else if(manualProxy.isSelected()) {
                webDriverConfig.setProxyType(ProxyType.MANUAL);
            } else {
                webDriverConfig.setProxyType(ProxyType.SYSTEM); // fallback
            }

            log.info("Proxy type is: "+webDriverConfig.getProxyType());
        }
    }

    /**
     * Implements JMeterGUIComponent.clearGui
     */
    @Override
    public void clearGui() {
        super.clearGui();

        server.setText(""); //$NON-NLS-1$
        port.setText(""); //$NON-NLS-1$
        remoteFile.setText(""); //$NON-NLS-1$
        localFile.setText(""); //$NON-NLS-1$
        inputData.setText(""); //$NON-NLS-1$
        binaryMode.setSelected(false);
        saveResponseData.setSelected(false);
        getBox.setSelected(true);
        putBox.setSelected(false);

        systemProxy.setSelected(true);
    }

    private JPanel createServerPanel() {
        JLabel label = new JLabel(JMeterUtils.getResString("server")); //$NON-NLS-1$

        server = new JTextField(10);
        label.setLabelFor(server);

        JPanel serverPanel = new JPanel(new BorderLayout(5, 0));
        serverPanel.add(label, BorderLayout.WEST);
        serverPanel.add(server, BorderLayout.CENTER);
        return serverPanel;
    }

    private JPanel getPortPanel() {
        port = new JTextField(4);

        JLabel label = new JLabel(JMeterUtils.getResString("web_server_port")); // $NON-NLS-1$
        label.setLabelFor(port);

        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(label, BorderLayout.WEST);
        panel.add(port, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createLocalFilenamePanel() {
        JLabel label = new JLabel(JMeterUtils.getResString("ftp_local_file")); //$NON-NLS-1$

        localFile = new JTextField(10);
        label.setLabelFor(localFile);

        JPanel filenamePanel = new JPanel(new BorderLayout(5, 0));
        filenamePanel.add(label, BorderLayout.WEST);
        filenamePanel.add(localFile, BorderLayout.CENTER);
        return filenamePanel;
    }

    private JPanel createLocalFileContentsPanel() {
        JLabel label = new JLabel(JMeterUtils.getResString("ftp_local_file_contents")); //$NON-NLS-1$

        inputData = new JTextArea();
        label.setLabelFor(inputData);

        JPanel contentsPanel = new JPanel(new BorderLayout(5, 0));
        contentsPanel.add(label, BorderLayout.WEST);
        contentsPanel.add(inputData, BorderLayout.CENTER);
        return contentsPanel;
    }

    private JPanel createRemoteFilenamePanel() {
        JLabel label = new JLabel(JMeterUtils.getResString("ftp_remote_file")); //$NON-NLS-1$

        remoteFile = new JTextField(10);
        label.setLabelFor(remoteFile);

        JPanel filenamePanel = new JPanel(new BorderLayout(5, 0));
        filenamePanel.add(label, BorderLayout.WEST);
        filenamePanel.add(remoteFile, BorderLayout.CENTER);
        return filenamePanel;
    }

    private JPanel createOptionsPanel() {

        ButtonGroup group = new ButtonGroup();

        getBox = new JRadioButton(JMeterUtils.getResString("ftp_get")); //$NON-NLS-1$
        group.add(getBox);
        getBox.setSelected(true);

        putBox = new JRadioButton(JMeterUtils.getResString("ftp_put")); //$NON-NLS-1$
        group.add(putBox);

        binaryMode = new JCheckBox(JMeterUtils.getResString("ftp_binary_mode")); //$NON-NLS-1$
        saveResponseData = new JCheckBox(JMeterUtils.getResString("ftp_save_response_data")); //$NON-NLS-1$


        JPanel optionsPanel = new HorizontalPanel();
        optionsPanel.add(getBox);
        optionsPanel.add(putBox);
        optionsPanel.add(binaryMode);
        optionsPanel.add(saveResponseData);
        return optionsPanel;
    }

    private JPanel createProxySelectionPanel() {
        JPanel selectionsPanel = new VerticalPanel();
        ButtonGroup group = new ButtonGroup();

        directProxy = new JRadioButton("No proxy");
        group.add(directProxy);
        selectionsPanel.add(directProxy);
        autoDetectProxy = new JRadioButton("Auto-detect proxy settings for this network");
        group.add(autoDetectProxy);
        selectionsPanel.add(autoDetectProxy);
        systemProxy = new JRadioButton("Use system proxy settings");
        group.add(systemProxy);
        selectionsPanel.add(systemProxy);
        manualProxy = new JRadioButton("Manual proxy configuration");
        group.add(manualProxy);
        selectionsPanel.add(manualProxy);
        pacUrlProxy = new JRadioButton("Automatic proxy configuration URL");
        group.add(pacUrlProxy);
        selectionsPanel.add(pacUrlProxy);

        JPanel pacUrlPanel = new HorizontalPanel();
        pacUrl = new JTextField();
        pacUrl.setEnabled(false);
        pacUrlPanel.add(pacUrl, BorderLayout.CENTER);
        selectionsPanel.add(pacUrlPanel);

        systemProxy.setSelected(true);

        return selectionsPanel;
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));

        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);

        // MAIN PANEL
        VerticalPanel mainPanel = new VerticalPanel();
        JPanel serverPanel = new HorizontalPanel();
        serverPanel.add(createServerPanel(), BorderLayout.CENTER);
        serverPanel.add(getPortPanel(), BorderLayout.EAST);
        mainPanel.add(serverPanel);
        mainPanel.add(createRemoteFilenamePanel());
        mainPanel.add(createLocalFilenamePanel());
        mainPanel.add(createLocalFileContentsPanel());
        mainPanel.add(createOptionsPanel());
        mainPanel.add(createProxySelectionPanel());

        add(mainPanel, BorderLayout.CENTER);
    }
}
