package com.googlecode.jmeter.plugins.webdriver.config.gui;

import com.googlecode.jmeter.plugins.webdriver.config.ChromeDriverConfig;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;
import java.awt.*;

public class ChromeDriverConfigGui extends WebDriverConfigGui {

    private static final long serialVersionUID = 1239147462983L;

    JTextField chromeServicePath;

    public ChromeDriverConfigGui() {
        init();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Chrome Driver Config");
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        if(element instanceof ChromeDriverConfig) {
            ChromeDriverConfig config = (ChromeDriverConfig)element;
            chromeServicePath.setText(config.getChromeDriverPath());
        }
    }

    @Override
    public TestElement createTestElement() {
        ChromeDriverConfig element = new ChromeDriverConfig();
        modifyTestElement(element);
        return element;
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.modifyTestElement(element);
        if(element instanceof ChromeDriverConfig) {
            ChromeDriverConfig config = (ChromeDriverConfig)element;
            config.setChromeDriverPath(chromeServicePath.getText());
        }
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));

        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);

        // Proxy
        final JPanel proxyPanel = createProxyPanel();

        // Chrome Service
        final JPanel servicePanel = createServicePanel();
        proxyPanel.add(servicePanel);

        add(proxyPanel, BorderLayout.CENTER);
    }

    private JPanel createServicePanel() {
        final JPanel chromeServicePanel = new HorizontalPanel();
        final JLabel chromeDriverServiceLabel = new JLabel("Path to Chrome Driver");
        chromeServicePanel.add(chromeDriverServiceLabel);

        chromeServicePath = new JTextField();
        chromeServicePanel.add(chromeServicePath);
        return chromeServicePanel;
    }

}
