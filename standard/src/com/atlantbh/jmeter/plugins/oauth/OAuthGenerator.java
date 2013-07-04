/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */
package com.atlantbh.jmeter.plugins.oauth;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.jmeter.protocol.http.util.Base64Encoder;

public class OAuthGenerator {

    private static final String UTF_8 = "UTF-8";
    private static final String MAC_NAME = "HmacSHA1";
    private static final String MAC_VALUE = "HMAC-SHA1";
    private static final String VERSION_VALUE = "1.0";
    // header field names
    public static final String REALM = "realm";
    public static final String CONSUMER_KEY = "oauth_consumer_key";
    public static final String SIGNATURE = "oauth_signature";
    public static final String SIGNATURE_METHOD = "oauth_signature_method";
    public static final String TIMESTAMP = "oauth_timestamp";
    public static final String NONCE = "oauth_nonce";
    public static final String VERSION = "oauth_version";
    public static final String TOKEN = "oauth_token";
    private static final Logger log = Logger.getLogger(OAuthGenerator.class.getName());
    private Map<String, String> parameterMap = new HashMap<String, String>();
    private Mac mac;
    private List<Parameter> queryParameters;

    public static OAuthGenerator getInstance(String consumerKey, String consumerSecret) {
        try {
            return new OAuthGenerator(consumerKey, consumerSecret);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Initialization of OAuthGenerator has failed.", e);
            return null;
        }
    }

    public String getAuthorization(String url, String method) {

        if (url == null) {
            log.severe("The request url is null.");
            return null;
        }
        if (method == null) {
            log.severe("The request method is null.");
            return null;
        }

        String normalizedUrl = processUrl(url);
        if (normalizedUrl == null) {
            log.severe("The provided url was rejected.");
            return null;
        }

        String signature = getSignature(normalizedUrl, method);

        if (signature == null) {
            log.severe("Could not calculate signature for provided parameters.");
            return null;
        }
        return getHeaderValue(signature, normalizedUrl);
    }

    private OAuthGenerator(String consumerKey, String consumerSecret) {
        if ((consumerKey == null) || (consumerSecret == null)) {
            throw new IllegalArgumentException();
        }
        buildInitialParameterMap(consumerKey);
        mac = getMac(consumerSecret);
        if (mac == null) {
            throw new IllegalArgumentException("Could not initialize MAC with provided consumer secret.");
        }
    }

    private Mac getMac(String consumerSecret) {
        try {
            SecretKey key = new SecretKeySpec((consumerSecret + '&').getBytes(UTF_8), MAC_NAME);
            Mac result = Mac.getInstance(MAC_NAME);
            result.init(key);
            return result;
        } catch (UnsupportedEncodingException e) {
            log.log(Level.SEVERE, "This exception should never ocurr!", e);
        } catch (NoSuchAlgorithmException e) {
            log.log(Level.SEVERE, "This exception should never ocurr!", e);
        } catch (InvalidKeyException e) {
            log.log(Level.SEVERE, "The key used to initialize MAC algorithm is invalid.", e);
        }
        return null;
    }

    private String getSignature(String requestUrl, String method) {

        updateParameterMap();

        String sbs = method + '&' + Parameter.percentEncode(requestUrl) + '&' + Parameter.percentEncode(sortAndEncodeParameters());

        try {
            return Base64Encoder.encode(mac.doFinal(sbs.getBytes(UTF_8))).trim();
        } catch (IllegalStateException e) {
            log.log(Level.SEVERE, "This exception should never ocurr!", e);
        } catch (UnsupportedEncodingException e) {
            log.log(Level.SEVERE, "This exception should never ocurr!", e);
        }
        return null;
    }

    private String sortAndEncodeParameters() {
        ArrayList<Parameter> params = new ArrayList<Parameter>(queryParameters);

        for (String key : parameterMap.keySet()) {
            params.add(new Parameter(key, parameterMap.get(key)));
        }

        Collections.sort(params);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        boolean first = true;
        for (Parameter parameter : params) {
            if (first) {
                first = false;
            } else {
                out.write(38);
            }
            try {
                out.write(parameter.getKey().getBytes());
                out.write(61);
                out.write(Parameter.percentEncode(parameter.getValue()).getBytes());
            } catch (IOException e) {
                log.log(Level.SEVERE, "This should never happen.", e);
            }

        }
        return new String(out.toByteArray());
    }

    private String processUrl(String requestUrl) {
        URI uri;
        try {
            uri = new URI(requestUrl);
        } catch (URISyntaxException e) {
            log.log(Level.SEVERE, "The url " + requestUrl + " has invalid syntax.", e);
            return null;
        }

        processQueryParameters(uri.getQuery());

        String scheme = uri.getScheme().toLowerCase();
        String authority = uri.getAuthority().toLowerCase();

        if (((scheme.equals("http")) && (uri.getPort() == 80)) || ((scheme.equals("https")) && (uri.getPort() == 443))) {
            int index = authority.lastIndexOf(":");
            if (index >= 0) {
                authority = authority.substring(0, index);
            }
        }

        String path = uri.getRawPath();

        if ((path == null) || path.length() <= 0) {
            path = "/";
        }

        return scheme + "://" + authority + path;
    }

    private void processQueryParameters(String decodedQuery) {
        queryParameters = new ArrayList<Parameter>();
        if (decodedQuery == null) {
            return;
        }

        for (String queryPart : decodedQuery.split("&")) {
            if (queryPart.contains("=")) {
                String[] split = queryPart.split("=");
                if (split.length == 2) {
                    queryParameters.add(new Parameter(split[0], split[1]));
                } else {
                    queryParameters.add(new Parameter(split[0], ""));
                }
            } else {
                queryParameters.add(new Parameter(queryPart, ""));
            }
        }
    }

    private String getHeaderValue(String signature, String realm) {
        StringBuilder sb = new StringBuilder();

        sb.append("OAuth ");
        sb.append(getHeaderElement(REALM, realm)).append(",");
        for (String key : parameterMap.keySet()) {
            String value = parameterMap.get(key);
            sb.append(getHeaderElement(key, value));
            sb.append(",");
        }

        sb.append(getHeaderElement(SIGNATURE, signature));

        return sb.toString();
    }

    private void buildInitialParameterMap(String consumerKey) {
        parameterMap.put(CONSUMER_KEY, consumerKey);
        parameterMap.put(SIGNATURE_METHOD, MAC_VALUE);
        parameterMap.put(VERSION, VERSION_VALUE);

    }

    private void updateParameterMap() {
        parameterMap.put(TIMESTAMP, Long.toString(System.currentTimeMillis() / 1000L));
        parameterMap.put(NONCE, Long.toString(System.nanoTime()));
    }

    private String getHeaderElement(String name, String value) {
        return Parameter.percentEncode(name) + "=\"" + Parameter.percentEncode(value) + "\"";
    }
}
