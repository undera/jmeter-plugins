package com.googlecode.jmeter.plugins.webdriver.config;

import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class FirefoxDriverConfig extends WebDriverConfig<FirefoxDriver> implements ThreadListener {

    private static final long serialVersionUID = 100L;
    private static final Logger LOGGER = LoggingManager.getLoggerForClass();

    @Override
    public void threadStarted() {
        if(hasThreadBrowser()) {
            LOGGER.warn("Thread: " + currentThreadName() + " already has a WebDriver("+ getThreadBrowser()+") associated with it. ThreadGroup can only contain a single WebDriverConfig.");
            return;
        }
        setThreadBrowser(new FirefoxDriver(createCapabilities()));
    }

    Capabilities createCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, createProxy());
        return capabilities;
    }

    @Override
    public void threadFinished() {
        final FirefoxDriver firefoxDriver = removeThreadBrowser();
        if(firefoxDriver != null) {
            firefoxDriver.quit();
        }
    }
}
