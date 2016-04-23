package com.googlecode.jmeter.plugins.webdriver.config.gui;

import com.googlecode.jmeter.plugins.webdriver.config.WebDriverConfig;
import com.googlecode.jmeter.plugins.webdriver.proxy.ProxyType;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;

public abstract class WebDriverConfigGui extends AbstractConfigGui implements ItemListener {

    private static final long serialVersionUID = 100L;
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getIntegerInstance();
    private static final int PROXY_FIELD_INDENT = 28;
    private static final int DEFAULT_PROXY_PORT = 8080;
    private static final String DEFAULT_NO_PROXY_LIST = "localhost";

    static {
        NUMBER_FORMAT.setGroupingUsed(false);
    }

    JRadioButton directProxy; // synonymous with no proxy

    JRadioButton autoDetectProxy;

    JRadioButton systemProxy;

    JRadioButton manualProxy;

    JRadioButton pacUrlProxy;

    JTextField pacUrl;

    JTextField httpProxyHost;

    JFormattedTextField httpProxyPort;

    JCheckBox useHttpSettingsForAllProtocols;

    JTextField httpsProxyHost;

    JFormattedTextField httpsProxyPort;

    JTextField ftpProxyHost;

    JFormattedTextField ftpProxyPort;

    JTextField socksProxyHost;

    JFormattedTextField socksProxyPort;

    JTextArea noProxyList;

    JCheckBox maximizeBrowser;

    /*
     * THE FOLLOWING ATTRIBUTES ARE EXPERIMENTAL - USE WITH CAUTION
     */
    JCheckBox recreateBrowserOnIterationStart;
    JCheckBox devMode;

    public WebDriverConfigGui() {
        createGui();
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        if (element instanceof WebDriverConfig) {
            WebDriverConfig webDriverConfig = (WebDriverConfig) element;
            if (isProxyEnabled()) {
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
                pacUrl.setText(webDriverConfig.getProxyPacUrl());
                httpProxyHost.setText(webDriverConfig.getHttpHost());
                httpProxyPort.setText(String.valueOf(webDriverConfig.getHttpPort()));
                useHttpSettingsForAllProtocols.setSelected(webDriverConfig.isUseHttpSettingsForAllProtocols());
                httpsProxyHost.setText(webDriverConfig.getHttpsHost());
                httpsProxyPort.setText(String.valueOf(webDriverConfig.getHttpsPort()));
                ftpProxyHost.setText(webDriverConfig.getFtpHost());
                ftpProxyPort.setText(String.valueOf(webDriverConfig.getFtpPort()));
                socksProxyHost.setText(webDriverConfig.getSocksHost());
                socksProxyPort.setText(String.valueOf(webDriverConfig.getSocksPort()));
                noProxyList.setText(webDriverConfig.getNoProxyHost());
            }
            if (isExperimentalEnabled()) {
                // EXPERIMENTAL
                maximizeBrowser.setSelected(webDriverConfig.isBrowserMaximized());
                recreateBrowserOnIterationStart.setSelected(webDriverConfig.isRecreateBrowserOnIterationStart());
                devMode.setSelected(webDriverConfig.isDevMode());
            }
        }
    }

