package com.googlecode.jmeter.plugins.webdriver.config;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.googlecode.jmeter.plugins.webdriver.config.RemoteCapability;
import com.googlecode.jmeter.plugins.webdriver.config.RemoteDesiredCapabilitiesFactory;
import com.googlecode.jmeter.plugins.webdriver.config.WebDriverConfig;

public class RemoteDriverConfig extends WebDriverConfig<RemoteWebDriver> {

    private static final long serialVersionUID = 100L;
    private static final String REMOTE_SELENIUM_GRID_URL = "RemoteDriverConfig.general.selenium.grid.url";
    private static final String REMOTE_CAPABILITY = "RemoteDriverConfig.general.selenium.capability";

    Capabilities createCapabilities() {
    	DesiredCapabilities capabilities = RemoteDesiredCapabilitiesFactory.build(getCapability());
        capabilities.setCapability(CapabilityType.PROXY, createProxy());
        capabilities.setJavascriptEnabled(true);
        return capabilities;
    }

    @Override
    protected RemoteWebDriver createBrowser() {
    	try {
			return new RemoteWebDriver(new URL(getSeleniumGridUrl()), createCapabilities());
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
    }

	public void setSeleniumGridUrl(String seleniumUrl) {
		setProperty(REMOTE_SELENIUM_GRID_URL, seleniumUrl);
	}
	
	public String getSeleniumGridUrl() {
		return getPropertyAsString(REMOTE_SELENIUM_GRID_URL);
	}
	
	public RemoteCapability getCapability(){
		return RemoteCapability.valueOf(getPropertyAsString(REMOTE_CAPABILITY));
	}

	public void setCapability(RemoteCapability selectedCapability) {
		setProperty(REMOTE_CAPABILITY, selectedCapability.name());
	}
}
