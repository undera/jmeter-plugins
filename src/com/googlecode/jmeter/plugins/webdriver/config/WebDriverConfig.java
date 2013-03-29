package com.googlecode.jmeter.plugins.webdriver.config;

import com.googlecode.jmeter.plugins.webdriver.proxy.ProxyFactory;
import com.googlecode.jmeter.plugins.webdriver.proxy.ProxyHostPort;
import com.googlecode.jmeter.plugins.webdriver.proxy.ProxyType;
import org.apache.jmeter.config.ConfigTestElement;
import org.openqa.selenium.Proxy;

public abstract class WebDriverConfig extends ConfigTestElement {
    private static final String PROXY_PAC_URL = "proxy_pac_url";
    private static final String HTTP_HOST = "http_host";
    private static final String HTTP_PORT = "http_port";
    private static final String USE_HTTP_FOR_ALL_PROTOCOLS = "use_http_for_all_protocols";
    private static final String HTTPS_HOST = "https_host";
    private static final String HTTPS_PORT = "https_port";
    private static final String FTP_HOST = "ftp_host";
    private static final String FTP_PORT = "ftp_port";
    private static final String SOCKS_HOST = "socks_host";
    private static final String SOCKS_PORT = "socks_port";
    private static final String NO_PROXY = "no_proxy";
    private static final String PROXY_TYPE = "proxy_type";

    private final ProxyFactory proxyFactory;

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

    public Proxy createProxy() {
        switch(getProxyType()) {
            case PROXY_PAC:
                return proxyFactory.getConfigUrlProxy(getProxyPacUrl());
            case DIRECT:
                return proxyFactory.getDirectProxy();
            case AUTO_DETECT:
                return proxyFactory.getAutodetectProxy();
            case MANUAL:
                ProxyHostPort http = new ProxyHostPort(getHttpHost(), getHttpPort());
                if(isUseHttpSettingsForAllProtocols()) {
                    return proxyFactory.getManualProxy(http, http, http, http, getNoProxyHost());
                }
                ProxyHostPort https = new ProxyHostPort(getHttpsHost(), getHttpsPort());
                ProxyHostPort ftp = new ProxyHostPort(getFtpHost(), getFtpPort());
                ProxyHostPort socks = new ProxyHostPort(getSocksHost(), getSocksPort());
                return proxyFactory.getManualProxy(http, https, ftp, socks, getNoProxyHost());
            default:
                return proxyFactory.getSystemProxy();
        }
    }
}
