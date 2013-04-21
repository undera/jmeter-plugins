package com.googlecode.jmeter.plugins.webdriver.config;

import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.testelement.ThreadListener;
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

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.hamcrest.CoreMatchers.*;
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
        ChromeDriverConfig.webdrivers.clear();
        ChromeDriverConfig.services.clear();
        JMeterContextService.getContext().setVariables(null);
    }

    @Test
    public void shouldImplementThreadListener() {
        assertThat(config, is(instanceOf(ThreadListener.class)));
    }

    @Test
    public void shouldCreateWebDriverWhenThreadStartedIsInvoked() throws Exception {
        ChromeDriver mockChromeDriver = mock(ChromeDriver.class);
        whenNew(ChromeDriver.class).withParameterTypes(ChromeDriverService.class, Capabilities.class).withArguments(isA(ChromeDriverService.class), isA(Capabilities.class)).thenReturn(mockChromeDriver);
        ChromeDriverService.Builder mockServiceBuilder = mock(ChromeDriverService.Builder.class);
        whenNew(ChromeDriverService.Builder.class).withNoArguments().thenReturn(mockServiceBuilder);
        when(mockServiceBuilder.usingDriverExecutable(isA(File.class))).thenReturn(mockServiceBuilder);
        ChromeDriverService mockService = mock(ChromeDriverService.class);
        when(mockServiceBuilder.build()).thenReturn(mockService);

        config.threadStarted();

        assertThat((ChromeDriver) config.getCurrentThreadBrowser(), is(mockChromeDriver));
        assertThat(config.services.size(), is(1));
        assertThat(config.services.values(), hasItem(mockService));
        verifyNew(ChromeDriver.class, times(1)).withArguments(isA(ChromeDriverService.class), isA(Capabilities.class));
        verify(mockServiceBuilder, times(1)).build();
    }

    @Test
    public void shouldOnlyCreateSingleWebDriverEvenWhenThreadStartedIsCalledMultipleTimes() throws Exception {
        ChromeDriver mockChromeDriver = mock(ChromeDriver.class);
        whenNew(ChromeDriver.class).withParameterTypes(ChromeDriverService.class, Capabilities.class).withArguments(isA(ChromeDriverService.class), isA(Capabilities.class)).thenReturn(mockChromeDriver);
        ChromeDriverService.Builder mockServiceBuilder = mock(ChromeDriverService.Builder.class);
        whenNew(ChromeDriverService.Builder.class).withNoArguments().thenReturn(mockServiceBuilder);
        when(mockServiceBuilder.usingDriverExecutable(isA(File.class))).thenReturn(mockServiceBuilder);
        ChromeDriverService mockService = mock(ChromeDriverService.class);
        when(mockServiceBuilder.build()).thenReturn(mockService);

        config.threadStarted();
        config.threadStarted();

        assertThat((ChromeDriver) config.getCurrentThreadBrowser(), is(mockChromeDriver));
        assertThat(config.services.size(), is(1));
        assertThat(config.services.values(), hasItem(mockService));
        verifyNew(ChromeDriver.class, times(1)).withArguments(isA(ChromeDriverService.class), isA(Capabilities.class));
        verify(mockServiceBuilder, times(1)).build();
    }

    @Test
    public void shouldNotContainWebDriverWhenStartingServiceThrowsAnException() throws Exception {
        ChromeDriverService.Builder mockServiceBuilder = mock(ChromeDriverService.Builder.class);
        whenNew(ChromeDriverService.Builder.class).withNoArguments().thenReturn(mockServiceBuilder);
        when(mockServiceBuilder.usingDriverExecutable(isA(File.class))).thenReturn(mockServiceBuilder);
        ChromeDriverService mockService = mock(ChromeDriverService.class);
        when(mockServiceBuilder.build()).thenReturn(mockService);
        doThrow(new IOException("Stubbed exception")).when(mockService).start();

        config.threadStarted();

        assertThat((ChromeDriver) config.getCurrentThreadBrowser(), is(nullValue()));
        assertThat(config.services, is(Collections.<String, ChromeDriverService>emptyMap()));
        verify(mockServiceBuilder, times(1)).build();
    }

    @Test
    public void shouldQuitWebDriverAndStopServiceWhenThreadFinishedIsInvoked() throws Exception {
        ChromeDriver mockChromeDriver = mock(ChromeDriver.class);
        ChromeDriverConfig.webdrivers.put(config.currentThreadName(), mockChromeDriver);
        ChromeDriverService mockService = mock(ChromeDriverService.class);
        when(mockService.isRunning()).thenReturn(true);
        ChromeDriverConfig.services.put(config.currentThreadName(), mockService);

        config.threadFinished();

        assertThat(config.getCurrentThreadBrowser(), is(nullValue()));
        verify(mockChromeDriver, times(1)).quit();
        assertThat(config.services, is(Collections.<String, ChromeDriverService>emptyMap()));
        verify(mockService, times(1)).stop();
    }

    @Test
    public void shouldNotStopServiceIfNotRunningWhenThreadFinishedIsInvoked() throws Exception {
        ChromeDriver mockChromeDriver = mock(ChromeDriver.class);
        ChromeDriverConfig.webdrivers.put(config.currentThreadName(), mockChromeDriver);
        ChromeDriverService mockService = mock(ChromeDriverService.class);
        when(mockService.isRunning()).thenReturn(false);
        ChromeDriverConfig.services.put(config.currentThreadName(), mockService);

        config.threadFinished();

        assertThat(config.getCurrentThreadBrowser(), is(nullValue()));
        verify(mockChromeDriver, times(1)).quit();
        assertThat(config.services, is(Collections.<String, ChromeDriverService>emptyMap()));
        verify(mockService, times(0)).stop();
    }

    @Test
    public void shouldBeAbleToCallThreadFinishedMultipleTimes() throws Exception {
        ChromeDriver mockChromeDriver = mock(ChromeDriver.class);
        ChromeDriverConfig.webdrivers.put(config.currentThreadName(), mockChromeDriver);
        ChromeDriverService mockService = mock(ChromeDriverService.class);
        when(mockService.isRunning()).thenReturn(true);
        ChromeDriverConfig.services.put(config.currentThreadName(), mockService);

        config.threadFinished();
        config.threadFinished();

        assertThat(config.getCurrentThreadBrowser(), is(nullValue()));
        verify(mockChromeDriver, times(1)).quit();
        assertThat(config.services, is(Collections.<String, ChromeDriverService>emptyMap()));
        verify(mockService, times(1)).stop();
    }

    @Test
    public void shouldCreateWebDriverPerThreadWhenThreadStartedIsInvoked() throws Exception {
        // mock the browsers that will be created per thread
        ChromeDriver firstChrome = mock(ChromeDriver.class);
        ChromeDriver secondChrome = mock(ChromeDriver.class);
        whenNew(ChromeDriver.class).withParameterTypes(ChromeDriverService.class, Capabilities.class).withArguments(isA(ChromeDriverService.class), isA(Capabilities.class)).thenReturn(firstChrome, secondChrome);
        ChromeDriverService.Builder firstServiceBuilder = mock(ChromeDriverService.Builder.class);
        ChromeDriverService.Builder secondServiceBuilder = mock(ChromeDriverService.Builder.class);
        whenNew(ChromeDriverService.Builder.class).withNoArguments().thenReturn(firstServiceBuilder, secondServiceBuilder);
        when(firstServiceBuilder.usingDriverExecutable(isA(File.class))).thenReturn(firstServiceBuilder);
        when(secondServiceBuilder.usingDriverExecutable(isA(File.class))).thenReturn(secondServiceBuilder);
        ChromeDriverService firstService = mock(ChromeDriverService.class);
        when(firstServiceBuilder.build()).thenReturn(firstService);
        ChromeDriverService secondService = mock(ChromeDriverService.class);
        when(secondServiceBuilder.build()).thenReturn(secondService);
        whenNew(ChromeDriver.class).withParameterTypes(Capabilities.class).withArguments(isA(ChromeDriverService.class), isA(Capabilities.class)).thenReturn(firstChrome, secondChrome);

        // the list will store the returned browsers for each thread so they can be verified.
        final List<ChromeDriver> ChromeesFromThread = new CopyOnWriteArrayList<ChromeDriver>();
        Thread firstThread = new Thread() {
            public void run() {
                config.threadStarted();
                ChromeesFromThread.add((ChromeDriver) config.getCurrentThreadBrowser());
            }
        };
        Thread secondThread = new Thread() {
            public void run() {
                config.threadStarted();
                ChromeesFromThread.add((ChromeDriver) config.getCurrentThreadBrowser());
            }
        };

        // start and wait for threads to finish
        firstThread.start();
        secondThread.start();
        firstThread.join();
        secondThread.join();

        // assertions
        assertThat(ChromeesFromThread.size(), is(2));
        assertThat(ChromeesFromThread, hasItem(firstChrome));
        assertThat(ChromeesFromThread, hasItem(secondChrome));
        verifyNew(ChromeDriver.class, times(2)).withArguments(isA(ChromeDriverService.class), isA(Capabilities.class));
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
        ChromeDriver mockChromeDriver = mock(ChromeDriver.class);
        whenNew(ChromeDriver.class).withParameterTypes(ChromeDriverService.class, Capabilities.class).withArguments(isA(ChromeDriverService.class), isA(Capabilities.class)).thenReturn(mockChromeDriver);
        ChromeDriverService.Builder mockServiceBuilder = mock(ChromeDriverService.Builder.class);
        whenNew(ChromeDriverService.Builder.class).withNoArguments().thenReturn(mockServiceBuilder);
        when(mockServiceBuilder.usingDriverExecutable(isA(File.class))).thenReturn(mockServiceBuilder);
        ChromeDriverService mockService = mock(ChromeDriverService.class);
        when(mockServiceBuilder.build()).thenReturn(mockService);

        config.threadStarted();
        config.iterationStart(null);

        assertThat(variables.getObject(WebDriverConfig.BROWSER), is(notNullValue()));
        assertThat((ChromeDriver) variables.getObject(WebDriverConfig.BROWSER), is(config.getCurrentThreadBrowser()));
    }
}
