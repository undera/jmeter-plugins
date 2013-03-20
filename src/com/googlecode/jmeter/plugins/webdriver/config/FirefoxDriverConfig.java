package com.googlecode.jmeter.plugins.webdriver.config;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FirefoxDriverConfig extends ConfigTestElement implements ThreadListener {

    private static final long serialVersionUID = 9239127462983L;
    private static final Logger log = LoggingManager.getLoggerForClass();
    static final Map<String, FirefoxDriver> webdrivers = new ConcurrentHashMap<String, FirefoxDriver>();

    @Override
    public void threadStarted() {
        if(webdrivers.containsKey(currentThreadName())) {
            log.warn("Thread: "+currentThreadName()+" already has a FirefoxDriver associated with it.  Was there multiple FirefoxConfigs created for a single Thread Group?");
            return;
        }
        webdrivers.put(currentThreadName(), new FirefoxDriver());
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
}
