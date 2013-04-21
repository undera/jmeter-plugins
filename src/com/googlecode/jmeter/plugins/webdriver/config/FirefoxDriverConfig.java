package com.googlecode.jmeter.plugins.webdriver.config;

import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class FirefoxDriverConfig extends WebDriverConfig implements ThreadListener, LoopIterationListener {

    private static final long serialVersionUID = 9239127462983L;
    private static final Logger LOGGER = LoggingManager.getLoggerForClass();

    @Override
    public void threadStarted() {
        if(webdrivers.containsKey(currentThreadName())) {
            LOGGER.warn("Thread: " + currentThreadName() + " already has a FirefoxDriver associated with it.  Ware there multiple FirefoxDriverConfigs created for a single Thread Group?");
            return;
        }
        webdrivers.put(currentThreadName(), new FirefoxDriver(createCapabilities()));
    }

    Capabilities createCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, createProxy());
        return capabilities;
    }

    @Override
    public void threadFinished() {
        final WebDriver firefoxDriver = webdrivers.remove(currentThreadName());
        if(firefoxDriver != null) {
            firefoxDriver.quit();
        }
    }

    public WebDriver getCurrentThreadBrowser() {
        return webdrivers.get(currentThreadName());
    }

    @Override
    public void iterationStart(LoopIterationEvent loopIterationEvent) {
        getThreadContext().getVariables().putObject(WebDriverConfig.BROWSER, getCurrentThreadBrowser());
    }
}
