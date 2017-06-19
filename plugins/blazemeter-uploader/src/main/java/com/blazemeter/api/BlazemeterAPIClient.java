package com.blazemeter.api;

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

public class BlazemeterAPIClient {


    private static final Logger log = LoggingManager.getLoggerForClass();
    private final static int TIMEOUT = 5;

    private final AbstractHttpClient httpClient;
    private final StatusNotifierCallback notifier;
    private final String address;
    private final String dataAddress;

    private final BlazemeterReport report;

    private Session session;
    private String signature;

//            if isinstance(self.token, string_types) and ':' in self.token:
//    headers['Authorization'] = 'Basic ' + base64.b64encode(self.token)
//    elif self.token:
//    headers["X-Api-Key"] = self.token

    public BlazemeterAPIClient(StatusNotifierCallback notifier, String address, String dataAddress, BlazemeterReport report) {
        this.notifier = notifier;
        this.address = address;
        this.dataAddress = dataAddress;
        this.report = report;
        this.httpClient = getHTTPClient();
        if (!isAnonymousTest()) {
            prepareClient();
        }
    }

    private void prepareClient() {
        try {
            ping();
        } catch (IOException e) {
            log.error("Cannot reach online results storage, maybe the address/token is wrong");
            return;
        }
        System.out.println();

    }

    public void ping() throws IOException {
        String uri = address + "/api/v4/web/version";
        query(createGet(uri), 200);
    }


    public String startOnline() throws IOException {
        if (isAnonymousTest()) {
            notifier.notifyAbout("No BlazeMeter API key provided, will upload anonymously");
            return startAnonymousTest();
        } else {
            return startTest();
        }
    }

    public void sendOnlineData(JSONObject data) throws IOException {
        if (isAnonymousTest()) {
            sendAnonymousData(data);
        } else {
            // TODO:
        }

    }

    public void endOnline() throws IOException {
        if (isAnonymousTest()) {
            finishAnonymousTest();
        } else {
            finishTest();
        }
    }

    private String startAnonymousTest() throws IOException {
        String uri = address + "/api/v4/sessions";
        JSONObject response = queryObject(createPost(uri, ""), 201);
        JSONObject result = response.getJSONObject("result");
        this.session = extractSession(result);
        this.signature = result.optString("signature");
        return result.optString("publicTokenUrl");
    }

    private Session extractSession(JSONObject result) {
        final JSONObject session = result.getJSONObject("session");
        return new Session(session.optString("id"), session.optString("testId"), session.optString("userId"));
    }

    private String startTest() {
        return "startTest";
    }

    private void sendAnonymousData(JSONObject data) throws IOException {
        String uri = dataAddress +
                String.format("/submit.php?session_id=%s&signature=%s&test_id=%s&user_id=%s",
                        session.getId(), signature, session.getTestId(), session.getUserId());
        uri += "&pq=0&target=labels_bulk&update=1"; //TODO: % self.kpi_target
        String dataStr = data.toString();
        log.info("Sending active test data: " + dataStr);
        // TODO:
        System.out.println("Sending active test data: " + dataStr);
        query(createPost(uri, dataStr), 200);
    }

    private HttpGet createGet(String uri) {
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeader("Content-Type", "application/json");
        setTokenToHeader(httpGet);
        return httpGet;
    }

    private HttpPost createPost(String uri, String data) {
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Content-Type", "application/json");
        setTokenToHeader(httpPost);
        HttpEntity entity = new StringEntity(data, ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);
        return httpPost;
    }

    private void setTokenToHeader(HttpRequestBase httpRequestBase) {
        if (!isAnonymousTest()) {
            String token = report.getToken();
            if (token.contains(":")) {
                httpRequestBase.setHeader("Authorization", "Basic" + new String(Base64.encodeBase64("Test".getBytes())));
            } else {
                httpRequestBase.setHeader("X-Api-Key", token);
            }
        }
    }

    private void finishAnonymousTest() throws IOException {
        String uri = address + String.format("/api/v4/sessions/%s/terminate-external", session.getId());
        JSONObject data = new JSONObject();
        data.put("signature", signature);
        data.put("testId", session.getTestId());
        data.put("sessionId", session.getId());
        query(createPost(uri, data.toString()), 500);
    }

    private void finishTest() {
    }

    private boolean isAnonymousTest() {
        return report.isAnonymousTest();
    }

    protected JSON query(HttpRequestBase request, int expectedCode) throws IOException {
        log.info("Requesting: " + request);
//        request.setHeader("Authorization", "Token " + token);

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

    private JSONObject queryObject(HttpRequestBase req, int expectedRC) throws IOException {
        JSON res = query(req, expectedRC);
        if (!(res instanceof JSONObject)) {
            throw new IOException("Unexpected response: " + res);
        }
        return (JSONObject) res;
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

    private static AbstractHttpClient getHTTPClient() {
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

}

