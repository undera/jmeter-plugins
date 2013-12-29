package com.googlecode.jmeter.plugins.webdriver.config;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class FirefoxDriverConfig extends WebDriverConfig<FirefoxDriver> {

    private static final long serialVersionUID = 100L;

    Capabilities createCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, createProxy());
        return capabilities;
    }

    @Override
    protected FirefoxDriver createBrowser() {
        return new FirefoxDriver(createCapabilities());
    }
}
