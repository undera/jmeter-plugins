package com.googlecode.jmeter.plugins.webdriver.config;

import com.googlecode.jmeter.plugins.webdriver.proxy.ProxyFactory;
import com.googlecode.jmeter.plugins.webdriver.proxy.ProxyHostPort;
import com.googlecode.jmeter.plugins.webdriver.proxy.ProxyType;
import org.apache.jmeter.config.ConfigTestElement;
import org.openqa.selenium.Proxy;

public class WebDriverConfig extends ConfigTestElement {
    /**
     * This is the key used to store a WebDriver instance in the {@link org.apache.jmeter.threads.JMeterVariables} object.
     */
    public static final String BROWSER = "Browser";

    private static final String PROXY_PAC_URL = "WebDriverSampler.proxy_pac_url";
    private static final String HTTP_HOST = "WebDriverSampler.http_host";
    private static final String HTTP_PORT = "WebDriverSampler.http_port";
    private static final String USE_HTTP_FOR_ALL_PROTOCOLS = "WebDriverSampler.use_http_for_all_protocols";
    private static final String HTTPS_HOST = "WebDriverSampler.https_host";
    private static final String HTTPS_PORT = "WebDriverSampler.https_port";
    private static final String FTP_HOST = "WebDriverSampler.ftp_host";
    private static final String FTP_PORT = "WebDriverSampler.ftp_port";
    private static final String SOCKS_HOST = "WebDriverSampler.socks_host";
    private static final String SOCKS_PORT = "WebDriverSampler.socks_port";
    private static final String NO_PROXY = "WebDriverSampler.no_proxy";
    private static final String PROXY_TYPE = "WebDriverSampler.proxy_type";

    private final ProxyFactory proxyFactory;

    public WebDriverConfig() {
        this(ProxyFactory.getInstance());
    }

    protected WebDriverConfig(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    public void setProxyPacUrl(String pacUrl) {
        setProperty(PROXY_PAC_URL, pacUrl);
    }

    public String getProxyPacUrl() {
        return getPropertyAsString(PROXY_PAC_URL);
    }

    public void setHttpHost(String host) {
        setProperty(HTTP_HOST, host);
    }

    public String getHttpHost() {
        return getPropertyAsString(HTTP_HOST);
    }

    public void setHttpPort(int port) {
        setProperty(HTTP_PORT, port);
    }

    public int getHttpPort() {
        return getPropertyAsInt(HTTP_PORT);
    }

    public void setUseHttpSettingsForAllProtocols(boolean override) {
        setProperty(USE_HTTP_FOR_ALL_PROTOCOLS, override);
    }

    public boolean isUseHttpSettingsForAllProtocols() {
        return getPropertyAsBoolean(USE_HTTP_FOR_ALL_PROTOCOLS, true);
    }

    public void setHttpsHost(String httpsHost) {
        setProperty(HTTPS_HOST, httpsHost);
    }

    public String getHttpsHost() {
        return getPropertyAsString(HTTPS_HOST);
    }

    public void setHttpsPort(int port) {
        setProperty(HTTPS_PORT, port);
    }

    public int getHttpsPort() {
        return getPropertyAsInt(HTTPS_PORT);
    }

    public void setFtpHost(String host) {
        setProperty(FTP_HOST, host);
    }

    public String getFtpHost() {
        return getPropertyAsString(FTP_HOST);
    }

    public void setFtpPort(int port) {
        setProperty(FTP_PORT, port);
    }

    public int getFtpPort() {
        return getPropertyAsInt(FTP_PORT);
    }

    public void setSocksHost(String host) {
        setProperty(SOCKS_HOST, host);
    }

    public String getSocksHost() {
        return getPropertyAsString(SOCKS_HOST);
    }

    public void setSocksPort(int port) {
        setProperty(SOCKS_PORT, port);
    }

    public int getSocksPort() {
        return getPropertyAsInt(SOCKS_PORT);
    }

    public void setNoProxyHost(String noProxyHostList) {
        setProperty(NO_PROXY, noProxyHostList);
    }

    public String getNoProxyHost() {
        return getPropertyAsString(NO_PROXY);
    }

    public void setProxyType(ProxyType type) {
        setProperty(PROXY_TYPE, type.name());
    }

    public ProxyType getProxyType() {
        return ProxyType.valueOf(getPropertyAsString(PROXY_TYPE, ProxyType.SYSTEM.name()));
    }

    /**
     * Call this method to create a {@link Proxy} instance for use when creating a {@link org.openqa.selenium.WebDriver}
     * instance.  The values/settings of the proxy depends entirely on the values set on this config instance.
     *
     * @return a {@link Proxy}
     */
    public Proxy createProxy() {
        switch(getProxyType()) {
            case PROXY_PAC:
                return proxyFactory.getConfigUrlProxy(getProxyPacUrl());
            case DIRECT:
                return proxyFactory.getDirectProxy();
            case AUTO_DETECT:
                return proxyFactory.getAutodetectProxy();
            case MANUAL:
                if(isUseHttpSettingsForAllProtocols()) {
                    ProxyHostPort proxy = new ProxyHostPort(getHttpHost(), getHttpPort());
                    return proxyFactory.getManualProxy(proxy, proxy, proxy, proxy, getNoProxyHost());
                }
                ProxyHostPort http = new ProxyHostPort(getHttpHost(), getHttpPort());
                ProxyHostPort https = new ProxyHostPort(getHttpsHost(), getHttpsPort());
                ProxyHostPort ftp = new ProxyHostPort(getFtpHost(), getFtpPort());
                ProxyHostPort socks = new ProxyHostPort(getSocksHost(), getSocksPort());
                return proxyFactory.getManualProxy(http, https, ftp, socks, getNoProxyHost());
            default:
                return proxyFactory.getSystemProxy();
        }
    }
}
