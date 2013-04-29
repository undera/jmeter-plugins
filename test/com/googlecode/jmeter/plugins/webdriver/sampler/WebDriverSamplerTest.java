package com.googlecode.jmeter.plugins.webdriver.sampler;

import com.googlecode.jmeter.plugins.webdriver.config.WebDriverConfig;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.log.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class WebDriverSamplerTest {

    private WebDriverSampler sampler;
    private JMeterVariables variables;
    private WebDriver browser;

    @Before
    public void createSampler() {
        browser = Mockito.mock(WebDriver.class);
        when(browser.getPageSource()).thenReturn("page source");
        when(browser.getCurrentUrl()).thenReturn("http://google.com.au");
        variables = new JMeterVariables();
        variables.putObject(WebDriverConfig.BROWSER, browser);
        JMeterContextService.getContext().setVariables(variables);
        sampler = new WebDriverSampler();
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
    public void shouldHaveExpectedInstanceVariablesOnScriptContext() {
        sampler.setName("name");
        sampler.setParameters("p1 p2 p3");
        final SampleResult sampleResult = new SampleResult();
        final ScriptEngine scriptEngine = sampler.createScriptEngineWith(sampleResult);
        final ScriptContext scriptContext = scriptEngine.getContext();
        assertThat(scriptContext.getAttribute("log"), is(instanceOf(Logger.class)));
        assertThat((PrintStream) scriptContext.getAttribute("OUT"), is(System.out));
        assertThat((String) scriptContext.getAttribute("Label"), is(sampler.getName()));
        assertThat((String) scriptContext.getAttribute("Parameters"), is(sampler.getParameters()));
        assertThat((String[]) scriptContext.getAttribute("args"), is(new String[]{"p1", "p2", "p3"}));
        assertThat(scriptContext.getAttribute("Browser"), is(instanceOf(WebDriver.class)));
        assertThat((SampleResult) scriptContext.getAttribute("SampleResult"), is(sampleResult));
    }

    @Test
    public void shouldReturnSuccessfulSampleResultWhenEvalScriptCompletes() throws MalformedURLException {
        sampler.setName("name");
        sampler.setScript("var x = 'hello';");
        final SampleResult sampleResult = sampler.sample(null);

        assertThat(sampleResult.isResponseCodeOK(), is(true));
        assertThat(sampleResult.getResponseMessage(), is("OK"));
        assertThat(sampleResult.isSuccessful(), is(true));
        assertThat(sampleResult.getContentType(), is("text/plain"));
        assertThat(sampleResult.getDataEncodingNoDefault(), is("UTF-8"));
        assertThat(sampleResult.getDataType(), is(SampleResult.TEXT));
        assertThat(sampleResult.getSampleLabel(), is("name"));
        assertThat(sampleResult.getResponseDataAsString(), is("page source"));
        assertThat(sampleResult.getURL(), is(new URL("http://google.com.au")));

        verify(browser, times(1)).getPageSource();
        verify(browser, times(1)).getCurrentUrl();
    }

    @Test
    public void shouldReturnSuccessfulSampleResultWhenLastStatementFromEvalScriptIsTrue() throws MalformedURLException {
        sampler.setScript("true");
        final SampleResult sampleResult = sampler.sample(null);

        assertThat(sampleResult.isResponseCodeOK(), is(true));
        assertThat(sampleResult.getResponseMessage(), is("OK"));
        assertThat(sampleResult.isSuccessful(), is(true));
        assertThat(sampleResult.getResponseDataAsString(), is("page source"));
        assertThat(sampleResult.getURL(), is(new URL("http://google.com.au")));

        verify(browser, times(1)).getPageSource();
        verify(browser, times(1)).getCurrentUrl();
    }

    @Test
    public void shouldReturnFailureSampleResultWhenLastStatementFromEvalScriptIsFalse() throws MalformedURLException {
        sampler.setScript("false");
        final SampleResult sampleResult = sampler.sample(null);

        assertThat(sampleResult.isResponseCodeOK(), is(false));
        assertThat(sampleResult.getResponseMessage(), is("Failed to find/verify expected content on page"));
        assertThat(sampleResult.isSuccessful(), is(false));
        assertThat(sampleResult.getResponseDataAsString(), is("page source"));
        assertThat(sampleResult.getURL(), is(new URL("http://google.com.au")));

        verify(browser, times(1)).getPageSource();
        verify(browser, times(1)).getCurrentUrl();
    }

    @Test
    public void shouldReturnFailureSampleResultWhenEvalScriptIsInvalid() {
        sampler.setScript("x.methodThatDoesNotExist();");
        final SampleResult sampleResult = sampler.sample(null);

        assertThat(sampleResult.isResponseCodeOK(), is(false));
        assertThat(sampleResult.getResponseMessage(), containsString("javax.script.ScriptException"));
        assertThat(sampleResult.isSuccessful(), is(false));

        verify(browser, never()).getPageSource();
        verify(browser, never()).getCurrentUrl();
    }

    @Test
    public void shouldReturnFailureSampleResultWhenBrowserURLIsInvalid() {
        when(browser.getCurrentUrl()).thenReturn("unknown://uri");
        sampler.setScript("var x = 'hello';");
        final SampleResult sampleResult = sampler.sample(null);

        assertThat(sampleResult.isResponseCodeOK(), is(false));
        assertThat(sampleResult.getResponseMessage(), containsString("MalformedURLException"));
        assertThat(sampleResult.isSuccessful(), is(false));

        verify(browser, times(1)).getCurrentUrl();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenBrowserNotConfigured() {
        variables.remove(WebDriverConfig.BROWSER);
        sampler.setScript("var x=1;");
        sampler.sample(null);
        fail("Did not throw expected exception"); // should throw exception if Browser is null
    }
}
