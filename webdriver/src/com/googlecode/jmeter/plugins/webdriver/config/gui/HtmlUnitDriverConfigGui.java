package com.googlecode.jmeter.plugins.webdriver.config.gui;

import com.googlecode.jmeter.plugins.webdriver.config.HtmlUnitDriverConfig;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;

public class HtmlUnitDriverConfigGui extends WebDriverConfigGui {

    private static final long serialVersionUID = 100L;

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("HtmlUnit Driver Config");
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
        return "HtmlUnit";
    }

    @Override
    protected String getWikiPage() {
        return "HtmlUnitDriverConfig";
    }

    @Override
    public TestElement createTestElement() {
        HtmlUnitDriverConfig element = new HtmlUnitDriverConfig();
        modifyTestElement(element);
        return element;
    }

	@Override
	protected boolean isProxyEnabled() {
		return true;
	}

	@Override
	protected boolean isExperimentalEnabled() {
		return true;
	}
}
