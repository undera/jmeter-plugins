package com.googlecode.jmeter.plugins.webdriver.config;

import com.googlecode.jmeter.plugins.webdriver.proxy.ProxyFactory;
import com.googlecode.jmeter.plugins.webdriver.proxy.ProxyHostPort;
import com.googlecode.jmeter.plugins.webdriver.proxy.ProxyType;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.*;

public class WebDriverConfigTest {

    private ProxyFactory proxyFactory;
    private WebDriverConfig config;
    private JMeterVariables variables;

    @Before
    public void createConfig() {
        proxyFactory = mock(ProxyFactory.class);
        config = new WebDriverConfigImpl(proxyFactory);
        variables = new JMeterVariables();
        JMeterContextService.getContext().setVariables(variables);
    }

    @After
    public void resetConfig() {
        config.clearThreadBrowsers();
        JMeterContextService.getContext().setVariables(null);
    }

    @Test
    public void shouldAssignProxyPacUrl() {
        String pacUrl = "http://proxy/pac.url";
        config.setProxyPacUrl(pacUrl);

        assertThat(config.getProxyPacUrl(), is(pacUrl));
    }

    @Test
    public void shouldAssignHttpHost() {
        String host = "host";
        config.setHttpHost(host);

        assertThat(config.getHttpHost(), is(host));
    }

    @Test
    public void shouldAssignHttpPort() {
        int port = 1;
        config.setHttpPort(port);

        assertThat(config.getHttpPort(), is(port));
    }

    @Test
    public void byDefaultShouldReturnTrueForUseHttpSettingsForAllProtocols() {
        assertThat(config.isUseHttpSettingsForAllProtocols(), is(true));
    }

    @Test
    public void shouldBeAbleToDisableUseHttpSettingsForAllProtocols() {
        config.setUseHttpSettingsForAllProtocols(false);
        assertThat(config.isUseHttpSettingsForAllProtocols(), is(false));
    }

    @Test
    public void shouldBeAbleToEnableUseHttpSettingsForAllProtocols() {
        config.setUseHttpSettingsForAllProtocols(true);
        assertThat(config.isUseHttpSettingsForAllProtocols(), is(true));
    }

    @Test
    public void shouldAssignHttpsHost() {
        String host = "host";
        config.setHttpsHost(host);

        assertThat(config.getHttpsHost(), is(host));
    }

    @Test
    public void shouldAssignHttpsPort() {
        int port = 1;
        config.setHttpsPort(port);

        assertThat(config.getHttpsPort(), is(port));
    }

    @Test
    public void shouldAssignFtpHost() {
        String host = "host";
        config.setFtpHost(host);

        assertThat(config.getFtpHost(), is(host));
    }

    @Test
    public void shouldAssignFtpPort() {
        int port = 1;
        config.setFtpPort(port);

        assertThat(config.getFtpPort(), is(port));
    }

    @Test
    public void shouldAssignSocksHost() {
        String host = "host";
        config.setSocksHost(host);

        assertThat(config.getSocksHost(), is(host));
    }

    @Test
    public void shouldAssignSocksPort() {
        int port = 1;
        config.setSocksPort(port);

        assertThat(config.getSocksPort(), is(port));
    }

    @Test
    public void shouldAssignNoProxyHost() {
        String list = "host1, host2";
        config.setNoProxyHost(list);

        assertThat(config.getNoProxyHost(), is(list));
    }

    @Test
    public void byDefaultShouldReturnSystemProxyType() {
        assertThat(config.getProxyType(), is(ProxyType.SYSTEM));
    }

    @Test
    public void shouldAssignProxyType() {
        ProxyType type = ProxyType.MANUAL;
        config.setProxyType(type);

        assertThat(config.getProxyType(), is(type));
    }

    @Test
    public void shouldDelegateToProxyFactoryWhenCreatingSystemProxy() {
        when(proxyFactory.getSystemProxy()).thenReturn(new Proxy());
        config.setProxyType(ProxyType.SYSTEM);

        Proxy proxy = config.createProxy();

        assertThat(proxy, is(notNullValue()));
        verify(proxyFactory, times(1)).getSystemProxy();
    }

    @Test
    public void shouldDelegateToProxyFactoryWhenCreatingDirectProxy() {
        when(proxyFactory.getDirectProxy()).thenReturn(new Proxy());
        config.setProxyType(ProxyType.DIRECT);

        Proxy proxy = config.createProxy();

        assertThat(proxy, is(notNullValue()));
        verify(proxyFactory, times(1)).getDirectProxy();
    }

