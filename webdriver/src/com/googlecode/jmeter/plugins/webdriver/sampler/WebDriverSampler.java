package com.googlecode.jmeter.plugins.webdriver.sampler;

import com.googlecode.jmeter.plugins.webdriver.config.WebDriverConfig;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.openqa.selenium.WebDriver;

import javax.script.*;
import java.net.URL;


/**
 * A Sampler that makes HTTP requests using a real browser (via. Selenium/WebDriver).  It currently 
 * provides a scripting mechanism via. Javascript to control the browser instance.
 */
public class WebDriverSampler extends AbstractSampler {

    private static final long serialVersionUID = 100L;
    public static final String SCRIPT = "WebDriverSampler.script";
	public static final String PARAMETERS = "WebDriverSampler.parameters";
	private static final Logger LOGGER = LoggingManager.getLoggerForClass();
    private static final String DEFAULT_ENGINE = "JavaScript";
    private final transient ScriptEngineManager scriptEngineManager;

    private static final String preloading =     "importPackage(org.openqa.selenium); " +
    	    "_s = function(){return WDS.sampleResult.sampleStart.apply(WDS.sampleResult,arguments)};" +
    	    "_e = function(){return WDS.sampleResult.sampleEnd.apply(WDS.sampleResult,arguments)};" +
    	    "measure = function(f){_s();f.apply();_e();};" +  
    	    "_iW = function(t){return WDS.browser.manage().timeouts().implicitlyWait(t,java.util.concurrent.TimeUnit.SECONDS)};"+
    	    "_iWait = function(f,t){_iW(t);f.apply();_iW(0);};" +
    	    "iWait = function(f){_iWait(f,7);};" +
    	    "$ID = function(){return WDS.browser.findElementById.apply(WDS.browser,arguments)};" +
    	    "$x = function(){return WDS.browser.findElementByXPath.apply(WDS.browser,arguments)};" +
    	    "$$ = function(){return WDS.browser.findElementsByCssSelector.apply(WDS.browser, arguments)};" +
    	    "$ = function(){return WDS.browser.findElementByCssSelector.apply(WDS.browser, arguments)};" +
    	    "go = function(){return WDS.browser.get.apply(WDS.browser,arguments)};";
    
    public WebDriverSampler() {
        this.scriptEngineManager = new ScriptEngineManager();
    }
    
    public String toString(){
    	return getScript();
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
        res.setContentType("text/plain");
        res.setDataEncoding("UTF-8");
        res.setSuccessful(true);

        LOGGER.info("Current thread name: '"+getThreadName()+"', has browser: '"+getWebDriver()+"'");

        try {
            final ScriptEngine scriptEngine = createScriptEngineWith(res);
            scriptEngine.eval(preloading + getScript());

            // setup the data in the SampleResult
            res.setResponseData(getWebDriver().getPageSource(), null);
            String currentURL = getWebDriver().getCurrentUrl();            
            res.setURL(new URL(currentURL));
            res.setResponseHeaders("URL: "+ currentURL);
            res.setResponseCode(res.isSuccessful() ? "200" : "500");
            if(res.isSuccessful()) {
                res.setResponseMessageOK();
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

    ScriptEngine createScriptEngineWith(SampleResult sampleResult) {
        final ScriptEngine scriptEngine = scriptEngineManager.getEngineByName(DEFAULT_ENGINE);
        Bindings engineBindings = new SimpleBindings();
        WebDriverScriptable scriptable = new WebDriverScriptable();
        scriptable.setName(getName());
        scriptable.setParameters(getParameters());
        scriptable.setLog(LOGGER);
        scriptable.setSampleResult(sampleResult);
        scriptable.setBrowser(getWebDriver());
        engineBindings.put("WDS", scriptable);
        scriptEngine.setBindings(engineBindings, ScriptContext.ENGINE_SCOPE);
        return scriptEngine;
    }
}
