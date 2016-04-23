package com.googlecode.jmeter.plugins.webdriver.proxy;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProxyHostPortTest {

    @Test
    public void shouldGenerateUnifiedForm() {
        final ProxyHostPort hostPort = new ProxyHostPort("host", 1);
        assertThat(hostPort.toUnifiedForm(), is("host:1"));
    }

    @Test
    public void shouldBaseEqualityOnValues() {
        ProxyHostPort first = new ProxyHostPort("host", 1);
        ProxyHostPort second = new ProxyHostPort("host", 1);
        assertThat(first, is(equalTo(second)));
    }

    @Test
    public void shouldBaseHashCodeOnValues() {
        ProxyHostPort first = new ProxyHostPort("host", 1);
        ProxyHostPort second = new ProxyHostPort("host", 1);
        assertThat(first.hashCode(), is(equalTo(second.hashCode())));
    }

}
