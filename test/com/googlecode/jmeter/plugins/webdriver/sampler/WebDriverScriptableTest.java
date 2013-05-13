package com.googlecode.jmeter.plugins.webdriver.sampler;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class WebDriverScriptableTest {
    private WebDriverScriptable scriptable;
    @Before
    public void createScriptable() {
        scriptable = new WebDriverScriptable();
    }

    @Test
    public void shouldAssignName() {
        scriptable.setName("name");
        assertThat(scriptable.getName(), is("name"));
    }

    @Test
    public void shouldAssignParameters() {
        scriptable.setParameters("p1 p2");
        assertThat(scriptable.getParameters(), is("p1 p2"));
    }

    @Test
    public void shouldReturnArgsBySplittingParametersBySpace() {
        final String parameters = "p1 p2 p3";
        final String[] args = parameters.split(" ");

        scriptable.setParameters(parameters);
        assertThat(scriptable.getArgs(), is(args));
    }

    @Test
    public void shouldReturnEmptyArgsWhenParametersIsNull() {
        scriptable.setParameters(null);
        assertThat(scriptable.getArgs(), is(new String[]{}));
    }

    @Test
    public void shouldTrimSpacesBetweenParameters() {
        scriptable.setParameters(" p1 p2  p3   ");
        assertThat(scriptable.getArgs(), is(new String[]{"p1", "p2", "p3"}));
    }

    @Test
    public void shouldAssignLog() {
        final Logger logger = LoggingManager.getLoggerForClass();
        scriptable.setLog(logger);
        assertThat(scriptable.getLog(), is(logger));
    }

    @Test
    public void shouldAssignBrowser() {
        final WebDriver browser = Mockito.mock(WebDriver.class);
        scriptable.setBrowser(browser);
        assertThat(scriptable.getBrowser(), is(browser));
    }

    @Test
    public void shouldAssignSampleResult() {
        final SampleResult sampleResult = new SampleResult();
        scriptable.setSampleResult(sampleResult);
        assertThat(scriptable.getSampleResult(), is(sampleResult));
    }
}
