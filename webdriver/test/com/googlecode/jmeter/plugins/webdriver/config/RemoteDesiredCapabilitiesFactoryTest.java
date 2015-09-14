package com.googlecode.jmeter.plugins.webdriver.config;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.CoreMatchers.notNullValue;

public class RemoteDesiredCapabilitiesFactoryTest {

	@Test
	public void shouldReturnFirefoxDriverWhenFirefoxCapabilityIsPassed() throws Exception {
		DesiredCapabilities capabilities = RemoteDesiredCapabilitiesFactory.build(RemoteCapability.FIREFOX);
		assertThat(capabilities.getCapability(FirefoxDriver.PROFILE), is(notNullValue()));
		assertThat(capabilities.getBrowserName(), is("firefox"));
	}

	@Test
	public void shouldReturnChromeDriverWhenChromeCapabilityIsPassed() throws Exception {
		DesiredCapabilities capabilities = RemoteDesiredCapabilitiesFactory.build(RemoteCapability.CHROME);
		assertThat(capabilities.getCapability(ChromeOptions.CAPABILITY), is(notNullValue()));
		assertThat(capabilities.getBrowserName(), is("chrome"));
	}

	@Test
	public void shouldReturnInternetExplorerDriverWhenInternetExplorerCapabilityIsPassed() throws Exception {
		DesiredCapabilities capabilities = RemoteDesiredCapabilitiesFactory.build(RemoteCapability.INTERNET_EXPLORER);
		assertThat(capabilities.getBrowserName(), is("internet explorer"));
	}

	@Test
	public void shouldReturnPhantomJSDriverWhenPhantomJSCapabilityIsPassed() throws Exception {
		DesiredCapabilities capabilities = RemoteDesiredCapabilitiesFactory.build(RemoteCapability.PHANTOMJS);
		assertThat(capabilities.getBrowserName(), is("phantomjs"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowAnExceptionWhenAnInvalidCapabilityIsPassed() throws Exception {
		RemoteDesiredCapabilitiesFactory.build(null);
	}

}
