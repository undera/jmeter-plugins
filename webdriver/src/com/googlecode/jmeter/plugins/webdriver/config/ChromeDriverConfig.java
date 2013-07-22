package com.googlecode.jmeter.plugins.webdriver.config;

import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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
    private static final String CHROME_SERVICE_ARGS = "ChromeDriverConfig.chromedriver_args";
    private static final Map<String, ChromeDriverService> services = new ConcurrentHashMap<String, ChromeDriverService>();

    private static volatile ChromeDriverService freeSharedService = null;
    private static volatile ChromeDriver freeSharedBrowser = null;
    
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
        final ChromeDriverService service = services.remove(currentThreadName());
        
        if(isDevMode()){
        	freeSharedBrowser = chromeDriverDriver;
        	freeSharedService = service;
        	return;
        }
        
        if(chromeDriverDriver != null) {
            chromeDriverDriver.quit();
        }
        
        if(service != null && service.isRunning() ) {
            service.stop();
        }
    }

    public void setChromeDriverPath(String path) {
        setProperty(CHROME_SERVICE_PATH, path);
    }

    public String getChromeDriverPath() {
        return getPropertyAsString(CHROME_SERVICE_PATH);
    }
    
    public void setChromeDriverArgs(String path) {
        setProperty(CHROME_SERVICE_ARGS, path);
    }

    public String getChromeDriverArgs() {
        return getPropertyAsString(CHROME_SERVICE_ARGS);
    }

    Capabilities createCapabilities() {
	ChromeOptions opts = new ChromeOptions();
	opts.addArguments(getChromeDriverArgs().split(";"));
    DesiredCapabilities capabilities = DesiredCapabilities.chrome();
    capabilities.setCapability(CapabilityType.PROXY, createProxy());
	capabilities.setCapability(ChromeOptions.CAPABILITY, opts);
        return capabilities;
    }

    Map<String, ChromeDriverService> getServices() {
        return services;
    }

    @Override
    protected ChromeDriver createBrowser() {
    	if(isDevMode())
        	synchronized (ChromeDriverConfig.class) {
        		if(freeSharedBrowser != null){                	
        			ChromeDriver dr = freeSharedBrowser;
        			freeSharedBrowser = null; 
        			try{
        				dr.getRemoteStatus();
        				dr.navigate().to("about:blank");
        				return dr;
        			}catch(Exception e){
        				LOGGER.error("Invalid browser?:",e);
        			}
                	
                }	            
        	}
        final ChromeDriverService service = getThreadService();
        return service != null ? new ChromeDriver(service, createCapabilities()) : null;
    }

    private ChromeDriverService getThreadService() {
        ChromeDriverService service = services.get(currentThreadName());
        if (service != null) {
            return service;
        }
        
        try {
        	if(isDevMode())
            	synchronized (ChromeDriverConfig.class) {
            		if(freeSharedService != null){
            			ChromeDriverService serv = freeSharedService;
                    	services.put(currentThreadName(), serv);
                    	freeSharedService = null;  
                    	if(!serv.isRunning())
                    		serv.start();
                    	return serv;
                    }	            
            	}
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
