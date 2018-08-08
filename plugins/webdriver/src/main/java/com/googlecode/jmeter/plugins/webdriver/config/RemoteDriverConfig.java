package com.googlecode.jmeter.plugins.webdriver.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.*;

import java.net.MalformedURLException;
import java.net.URL;

public class RemoteDriverConfig extends WebDriverConfig<RemoteWebDriver> {

	private static final long serialVersionUID = 100L;
	private static final String REMOTE_SELENIUM_GRID_URL = "RemoteDriverConfig.general.selenium.grid.url";
	private static final String REMOTE_CAPABILITY = "RemoteDriverConfig.general.selenium.capability";
	private static final String REMOTE_FILE_DETECTOR = "RemoteDriverConfig.general.selenium.file.detector";

	private static final Logger LOGGER = LoggingManager.getLoggerForClass();

	Capabilities createCapabilities() {
		DesiredCapabilities capabilities = RemoteDesiredCapabilitiesFactory.build(getCapability());
		capabilities.setCapability(CapabilityType.PROXY, createProxy());
		capabilities.setJavascriptEnabled(true);
		return capabilities;
	}

	@Override
	protected RemoteWebDriver createBrowser() {
		try {
			RemoteWebDriver driver = new RemoteWebDriver(new URL(getSeleniumGridUrl()), createCapabilities());
			driver.setFileDetector(createFileDetector());
			LOGGER.debug("Created web driver with " + createFileDetector().getClass().getName());
			return driver;
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

	public FileDetectorOption getFileDetectorOption() {
		String fileDetectorString = getPropertyAsString(REMOTE_FILE_DETECTOR);
		if (StringUtils.isBlank(fileDetectorString)) {
			LOGGER.warn("No remote file detector configured, reverting to default of useless file detector");
			return FileDetectorOption.USELESS;
		}
		return FileDetectorOption.valueOf(fileDetectorString);
	}

	public void setFileDetectorOption(FileDetectorOption fileDetectorOption) {
		setProperty(REMOTE_FILE_DETECTOR, fileDetectorOption.name());
	}

	protected FileDetector createFileDetector() {
		try {
			return getFileDetectorOption().getClazz().newInstance();
		} catch (Exception e) {
			LOGGER.warn("Cannot create a file detector of type " + getFileDetectorOption().getClazz().getCanonicalName() + ", reverting to default of useless file detector");
			return new UselessFileDetector();
		}
	}

}
