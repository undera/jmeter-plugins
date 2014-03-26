package com.googlecode.jmeter.plugins.webdriver.config;

import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
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

/**
 *
 * @author cpl_rewinds
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ChromeDriverConfig.class)
public class ChromeDriverConfigTest {

    private ChromeDriverConfig config;
    private JMeterVariables variables;

    @Before
    public void createConfig() {
        config = new ChromeDriverConfig();
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
        final ChromeDriverConfig deserializedConfig = (ChromeDriverConfig) input.readObject();

        assertThat(deserializedConfig, is(config));
    }

    @Test
    public void shouldCreateChromeAndStartService() throws Exception {
        ChromeDriver mockChromeDriver = mock(ChromeDriver.class);
        whenNew(ChromeDriver.class).withParameterTypes(ChromeDriverService.class, Capabilities.class).withArguments(isA(ChromeDriverService.class), isA(Capabilities.class)).thenReturn(mockChromeDriver);
        ChromeDriverService.Builder mockServiceBuilder = mock(ChromeDriverService.Builder.class);
        whenNew(ChromeDriverService.Builder.class).withNoArguments().thenReturn(mockServiceBuilder);
        when(mockServiceBuilder.usingDriverExecutable(isA(File.class))).thenReturn(mockServiceBuilder);
        ChromeDriverService mockService = mock(ChromeDriverService.class);
        when(mockServiceBuilder.build()).thenReturn(mockService);

        final ChromeDriver browser = config.createBrowser();

        assertThat(browser, is(mockChromeDriver));
        verifyNew(ChromeDriver.class, times(1)).withArguments(isA(ChromeDriverService.class), isA(Capabilities.class));
        verify(mockServiceBuilder, times(1)).build();
        assertThat(config.getServices().size(), is(1));
        assertThat(config.getServices().values(), hasItem(mockService));
    }

    @Test
    public void shouldNotCreateChromeWhenStartingServiceThrowsAnException() throws Exception {
        ChromeDriverService.Builder mockServiceBuilder = mock(ChromeDriverService.Builder.class);
        whenNew(ChromeDriverService.Builder.class).withNoArguments().thenReturn(mockServiceBuilder);
        when(mockServiceBuilder.usingDriverExecutable(isA(File.class))).thenReturn(mockServiceBuilder);
        ChromeDriverService mockService = mock(ChromeDriverService.class);
        when(mockServiceBuilder.build()).thenReturn(mockService);
        doThrow(new IOException("Stubbed exception")).when(mockService).start();

        final ChromeDriver browser = config.createBrowser();

        assertThat(browser, is(nullValue()));
        assertThat(config.getServices(), is(Collections.<String, ChromeDriverService>emptyMap()));
        verify(mockServiceBuilder, times(1)).build();
    }

    @Test
    public void shouldQuitWebDriverAndStopServiceWhenQuitBrowserIsInvoked() throws Exception {
        ChromeDriver mockChromeDriver = mock(ChromeDriver.class);
        ChromeDriverService mockService = mock(ChromeDriverService.class);
        when(mockService.isRunning()).thenReturn(true);
        config.getServices().put(config.currentThreadName(), mockService);

        config.quitBrowser(mockChromeDriver);

        verify(mockChromeDriver).quit();
        assertThat(config.getServices(), is(Collections.<String, ChromeDriverService>emptyMap()));
        verify(mockService, times(1)).stop();
    }

    @Test
    public void shouldNotStopServiceIfNotRunningWhenQuitBrowserIsInvoked() throws Exception {
        ChromeDriver mockChromeDriver = mock(ChromeDriver.class);
        ChromeDriverService mockService = mock(ChromeDriverService.class);
        when(mockService.isRunning()).thenReturn(false);
        config.getServices().put(config.currentThreadName(), mockService);

        config.quitBrowser(mockChromeDriver);

        verify(mockChromeDriver).quit();
        assertThat(config.getServices(), is(Collections.<String, ChromeDriverService>emptyMap()));
        verify(mockService, times(0)).stop();
    }

    @Test
    public void shouldBeAbleToCallQuitBrowserMultipleTimes() throws Exception {
        ChromeDriver mockChromeDriver = mock(ChromeDriver.class);
        ChromeDriverService mockService = mock(ChromeDriverService.class);
        when(mockService.isRunning()).thenReturn(true);
        config.getServices().put(config.currentThreadName(), mockService);

        config.quitBrowser(mockChromeDriver);
        config.quitBrowser(mockChromeDriver);

        assertThat(config.getServices(), is(Collections.<String, ChromeDriverService>emptyMap()));
        verify(mockService, times(1)).stop();
    }

    @Test
    public void shouldHaveProxyInCapability() {
        final Capabilities capabilities = config.createCapabilities();
        assertThat(capabilities.getCapability(CapabilityType.PROXY), is(notNullValue()));
    }
}
