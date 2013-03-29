package com.googlecode.jmeter.plugins.webdriver.config;

import com.googlecode.jmeter.plugins.webdriver.proxy.ProxyFactory;
import com.googlecode.jmeter.plugins.webdriver.proxy.ProxyHostPort;
import com.googlecode.jmeter.plugins.webdriver.proxy.ProxyType;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Proxy;

import java.net.MalformedURLException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class WebDriverConfigTest {

    private WebDriverConfig config;

    private ProxyFactory proxyFactory;

    @Before
    public void createConfig() {
        proxyFactory = mock(ProxyFactory.class);
        config = new WebDriverConfigImpl(proxyFactory);
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

    private static class WebDriverConfigImpl extends WebDriverConfig {
        public WebDriverConfigImpl(ProxyFactory proxyFactory) {
            super(proxyFactory);
        }
    };
}
