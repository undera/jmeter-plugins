package com.googlecode.jmeter.plugins.webdriver.config;

import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.openqa.selenium.android.AndroidDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * @author Sergey Marakhov
 * @author Linh Pham
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AndroidDriverConfig.class)
public class AndroidDriverConfigTest {

    private AndroidDriverConfig config;
    private JMeterVariables variables;

    @Before
    public void createConfig() {
        config = new AndroidDriverConfig();
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
        final AndroidDriverConfig deserializedConfig = (AndroidDriverConfig) input.readObject();

        assertThat(deserializedConfig, is(config));
    }

    @Test
    public void shouldCreateAndroidDriver() throws Exception {
        AndroidDriver mockAndroidDriver = Mockito.mock(AndroidDriver.class);
        whenNew(AndroidDriver.class).withParameterTypes(URL.class, DesiredCapabilities.class).withArguments(isA(URL.class), isA(DesiredCapabilities.class)).thenReturn(mockAndroidDriver);

        final AndroidDriver browser = config.createBrowser();

        assertThat(browser, is(mockAndroidDriver));
        verifyNew(AndroidDriver.class, times(1)).withArguments(isA(URL.class), isA(DesiredCapabilities.class));
    }

    @Test
    public void shouldHandleInvalidUrl() throws Exception {
        whenNew(AndroidDriver.class).withParameterTypes(URL.class, DesiredCapabilities.class).withArguments(isA(URL.class), isA(DesiredCapabilities.class)).thenThrow(new MalformedURLException("testing123"));

        final AndroidDriver browser = config.createBrowser();
        assertNull(browser);
    }

    @Test
    public void shouldHaveProxyInCapability() {
        final DesiredCapabilities capabilities = config.createCapabilities();
        assertThat(capabilities.getCapability(CapabilityType.PROXY), is(notNullValue()));
    }
}