    @Override
    public void modifyTestElement(TestElement element) {
        configureTestElement(element);
        if (element instanceof WebDriverConfig) {
            WebDriverConfig webDriverConfig = (WebDriverConfig) element;
            if (isProxyEnabled()) {
                if (directProxy.isSelected()) {
                    webDriverConfig.setProxyType(ProxyType.DIRECT);
                } else if (autoDetectProxy.isSelected()) {
                    webDriverConfig.setProxyType(ProxyType.AUTO_DETECT);
                } else if (pacUrlProxy.isSelected()) {
                    webDriverConfig.setProxyType(ProxyType.PROXY_PAC);
                } else if (manualProxy.isSelected()) {
                    webDriverConfig.setProxyType(ProxyType.MANUAL);
                } else {
                    webDriverConfig.setProxyType(ProxyType.SYSTEM); // fallback
                }
                webDriverConfig.setProxyPacUrl(pacUrl.getText());
                webDriverConfig.setHttpHost(httpProxyHost.getText());
                webDriverConfig.setHttpPort(Integer.parseInt(httpProxyPort.getText()));
                webDriverConfig.setUseHttpSettingsForAllProtocols(useHttpSettingsForAllProtocols.isSelected());
                webDriverConfig.setHttpsHost(httpsProxyHost.getText());
                webDriverConfig.setHttpsPort(Integer.parseInt(httpsProxyPort.getText()));
                webDriverConfig.setFtpHost(ftpProxyHost.getText());
                webDriverConfig.setFtpPort(Integer.parseInt(ftpProxyPort.getText()));
                webDriverConfig.setSocksHost(socksProxyHost.getText());
                webDriverConfig.setSocksPort(Integer.parseInt(socksProxyPort.getText()));
                webDriverConfig.setNoProxyHost(noProxyList.getText());
            }
            if (isExperimentalEnabled()) {
                // EXPERIMENTAL
                webDriverConfig.setBrowserMaximized(maximizeBrowser.isSelected());
                webDriverConfig.setRecreateBrowserOnIterationStart(recreateBrowserOnIterationStart.isSelected());
                webDriverConfig.setDevMode(devMode.isSelected());
            }
        }
    }

    @Override
    public void clearGui() {
        super.clearGui();
        if (isProxyEnabled()) {
            systemProxy.setSelected(true);
            pacUrl.setText("");
            httpProxyHost.setText("");
            httpProxyPort.setText(String.valueOf(DEFAULT_PROXY_PORT));
            useHttpSettingsForAllProtocols.setSelected(true);
            httpsProxyHost.setText("");
            httpsProxyPort.setText(String.valueOf(DEFAULT_PROXY_PORT));
            ftpProxyHost.setText("");
            ftpProxyPort.setText(String.valueOf(DEFAULT_PROXY_PORT));
            socksProxyHost.setText("");
            socksProxyPort.setText(String.valueOf(DEFAULT_PROXY_PORT));
            noProxyList.setText(DEFAULT_NO_PROXY_LIST);
        }
        if (isExperimentalEnabled()) {
            maximizeBrowser.setSelected(true);
        }
    }