    @Test
    public void shouldDelegateToProxyFactoryWhenCreatingAutoDetectProxy() {
        when(proxyFactory.getAutodetectProxy()).thenReturn(new Proxy());
        config.setProxyType(ProxyType.AUTO_DETECT);

        Proxy proxy = config.createProxy();

        assertThat(proxy, is(notNullValue()));
        verify(proxyFactory, times(1)).getAutodetectProxy();
    }

    @Test
    public void shouldDelegateToProxyFactoryWhenCreatingManualProxyWithAllValuesSpecified() {
        final ProxyHostPort http = new ProxyHostPort("http", 1);
        final ProxyHostPort https = new ProxyHostPort("https", 2);
        final ProxyHostPort ftp = new ProxyHostPort("ftp", 3);
        final ProxyHostPort socks = new ProxyHostPort("socks", 4);
        final String noProxy = "host1, host2";
        config.setHttpHost(http.getHost());
        config.setHttpPort(http.getPort());
        config.setUseHttpSettingsForAllProtocols(false);
        config.setHttpsHost(https.getHost());
        config.setHttpsPort(https.getPort());
        config.setFtpHost(ftp.getHost());
        config.setFtpPort(ftp.getPort());
        config.setSocksHost(socks.getHost());
        config.setSocksPort(socks.getPort());
        config.setNoProxyHost(noProxy);
        when(proxyFactory.getManualProxy(http, https, ftp, socks, noProxy)).thenReturn(new Proxy());
        config.setProxyType(ProxyType.MANUAL);

        Proxy proxy = config.createProxy();

        assertThat(proxy, is(notNullValue()));
        verify(proxyFactory, times(1)).getManualProxy(http, https, ftp, socks, noProxy);
    }

    @Test
    public void shouldDelegateToProxyFactoryWhenCreatingManualProxyWithHttpAsDefault() {
        final ProxyHostPort http = new ProxyHostPort("http", 1);
        final String noProxy = "host1, host2";
        config.setHttpHost(http.getHost());
        config.setHttpPort(http.getPort());
        config.setUseHttpSettingsForAllProtocols(true);
        config.setNoProxyHost(noProxy);
        when(proxyFactory.getManualProxy(http, http, http, http, noProxy)).thenReturn(new Proxy());
        config.setProxyType(ProxyType.MANUAL);

        Proxy proxy = config.createProxy();

        assertThat(proxy, is(notNullValue()));
        verify(proxyFactory, times(1)).getManualProxy(http, http, http, http, noProxy);
    }

    @Test
    public void shouldDelegateToProxyFactoryWhenCreatingProxyPacProxy() throws MalformedURLException {
        String url = "http://proxy/pac.url";
        when(proxyFactory.getConfigUrlProxy(url)).thenReturn(new Proxy());
        config.setProxyPacUrl(url);
        config.setProxyType(ProxyType.PROXY_PAC);

        Proxy proxy = config.createProxy();

        assertThat(proxy, is(notNullValue()));
        verify(proxyFactory, times(1)).getConfigUrlProxy(isA(String.class));
    }

    @Test
    public void shouldSetBrowserForCurrentThread() {
        WebDriver browser = mock(WebDriver.class);

        config.setThreadBrowser(browser);

        assertThat((Collection<WebDriver>)config.getThreadBrowsers().values(), hasSize(1));
        assertThat((Collection<WebDriver>)config.getThreadBrowsers().values(), hasItem(browser));
    }

    @Test
    public void shouldNotSetNullBrowserForCurrentThread() {
        WebDriver browser = mock(WebDriver.class);

        config.setThreadBrowser(browser);
        assertThat((Collection<WebDriver>)config.getThreadBrowsers().values(), hasSize(1));
        assertThat((Collection<WebDriver>)config.getThreadBrowsers().values(), hasItem(browser));

        config.setThreadBrowser(null);
        assertThat((Collection<WebDriver>)config.getThreadBrowsers().values(), hasSize(1));
        assertThat((Collection<WebDriver>)config.getThreadBrowsers().values(), hasItem(browser));
    }

    @Test
    public void shouldOnlyHaveOneBrowserForCurrentThread() {
        WebDriver firstBrowser = mock(WebDriver.class);
        WebDriver secondBrowser = mock(WebDriver.class);

        config.setThreadBrowser(firstBrowser);
        config.setThreadBrowser(secondBrowser);

        assertThat((Collection<WebDriver>)config.getThreadBrowsers().values(), hasSize(1));
        assertThat((Collection<WebDriver>)config.getThreadBrowsers().values(), hasItem(secondBrowser));
    }

