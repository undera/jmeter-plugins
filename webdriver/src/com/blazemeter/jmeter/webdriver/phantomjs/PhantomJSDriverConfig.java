package com.blazemeter.jmeter.webdriver.phantomjs;

import com.googlecode.jmeter.plugins.webdriver.config.WebDriverConfig;
import org.apache.jmeter.testelement.ThreadListener;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class PhantomJSDriverConfig extends WebDriverConfig<PhantomJSDriver> implements ThreadListener {
    private static final long serialVersionUID = 100L;

    private static final String PHANTOMJS_EXECUTABLE_PATH_PROPERTY_KEY = "PhantomJSDriverConfig.phantomjs_path";
    private static final String PHANTOMJS_CLI_ARG_PROPERTY_KEY = "PhantomJSDriverConfig.phantomjs_cli_args";
    private static final String PHANTOMJS_GHOSTDRIVER_CLI_ARG_PROPERTY_KEY = "PhantomJSDriverConfig.phantomjs_ghostdriver_cli_args";

    public String getPhantomJsExecutablePath() {
        return getPropertyAsString(PHANTOMJS_EXECUTABLE_PATH_PROPERTY_KEY);
    }

    public void setPhantomJsExecutablePath(String value) {
        setProperty(PHANTOMJS_EXECUTABLE_PATH_PROPERTY_KEY, value);
    }
    
    public String getPhantomJsCliArgs() {
        return getPropertyAsString(PHANTOMJS_CLI_ARG_PROPERTY_KEY);
    }

    public void setPhantomJsCliArgs(String value) {
        setProperty(PHANTOMJS_CLI_ARG_PROPERTY_KEY, value);
    }
    
    public String getPhantomJsGhostdriverCliArgs() {
        return getPropertyAsString(PHANTOMJS_GHOSTDRIVER_CLI_ARG_PROPERTY_KEY);
    }

    public void setPhantomJsGhostdriverCliArgs(String value) {
        setProperty(PHANTOMJS_GHOSTDRIVER_CLI_ARG_PROPERTY_KEY, value);
    }

    @Override
    public void threadStarted() {
        if (hasThreadBrowser()) {
            return;
        }
        setThreadBrowser(createBrowser());
    }

    protected Capabilities createCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, createProxy());
        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                getPhantomJsExecutablePath());
        
        if (getPhantomJsCliArgs() != null && getPhantomJsCliArgs().trim().length() > 0) {
        	capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliParamsToStringArray(getPhantomJsCliArgs()));
        }

        if (getPhantomJsGhostdriverCliArgs() != null && getPhantomJsGhostdriverCliArgs().trim().length() > 0) {
        	capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS, cliParamsToStringArray(getPhantomJsGhostdriverCliArgs()));
        }
        
        return capabilities;
    }

    @Override
    public void threadFinished() {
        final PhantomJSDriver phantomJsDriver = removeThreadBrowser();
        if (phantomJsDriver != null) {
            phantomJsDriver.quit();
        }
    }

    @Override
    protected PhantomJSDriver createBrowser() {
        return new PhantomJSDriver(createCapabilities());
    }
    
    /*
     * Convert a string with params (comma separator) to an array of String
     * Example : "--web-security=false, --ignore-ssl-errors=true" converts to  ["--web-security=false","--ignore-ssl-errors=true"] (trim String)
     */
    private String [] cliParamsToStringArray(String params) {
    	String[] sSplit = {""};
    	
    	if (params != null && params.trim().length() > 0) {
    		sSplit = params.split(",");
	        for(int i = 0; i < sSplit.length;i++) {
	        	String s = sSplit[i];
	        	s = s.trim();
	        	sSplit[i] = s;
	        }
    	}
        return sSplit;
    	
    }
}
