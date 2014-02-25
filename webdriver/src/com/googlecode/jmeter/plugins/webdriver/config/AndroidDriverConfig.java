package com.googlecode.jmeter.plugins.webdriver.config;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.openqa.selenium.android.AndroidDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Sergey Marakhov
 * @author Linh Pham
 */
public class AndroidDriverConfig extends WebDriverConfig<AndroidDriver> {
    private static final long serialVersionUID = 100L;

    private static final Logger LOGGER = LoggingManager.getLoggerForClass();

    private static final String ANDROID_DRIVER_HOST_PORT = "AndroidDriverConfig.driver_host_port";

    DesiredCapabilities createCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, createProxy());
        return capabilities;
    }

    @Override
    protected AndroidDriver createBrowser() {
        try {
            return new AndroidDriver(new URL(getAndroidDriverUrl()), createCapabilities());
        } catch (MalformedURLException e) {
            LOGGER.error("MalformedURLException thrown for invalid URL: " + getAndroidDriverUrl());
            return null;
        }
    }

    private String getAndroidDriverUrl() {
        return "http://localhost:" + getAndroidDriverHostPort() + "/wd/hub";
    }

    public void setAndroidDriverHostPort(String port) {
        setProperty(ANDROID_DRIVER_HOST_PORT, port);
    }

    public String getAndroidDriverHostPort() {
        return getPropertyAsString(ANDROID_DRIVER_HOST_PORT);
    }
}
