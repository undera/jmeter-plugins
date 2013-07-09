package com.googlecode.jmeter.plugins.webdriver.proxy;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Proxy;

import java.net.MalformedURLException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ProxyFactoryTest {
    private ProxyFactory factory;

    @Before
    public void initFactory() {
        factory = ProxyFactory.getInstance();
    }

    @Test
    public void shouldCreateAnAutoDetectProxy() {
        Proxy proxy = factory.getAutodetectProxy();

        assertThat(proxy.getProxyType(), is(Proxy.ProxyType.AUTODETECT));
        assertThat(proxy.isAutodetect(), is(true));
    }

    @Test
    public void shouldCreateDirectProxy() {
        Proxy proxy = factory.getDirectProxy();

        assertThat(proxy.getProxyType(), is(Proxy.ProxyType.DIRECT));
    }

    @Test
    public void shouldCreateConfigUrlProxy() throws MalformedURLException {
        String pacUrl = "http://example.com/proxy.pac";
        Proxy proxy = factory.getConfigUrlProxy(pacUrl);

        assertThat(proxy.getProxyType(), is(Proxy.ProxyType.PAC));
        assertThat(proxy.getProxyAutoconfigUrl(), is(pacUrl));
    }

    @Test
    public void shouldCreateManualProxy() {
        ProxyHostPort http = new ProxyHostPort("http.com", 1234);
        ProxyHostPort https = new ProxyHostPort("https.com", 1234);
        ProxyHostPort ftp = new ProxyHostPort("ftp", 1234);
        ProxyHostPort socks = new ProxyHostPort("socks", 1234);
        String noProxy = "none";
        Proxy proxy = factory.getManualProxy(http, https, ftp, socks, noProxy);

        assertThat(proxy.getProxyType(), is(Proxy.ProxyType.MANUAL));
        assertThat(proxy.getHttpProxy(), is(http.toUnifiedForm()));
        assertThat(proxy.getSslProxy(), is(https.toUnifiedForm()));
        assertThat(proxy.getFtpProxy(), is(ftp.toUnifiedForm()));
        assertThat(proxy.getSocksProxy(), is(socks.toUnifiedForm()));
        assertThat(proxy.getNoProxy(), is(noProxy));
    }

    @Test
    public void shouldCreateSystemProxy() {
        Proxy proxy = factory.getSystemProxy();

        assertThat(proxy.getProxyType(), is(Proxy.ProxyType.SYSTEM));
    }
}
