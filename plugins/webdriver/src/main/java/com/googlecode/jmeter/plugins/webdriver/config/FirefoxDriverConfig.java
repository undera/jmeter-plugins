package com.googlecode.jmeter.plugins.webdriver.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class FirefoxDriverConfig extends WebDriverConfig<FirefoxDriver> {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final long serialVersionUID = 100L;
    private static final String GENERAL_USERAGENT_OVERRIDE = "FirefoxDriverConfig.general.useragent.override";
    private static final String ENABLE_USERAGENT_OVERRIDE = "FirefoxDriverConfig.general.useragent.override.enabled";
    private static final String ENABLE_NTML = "FirefoxDriverConfig.network.negotiate-auth.allow-insecure-ntlm-v1";
    private static final String EXTENSIONS_TO_LOAD = "FirefoxDriverConfig.general.extensions";
    private static final String PREFERENCES = "FirefoxDriverConfig.general.preferences";

    Capabilities createCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, createProxy());
        return capabilities;
    }

    FirefoxProfile createProfile() {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("app.update.enabled", false);
        
        String userAgentOverride = getUserAgentOverride();
        if (StringUtils.isNotEmpty(userAgentOverride)) {
            profile.setPreference("general.useragent.override", userAgentOverride);
        }
        
        String ntlmOverride = getNtlmSetting();
        if (StringUtils.isNotEmpty(ntlmOverride)) {
            profile.setPreference("network.negotiate-auth.allow-insecure-ntlm-v1", true);
        }

        addExtensions(profile);
        setPreferences(profile);

        return profile;
    }

    private void addExtensions(FirefoxProfile profile) {
        JMeterProperty property = getProperty(EXTENSIONS_TO_LOAD);
        if (property instanceof NullProperty) {
            return;
        }
        CollectionProperty rows = (CollectionProperty) property;
        for (int i = 0; i < rows.size(); i++) {
            ArrayList row = (ArrayList) rows.get(i).getObjectValue();
            String filename = ((JMeterProperty) row.get(0)).getStringValue();
            try {
                profile.addExtension(new File(filename));
            } catch (IOException e) {
                log.error("Failed to add extension " + filename, e);
            }
        }
    }

    private void setPreferences(FirefoxProfile profile) {
        JMeterProperty property = getProperty(PREFERENCES);
        if (property instanceof NullProperty) {
            return;
        }
        CollectionProperty rows = (CollectionProperty) property;
        for (int i = 0; i < rows.size(); i++) {
            ArrayList row = (ArrayList) rows.get(i).getObjectValue();
            String name = ((JMeterProperty) row.get(0)).getStringValue();
            String value = ((JMeterProperty) row.get(1)).getStringValue();
            switch (value) {
                case "true":
                    profile.setPreference(name, true);
                    break;
                case "false":
                    profile.setPreference(name, false);
                    break;
                default:
                    profile.setPreference(name, value);
                    break;
            }
        }
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

    public void setExtensions(PowerTableModel model) {
        CollectionProperty prop = JMeterPluginsUtils.tableModelRowsToCollectionProperty(model, EXTENSIONS_TO_LOAD);
        setProperty(prop);
    }

    public void setPreferences(PowerTableModel model) {
        CollectionProperty prop = JMeterPluginsUtils.tableModelRowsToCollectionProperty(model, PREFERENCES);
        setProperty(prop);
    }

    public JMeterProperty getExtensions() {
        return getProperty(EXTENSIONS_TO_LOAD);
    }

    public JMeterProperty getPreferences() {
        return getProperty(PREFERENCES);
    }
}
