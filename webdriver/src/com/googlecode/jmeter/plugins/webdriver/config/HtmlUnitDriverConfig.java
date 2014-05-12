package com.googlecode.jmeter.plugins.webdriver.config;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class HtmlUnitDriverConfig extends WebDriverConfig<HtmlUnitDriver> {

    private static final long serialVersionUID = 100L;

    Capabilities createCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, createProxy());
        capabilities.setCapability("javascriptEnabled", "true");
        return capabilities;
    }

    @Override
    protected HtmlUnitDriver createBrowser() {
        return new HtmlUnitDriver(createCapabilities());
    }
}
