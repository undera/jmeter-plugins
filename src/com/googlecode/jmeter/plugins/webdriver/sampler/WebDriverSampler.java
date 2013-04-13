package com.googlecode.jmeter.plugins.webdriver.sampler;

import com.googlecode.jmeter.plugins.webdriver.config.WebDriverConfig;
import org.apache.bsf.BSFException;
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
	
	/**
	 * This declares the 'websampler' variable, which is a shorthand for accessing <code>org.openqa.selenium</code> and
	 * <code>org.openqa.selenium.support.ui</code> classes without specifying the full package name.  The shorthand for
	 * accessing these classes is as follows:
	 * <pre>
	 * with(websampler) {
	 *     var element = browser.findElement(By.id('myId'));
	 * }
	 * </pre>
	 */
	private static final String SCRIPT_UTILITY = "var websampler = JavaImporter(org.openqa.selenium, org.openqa.selenium.support.ui, org.openqa.selenium.interactions.touch)";

    public static final String SCRIPT = "WebDriverSampler.script";

	public static final String PARAMETERS = "WebDriverSampler.parameters";
    
	private static final Logger LOGGER = LoggingManager.getLoggerForClass();
	
	private static final long serialVersionUID = 234L;
	
	@Override
	public SampleResult sample(Entry e) {
        LOGGER.info("sampling web");
        
        // BSF Code copied liberally from BSFSampler
        final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        
        final SampleResult res = new SampleResult();
        res.setSampleLabel(getName());
        res.setSamplerData(toString());
        res.setDataType(SampleResult.TEXT);
        res.setContentType("text/plain"); // $NON-NLS-1$
        res.setDataEncoding("UTF-8");

        // Assume we will be successful
        res.setSuccessful(true);
        res.setResponseMessageOK();
        res.setResponseCodeOK();

        LOGGER.info("Current thread name: '"+getThreadName()+"', has browser: '"+getThreadContext().getVariables().getObject(WebDriverConfig.BROWSER)+"'");

        try {
            final ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
            initManager(scriptEngine, res);

            // utility importer
            scriptEngine.eval(SCRIPT_UTILITY);

            final Object outcome;
            if(scriptEngine instanceof Compilable) {
                LOGGER.info("Compiling!");
                CompiledScript compiled = ((Compilable)scriptEngine).compile(getScript());
                outcome = compiled.eval();
            } else {
                LOGGER.info("Interpreting!");
                outcome = scriptEngine.eval(getScript());
            }

            // setup status and data useful for verification
            res.setResponseData(getWebDriver().getPageSource().getBytes());
            if(outcome instanceof Boolean) { // only set this if the return value is boolean
                res.setSuccessful((Boolean) outcome);
            }

            // fail the response if unsuccessful
            if(!res.isSuccessful()) {
                res.setResponseCode("500");
                res.setResponseMessage("Failed to find/verify expected content on page");
            }

        } catch (Exception ex) {
            res.setResponseMessage(ex.toString());
            res.setResponseCode("500");
            if(ex.getMessage() != null) {
                res.setResponseData(ex.getMessage().getBytes());
            }
            res.setSuccessful(false);
        }

        return res;
	}

    private void initManager(ScriptEngine scriptEngine, SampleResult res) throws BSFException {
   		final String scriptParameters = getParameters();

        Bindings global = scriptEngine.createBindings();
        global.put("log", LOGGER); // $NON-NLS-1$
        global.put("Label", getName()); // $NON-NLS-1$
        global.put("OUT", System.out); // $NON-NLS-1$
        scriptEngine.setBindings(global, ScriptContext.GLOBAL_SCOPE);

        Bindings perExecution = scriptEngine.createBindings();
        perExecution.put("SampleResult", res); // $NON-NLS-1$
        perExecution.put("Parameters", scriptParameters); // $NON-NLS-1$
        String[] args = JOrphanUtils.split(scriptParameters, " ");//$NON-NLS-1$
        perExecution.put("args", args);//$NON-NLS-1$
        perExecution.put("Browser", getWebDriver());
        scriptEngine.setBindings(perExecution, ScriptContext.ENGINE_SCOPE);
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
}
