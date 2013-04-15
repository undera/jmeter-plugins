package com.googlecode.jmeter.plugins.webdriver.sampler;

import com.googlecode.jmeter.plugins.webdriver.config.WebDriverConfig;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.log.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;

import javax.script.*;

import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WebDriverSamplerTest {

    private ScriptEngineManager scriptEngineManager;
    private WebDriverSampler sampler;
    private JMeterVariables variables;
    private WebDriver browser;

    @Before
    public void createSampler() {
        scriptEngineManager = Mockito.mock(ScriptEngineManager.class);
        browser = Mockito.mock(WebDriver.class);
        when(browser.getPageSource()).thenReturn("");
        variables = new JMeterVariables();
        variables.putObject(WebDriverConfig.BROWSER, browser);
        JMeterContextService.getContext().setVariables(variables);
        sampler = new WebDriverSampler(scriptEngineManager);
    }

    @Test
    public void shouldBeAbleToSetParameters() {
        sampler.setParameters("parameters");
        assertThat(sampler.getParameters(), is("parameters"));
    }

    @Test
    public void shouldBeAbleToSetScript() {
        sampler.setScript("script");
        assertThat(sampler.getScript(), is("script"));
    }

    @Test
    public void shouldReturnSuccessfulSampleResultWhenEvalScriptCompletes() throws ScriptException {
        final ArgumentCaptor<Bindings> globalBindingsCaptor = ArgumentCaptor.forClass(Bindings.class);
        final ArgumentCaptor<Bindings> engineBindingsCaptor = ArgumentCaptor.forClass(Bindings.class);
        final ScriptEngine scriptEngine = Mockito.mock(ScriptEngine.class);
        when(scriptEngineManager.getEngineByName("JavaScript")).thenReturn(scriptEngine);
        when(scriptEngine.eval("script")).thenReturn(null);

        sampler.setName("name");
        sampler.setScript("script");
        final String parameters = "parameter1 parameter2";
        final String[] args = parameters.split(" ");
        sampler.setParameters(parameters);
        final SampleResult sampleResult = sampler.sample(null);

        assertThat(sampleResult.isResponseCodeOK(), is(true));
        assertThat(sampleResult.getResponseMessage(), is("OK"));
        assertThat(sampleResult.isSuccessful(), is(true));
        assertThat(sampleResult.getContentType(), is("text/plain"));
        assertThat(sampleResult.getDataEncodingNoDefault(), is("UTF-8"));
        assertThat(sampleResult.getDataType(), is(SampleResult.TEXT));
        assertThat(sampleResult.getSampleLabel(), is("name"));

        verify(scriptEngine).setBindings(globalBindingsCaptor.capture(), eq(ScriptContext.GLOBAL_SCOPE));
        Bindings globalBindings = globalBindingsCaptor.getValue();
        assertThat(globalBindings.get("log"), is(instanceOf(Logger.class)));
        assertThat((String) globalBindings.get("Label"), is("name"));
        assertThat(globalBindings.get("OUT"), is(instanceOf(PrintStream.class)));

        verify(scriptEngine).setBindings(engineBindingsCaptor.capture(), eq(ScriptContext.ENGINE_SCOPE));
        Bindings engineBindings = engineBindingsCaptor.getValue();
        assertThat((SampleResult) engineBindings.get("SampleResult"), is(sampleResult));
        assertThat((String) engineBindings.get("Parameters"), is(parameters));
        assertThat((String[]) engineBindings.get("args"), is(args));
        assertThat((WebDriver) engineBindings.get("Browser"), is(browser));

        verify(browser, times(1)).getPageSource();
        verify(scriptEngineManager, times(1)).getEngineByName("JavaScript");
        verify(scriptEngine, times(1)).eval("script");
    }
}
