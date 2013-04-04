package com.googlecode.jmeter.plugins.webdriver.config.gui;

import com.googlecode.jmeter.plugins.webdriver.config.WebDriverConfig;
import com.googlecode.jmeter.plugins.webdriver.proxy.ProxyType;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class WebDriverConfigGui extends AbstractConfigGui implements ItemListener {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final long serialVersionUID = 241L;

    private static final int PROXY_FIELD_INDENT = 28;

    private JRadioButton directProxy; // synonymous with no proxy

    private JRadioButton autoDetectProxy;

    private JRadioButton systemProxy;

    private JRadioButton manualProxy;

    private JRadioButton pacUrlProxy;

    private JTextField pacUrl;

    private JTextField httpProxyHost;

    private JTextField httpProxyPort;

    private JCheckBox useHttpSettingsForAllProxies;

    private JTextField httpsProxyHost;

    private JTextField httpsProxyPort;

    private JTextField ftpProxyHost;

    private JTextField ftpProxyPort;

    private JTextField socksProxyHost;

    private JTextField socksProxyPort;

    private JTextArea noProxyList;

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

    @Override
    public void clearGui() {
        super.clearGui();

        systemProxy.setSelected(true);
    }

    private void createPacUrlProxy(JPanel panel, ButtonGroup group) {
        pacUrlProxy = new JRadioButton("Automatic proxy configuration URL");
        group.add(pacUrlProxy);
        panel.add(pacUrlProxy);

        pacUrlProxy.addItemListener(this);

        JPanel pacUrlPanel = new HorizontalPanel();
        pacUrl = new JTextField();
        pacUrl.setEnabled(false);
        pacUrlPanel.add(pacUrl, BorderLayout.CENTER);
        pacUrlPanel.setBorder(BorderFactory.createEmptyBorder(0, PROXY_FIELD_INDENT, 0, 0));
        panel.add(pacUrlPanel);
    }

    private void createManualProxy(JPanel panel, ButtonGroup group) {
        manualProxy = new JRadioButton("Manual proxy configuration");
        group.add(manualProxy);
        panel.add(manualProxy);

        manualProxy.addItemListener(this);

        JPanel manualPanel = new VerticalPanel();
        manualPanel.setBorder(BorderFactory.createEmptyBorder(0, PROXY_FIELD_INDENT, 0, 0));

        httpProxyHost = new JTextField();
        httpProxyPort = new JTextField();
        manualPanel.add(createProxyHostAndPortPanel(httpProxyHost, httpProxyPort, "HTTP Proxy:"));
        useHttpSettingsForAllProxies = new JCheckBox("Use HTTP proxy server for all protocols");
        useHttpSettingsForAllProxies.setSelected(true);
        useHttpSettingsForAllProxies.setEnabled(false);
        useHttpSettingsForAllProxies.addItemListener(this);
        manualPanel.add(useHttpSettingsForAllProxies);

        httpsProxyHost = new JTextField();
        httpsProxyPort = new JTextField();
        manualPanel.add(createProxyHostAndPortPanel(httpsProxyHost, httpsProxyPort, "SSL Proxy:"));

        ftpProxyHost = new JTextField();
        ftpProxyPort = new JTextField();
        manualPanel.add(createProxyHostAndPortPanel(ftpProxyHost, ftpProxyPort, "FTP Proxy:"));

        socksProxyHost = new JTextField();
        socksProxyPort = new JTextField();
        manualPanel.add(createProxyHostAndPortPanel(socksProxyHost, socksProxyPort, "SOCKS Proxy:"));

        manualPanel.add(createNoProxyPanel());

        panel.add(manualPanel);
    }

    private JPanel createNoProxyPanel() {
        JPanel noProxyPanel = new VerticalPanel();
        JLabel noProxyListLabel = new JLabel("No Proxy for:");
        noProxyPanel.add(noProxyListLabel);

        noProxyList = new JTextArea(3,10);
        noProxyList.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        noProxyList.setEnabled(false);
        noProxyPanel.add(noProxyList);

        JLabel noProxyExample = new JLabel("Example: .jmeter.org, .com.au, 192.168.1.0/24");
        noProxyPanel.add(noProxyExample);

        return noProxyPanel;
    }

    private JPanel createProxyHostAndPortPanel(JTextField proxyHost, JTextField proxyPort, String label) {
        JPanel httpPanel = new HorizontalPanel();
        JLabel httpProxyHostLabel = new JLabel(label);
        httpPanel.add(httpProxyHostLabel);
        httpPanel.add(proxyHost);
        proxyHost.setEnabled(false);
        JLabel httpProxyPortLabel = new JLabel("Port:");
        httpPanel.add(httpProxyPortLabel);
        httpPanel.add(proxyPort);
        proxyPort.setEnabled(false);
        return httpPanel;
    }

    private void createSystemProxy(JPanel panel, ButtonGroup group) {
        systemProxy = new JRadioButton("Use system proxy settings");
        group.add(systemProxy);
        panel.add(systemProxy);
    }

    private void createAutoDetectProxy(JPanel panel, ButtonGroup group) {
        autoDetectProxy = new JRadioButton("Auto-detect proxy settings for this network");
        group.add(autoDetectProxy);
        panel.add(autoDetectProxy);
    }

    private void createDirectProxy(JPanel panel, ButtonGroup group) {
        directProxy = new JRadioButton("No proxy");
        group.add(directProxy);
        panel.add(directProxy);
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));

        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);

        // MAIN PANEL
        JPanel mainPanel = new VerticalPanel();
        ButtonGroup group = new ButtonGroup();

        createDirectProxy(mainPanel, group);
        createAutoDetectProxy(mainPanel, group);
        createSystemProxy(mainPanel, group);
        createManualProxy(mainPanel, group);
        createPacUrlProxy(mainPanel, group);

        systemProxy.setSelected(true);

        add(mainPanel, BorderLayout.CENTER);
    }

    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        if(itemEvent.getSource() == pacUrlProxy) {
            pacUrl.setEnabled(itemEvent.getStateChange() == ItemEvent.SELECTED);
        } else if(itemEvent.getSource() == manualProxy) {
            httpProxyHost.setEnabled(itemEvent.getStateChange() == ItemEvent.SELECTED);
            httpProxyPort.setEnabled(itemEvent.getStateChange() == ItemEvent.SELECTED);
            useHttpSettingsForAllProxies.setEnabled(itemEvent.getStateChange() == ItemEvent.SELECTED);
            noProxyList.setEnabled(itemEvent.getStateChange() == ItemEvent.SELECTED);
        } else if(itemEvent.getSource() == useHttpSettingsForAllProxies) {
            httpsProxyHost.setEnabled(itemEvent.getStateChange() == ItemEvent.DESELECTED);
            httpsProxyPort.setEnabled(itemEvent.getStateChange() == ItemEvent.DESELECTED);
            ftpProxyHost.setEnabled(itemEvent.getStateChange() == ItemEvent.DESELECTED);
            ftpProxyPort.setEnabled(itemEvent.getStateChange() == ItemEvent.DESELECTED);
            socksProxyHost.setEnabled(itemEvent.getStateChange() == ItemEvent.DESELECTED);
            socksProxyPort.setEnabled(itemEvent.getStateChange() == ItemEvent.DESELECTED);
        }
    }
}
