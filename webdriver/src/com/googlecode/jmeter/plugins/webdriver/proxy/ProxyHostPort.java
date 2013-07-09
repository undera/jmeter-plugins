package com.googlecode.jmeter.plugins.webdriver.proxy;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public final class ProxyHostPort {
    final int port;
    final String host;

    public ProxyHostPort(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public String toUnifiedForm() {
        return host+":"+port;
    }

    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
