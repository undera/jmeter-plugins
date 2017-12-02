package com.googlecode.jmeter.plugins.webdriver.sampler;

import java.net.URL;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;

import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.openqa.selenium.WebDriver;

import com.googlecode.jmeter.plugins.webdriver.config.WebDriverConfig;

import kg.apc.jmeter.JMeterPluginsUtils;


/**
 * A Sampler that makes HTTP requests using a real browser (via. Selenium/WebDriver).  It currently
 * provides a scripting mechanism via. Javascript to control the browser instance.
 */
public class WebDriverSampler extends AbstractSampler {

    private static final long serialVersionUID = 100L;
    public static final String SCRIPT = "WebDriverSampler.script";
    public static final String PARAMETERS = "WebDriverSampler.parameters";
    private static final Logger LOGGER = LoggingManager.getLoggerForClass();
    public static final String DEFAULT_ENGINE = "javascript";
    public static final String SCRIPT_LANGUAGE = "WebDriverSampler.language";
    private final transient ScriptEngineManager scriptEngineManager;
    private final Class<SampleResult> sampleResultClass;

    public static final String defaultScript = "WDS.sampleResult.sampleStart()\n" +
            "WDS.browser.get('http://jmeter-plugins.org')\n" +
            "WDS.sampleResult.sampleEnd()\n";

    public WebDriverSampler() {
        Class<SampleResult> srClass;
        this.scriptEngineManager = new ScriptEngineManager();
        String className = JMeterUtils.getPropDefault("webdriver.sampleresult_class", SampleResult.class.getCanonicalName());
        try {
            srClass = (Class<SampleResult>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Class " + className + " not found, defaulted to " + SampleResult.class.getCanonicalName(), e);
            srClass = SampleResult.class;
        }
        sampleResultClass = srClass;
    }

    @Override
    public SampleResult sample(Entry e) {
        if (getWebDriver() == null) {
            throw new IllegalArgumentException("Browser has not been configured.  Please ensure at least 1 WebDriverConfig is created for a ThreadGroup.");
        }

        SampleResult res;
        try {
            res = sampleResultClass.newInstance();
        } catch (InstantiationException e1) {
            LOGGER.warn("Class " + sampleResultClass + " failed to instantiate, defaulted to " + SampleResult.class.getCanonicalName(), e1);
            res = new SampleResult();
        } catch (IllegalAccessException e1) {
            LOGGER.warn("Class " + sampleResultClass + " failed to instantiate, defaulted to " + SampleResult.class.getCanonicalName(), e1);
            res = new SampleResult();
        }
        res.setSampleLabel(getName());
        res.setSamplerData(toString());
        res.setDataType(SampleResult.TEXT);
        res.setContentType("text/plain");
        res.setDataEncoding("UTF-8");
        res.setSuccessful(true);

        LOGGER.debug("Current thread name: '" + getThreadName() + "', has browser: '" + getWebDriver() + "'");

        try {
            final ScriptEngine scriptEngine = createScriptEngineWith(res);
            scriptEngine.eval(getScript());

            // setup the data in the SampleResult
            res.setResponseData(getWebDriver().getPageSource(), null);
            res.setURL(new URL(getWebDriver().getCurrentUrl()));
            if(StringUtils.isEmpty(res.getResponseCode())) {
                res.setResponseCode(res.isSuccessful() ? "200" : "500");
            }
            if (res.isSuccessful()) {
                res.setResponseMessageOK();
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            res.setResponseMessage(ex.getMessage());
            res.setResponseData((ex.toString() + "\r\n" + JMeterPluginsUtils.getStackTrace(ex)).getBytes());
            res.setResponseCode("500");
            res.setSuccessful(false);
            if (res.getStartTime() == 0) {
                res.sampleStart();
            }

            if (res.getEndTime() == 0) {
                res.sampleEnd();
            }
        }

        return res;
    }

    public String getScript() {
        return getPropertyAsString(SCRIPT, defaultScript);
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

    public String getScriptLanguage() {
        return getPropertyAsString(SCRIPT_LANGUAGE, DEFAULT_ENGINE);
    }

    public void setScriptLanguage(String lang) {
        setProperty(SCRIPT_LANGUAGE, lang);
    }

    private WebDriver getWebDriver() {
        return (WebDriver) getThreadContext().getVariables().getObject(WebDriverConfig.BROWSER);
    }

    ScriptEngine createScriptEngineWith(SampleResult sampleResult) {
        final ScriptEngine scriptEngine = scriptEngineManager.getEngineByName(this.getScriptLanguage());
        Bindings engineBindings = new SimpleBindings();
        WebDriverScriptable scriptable = new WebDriverScriptable();
        scriptable.setName(getName());
        scriptable.setParameters(getParameters());
        JMeterContext context = JMeterContextService.getContext();
        scriptable.setVars(context.getVariables());
        scriptable.setProps(JMeterUtils.getJMeterProperties());
        scriptable.setCtx(context);
        scriptable.setLog(LOGGER);
        scriptable.setSampleResult(sampleResult);
        scriptable.setBrowser(getWebDriver());
        engineBindings.put("WDS", scriptable);
        scriptEngine.setBindings(engineBindings, ScriptContext.ENGINE_SCOPE);
        return scriptEngine;
    }
}
