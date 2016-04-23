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
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
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
    public void shouldBeAbleToSerialiseAndDeserialise() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(bytes);

        output.writeObject(sampler);
        output.flush();
        output.close();

        ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()));
        final WebDriverSampler deserializedSampler = (WebDriverSampler) input.readObject();

        assertThat(deserializedSampler, is(sampler));
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
        final WebDriverScriptable scriptable = (WebDriverScriptable) scriptContext.getAttribute("WDS");
        assertThat(scriptable.getLog(), is(instanceOf(Logger.class)));
        assertThat(scriptable.getName(), is(sampler.getName()));
        assertThat(scriptable.getParameters(), is(sampler.getParameters()));
        assertThat(scriptable.getArgs(), is(new String[]{"p1", "p2", "p3"}));
        assertThat(scriptable.getBrowser(), is(instanceOf(WebDriver.class)));
        assertThat(scriptable.getSampleResult(), is(sampleResult));
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
        assertThat(sampleResult.getDataType(), is(SampleResult.TEXT));
        assertThat(sampleResult.getSampleLabel(), is("name"));
        assertThat(sampleResult.getResponseDataAsString(), is("page source"));
        assertThat(sampleResult.getURL(), is(new URL("http://google.com.au")));

        verify(browser, times(1)).getPageSource();
        verify(browser, times(1)).getCurrentUrl();
    }

    @Test
    public void shouldReturnSuccessfulSampleResultWhenScriptSetsSampleResultToSuccess() throws MalformedURLException {
        sampler.setScript("WDS.sampleResult.setSuccessful(true);");
        final SampleResult sampleResult = sampler.sample(null);

        assertThat(sampleResult.isSuccessful(), is(true));
        assertThat(sampleResult.getResponseCode(), is("200"));
        assertThat(sampleResult.getResponseMessage(), is("OK"));
        assertThat(sampleResult.getResponseDataAsString(), is("page source"));
        assertThat(sampleResult.getURL(), is(new URL("http://google.com.au")));

        verify(browser, times(1)).getPageSource();
        verify(browser, times(1)).getCurrentUrl();
    }

    @Test
    public void shouldReturnFailureSampleResultWhenScriptSetsSampleResultToFailure() throws MalformedURLException {
        sampler.setScript("WDS.sampleResult.setSuccessful(false);");
        final SampleResult sampleResult = sampler.sample(null);

        assertThat(sampleResult.isSuccessful(), is(false));
        assertThat(sampleResult.getResponseCode(), is("500"));
        assertThat(sampleResult.getResponseMessage(), not("OK"));
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
        assertThat(sampleResult.getResponseMessage(), containsString("ReferenceError"));
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
        assertThat(sampleResult.getResponseMessage(), containsString("unknown"));
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
