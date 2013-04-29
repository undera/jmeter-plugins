package com.googlecode.jmeter.plugins.webdriver.sampler;

/**
 * Properties are based on W3C Navigation Timing recommendation - http://www.w3.org/TR/navigation-timing/
 */
public class NavigationTimingSampleResult {
    private long navigationStart;
    private long unloadEventStart;
    private long unloadEventEnd;
    private long redirectStart;
    private long redirectEnd;
    private long fetchStart;
    private long domainLookupStart;
    private long domainLookupEnd;
    private long connectStart;
    private long connectEnd;
    private long secureConnectionStart;
    private long requestStart;
    private long responseStart;
    private long responseEnd;
    private long domLoading;
    private long domInteractive;
    private long domContentLoadedEventStart;
    private long domContentLoadedEventEnd;
    private long domComplete;
    private long loadEventStart;
    private long loadEventEnd;

    public void setNavigationStart(long navigationStart) {
        this.navigationStart = navigationStart;
    }

    public long getNavigationStart() {
        return navigationStart;
    }

    public void setUnloadEventStart(long unloadEventStart) {
        this.unloadEventStart = unloadEventStart;
    }

    public long getUnloadEventStart() {
        return unloadEventStart;
    }

    public void setUnloadEventEnd(long unloadEventEnd) {
        this.unloadEventEnd = unloadEventEnd;
    }

    public long getUnloadEventEnd() {
        return unloadEventEnd;
    }

    public void setRedirectStart(long redirectStart) {
        this.redirectStart = redirectStart;
    }

    public long getRedirectStart() {
        return redirectStart;
    }

    public void setRedirectEnd(long redirectEnd) {
        this.redirectEnd = redirectEnd;
    }

    public long getRedirectEnd() {
        return redirectEnd;
    }

    public void setFetchStart(long fetchStart) {
        this.fetchStart = fetchStart;
    }

    public long getFetchStart() {
        return fetchStart;
    }

    public void setDomainLookupStart(long domainLookupStart) {
        this.domainLookupStart = domainLookupStart;
    }

    public long getDomainLookupStart() {
        return domainLookupStart;
    }

    public void setDomainLookupEnd(long domainLookupEnd) {
        this.domainLookupEnd = domainLookupEnd;
    }

    public long getDomainLookupEnd() {
        return domainLookupEnd;
    }

    public void setConnectStart(long connectStart) {
        this.connectStart = connectStart;
    }

    public long getConnectStart() {
        return connectStart;
    }

    public void setConnectEnd(long connectEnd) {
        this.connectEnd = connectEnd;
    }

    public long getConnectEnd() {
        return connectEnd;
    }

    public void setSecureConnectionStart(long secureConnectionStart) {
        this.secureConnectionStart = secureConnectionStart;
    }

    public long getSecureConnectionStart() {
        return secureConnectionStart;
    }

    public void setRequestStart(long requestStart) {
        this.requestStart = requestStart;
    }

    public long getRequestStart() {
        return requestStart;
    }

    public void setResponseStart(long responseStart) {
        this.responseStart = responseStart;
    }

    public long getResponseStart() {
        return responseStart;
    }

    public void setResponseEnd(long responseEnd) {
        this.responseEnd = responseEnd;
    }

    public long getResponseEnd() {
        return responseEnd;
    }

    public void setDomLoading(long domLoading) {
        this.domLoading = domLoading;
    }

    public long getDomLoading() {
        return domLoading;
    }

    public void setDomInteractive(long domInteractive) {
        this.domInteractive = domInteractive;
    }

    public long getDomInteractive() {
        return domInteractive;
    }

    public void setDomContentLoadedEventStart(long domContentLoadedEventStart) {
        this.domContentLoadedEventStart = domContentLoadedEventStart;
    }

    public long getDomContentLoadedEventStart() {
        return domContentLoadedEventStart;
    }

    public void setDomContentLoadedEventEnd(long domContentLoadedEventEnd) {
        this.domContentLoadedEventEnd = domContentLoadedEventEnd;
    }

    public long getDomContentLoadedEventEnd() {
        return domContentLoadedEventEnd;
    }

    public void setDomComplete(long domComplete) {
        this.domComplete = domComplete;
    }

    public long getDomComplete() {
        return domComplete;
    }

    public void setLoadEventStart(long loadEventStart) {
        this.loadEventStart = loadEventStart;
    }

    public long getLoadEventStart() {
        return loadEventStart;
    }

    public void setLoadEventEnd(long loadEventEnd) {
        this.loadEventEnd = loadEventEnd;
    }

    public long getLoadEventEnd() {
        return loadEventEnd;
    }
}
