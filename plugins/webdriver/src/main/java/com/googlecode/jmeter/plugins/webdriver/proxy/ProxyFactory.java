package com.googlecode.jmeter.plugins.webdriver.proxy;

import org.openqa.selenium.Proxy;

/**
 * A utility class to help create selenium {@link Proxy} instances.
 */
public class ProxyFactory {
    private static final ProxyFactory INSTANCE = new ProxyFactory();

    public static ProxyFactory getInstance() {
        return INSTANCE;
    }

    private ProxyFactory() {}

    /**
     * This returns a {@link Proxy} with HTTP, HTTPS and FTP hosts and ports configured as specified.
     *
     *
     * @param httpProxy is the http proxy host and port
     * @param httpsProxy is the https proxy host and port
     * @param ftpProxy is the ftp proxy host and port
     * @param socksProxy is the socks proxy host and port
     * @param noProxy is a comma separated list of hosts that will bypass the proxy
     *
     * @return a proxy object with the hosts manually specified.
     */
    public Proxy getManualProxy(ProxyHostPort httpProxy, ProxyHostPort httpsProxy, ProxyHostPort ftpProxy, ProxyHostPort socksProxy, String noProxy) {
        return new Proxy()
            .setProxyType(Proxy.ProxyType.MANUAL)
            .setHttpProxy(httpProxy.toUnifiedForm())
            .setSslProxy(httpsProxy.toUnifiedForm())
            .setFtpProxy(ftpProxy.toUnifiedForm())
            .setSocksProxy(socksProxy.toUnifiedForm())
            .setNoProxy(noProxy);
    }

    /**
     * This will not use a proxy and expects a direct connection to the internet.
     *
     * @return a proxy object that does not use proxies.
     */
    public Proxy getDirectProxy() {
        return new Proxy()
            .setProxyType(Proxy.ProxyType.DIRECT);
    }

    /**
     * This is a proxy which will have its settings automatically configured.
     *
     * @return a proxy object which will try to automatically detect the proxy settings.
     */
    public Proxy getAutodetectProxy() {
        return new Proxy()
            .setProxyType(Proxy.ProxyType.AUTODETECT)
            .setAutodetect(true);
    }

    /**
     * If the proxy can be configured using a PAC file at a URL, set this value to the location of this PAC file.
     *
     * @param pacUrl is the url to the Proxy PAC file
     *
     * @return a proxy object with its proxies configured automatically using a PAC file.
     */
    public Proxy getConfigUrlProxy(String pacUrl) {
        return new Proxy()
            .setProxyType(Proxy.ProxyType.PAC)
            .setProxyAutoconfigUrl(pacUrl);
    }

    /**
     * This will sttempt to use the system's proxy settings.
     *
     * @return a proxy object with the system's default proxy configured in it.
     */
    public Proxy getSystemProxy() {
        return new Proxy()
            .setProxyType(Proxy.ProxyType.SYSTEM);
    }
}
