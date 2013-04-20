package com.googlecode.jmeter.plugins.webdriver.config;

import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
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

public class ChromeDriverConfig extends WebDriverConfig implements ThreadListener, LoopIterationListener {

    private static final long serialVersionUID = 9239127462983L;
    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final String CHROME_SERVICE_PATH = "ChromeDriverConfig.chromedriver_path";

    static final Map<String, ChromeDriver> webdrivers = new ConcurrentHashMap<String, ChromeDriver>();
    static final Map<String, ChromeDriverService> services = new ConcurrentHashMap<String, ChromeDriverService>();

    @Override
    public void threadStarted() {
        if(webdrivers.containsKey(currentThreadName())) {
            log.warn("Thread: "+currentThreadName()+" already has a ChromeDriver associated with it.  Ware there multiple ChromeDriverConfigs created for a single Thread Group?");
            return;
        }
        final ChromeDriverService service;
        try {
            service = new ChromeDriverService.Builder().usingDriverExecutable(new File(getChromeDriverPath())).build();
            service.start();
            services.put(currentThreadName(), service);
            webdrivers.put(currentThreadName(), new ChromeDriver(service, createCapabilities()));
        } catch (IOException e) {
            log.error("Failed to start chrome service", e);
        }
    }

    Capabilities createCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, createProxy());
        return capabilities;
    }

    @Override
    public void threadFinished() {
        final ChromeDriver chromeDriverDriver = webdrivers.remove(currentThreadName());
        if(chromeDriverDriver != null) {
            chromeDriverDriver.quit();
        }
        final ChromeDriverService service = services.remove(currentThreadName());
        if(service != null && service.isRunning()) {
            service.stop();
        }
    }

    public ChromeDriver getCurrentThreadBrowser() {
        return webdrivers.get(currentThreadName());
    }

    String currentThreadName() {
        return Thread.currentThread().getName();
    }

    @Override
    public void iterationStart(LoopIterationEvent loopIterationEvent) {
        getThreadContext().getVariables().putObject(WebDriverConfig.BROWSER, getCurrentThreadBrowser());
    }

    public void setChromeDriverPath(String path) {
        setProperty(CHROME_SERVICE_PATH, path);
    }

    public String getChromeDriverPath() {
        return getPropertyAsString(CHROME_SERVICE_PATH);
    }


}
