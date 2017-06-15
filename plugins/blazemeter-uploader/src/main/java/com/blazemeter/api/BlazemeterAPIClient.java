package com.blazemeter.api;

import com.blazemeter.jmeter.BlazemeterUploaderGui;
import com.blazemeter.jmeter.StatusNotifierCallback;
import net.sf.json.JSON;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
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
import java.util.LinkedList;

public class BlazemeterAPIClient {


    private static final Logger log = LoggingManager.getLoggerForClass();
    private final static int TIMEOUT = 5;

    private final AbstractHttpClient httpClient;
    private final StatusNotifierCallback notifier;
    private final String address;
    private final String dataAddress;
    private final String project;
    private final String workspace;
    private final String token;
    private final String title;

    private Session session;
    private String signature;

//            if isinstance(self.token, string_types) and ':' in self.token:
//    headers['Authorization'] = 'Basic ' + base64.b64encode(self.token)
//    elif self.token:
//    headers["X-Api-Key"] = self.token

    public BlazemeterAPIClient(StatusNotifierCallback notifier, String address, String dataAddress, String project, String workspace, String token, String title) {
        this.notifier = notifier;
        this.address = address;
        this.dataAddress = dataAddress;
        this.project = project;
        this.workspace = workspace;
        this.token = token;
        this.title = title;
        this.httpClient = getHTTPClient();
    }

    public String startOnline() throws IOException {
        if (isAnonymousTest()) {
            notifier.notifyAbout("No BlazeMeter API key provided, will upload anonymously");
            return startAnonymousTest();
        } else {
            return startTest();
        }
    }

    private String startAnonymousTest() throws IOException {
        String uri = address + "/api/v4/sessions";
        JSONObject response = queryObject(createPost(uri, new LinkedList<FormBodyPart>()), 201);
        JSONObject result = response.getJSONObject("result");
        this.session = getSession(result);
        this.signature = result.optString("signature");
        return result.optString("publicTokenUrl");
    }

    private Session getSession(JSONObject result) {
        final JSONObject session = result.getJSONObject("session");
        return new Session(session.optString("id"), session.optString("testId"), session.optString("userId"));
    }

    private String startTest() {
        return "startTest";
    }


    public void sendOnlineData(JSONObject data) throws IOException {
        if (isAnonymousTest()) {
            sendAnonymousData(data);
        } else {
            // TODO:
        }

    }

    private void sendAnonymousData(JSONObject data) throws IOException {
        String uri = dataAddress +
                String.format("/submit.php?session_id=%s&signature=%s&test_id=%s&user_id=%s",
                        session.getId(), signature, session.getTestId(), session.getUserId());
        uri += "&pq=0&target=labels_bulk&update=1"; //TODO: % self.kpi_target
        String dataStr = data.toString();
        log.debug("Sending active test data: " + dataStr);
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Content-Type", "application/json");
        HttpEntity entity = new StringEntity(dataStr, ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);
        query(httpPost, 200);

    }

    public void endOnline() throws IOException {
        if (isAnonymousTest()) {
            finishAnonymousTest();
        } else {
            finishTest();
        }
    }

    private void finishAnonymousTest() throws IOException {
        String uri = address + String.format("/api/v4/sessions/%s/terminate-external", session.getId());
        LinkedList<FormBodyPart> partsList = new LinkedList<>();
        partsList.add(new FormBodyPart("signature", new StringBody(signature)));
        partsList.add(new FormBodyPart("testId", new StringBody(session.getTestId())));
        partsList.add(new FormBodyPart("sessionId", new StringBody(session.getId())));
        query(createPost(uri, partsList), 201);
    }

    private void finishTest() {
    }

    private boolean isAnonymousTest() {
        return token == null || token.isEmpty() || BlazemeterUploaderGui.UPLOAD_TOKEN_PLACEHOLDER.equals(token);
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

    private HttpPost createPost(String uri, LinkedList<FormBodyPart> partsList) {
        HttpPost postRequest = new HttpPost(uri);
        MultipartEntity multipartRequest = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        for (FormBodyPart part : partsList) {
            multipartRequest.addPart(part);
        }
        postRequest.setEntity(multipartRequest);
        return postRequest;
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

