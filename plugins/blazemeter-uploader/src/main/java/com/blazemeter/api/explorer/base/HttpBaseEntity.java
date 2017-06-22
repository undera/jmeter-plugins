package com.blazemeter.api.explorer.base;

import com.blazemeter.jmeter.StatusNotifierCallback;
import net.sf.json.JSON;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.jmeter.JMeter;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;

/**
 * Base Entity with Http client
 */
public class HttpBaseEntity extends BaseEntity {

    protected static final Logger log = LoggingManager.getLoggerForClass();
    protected final static int TIMEOUT = 5;

    protected final AbstractHttpClient httpClient;
    protected final StatusNotifierCallback notifier;
    protected final String address;
    protected final String dataAddress;
    protected final String token;
    protected final boolean isAnonymousTest;

    /**
     * Constructor that create new HTTP Client
     */
    public HttpBaseEntity(StatusNotifierCallback notifier, String address, String dataAddress, String token, boolean isAnonymousTest) {
        super("", "");
        this.notifier = notifier;
        this.address = address;
        this.dataAddress = dataAddress;
        this.token = token;
        this.isAnonymousTest = isAnonymousTest;
        this.httpClient = createHTTPClient();
    }

    /**
     * Constructor that clone only HttpBaseEntity.class fields
     */
    public HttpBaseEntity(HttpBaseEntity entity) {
        super("", "");
        this.notifier = entity.getNotifier();
        this.address = entity.getAddress();
        this.dataAddress = entity.getDataAddress();
        this.token = entity.getToken();
        this.isAnonymousTest = entity.isAnonymousTest();
        this.httpClient = entity.getHttpClient();
    }

    /**
     * Constructor that clone HttpBaseEntity.class fields
     */
    public HttpBaseEntity(HttpBaseEntity entity, String id, String name) {
        super(id, name);
        this.notifier = entity.getNotifier();
        this.address = entity.getAddress();
        this.dataAddress = entity.getDataAddress();
        this.token = entity.getToken();
        this.isAnonymousTest = entity.isAnonymousTest();
        this.httpClient = entity.getHttpClient();
    }

    /**
     * Create Get Request
     */
    protected HttpGet createGet(String uri) {
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeader("Content-Type", "application/json");
        return httpGet;
    }

    /**
     * Create Post Request
     */
    protected HttpPost createPost(String uri, String data) {
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Content-Type", "application/json");
        HttpEntity entity = new StringEntity(data, ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);
        return httpPost;
    }

    /**
     * Execute Http request and verify response
     * @param request - HTTP Request
     * @param expectedCode - expected response code
     * @return - response in JSONObject
     */
    protected JSONObject queryObject(HttpRequestBase request, int expectedCode) throws IOException {
        JSON res = query(request, expectedCode);
        if (!(res instanceof JSONObject)) {
            throw new IOException("Unexpected response: " + res);
        }
        return (JSONObject) res;
    }

    /**
     * Execute Http request and response code
     * @param request - HTTP Request
     * @param expectedCode - expected response code
     * @return - response in JSONObject
     */
    protected JSON query(HttpRequestBase request, int expectedCode) throws IOException {
        log.info("Requesting: " + request);
        setTokenToHeader(request);

        HttpParams requestParams = request.getParams();
        requestParams.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, TIMEOUT * 1000);
        requestParams.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT * 1000);

        synchronized (httpClient) {
            String response;
            try {
                HttpResponse result = httpClient.execute(request);

                int statusCode = result.getStatusLine().getStatusCode();

                response = getResponseEntity(result);

                if (statusCode != expectedCode) {
                    notifier.notifyAbout("Response with code " + statusCode + ": " + response);
                    throw new IOException("API responded with wrong status code: " + statusCode);
                } else {
                    log.debug("Response: " + response);
                }
            } finally {
                request.abort();
            }

            if (response == null || response.isEmpty()) {
                return JSONNull.getInstance();
            } else {
                return JSONSerializer.toJSON(response, new JsonConfig());
            }
        }
    }

    private void setTokenToHeader(HttpRequestBase httpRequestBase) {
        if (!isAnonymousTest) {
            if (token.contains(":")) {
                httpRequestBase.setHeader("Authorization", "Basic" + new String(Base64.encodeBase64("Test".getBytes())));
            } else {
                httpRequestBase.setHeader("X-Api-Key", token);
            }
        }
    }

    private String getResponseEntity(HttpResponse result) throws IOException {
        HttpEntity entity = result.getEntity();
        if (entity == null) {
            log.debug("Null response entity");
            return null;
        }

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            entity.writeTo(bos);
            byte[] bytes = bos.toByteArray();
            if (bytes == null) {
                bytes = "null".getBytes();
            }
            String response = new String(bytes);
            log.debug("Response with code " + result + ": " + response);
            return response;
        } finally {
            InputStream content = entity.getContent();
            if (content != null) {
                content.close();
            }
        }
    }

    private static AbstractHttpClient createHTTPClient() {
        AbstractHttpClient client = new DefaultHttpClient();
        String proxyHost = System.getProperty("https.proxyHost", "");
        if (!proxyHost.isEmpty()) {
            int proxyPort = Integer.parseInt(System.getProperty("https.proxyPort", "-1"));
            log.info("Using proxy " + proxyHost + ":" + proxyPort);
            org.apache.http.params.HttpParams params = client.getParams();
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            params.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

            String proxyUser = System.getProperty(JMeter.HTTP_PROXY_USER, JMeterUtils.getProperty(JMeter.HTTP_PROXY_USER));
            if (proxyUser != null) {
                log.info("Using authenticated proxy with username: " + proxyUser);
                String proxyPass = System.getProperty(JMeter.HTTP_PROXY_PASS, JMeterUtils.getProperty(JMeter.HTTP_PROXY_PASS));

                String localHost;
                try {
                    localHost = InetAddress.getLocalHost().getCanonicalHostName();
                } catch (Throwable e) {
                    log.error("Failed to get local host name, defaulting to 'localhost'", e);
                    localHost = "localhost";
                }

                AuthScope authscope = new AuthScope(proxyHost, proxyPort);
                String proxyDomain = JMeterUtils.getPropDefault("http.proxyDomain", "");
                NTCredentials credentials = new NTCredentials(proxyUser, proxyPass, localHost, proxyDomain);
                client.getCredentialsProvider().setCredentials(authscope, credentials);
            }
        }
        return client;
    }

    public AbstractHttpClient getHttpClient() {
        return httpClient;
    }

    public StatusNotifierCallback getNotifier() {
        return notifier;
    }

    public String getAddress() {
        return address;
    }

    public String getDataAddress() {
        return dataAddress;
    }

    public String getToken() {
        return token;
    }

    public boolean isAnonymousTest() {
        return isAnonymousTest;
    }
}
