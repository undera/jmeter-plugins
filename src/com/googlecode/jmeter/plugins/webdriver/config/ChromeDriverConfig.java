package com.googlecode.jmeter.plugins.webdriver.config;

import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChromeDriverConfig extends WebDriverConfig<ChromeDriver> implements ThreadListener {

    private static final long serialVersionUID = 100L;
    private static final Logger LOGGER = LoggingManager.getLoggerForClass();
    private static final String CHROME_SERVICE_PATH = "ChromeDriverConfig.chromedriver_path";
    private static final Map<String, ChromeDriverService> services = new ConcurrentHashMap<String, ChromeDriverService>();

    @Override
    public void threadStarted() {

        LOGGER.info("ChromeDriverConfig.threadStarted()");

        if (hasThreadBrowser()) {
            LOGGER.warn("Thread: " + currentThreadName() + " already has a WebDriver(" + getThreadBrowser() + ") associated with it. ThreadGroup can only contain a single WebDriverConfig.");
            return;
        }
        setThreadBrowser(createBrowser());
    }

    @Override
    public void threadFinished() {
        final ChromeDriver chromeDriverDriver = removeThreadBrowser();
        if (chromeDriverDriver != null) {
            chromeDriverDriver.quit();
        }
        final ChromeDriverService service = services.remove(currentThreadName());
        if (service != null && service.isRunning()) {
            service.stop();
        }
    }

    public void setChromeDriverPath(String path) {
        setProperty(CHROME_SERVICE_PATH, path);
    }

    public String getChromeDriverPath() {
        return getPropertyAsString(CHROME_SERVICE_PATH);
    }

    Capabilities createCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, createProxy());
        return capabilities;
    }

    Map<String, ChromeDriverService> getServices() {
        return services;
    }

    @Override
    protected ChromeDriver createBrowser() {
        final ChromeDriverService service = getThreadService();
        return service != null ? new ChromeDriver(service, createCapabilities()) : null;
    }

    private ChromeDriverService getThreadService() {
        ChromeDriverService service = services.get(currentThreadName());
        if (service != null) {
            return service;
        }
        try {
            service = new ChromeDriverService.Builder().usingDriverExecutable(new File(getChromeDriverPath())).build();
            service.start();
            services.put(currentThreadName(), service);
        } catch (IOException e) {
            LOGGER.error("Failed to start chrome service");
            service = null;
        }
        return service;
    }
}
