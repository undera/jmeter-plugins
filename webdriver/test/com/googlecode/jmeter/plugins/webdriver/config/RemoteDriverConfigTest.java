package com.googlecode.jmeter.plugins.webdriver.config;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RemoteDriverConfig.class)
public class RemoteDriverConfigTest {

    private RemoteDriverConfig config;
    private JMeterVariables variables;

    @Before
    public void createConfig() {
        config = new RemoteDriverConfig();
        variables = new JMeterVariables();
        config.setCapability(RemoteCapability.CHROME);
        JMeterContextService.getContext().setVariables(variables);
    }

    @After
    public void resetConfig() {
        config.clearThreadBrowsers();
        JMeterContextService.getContext().setVariables(null);
    }
    
    @Test
	public void shouldSetTheCapability() throws Exception {
		assertThat(config.getCapability(), is(RemoteCapability.CHROME));
		config.setCapability(RemoteCapability.FIREFOX);
		assertThat(config.getCapability(), is(RemoteCapability.FIREFOX));
		config.setCapability(RemoteCapability.INTERNET_EXPLORER);
		assertThat(config.getCapability(), is(RemoteCapability.INTERNET_EXPLORER));
		config.setCapability(RemoteCapability.PHANTOMJS);
		assertThat(config.getCapability(), is(RemoteCapability.PHANTOMJS));
	}

    @Test
    public void shouldBeAbleToSerialiseAndDeserialise() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(bytes);

        output.writeObject(config);
        output.flush();
        output.close();

        ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()));
        final RemoteDriverConfig deserializedConfig = (RemoteDriverConfig) input.readObject();

        assertThat(deserializedConfig, is(config));
    }

    @Test
    public void shouldCreateRemoteDriver() throws Exception {
    	config.setSeleniumGridUrl("http://my.awesomegrid.com");
        RemoteWebDriver mockRemoteWebDriver = Mockito.mock(RemoteWebDriver.class);
        whenNew(RemoteWebDriver.class)
            .withParameterTypes(URL.class, Capabilities.class)
            .withArguments(isA(URL.class), isA(Capabilities.class))
            .thenReturn(mockRemoteWebDriver);

        final RemoteWebDriver browser = config.createBrowser();

        assertThat(browser, is(mockRemoteWebDriver));
        verifyNew(RemoteWebDriver.class, times(1)).withArguments(isA(URL.class), isA(Capabilities.class));
    }

    @Test
    public void shouldHaveProxyInCapability() {
        final Capabilities capabilities = config.createCapabilities();
        assertThat(capabilities.getCapability(CapabilityType.PROXY), is(notNullValue()));
        assertThat(capabilities.getCapability(ChromeOptions.CAPABILITY), is(notNullValue()));
        assertThat(capabilities.isJavascriptEnabled(), is(true));
    }
    
    @Test
	public void should() throws Exception {
		
	}
    
    @Test
	public void shouldThrowAnExceptionWhenTheURLIsMalformed() throws Exception {
    	try{
    		config.setSeleniumGridUrl("BadURL");
    		config.createBrowser();
    		fail();
    	} catch (Exception unit){
    		assertThat(unit, instanceOf(RuntimeException.class));
    		assertThat(unit.getMessage(), is("java.net.MalformedURLException: no protocol: BadURL"));
    	}
	}
}
