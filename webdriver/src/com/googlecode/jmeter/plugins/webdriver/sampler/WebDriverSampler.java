package com.googlecode.jmeter.plugins.webdriver.sampler;

import com.googlecode.jmeter.plugins.webdriver.config.WebDriverConfig;
import java.net.URL;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.openqa.selenium.WebDriver;


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
    private final Class<SampleResult> sampleResultClass;

    private static final String defaultScript = "WDS.sampleResult.sampleStart()\n" +
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
            res.setResponseCode(res.isSuccessful() ? "200" : "500");
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
        String propertyAsString = getPropertyAsString(SCRIPT, defaultScript);
        return propertyAsString;
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
