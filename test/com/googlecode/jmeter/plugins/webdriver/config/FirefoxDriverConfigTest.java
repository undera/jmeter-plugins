package com.googlecode.jmeter.plugins.webdriver.config;

import org.apache.jmeter.testelement.ThreadListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.internal.matchers.IsCollectionContaining.hasItem;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 *
 * @author cpl_rewinds
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(FirefoxDriverConfig.class)
public class FirefoxDriverConfigTest {
    private FirefoxDriverConfig config;

    @Before
    public void createConfig() {
        config = new FirefoxDriverConfig();
    }

    @After
    public void resetConfig() {
        FirefoxDriverConfig.webdrivers.clear();
    }

    @Test
    public void shouldImplementThreadListener() {
        assertThat(config, is(instanceOf(ThreadListener.class)));
    }

    @Test
    public void shouldCreateWebDriverWhenThreadStartedIsInvoked() throws Exception {
        FirefoxDriver mockFirefoxDriver = Mockito.mock(FirefoxDriver.class);
        whenNew(FirefoxDriver.class).withNoArguments().thenReturn(mockFirefoxDriver);

        config.threadStarted();

        assertThat(config.getCurrentThreadBrowser(), is(mockFirefoxDriver));
        verifyNew(FirefoxDriver.class, times(1)).withNoArguments();
    }

    @Test
    public void shouldOnlyCreateSingleWebDriverEvenWhenThreadStartedIsCalledMultipleTimes() throws Exception {
        FirefoxDriver mockFirefoxDriver = Mockito.mock(FirefoxDriver.class);
        whenNew(FirefoxDriver.class).withNoArguments().thenReturn(mockFirefoxDriver);

        config.threadStarted();
        config.threadStarted();

        assertThat(config.getCurrentThreadBrowser(), is(mockFirefoxDriver));
        verifyNew(FirefoxDriver.class, times(1)).withNoArguments();
    }

    @Test
    public void shouldQuitWebDriverWhenThreadFinishedIsInvoked() throws Exception {
        FirefoxDriver mockFirefoxDriver = Mockito.mock(FirefoxDriver.class);
        FirefoxDriverConfig.webdrivers.put(config.currentThreadName(), mockFirefoxDriver);

        config.threadFinished();

        assertThat(config.getCurrentThreadBrowser(), is(nullValue()));
        verify(mockFirefoxDriver, times(1)).quit();
    }

    @Test
    public void shouldBeAbleToCallThreadFinishedMultipleTimes() throws Exception {
        FirefoxDriver mockFirefoxDriver = Mockito.mock(FirefoxDriver.class);
        FirefoxDriverConfig.webdrivers.put(config.currentThreadName(), mockFirefoxDriver);

        config.threadFinished();
        config.threadFinished();

        assertThat(config.getCurrentThreadBrowser(), is(nullValue()));
        verify(mockFirefoxDriver, times(1)).quit();
    }

    @Test
    public void shouldCreateWebDriverPerThreadWhenThreadStartedIsInvoked() throws Exception {
        // mock the browsers that will be created per thread
        FirefoxDriver firstFirefox = Mockito.mock(FirefoxDriver.class);
        FirefoxDriver secondFirefox = Mockito.mock(FirefoxDriver.class);
        whenNew(FirefoxDriver.class).withNoArguments().thenReturn(firstFirefox, secondFirefox);

        // the list will store the returned browsers for each thread so they can be verified.
        final List<FirefoxDriver> firefoxesFromThread = new CopyOnWriteArrayList<FirefoxDriver>();
        Thread firstThread = new Thread() {
            public void run() {
                config.threadStarted();
                firefoxesFromThread.add(config.getCurrentThreadBrowser());
            }
        };
        Thread secondThread = new Thread() {
            public void run() {
                config.threadStarted();
                firefoxesFromThread.add(config.getCurrentThreadBrowser());
            }
        };

        // start and wait for threads to finish
        firstThread.start();
        secondThread.start();
        firstThread.join();
        secondThread.join();

        // assertions
        assertThat(firefoxesFromThread.size(), is(2));
        assertThat(firefoxesFromThread, hasItem(firstFirefox));
        assertThat(firefoxesFromThread, hasItem(secondFirefox));
        verifyNew(FirefoxDriver.class, times(2)).withNoArguments();
    }
}
