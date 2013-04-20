package com.googlecode.jmeter.plugins.webdriver.config;

import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FirefoxDriverConfig extends WebDriverConfig implements ThreadListener, LoopIterationListener {

    private static final long serialVersionUID = 9239127462983L;
    private static final Logger log = LoggingManager.getLoggerForClass();
    static final Map<String, FirefoxDriver> webdrivers = new ConcurrentHashMap<String, FirefoxDriver>();

    @Override
    public void threadStarted() {
        if(webdrivers.containsKey(currentThreadName())) {
            log.warn("Thread: "+currentThreadName()+" already has a FirefoxDriver associated with it.  Ware there multiple FirefoxDriverConfigs created for a single Thread Group?");
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
        final FirefoxDriver firefoxDriver = webdrivers.remove(currentThreadName());
        if(firefoxDriver != null) {
            firefoxDriver.quit();
        }
    }

    public FirefoxDriver getCurrentThreadBrowser() {
        return webdrivers.get(currentThreadName());
    }

    String currentThreadName() {
        return Thread.currentThread().getName();
    }

    @Override
    public void iterationStart(LoopIterationEvent loopIterationEvent) {
        getThreadContext().getVariables().putObject(WebDriverConfig.BROWSER, getCurrentThreadBrowser());
    }
}
