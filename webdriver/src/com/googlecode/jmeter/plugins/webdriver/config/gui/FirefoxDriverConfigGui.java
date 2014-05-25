package com.googlecode.jmeter.plugins.webdriver.config.gui;

import com.googlecode.jmeter.plugins.webdriver.config.FirefoxDriverConfig;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;

public class FirefoxDriverConfigGui extends WebDriverConfigGui {

    private static final long serialVersionUID = 100L;
    JTextField useragentOverride;


    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Firefox Driver Config");
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    protected JPanel createBrowserPanel() {
        return createServicePanel();
    }

    @Override
    protected String browserName() {
        return "Firefox";
    }

    @Override
    protected String getWikiPage() {
        return "FirefoxDriverConfig";
    }

    @Override
    public TestElement createTestElement() {
        FirefoxDriverConfig element = new FirefoxDriverConfig();
        modifyTestElement(element);
        return element;
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        if(element instanceof FirefoxDriverConfig) {
            FirefoxDriverConfig config = (FirefoxDriverConfig)element;
            useragentOverride.setText(config.getUserAgentOverride());
        }
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.modifyTestElement(element);
        if(element instanceof FirefoxDriverConfig) {
            FirefoxDriverConfig config = (FirefoxDriverConfig)element;
            config.setUserAgentOverride(useragentOverride.getText());
        }
    }

    private JPanel createServicePanel() {
        final JPanel browserPanel = new VerticalPanel();
        final JPanel firefoxPanel = new HorizontalPanel();
        final JLabel firefoxUserAgentOverrideLabel = new JLabel("Override User Agent");
        firefoxPanel.add(firefoxUserAgentOverrideLabel);

        useragentOverride = new JTextField();
        firefoxPanel.add(useragentOverride);
        browserPanel.add(firefoxPanel);
        return browserPanel;
    }

    @Override
    public void clearGui() {
        super.clearGui();
        useragentOverride.setText("");
    }
}
