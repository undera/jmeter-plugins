package com.googlecode.jmeter.plugins.webdriver.config;

import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class HtmlUnitDriverConfig extends WebDriverConfig<HtmlUnitDriver> implements ThreadListener {

    private static final long serialVersionUID = 100L;
    private static final Logger LOGGER = LoggingManager.getLoggerForClass();

    @Override
    public void threadStarted() {
        if(hasThreadBrowser()) {
            LOGGER.warn("Thread: " + currentThreadName() + " already has a WebDriver("+ getThreadBrowser()+") associated with it. ThreadGroup can only contain a single WebDriverConfig.");
            return;
        }
        setThreadBrowser(createBrowser());
    }

    Capabilities createCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, createProxy());
        capabilities.setCapability("javascriptEnabled", "true");
        return capabilities;
    }

    @Override
    public void threadFinished() {
        final HtmlUnitDriver htmlunitDriver = removeThreadBrowser();
        if(htmlunitDriver != null) {
            htmlunitDriver.quit();
        }
    }

    @Override
    protected HtmlUnitDriver createBrowser() {
        return new HtmlUnitDriver(createCapabilities());
    }
}
