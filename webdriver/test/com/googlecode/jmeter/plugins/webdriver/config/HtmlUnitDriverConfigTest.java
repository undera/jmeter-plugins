package com.googlecode.jmeter.plugins.webdriver.config;

import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 *
 * @author cpl_rewinds
 * @author vincent daburon
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(HtmlUnitDriverConfig.class)
public class HtmlUnitDriverConfigTest {

    private HtmlUnitDriverConfig config;
    private JMeterVariables variables;

    @Before
    public void createConfig() {
        config = new HtmlUnitDriverConfig();
        variables = new JMeterVariables();
        JMeterContextService.getContext().setVariables(variables);
    }

    @After
    public void resetConfig() {
        config.clearThreadBrowsers();
        JMeterContextService.getContext().setVariables(null);
    }

    @Test
    public void shouldBeAbleToSerialiseAndDeserialise() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(bytes);

        output.writeObject(config);
        output.flush();
        output.close();

        ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()));
        final HtmlUnitDriverConfig deserializedConfig = (HtmlUnitDriverConfig) input.readObject();

        assertThat(deserializedConfig, is(config));
    }

    @Test
    public void shouldImplementThreadListener() {
        assertThat(config, is(instanceOf(ThreadListener.class)));
    }

    @Test
    public void shouldCreateHtmlUnit() throws Exception {
        HtmlUnitDriver mockHtmlUnitDriver = Mockito.mock(HtmlUnitDriver.class);
        whenNew(HtmlUnitDriver.class).withParameterTypes(Capabilities.class).withArguments(isA(Capabilities.class)).thenReturn(mockHtmlUnitDriver);

        final HtmlUnitDriver browser = config.createBrowser();

        assertThat(browser, is(mockHtmlUnitDriver));
        verifyNew(HtmlUnitDriver.class, times(1)).withArguments(isA(Capabilities.class));
    }

    @Test
    public void shouldCreateWebDriverWhenThreadStartedIsInvoked() throws Exception {
        HtmlUnitDriver mockHtmlUnitDriver = Mockito.mock(HtmlUnitDriver.class);
        whenNew(HtmlUnitDriver.class).withParameterTypes(Capabilities.class).withArguments(isA(Capabilities.class)).thenReturn(mockHtmlUnitDriver);

        config.threadStarted();

        assertThat(config.getThreadBrowser(), is(mockHtmlUnitDriver));
        verifyNew(HtmlUnitDriver.class, times(1)).withArguments(isA(Capabilities.class));
    }

    @Test
    public void shouldOnlyCreateSingleWebDriverEvenWhenThreadStartedIsCalledMultipleTimes() throws Exception {
        HtmlUnitDriver mockHtmlUnitDriver = Mockito.mock(HtmlUnitDriver.class);
        whenNew(HtmlUnitDriver.class).withParameterTypes(Capabilities.class).withArguments(isA(Capabilities.class)).thenReturn(mockHtmlUnitDriver);

        config.threadStarted();
        config.threadStarted();

        assertThat(config.getThreadBrowser(), is(mockHtmlUnitDriver));
        verifyNew(HtmlUnitDriver.class, times(1)).withArguments(isA(Capabilities.class));
    }

    @Test
    public void shouldQuitWebDriverWhenThreadFinishedIsInvoked() throws Exception {
        HtmlUnitDriver mockHtmlUnitDriver = Mockito.mock(HtmlUnitDriver.class);
        config.setThreadBrowser(mockHtmlUnitDriver);

        config.threadFinished();

        assertThat(config.getThreadBrowser(), is(nullValue()));
        verify(mockHtmlUnitDriver, times(1)).quit();
    }

    @Test
    public void shouldBeAbleToCallThreadFinishedMultipleTimes() throws Exception {
        HtmlUnitDriver mockHtmlUnitDriver = Mockito.mock(HtmlUnitDriver.class);
        config.setThreadBrowser(mockHtmlUnitDriver);

        config.threadFinished();
        config.threadFinished();

        assertThat(config.getThreadBrowser(), is(nullValue()));
        verify(mockHtmlUnitDriver, times(1)).quit();
    }

    @Test
    public void shouldHaveProxyInCapability() {
        final Capabilities capabilities = config.createCapabilities();
        assertThat(capabilities.getCapability(CapabilityType.PROXY), is(notNullValue()));
    }

    @Test
    public void shouldImplementLoopIterationListener() {
        assertThat(config, is(instanceOf(LoopIterationListener.class)));
    }

    @Test
    public void shouldAddWebDriverToJMeterVariablesWhenIterationStarts() throws Exception {
        HtmlUnitDriver mockHtmlUnitDriver = Mockito.mock(HtmlUnitDriver.class);
        whenNew(HtmlUnitDriver.class).withParameterTypes(Capabilities.class).withArguments(isA(Capabilities.class)).thenReturn(mockHtmlUnitDriver);

        config.threadStarted();
        config.iterationStart(null);

        assertThat(variables.getObject(WebDriverConfig.BROWSER), is(notNullValue()));
        assertThat((HtmlUnitDriver) variables.getObject(WebDriverConfig.BROWSER), is(config.getThreadBrowser()));
    }
}
