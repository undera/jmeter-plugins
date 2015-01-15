package com.googlecode.jmeter.plugins.webdriver.config;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class FirefoxDriverConfig extends WebDriverConfig<FirefoxDriver> {

    private static final long serialVersionUID = 100L;
    private static final String GENERAL_USERAGENT_OVERRIDE = "FirefoxDriverConfig.general.useragent.override";
    private static final String ENABLE_USERAGENT_OVERRIDE = "FirefoxDriverConfig.general.useragent.override.enabled";
    private static final String ENABLE_NTML = "FirefoxDriverConfig.network.negotiate-auth.allow-insecure-ntlm-v1";

    Capabilities createCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, createProxy());
        return capabilities;
    }

    FirefoxProfile createProfile() {
        FirefoxProfile profile = new FirefoxProfile();
        String userAgentOverride = getUserAgentOverride();
        String ntlmOverride = getNtlmSetting();
        if(StringUtils.isNotEmpty(userAgentOverride)) {
            profile.setPreference("general.useragent.override", userAgentOverride);
        }
        if(StringUtils.isNotEmpty(ntlmOverride)) {
            profile.setPreference("network.negotiate-auth.allow-insecure-ntlm-v1", true);
        }
        return profile;
    }

    @Override
    protected FirefoxDriver createBrowser() {
        return new FirefoxDriver(new FirefoxBinary(), createProfile(), createCapabilities());
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
    
    public void setNtlmSetting(boolean ntlm) {
        setProperty(ENABLE_NTML, ntlm);
    }

    public String getNtlmSetting() {
        return getPropertyAsString(ENABLE_NTML);
    }
}
