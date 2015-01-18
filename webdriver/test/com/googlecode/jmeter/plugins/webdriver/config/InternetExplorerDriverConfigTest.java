package com.googlecode.jmeter.plugins.webdriver.config;

import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.*;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(InternetExplorerDriverConfig.class)
public class InternetExplorerDriverConfigTest {

    private InternetExplorerDriverConfig config;
    private JMeterVariables variables;

    @Before
    public void createConfig() {
        config = new InternetExplorerDriverConfig();
        variables = new JMeterVariables();
        JMeterContextService.getContext().setVariables(variables);
    }

    @After
    public void resetConfig() {
        config.clearThreadBrowsers();
        config.getServices().clear();
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
        final InternetExplorerDriverConfig deserializedConfig = (InternetExplorerDriverConfig) input.readObject();

        assertThat(deserializedConfig, is(config));
    }

    @Test
    public void shouldCreateInternetExplorerAndStartService() throws Exception {
        InternetExplorerDriver mockInternetExplorerDriver = mock(InternetExplorerDriver.class);
        whenNew(InternetExplorerDriver.class).withParameterTypes(InternetExplorerDriverService.class, Capabilities.class).withArguments(isA(InternetExplorerDriverService.class), isA(Capabilities.class)).thenReturn(mockInternetExplorerDriver);
        InternetExplorerDriverService.Builder mockServiceBuilder = mock(InternetExplorerDriverService.Builder.class);
        whenNew(InternetExplorerDriverService.Builder.class).withNoArguments().thenReturn(mockServiceBuilder);
        when(mockServiceBuilder.usingDriverExecutable(isA(File.class))).thenReturn(mockServiceBuilder);
        InternetExplorerDriverService mockService = mock(InternetExplorerDriverService.class);
        when(mockServiceBuilder.build()).thenReturn(mockService);

        final InternetExplorerDriver browser = config.createBrowser();

        assertThat(browser, is(mockInternetExplorerDriver));
        verifyNew(InternetExplorerDriver.class, times(1)).withArguments(isA(InternetExplorerDriverService.class), isA(Capabilities.class));
        verify(mockServiceBuilder, times(1)).build();
        assertThat(config.getServices().size(), is(1));
        assertThat(config.getServices().values(), hasItem(mockService));
    }

    @Test
    public void shouldNotCreateInternetExplorerWhenStartingServiceThrowsAnException() throws Exception {
        InternetExplorerDriverService.Builder mockServiceBuilder = mock(InternetExplorerDriverService.Builder.class);
        whenNew(InternetExplorerDriverService.Builder.class).withNoArguments().thenReturn(mockServiceBuilder);
        when(mockServiceBuilder.usingDriverExecutable(isA(File.class))).thenReturn(mockServiceBuilder);
        InternetExplorerDriverService mockService = mock(InternetExplorerDriverService.class);
        when(mockServiceBuilder.build()).thenReturn(mockService);
        doThrow(new IOException("Stubbed exception")).when(mockService).start();

        final InternetExplorerDriver browser = config.createBrowser();

        assertThat(browser, is(nullValue()));
        assertThat(config.getServices(), is(Collections.<String, InternetExplorerDriverService>emptyMap()));
        verify(mockServiceBuilder, times(1)).build();
    }

    @Test
    public void shouldQuitWebDriverAndStopServiceWhenQuitBrowserIsInvoked() throws Exception {
        InternetExplorerDriver mockInternetExplorerDriver = mock(InternetExplorerDriver.class);
        InternetExplorerDriverService mockService = mock(InternetExplorerDriverService.class);
        when(mockService.isRunning()).thenReturn(true);
        config.getServices().put(config.currentThreadName(), mockService);

        config.quitBrowser(mockInternetExplorerDriver);

        verify(mockInternetExplorerDriver).quit();
        assertThat(config.getServices(), is(Collections.<String, InternetExplorerDriverService>emptyMap()));
        verify(mockService, times(1)).stop();
    }

    @Test
    public void shouldNotStopServiceIfNotRunningWhenQuitBrowserIsInvoked() throws Exception {
        InternetExplorerDriver mockInternetExplorerDriver = mock(InternetExplorerDriver.class);
        InternetExplorerDriverService mockService = mock(InternetExplorerDriverService.class);
        when(mockService.isRunning()).thenReturn(false);
        config.getServices().put(config.currentThreadName(), mockService);

        config.quitBrowser(mockInternetExplorerDriver);

        verify(mockInternetExplorerDriver).quit();
        assertThat(config.getServices(), is(Collections.<String, InternetExplorerDriverService>emptyMap()));
        verify(mockService, times(0)).stop();
    }

    @Test
    public void shouldBeAbleToCallQuitBrowserMultipleTimes() throws Exception {
        InternetExplorerDriver mockInternetExplorerDriver = mock(InternetExplorerDriver.class);
        InternetExplorerDriverService mockService = mock(InternetExplorerDriverService.class);
        when(mockService.isRunning()).thenReturn(true);
        config.getServices().put(config.currentThreadName(), mockService);

        config.quitBrowser(mockInternetExplorerDriver);
        config.quitBrowser(mockInternetExplorerDriver);

        assertThat(config.getServices(), is(Collections.<String, InternetExplorerDriverService>emptyMap()));
        verify(mockService, times(1)).stop();
    }

    @Test
    public void shouldHaveProxyInCapability() {
        final Capabilities capabilities = config.createCapabilities();
        assertThat(capabilities.getCapability(CapabilityType.PROXY), is(notNullValue()));
    }
}
