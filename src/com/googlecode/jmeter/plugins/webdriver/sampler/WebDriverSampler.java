package com.googlecode.jmeter.plugins.webdriver.sampler;

import com.googlecode.jmeter.plugins.webdriver.config.WebDriverConfig;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;
import org.openqa.selenium.WebDriver;

import javax.script.*;


/**
 * A Sampler that makes HTTP requests using a real browser (via. Selenium/WebDriver).  It currently 
 * provides a scripting mechanism via. Javascript to control the browser instance.
 */
public class WebDriverSampler extends AbstractSampler {
	
    public static final String SCRIPT = "WebDriverSampler.script";

	public static final String PARAMETERS = "WebDriverSampler.parameters";
    
	private static final Logger LOGGER = LoggingManager.getLoggerForClass();
	
	private static final long serialVersionUID = 234L;

    private static final String DEFAULT_ENGINE = "JavaScript";

    private final transient ScriptEngineManager scriptEngineManager;

    public WebDriverSampler() {
        this.scriptEngineManager = new ScriptEngineManager();
        initialiseGlobalVariables();
    }

    @Override
	public SampleResult sample(Entry e) {
        if(getWebDriver() == null) {
            throw new IllegalArgumentException("Browser has not been configured.  Please ensure at least 1 WebDriverConfig is created for a ThreadGroup.");
        }

        final SampleResult res = new SampleResult();
        res.setSampleLabel(getName());
        res.setSamplerData(toString());
        res.setDataType(SampleResult.TEXT);
        res.setContentType("text/plain"); // $NON-NLS-1$
        res.setDataEncoding("UTF-8");

        res.setSuccessful(true);
        res.setResponseMessageOK();
        res.setResponseCodeOK();

        LOGGER.info("Current thread name: '"+getThreadName()+"', has browser: '"+getWebDriver()+"'");

        try {
            final ScriptEngine scriptEngine = createScriptEngineWith(res);
            final Object outcome = scriptEngine.eval(getScript());

            res.setResponseData(getWebDriver().getPageSource(), null);
            if(outcome instanceof Boolean) { // only set this if the return value is boolean
                res.setSuccessful((Boolean) outcome);
            }

            if(!res.isSuccessful()) {
                res.setResponseCode("500");
                res.setResponseMessage("Failed to find/verify expected content on page");
            }

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            res.setResponseMessage(ex.toString());
            res.setResponseCode("500");
            res.setSuccessful(false);
        }

        return res;
	}

	public String getScript() {
		return getPropertyAsString(SCRIPT);
	}
	
	public void setScript(String script) {
		setProperty(SCRIPT, script);
	}

	public String getParameters() {
		return getPropertyAsString(PARAMETERS);
	}

	public void setParameters(String parameters) {
		setProperty(PARAMETERS, parameters);
	}

    private WebDriver getWebDriver() {
        return (WebDriver) getThreadContext().getVariables().getObject(WebDriverConfig.BROWSER);
    }

    private void initialiseGlobalVariables() {
        Bindings globalBindings = new SimpleBindings();
        globalBindings.put("log", LOGGER);
        globalBindings.put("OUT", System.out);
        scriptEngineManager.setBindings(globalBindings);
    }

    ScriptEngine createScriptEngineWith(SampleResult sampleResult) {
        final ScriptEngine scriptEngine = scriptEngineManager.getEngineByName(DEFAULT_ENGINE);
        Bindings engineBindings = new SimpleBindings();
        engineBindings.put("Label", getName());
        engineBindings.put("SampleResult", sampleResult);
        final String scriptParameters = getParameters();
        engineBindings.put("Parameters", scriptParameters);
        String[] args = JOrphanUtils.split(scriptParameters, " ");
        engineBindings.put("args", args);
        engineBindings.put("Browser", getWebDriver());
        scriptEngine.setBindings(engineBindings, ScriptContext.ENGINE_SCOPE);
        return scriptEngine;
    }
}