    @Test
    public void shouldBeAbleToAddMultipleBrowsersForEachThread() throws InterruptedException {
        // mock the browsers that will be created per thread
        final WebDriver firstBrowser = mock(WebDriver.class);
        final WebDriver secondBrowser = mock(WebDriver.class);

        Thread firstThread = new Thread() {
            public void run() {
                config.setThreadBrowser(firstBrowser);
            }
        };
        Thread secondThread = new Thread() {
            public void run() {
                config.setThreadBrowser(secondBrowser);
            }
        };

        // start and wait for threads to finish
        firstThread.start();
        secondThread.start();
        firstThread.join();
        secondThread.join();

        // assertions
        assertThat((Collection<WebDriver>)config.getThreadBrowsers().values(), hasSize(2));
        assertThat((Collection<WebDriver>)config.getThreadBrowsers().values(), hasItem(firstBrowser));
        assertThat((Collection<WebDriver>)config.getThreadBrowsers().values(), hasItem(secondBrowser));
    }

    @Test
    public void shouldHaveBrowserForCurrentThread() {
        WebDriver browser = mock(WebDriver.class);

        config.setThreadBrowser(browser);

        assertThat(config.hasThreadBrowser(), is(true));
    }

    @Test
    public void shouldNotHaveBrowserForCurrentThread() {
        assertThat(config.hasThreadBrowser(), is(false));
    }

    @Test
    public void shouldRemoveBrowserFromCurrentThread() {
        WebDriver browser = mock(WebDriver.class);
        config.setThreadBrowser(browser);

        final WebDriver removed = config.removeThreadBrowser();

        assertThat((Map<String, WebDriver>)config.getThreadBrowsers(), is(Collections.<String, WebDriver>emptyMap()));
        assertThat(browser, is(removed));
    }

    @Test
    public void shouldRemoveBrowserFromCurrentThreadEvenIfNoBrowserPresent() {
        final WebDriver removed = config.removeThreadBrowser();

        assertThat((Map<String, WebDriver>)config.getThreadBrowsers(), is(Collections.<String, WebDriver>emptyMap()));
        assertThat(removed, is(nullValue()));
    }

    @Test
    public void shouldImplementLoopIterationListener() {
        assertThat(config, is(instanceOf(LoopIterationListener.class)));
    }

    @Test
    public void shouldAddBrowserToJMeterVariablesWhenIterationStarts() throws Exception {
        WebDriver browser = mock(WebDriver.class);
        config.setThreadBrowser(browser);

        config.iterationStart(null);

        assertThat(variables.getObject(WebDriverConfig.BROWSER), is(notNullValue()));
        assertThat((WebDriver) variables.getObject(WebDriverConfig.BROWSER), is(browser));
    }

    @Test
    public void shouldAssignRecreateBrowserOnIterationStart() {
        config.setRecreateBrowserOnIterationStart(true);

        assertThat(config.isRecreateBrowserOnIterationStart(), is(true));
    }

    @Test
    public void byDefaultShouldNotRecreateBrowserOnIterationStart() {
        assertThat(config.isRecreateBrowserOnIterationStart(), is(false));
    }

    @Test
    public void shouldRecreateBrowserOnEachIterationStart() {
        final WebDriver firstBrowser = mock(WebDriver.class);
        final WebDriver secondBrowser = mock(WebDriver.class);
        this.config = new WebDriverConfigImpl(proxyFactory, firstBrowser, secondBrowser);
        this.config.setRecreateBrowserOnIterationStart(true);

        config.iterationStart(null);
        assertThat((WebDriver) variables.getObject(WebDriverConfig.BROWSER), is(firstBrowser));
        config.iterationStart(null);
        assertThat((WebDriver) variables.getObject(WebDriverConfig.BROWSER), is(secondBrowser));

        verify(firstBrowser, times(1)).quit();
    }

    private static class WebDriverConfigImpl extends WebDriverConfig {

        final List<WebDriver> browsers = new CopyOnWriteArrayList<WebDriver>();

        /**
         *
         * @param proxyFactory mock ProxyFactory to use
         * @param browsers the list of browsers (in order) to return when createBrowser() method is invoked.
         */
        public WebDriverConfigImpl(ProxyFactory proxyFactory, WebDriver... browsers) {
            super(proxyFactory);
            this.browsers.addAll(Arrays.asList(browsers));
        }

        @Override
        protected WebDriver createBrowser() {
            return (browsers.isEmpty() ? null : browsers.remove(0));
        }
    };
}
