package org.loadosophia.jmeter;

import net.sf.json.*;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.jmeter.JMeter;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.*;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.zip.GZIPOutputStream;

public class LoadosophiaAPIClient {

    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String COLOR_NONE = "none";
    public static final String[] colors = {COLOR_NONE, "red", "green", "blue", "gray", "orange", "violet", "cyan", "black"};
    public static final int STATUS_DONE = 4;
    public static final int STATUS_ERROR = 5;
    private final AbstractHttpClient httpClient;
    private final StatusNotifierCallback notifier;
    private final String project;
    private final String address;
    private final String token;
    private final String colorFlag;
    private final String title;
    private final static int TIMEOUT = 5;

    public LoadosophiaAPIClient(StatusNotifierCallback informer, String aAddress, String aToken, String projectName, String aColorFlag, String aTitle) {
        project = projectName;
        address = aAddress;
        token = aToken;
        notifier = informer;
        colorFlag = aColorFlag;
        title = aTitle;
        httpClient = getHTTPClient();
    }

    private static AbstractHttpClient getHTTPClient() {
        AbstractHttpClient client = new DefaultHttpClient();
        String proxyHost = System.getProperty("https.proxyHost", "");
        if (!proxyHost.isEmpty()) {
            int proxyPort = Integer.parseInt(System.getProperty("https.proxyPort", "-1"));
            log.info("Using proxy " + proxyHost + ":" + proxyPort);
            HttpParams params = client.getParams();
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

    public LoadosophiaUploadResults sendFiles(File targetFile, LinkedList<String> perfMonFiles) throws IOException {
        notifier.notifyAbout("Starting upload to BM.Sense");
        LoadosophiaUploadResults results = new LoadosophiaUploadResults();
        LinkedList<FormBodyPart> partsList = getUploadParts(targetFile, perfMonFiles);

        JSONObject res = queryObject(createPost(address + "api/files", partsList), 201);

        int queueID = Integer.parseInt(res.getString("QueueID"));
        results.setQueueID(queueID);

        int testID = getTestByUpload(queueID);
        results.setTestID(testID);

        setTestTitleAndColor(testID, title.trim(), colorFlag);
        results.setRedirectLink(address + "gui/" + testID + "/");
        return results;
    }

    private LinkedList<FormBodyPart> getUploadParts(File targetFile, LinkedList<String> perfMonFiles) throws IOException {
        if (targetFile.length() == 0) {
            throw new IOException("Cannot send empty file to BM.Sense");
        }

        log.info("Preparing files to send");
        LinkedList<FormBodyPart> partsList = new LinkedList<>();
        partsList.add(new FormBodyPart("projectKey", new StringBody(project)));
        partsList.add(new FormBodyPart("jtl_file", new FileBody(gzipFile(targetFile))));

        Iterator<String> it = perfMonFiles.iterator();
        int index = 0;
        while (it.hasNext()) {
            File perfmonFile = new File(it.next());
            if (!perfmonFile.exists()) {
                log.warn("File not exists, skipped: " + perfmonFile.getAbsolutePath());
                continue;
            }

            if (perfmonFile.length() == 0) {
                log.warn("Empty file skipped: " + perfmonFile.getAbsolutePath());
                continue;
            }

            partsList.add(new FormBodyPart("perfmon_" + index, new FileBody(gzipFile(perfmonFile))));
            index++;
        }
        return partsList;
    }

    public String startOnline() throws IOException {
        String uri = address + "api/active/receiver/start";
        LinkedList<FormBodyPart> partsList = new LinkedList<>();
        partsList.add(new FormBodyPart("token", new StringBody(token)));
        partsList.add(new FormBodyPart("projectKey", new StringBody(project)));
        partsList.add(new FormBodyPart("title", new StringBody(title)));
        JSONObject obj = queryObject(createPost(uri, partsList), 201);
        return address + "gui/active/" + obj.optString("OnlineID", "N/A") + "/";
    }

    public void sendOnlineData(JSONArray data) throws IOException {
        String uri = address + "api/active/receiver/data";
        LinkedList<FormBodyPart> partsList = new LinkedList<>();
        String dataStr = data.toString();
        log.debug("Sending active test data: " + dataStr);
        partsList.add(new FormBodyPart("data", new StringBody(dataStr)));
        query(createPost(uri, partsList), 202);
    }

    public void endOnline(String redirectLink) throws IOException {
        String uri = address + "api/active/receiver/stop";
        LinkedList<FormBodyPart> partsList = new LinkedList<>();
        partsList.add(new FormBodyPart("redirect", new StringBody(redirectLink)));
        query(createPost(uri, partsList), 205);
    }

    private File gzipFile(File src) throws IOException {
        // Never try to make it stream-like on the fly, because content-length still required
        // Create the GZIP output stream
        String outFilename = src.getAbsolutePath() + ".gz";
        notifier.notifyAbout("Gzipping " + src.getAbsolutePath());
        GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(outFilename), 1024 * 8, true);

        // Open the input file
        FileInputStream in = new FileInputStream(src);

        // Transfer bytes from the input file to the GZIP output stream
        byte[] buf = new byte[10240];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();

        // Complete the GZIP file
        out.finish();
        out.close();

        src.delete();

        return new File(outFilename);
    }

    private int getTestByUpload(int queueID) throws IOException {
        while (true) {
            try {
                Thread.sleep(5000); // TODO: parameterize it
            } catch (InterruptedException ex) {
                throw new RuntimeException("Interrupted on getting TestID");
            }

            JSONObject status = queryObject(new HttpGet(address + "api/files/" + queueID), 200);

            int intStatus = Integer.parseInt(status.getString("status"));
            if (intStatus == STATUS_DONE) {
                return Integer.parseInt(status.getString("TestID"));
            } else if (intStatus == STATUS_ERROR) {
                throw new IOException("File processing finished with error: " + status.getString("UserError"));
            }
        }
    }

    private void setTestTitleAndColor(int testID, String title, String color) throws IOException {
        if (title.isEmpty() && (color.isEmpty() || color.equals(COLOR_NONE)) ) {
            return;
        }

        JSONObject data = new JSONObject();
        if (!title.isEmpty()) {
            data.put("title", title);
        }

        if (!title.isEmpty()) {
            data.put("colorFlag", title);
        }

        query(createPatch(address + "api/tests/" + testID, data), 200);
    }

    protected JSON query(HttpRequestBase request, int expectedCode) throws IOException {
        log.info("Requesting: " + request);
        request.setHeader("Authorization", "Token " + token);

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

    private HttpPatch createPatch(String url, JSON data) {
        HttpPatch patch = new HttpPatch(url);
        patch.setHeader("Content-Type", "application/json");

        String string = data.toString(1);
        try {
            patch.setEntity(new StringEntity(string, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return patch;
    }

}
