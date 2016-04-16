/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */
package com.atlantbh.jmeter.plugins.oauth;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.*;
import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler2;
import org.apache.jmeter.protocol.http.util.HTTPConstantsInterface;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

public class OAuthSampler extends HTTPSampler2 {

    private static final long serialVersionUID = -5877623539165274730L;
    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String CONSUMER_KEY = "OAuthSampler.consumer_key";
    public static final String CONSUMER_SECRET = "OAuthSampler.consumer_secret";
    public static final String REQUEST_BODY = "OAuthSampler.request_body";
    public static final String PORT_NUMBER = "OAuthSampler.port_number";
    public static final String RESOURCE = "OAuthSampler.resource";
    public static final String BASE_HOST = "OAuthSampler.base_host";
    public static final String REQUEST_HEADERS = "OAuthSampler.request_headers";

    public OAuthSampler() {
    }

    public void setConsumerKey(String consumerKey) {
        setProperty(CONSUMER_KEY, consumerKey);
    }

    public void setConsumerSecret(String consumerSecret) {
        setProperty(CONSUMER_SECRET, consumerSecret);
    }

    public String getConsumerKey() {
        return getPropertyAsString(CONSUMER_KEY);
    }

    public String getConsumerSecret() {
        return getPropertyAsString(CONSUMER_SECRET);
    }

    public void setRequestBody(String data) {
        setProperty(REQUEST_BODY, data);
    }

    public void setRequestHeaders(String headers) {
        setProperty(REQUEST_HEADERS, headers);
    }

    public String getRequestBody() {
        return getPropertyAsString(REQUEST_BODY);
    }

    public String getRequestHeaders() {
        return getPropertyAsString(REQUEST_HEADERS);
    }

    public void setResource(String data) {
        setProperty(RESOURCE, data);
    }

    public String getResource() {
        return getPropertyAsString(RESOURCE);
    }

    public void setPortNumber(String data) {
        setProperty(PORT_NUMBER, data);

    }

    public String getPortNumber() {
        return getPropertyAsString(PORT_NUMBER);
    }

    public void setHostBaseUrl(final String data) {
        setProperty(BASE_HOST, data);
    }

    public String getHostBaseUrl() {
        return getPropertyAsString(BASE_HOST);
    }

    public URL getUrl() throws MalformedURLException {
        String validHost = toValidUrl(getHostBaseUrl());
        URL u = null;
        if (validHost != null && getResource() != null) {
            String fullUrl = validHost
                    + (getPortNumber() == null || getPortNumber().length() == 0 ? ""
                    : ":" + getPortNumber()) + "/" + getResource();
            u = toURL(fullUrl);
        }

        return u;
    }

    private String toValidUrl(String u) throws MalformedURLException {
        URL url = new URL(u);
        String urlStr = url.toString();
        if (urlStr.endsWith("/")) {
            url = toURL(urlStr.substring(0, urlStr.length() - 1));
            urlStr = url.toString();
        }
        return urlStr;
    }

