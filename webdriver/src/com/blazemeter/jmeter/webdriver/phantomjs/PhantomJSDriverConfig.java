package com.blazemeter.jmeter.webdriver.phantomjs;

import com.googlecode.jmeter.plugins.webdriver.config.WebDriverConfig;
import org.apache.jmeter.testelement.ThreadListener;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.threads.JMeterContextService;

public class PhantomJSDriverConfig extends WebDriverConfig<PhantomJSDriver> implements ThreadListener {
    private static final long serialVersionUID = 100L;

    private static final String PHANTOMJS_EXECUTABLE_PATH_PROPERTY_KEY = "PhantomJSDriverConfig.phantomjs_path";

    public String getPhantomJsExecutablePath() {
        return getPropertyAsString(PHANTOMJS_EXECUTABLE_PATH_PROPERTY_KEY);
    }

    public void setPhantomJsExecutablePath(String value) {
        setProperty(PHANTOMJS_EXECUTABLE_PATH_PROPERTY_KEY, value);
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
        String cliArgs = JMeterContextService.getContext().getVariables().get("phantomjs.cli.args");
        
        if(cliArgs!=null && cliArgs.trim().length()>0) {
            String args[] = cliArgs.split(",");
            capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, args);
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
}
