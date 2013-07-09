package com.googlecode.jmeter.plugins.webdriver.config.gui;

import com.googlecode.jmeter.plugins.webdriver.config.FirefoxDriverConfig;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;

public class FirefoxDriverConfigGui extends WebDriverConfigGui {

    private static final long serialVersionUID = 100L;

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
        return new VerticalPanel();
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
}