    private URL toURL(String u) {
        try {
            return new URL(u);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private void overrideHeaders(HttpMethodBase httpMethod, String url,
                                 String method) {
        String headers = getRequestHeaders();
        String[] header = headers.split(System.getProperty("line.separator"));
        for (String kvp : header) {
            int pos = kvp.indexOf(':');
            if (pos < 0) {
                pos = kvp.indexOf('=');
            }
            if (pos > 0) {
                String k = kvp.substring(0, pos).trim();
                String v = "";
                if (kvp.length() > pos + 1) {
                    v = kvp.substring(pos + 1).trim();
                }
                httpMethod.addRequestHeader(k, v);
            }
        }
        String authorization = OAuthGenerator.getInstance(getConsumerKey(),
                getConsumerSecret()).getAuthorization(url, method);
        httpMethod.addRequestHeader("Authorization", authorization);
    }

    protected HttpClient setupConnection(URL u, HttpMethodBase httpMethod)
            throws IOException {
        HTTPSampleResult temp = new HTTPSampleResult();
        return super.setupConnection(u, httpMethod, temp);
    }

    protected HTTPSampleResult sample(URL url, String method,
                                      boolean areFollowingRedirect, int frameDepth) {
        throw new UnsupportedOperationException("Should never be called");
    }

    @Override
    public SampleResult sample() {
        HttpMethodBase httpMethod = null;
        HttpClient client = null;
        InputStream instream = null;
        SampleResult res = new SampleResult();
        try {
            res.setSuccessful(false);
            res.setResponseCode("000");
            res.setSampleLabel(getName());
            res.setURL(getUrl());
            res.setDataEncoding("UTF-8");
            res.setDataType("text/xml");
            res.setSamplerData(getRequestBody());
            res.setMonitor(isMonitor());
            res.sampleStart();

            String urlStr = getUrl().toString();
            log.debug("Start : sample " + urlStr);
            log.debug("method " + getMethod());

            httpMethod = createHttpMethod(getMethod(), urlStr);
            setDefaultRequestHeaders(httpMethod);
            client = setupConnection(getUrl(), httpMethod);
            if (httpMethod instanceof EntityEnclosingMethod) {
                ((EntityEnclosingMethod) httpMethod)
                        .setRequestEntity(new StringRequestEntity(
                                getRequestBody(), "text/xml", "UTF-8"));
            }
            overrideHeaders(httpMethod, urlStr, getMethod());
            res.setRequestHeaders(getConnectionHeaders(httpMethod));

            int statusCode = -1;
            try {
                statusCode = client.executeMethod(httpMethod);
            } catch (RuntimeException e) {
                log.error("Exception when executing '" + httpMethod + "'", e);
                throw e;
            }

            instream = httpMethod.getResponseBodyAsStream();
            if (instream != null) {

                Header responseHeader = httpMethod
                        .getResponseHeader(HTTPConstantsInterface.HEADER_CONTENT_ENCODING);
                if (responseHeader != null
                        && HTTPConstantsInterface.ENCODING_GZIP.equals(responseHeader.getValue())) {
                    instream = new GZIPInputStream(instream);
                }
                res.setResponseData(readResponse(res, instream,
                        (int) httpMethod.getResponseContentLength()));
            }

            res.sampleEnd();

            res.setResponseCode(Integer.toString(statusCode));
            res.setSuccessful(isSuccessCode(statusCode));

            res.setResponseMessage(httpMethod.getStatusText());

            String ct = null;
            org.apache.commons.httpclient.Header h = httpMethod
                    .getResponseHeader(HTTPConstantsInterface.HEADER_CONTENT_TYPE);
            if (h != null) {
                ct = h.getValue();
                res.setContentType(ct);
                res.setEncodingAndType(ct);
            }

            String responseHeaders = getResponseHeaders(httpMethod);
            res.setResponseHeaders(responseHeaders);

            log.debug("End : sample");
            httpMethod.releaseConnection();

            return res;
        } catch (MalformedURLException e) {
            res.sampleEnd();
            log.warn(e.getMessage());
            res.setResponseMessage(e.getMessage());
            return res;
        } catch (IllegalArgumentException e) {
            res.sampleEnd();
            log.warn(e.getMessage());
            res.setResponseMessage(e.getMessage());
            return res;
        } catch (IOException e) {
            res.sampleEnd();
            log.warn(e.getMessage());
            res.setResponseMessage(e.getMessage());
            return res;
        } finally {
            JOrphanUtils.closeQuietly(instream);
            if (httpMethod != null) {
                httpMethod.releaseConnection();
                return res;
            }
        }
    }

    private void setDefaultRequestHeaders(HttpMethodBase httpMethod) {
        // TODO Auto-generated method stub
    }

    private HttpMethodBase createHttpMethod(String method, String urlStr) {
        HttpMethodBase httpMethod;
        // May generate IllegalArgumentException
        if (method.equals(HTTPConstantsInterface.POST)) {
            httpMethod = new PostMethod(urlStr);
        } else if (method.equals(HTTPConstantsInterface.PUT)) {
            httpMethod = new PutMethod(urlStr);
        } else if (method.equals(HTTPConstantsInterface.HEAD)) {
            httpMethod = new HeadMethod(urlStr);
        } else if (method.equals(HTTPConstantsInterface.TRACE)) {
            httpMethod = new TraceMethod(urlStr);
        } else if (method.equals(HTTPConstantsInterface.OPTIONS)) {
            httpMethod = new OptionsMethod(urlStr);
        } else if (method.equals(HTTPConstantsInterface.DELETE)) {
            httpMethod = new DeleteMethod(urlStr);
        } else if (method.equals(HTTPConstantsInterface.GET)) {
            httpMethod = new GetMethod(urlStr);
        } else if (method.equals(HTTPConstantsInterface.PATCH)) {
            httpMethod = new PutMethod(urlStr) {
                @Override
                public String getName() {
                    return "PATCH";
                }
            };
        } else {
            log.error("Unexpected method (converted to GET): " + method);
            httpMethod = new GetMethod(urlStr);
        }
        return httpMethod;
    }
}
