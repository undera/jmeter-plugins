package com.googlecode.jmeter.plugins.webdriver.config;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class RemoteDriverConfig extends WebDriverConfig<RemoteWebDriver> {

    private static final long serialVersionUID = 100L;
    private static final String GENERAL_USERAGENT_OVERRIDE = "RemoteDriverConfig.general.useragent.override";
    private static final String ENABLE_USERAGENT_OVERRIDE = "RemoteDriverConfig.general.useragent.override.enabled";
    private static final String REMOTE_SELENIUM_GRID_URL = "RemoteDriverConfig.general.selenium.grid.url";

    Capabilities createCapabilities() {
    	ChromeOptions options = new ChromeOptions();
    	DesiredCapabilities capabilities = DesiredCapabilities.chrome();
    	capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        capabilities.setCapability(CapabilityType.PROXY, createProxy());
        capabilities.setJavascriptEnabled(true);
        return capabilities;
    }

    @Override
    protected RemoteWebDriver createBrowser() {
    	try {
			return new RemoteWebDriver(new URL(getSeleniumGridUrl()), createCapabilities());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
    	return null;
    }

    public void setUserAgentOverride(String userAgent) {
        setProperty(GENERAL_USERAGENT_OVERRIDE, userAgent);
    }

    public String getUserAgentOverride() {
        return getPropertyAsString(GENERAL_USERAGENT_OVERRIDE);
    }

    public boolean isUserAgentOverridden() {
        return getPropertyAsBoolean(ENABLE_USERAGENT_OVERRIDE);
    }

    public void setUserAgentOverridden(boolean userAgentOverridden) {
        setProperty(ENABLE_USERAGENT_OVERRIDE, userAgentOverridden);
    }

	public void setSeleniumGridUrl(String seleniumUrl) {
		setProperty(REMOTE_SELENIUM_GRID_URL, seleniumUrl);
	}
	
	public String getSeleniumGridUrl() {
		return getPropertyAsString(REMOTE_SELENIUM_GRID_URL);
	}
}
