package com.googlecode.jmeter.plugins.webdriver.sampler;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NavigationTimingSampleResultTest {
    private NavigationTimingSampleResult result;

    @Before
    public void createSampleResult() {
        result = new NavigationTimingSampleResult();
    }

    @Test
    public void shouldHavePropertyNavigationStart() {
        final long timing = System.currentTimeMillis();
        result.setNavigationStart(timing);
        assertThat(result.getNavigationStart(), is(timing));
    }

    @Test
    public void shouldHavePropertyUnloadEventStart() {
        final long timing = System.currentTimeMillis();
        result.setUnloadEventStart(timing);
        assertThat(result.getUnloadEventStart(), is(timing));
    }

    @Test
    public void shouldHavePropertyUnloadEventEnd() {
        final long timing = System.currentTimeMillis();
        result.setUnloadEventEnd(timing);
        assertThat(result.getUnloadEventEnd(), is(timing));
    }

    @Test
    public void shouldHavePropertyRedirectStart() {
        final long timing = System.currentTimeMillis();
        result.setRedirectStart(timing);
        assertThat(result.getRedirectStart(), is(timing));
    }

    @Test
    public void shouldHavePropertyRedirectEnd() {
        final long timing = System.currentTimeMillis();
        result.setRedirectEnd(timing);
        assertThat(result.getRedirectEnd(), is(timing));
    }

    @Test
    public void shouldHavePropertyFetchStart() {
        final long timing = System.currentTimeMillis();
        result.setFetchStart(timing);
        assertThat(result.getFetchStart(), is(timing));
    }

    @Test
    public void shouldHavePropertyDomainLookupStart() {
        final long timing = System.currentTimeMillis();
        result.setDomainLookupStart(timing);
        assertThat(result.getDomainLookupStart(), is(timing));
    }

    @Test
    public void shouldHavePropertyDomainLookupEnd() {
        final long timing = System.currentTimeMillis();
        result.setDomainLookupEnd(timing);
        assertThat(result.getDomainLookupEnd(), is(timing));
    }

    @Test
    public void shouldHavePropertyConnectStart() {
        final long timing = System.currentTimeMillis();
        result.setConnectStart(timing);
        assertThat(result.getConnectStart(), is(timing));
    }

    @Test
    public void shouldHavePropertyConnectEnd() {
        final long timing = System.currentTimeMillis();
        result.setConnectEnd(timing);
        assertThat(result.getConnectEnd(), is(timing));
    }

    @Test
    public void shouldHavePropertySecureConnectionStart() {
        final long timing = System.currentTimeMillis();
        result.setSecureConnectionStart(timing);
        assertThat(result.getSecureConnectionStart(), is(timing));
    }

    @Test
    public void shouldHavePropertyRequestStart() {
        final long timing = System.currentTimeMillis();
        result.setRequestStart(timing);
        assertThat(result.getRequestStart(), is(timing));
    }

    @Test
    public void shouldHavePropertyResponseStart() {
        final long timing = System.currentTimeMillis();
        result.setResponseStart(timing);
        assertThat(result.getResponseStart(), is(timing));
    }

    @Test
    public void shouldHavePropertyResponseEnd() {
        final long timing = System.currentTimeMillis();
        result.setResponseEnd(timing);
        assertThat(result.getResponseEnd(), is(timing));
    }

    @Test
    public void shouldHavePropertyDomLoading() {
        final long timing = System.currentTimeMillis();
        result.setDomLoading(timing);
        assertThat(result.getDomLoading(), is(timing));
    }

    @Test
    public void shouldHavePropertyDomInteractive() {
        final long timing = System.currentTimeMillis();
        result.setDomInteractive(timing);
        assertThat(result.getDomInteractive(), is(timing));
    }

    @Test
    public void shouldHavePropertyDomContentLoadedEventStart() {
        final long timing = System.currentTimeMillis();
        result.setDomContentLoadedEventStart(timing);
        assertThat(result.getDomContentLoadedEventStart(), is(timing));
    }

    @Test
    public void shouldHavePropertyDomContentLoadedEventEnd() {
        final long timing = System.currentTimeMillis();
        result.setDomContentLoadedEventEnd(timing);
        assertThat(result.getDomContentLoadedEventEnd(), is(timing));
    }

    @Test
    public void shouldHavePropertyDomComplete() {
        final long timing = System.currentTimeMillis();
        result.setDomComplete(timing);
        assertThat(result.getDomComplete(), is(timing));
    }

    @Test
    public void shouldHavePropertyLoadEventStart() {
        final long timing = System.currentTimeMillis();
        result.setLoadEventStart(timing);
        assertThat(result.getLoadEventStart(), is(timing));
    }

    @Test
    public void shouldHavePropertyLoadEventEnd() {
        final long timing = System.currentTimeMillis();
        result.setLoadEventEnd(timing);
        assertThat(result.getLoadEventEnd(), is(timing));
    }
}
