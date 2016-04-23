package com.googlecode.jmeter.plugins.webdriver.sampler;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.log.Logger;
import org.openqa.selenium.WebDriver;

/**
 * An instance of this object is added to the running Script context when {@link WebDriverSampler} is running.
 *
 */
public final class WebDriverScriptable {
    private static final String[] EMPTY_ARGS = new String[0];
    private String name;
    private String parameters;
    private Logger log;
    private WebDriver browser;
    private SampleResult sampleResult;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getParameters() {
        return parameters;
    }

    public String[] getArgs() {
        return parameters != null ? parameters.trim().replaceAll("\\s+", " ").split(" ") : EMPTY_ARGS;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    public Logger getLog() {
        return log;
    }

    public void setBrowser(WebDriver browser) {
        this.browser = browser;
    }

    public WebDriver getBrowser() {
        return browser;
    }

    public void setSampleResult(SampleResult sampleResult) {
        this.sampleResult = sampleResult;
    }

    public SampleResult getSampleResult() {
        return sampleResult;
    }
}