    private void createGui() {
        setLayout(new BorderLayout(0, 5));

        setBorder(makeBorder());
        add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), getWikiPage()), BorderLayout.NORTH);

        final JTabbedPane tabbedPane = new JTabbedPane();
        if (isProxyEnabled()) {
            tabbedPane.add("Proxy", createProxyPanel());
        }
        tabbedPane.add(browserName(), createBrowserPanel());
        if (isExperimentalEnabled()) {
            tabbedPane.add("Experimental", createExperimentalPanel());
        }

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createExperimentalPanel() {
        JPanel panel = new VerticalPanel();

        // LABEL
        JLabel experimentalLabel = new JLabel("EXPERIMENTAL PROPERTIES - USE AT YOUR DISCRETION");
        experimentalLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        panel.add(experimentalLabel);

        maximizeBrowser = new JCheckBox("Maximize browser window");
        maximizeBrowser.setSelected(true);
        panel.add(maximizeBrowser);

        // EXPERIMENTAL PROPERTIES
        recreateBrowserOnIterationStart = new JCheckBox("Create a new Browser at the start of each iteration");
        recreateBrowserOnIterationStart.setSelected(false);
        panel.add(recreateBrowserOnIterationStart);

        devMode = new JCheckBox("Development Mode (keep browser opened on error)");
        devMode.setSelected(false);
        panel.add(devMode);

        return panel;
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
        httpProxyPort = new JFormattedTextField(NUMBER_FORMAT);
        httpProxyPort.setText(String.valueOf(DEFAULT_PROXY_PORT));
        manualPanel.add(createProxyHostAndPortPanel(httpProxyHost, httpProxyPort, "HTTP Proxy:"));
        useHttpSettingsForAllProtocols = new JCheckBox("Use HTTP proxy server for all protocols");
        useHttpSettingsForAllProtocols.setSelected(true);
        useHttpSettingsForAllProtocols.setEnabled(false);
        useHttpSettingsForAllProtocols.addItemListener(this);
        manualPanel.add(useHttpSettingsForAllProtocols);

        httpsProxyHost = new JTextField();
        httpsProxyPort = new JFormattedTextField(NUMBER_FORMAT);
        httpsProxyPort.setText(String.valueOf(DEFAULT_PROXY_PORT));
        manualPanel.add(createProxyHostAndPortPanel(httpsProxyHost, httpsProxyPort, "SSL Proxy:"));

        ftpProxyHost = new JTextField();
        ftpProxyPort = new JFormattedTextField(NUMBER_FORMAT);
        ftpProxyPort.setText(String.valueOf(DEFAULT_PROXY_PORT));
        manualPanel.add(createProxyHostAndPortPanel(ftpProxyHost, ftpProxyPort, "FTP Proxy:"));

        socksProxyHost = new JTextField();
        socksProxyPort = new JFormattedTextField(NUMBER_FORMAT);
        socksProxyPort.setText(String.valueOf(DEFAULT_PROXY_PORT));
        manualPanel.add(createProxyHostAndPortPanel(socksProxyHost, socksProxyPort, "SOCKS Proxy:"));

        manualPanel.add(createNoProxyPanel());

        panel.add(manualPanel);
    }

    private JPanel createNoProxyPanel() {
        JPanel noProxyPanel = new VerticalPanel();
        JLabel noProxyListLabel = new JLabel("No Proxy for:");
        noProxyPanel.add(noProxyListLabel);

        noProxyList = new JTextArea(3, 10);
        noProxyList.setText(DEFAULT_NO_PROXY_LIST);
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

    protected JPanel createProxyPanel() {
        JPanel mainPanel = new VerticalPanel();
        ButtonGroup group = new ButtonGroup();

        createDirectProxy(mainPanel, group);
        createAutoDetectProxy(mainPanel, group);
        createSystemProxy(mainPanel, group);
        createManualProxy(mainPanel, group);
        createPacUrlProxy(mainPanel, group);

        systemProxy.setSelected(true);
        return mainPanel;
    }

    protected abstract JPanel createBrowserPanel();

    protected abstract String browserName();

    protected abstract String getWikiPage();

    protected boolean isProxyEnabled() {
        return false;
    }

    protected boolean isExperimentalEnabled() {
        return false;
    }

    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getSource() == pacUrlProxy) {
            pacUrl.setEnabled(itemEvent.getStateChange() == ItemEvent.SELECTED);
        } else if (itemEvent.getSource() == manualProxy) {
            httpProxyHost.setEnabled(itemEvent.getStateChange() == ItemEvent.SELECTED);
            httpProxyPort.setEnabled(itemEvent.getStateChange() == ItemEvent.SELECTED);
            useHttpSettingsForAllProtocols.setEnabled(itemEvent.getStateChange() == ItemEvent.SELECTED);
            noProxyList.setEnabled(itemEvent.getStateChange() == ItemEvent.SELECTED);
            enableOtherProtocolsOnlyIfManualProxySelectedAndUseHttpSettingsIsNotSelected();
        } else if (itemEvent.getSource() == useHttpSettingsForAllProtocols) {
            enableOtherProtocolsOnlyIfManualProxySelectedAndUseHttpSettingsIsNotSelected();
        }
    }

    private void enableOtherProtocolsOnlyIfManualProxySelectedAndUseHttpSettingsIsNotSelected() {
        final boolean enabledState = !useHttpSettingsForAllProtocols.isSelected() && manualProxy.isSelected();
        httpsProxyHost.setEnabled(enabledState);
        httpsProxyPort.setEnabled(enabledState);
        ftpProxyHost.setEnabled(enabledState);
        ftpProxyPort.setEnabled(enabledState);
        socksProxyHost.setEnabled(enabledState);
        socksProxyPort.setEnabled(enabledState);
    }
}