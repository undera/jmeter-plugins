package org.jmeterplugins.repository;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.jmeter.JMeter;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.*;
import java.net.*;
import java.util.*;

public class JARSourceHTTP extends JARSource {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private final String[] addresses;
    protected AbstractHttpClient httpClient = new DefaultHttpClient();
    private int timeout = 1000; // don't delay JMeter startup for more than 1 second

    public JARSourceHTTP(String[] addresses) {
        this.addresses = addresses;
        httpClient = getHTTPClient();
    }

    private AbstractHttpClient getHTTPClient() {
        AbstractHttpClient client = new DefaultHttpClient();
        String proxyHost = System.getProperty("https.proxyHost", "");
        if (!proxyHost.isEmpty()) {
            int proxyPort = Integer.parseInt(System.getProperty("https.proxyPort", "-1"));
            log.info("Using proxy " + proxyHost + ":" + proxyPort);
            HttpParams params = client.getParams();
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            params.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

            String proxyUser = System.getProperty(JMeter.HTTP_PROXY_USER, org.apache.jmeter.util.JMeterUtils.getProperty(JMeter.HTTP_PROXY_USER));
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

    protected JSON getJSON(String uri) throws IOException {

        log.debug("Requesting " + uri);

        HttpRequestBase get = new HttpGet(uri);
        HttpParams requestParams = get.getParams();
        requestParams.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
        requestParams.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);

        HttpResponse result = httpClient.execute(get);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        HttpEntity entity = result.getEntity();
        try {
            entity.writeTo(bos);
            byte[] bytes = bos.toByteArray();
            if (bytes == null) {
                bytes = "null".getBytes();
            }
            String response = new String(bytes);
            int statusCode = result.getStatusLine().getStatusCode();
            if (statusCode >= 300) {
                log.warn("Response with code " + result + ": " + response);
                throw new IOException("Repository responded with wrong status code: " + statusCode);
            } else {
                log.debug("Response with code " + result + ": " + response);
            }
            return JSONSerializer.toJSON(response, new JsonConfig());
        } finally {
            get.abort();
            try {
                entity.getContent().close();
            } catch (IOException | IllegalStateException e) {
                log.warn("Exception in finalizing request", e);
            }
        }
    }

    public JSON getRepositories(String path) throws IOException {
        final List<JSON> repositories = new ArrayList<>(addresses.length);
        for (String address : addresses) {
            repositories.add(getJSON(address + path));
        }
        final JSONArray result = new JSONArray();
        for (JSON json : repositories) {
            if (!(json instanceof JSONArray)) {
                throw new RuntimeException("Result is not array");
            }
            for (Object elm : (JSONArray) json) {
                result.add(elm);
            }
        }
        return result;
    }

    @Override
    public JSON getRepo() throws IOException {
        return getRepositories("?installID=" + getInstallID());
    }

    /**
     * This function makes sure anonymous identifier sent
     *
     * @return unique ID for installation
     */
    public String getInstallID() {
        String str = "";
        str += getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        try {
            str += "\t" + InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.warn("Cannot get local host name", e);
        }

        try {
            Enumeration<NetworkInterface> ifs = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(ifs)) {
                str += "\t" + Arrays.toString(netint.getHardwareAddress());
            }
        } catch (SocketException e) {
            log.warn("Failed to get network addresses", e);
        }

        return DigestUtils.md5Hex(str);
    }

    @Override
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public DownloadResult getJAR(final String id, String location, final GenericCallback<String> callback) throws IOException {
        URI url = URI.create(location);
        log.info("Downloading: " + url);
        callback.notify("Downloading " + id + "...");
        HttpGet httpget = new HttpGet(url);

        HttpContext context = new BasicHttpContext();
        HttpResponse response = httpClient.execute(httpget, context);
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            throw new IOException(response.getStatusLine().toString());
        }

        HttpEntity entity = response.getEntity();

        File tempFile = File.createTempFile(id, ".jar");

        final long size = entity.getContentLength();

        try (InputStream inputStream = entity.getContent();
             OutputStream fos = new FileOutputStream(tempFile);
             BufferedOutputStream bos = new BufferedOutputStream(fos);) {

            copyLarge(inputStream, bos, new GenericCallback<Long>() {
                @Override
                public void notify(Long progress) {
                    callback.notify(String.format("Downloading %s: %d%%", id, 100 * progress / size));
                }
            });
            callback.notify("Downloaded " + id + "...");

            Header cd = response.getLastHeader("Content-Disposition");
            String filename;
            if (cd != null) {
                filename = cd.getValue().split(";")[1].split("=")[1];
            } else {
                HttpUriRequest currentReq = (HttpUriRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
                HttpHost currentHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
                String currentUrl = (currentReq.getURI().isAbsolute()) ? currentReq.getURI().toString() : (currentHost.toURI() + currentReq.getURI());
                filename = FilenameUtils.getName(currentUrl);
            }

            return new DownloadResult(tempFile.getPath(), filename);
        }
    }

    @Override
    public void reportStats(String[] usageStats) throws IOException {
        ArrayList<String> stats = new ArrayList<>();
        stats.add(getInstallID());
        Collections.addAll(stats, usageStats);

        for (String uri : addresses) {
            HttpPost post = null;
            try {
                post = new HttpPost(uri);
                post.setHeader("Content-Type", "application/x-www-form-urlencoded");
                HttpEntity body = new StringEntity("stats=" + URLEncoder.encode(Arrays.toString(stats.toArray(new String[0])), "UTF-8"));
                post.setEntity(body);
                HttpParams requestParams = post.getParams();
                requestParams.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
                requestParams.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000);

                log.debug("Requesting " + uri);
                httpClient.execute(post);
            } finally {
                if (post != null) {
                    try {
                        post.abort();
                    } catch (Exception e) {
                        log.warn("Failure while aborting POST", e);
                    }
                }
            }
        }
    }

    private static long copyLarge(InputStream input, OutputStream output, GenericCallback<Long> progressCallback) throws IOException {
        byte[] buffer = new byte[4096];
        long count = 0L;

        int n;
        for (; -1 != (n = input.read(buffer)); count += (long) n) {
            output.write(buffer, 0, n);
            progressCallback.notify(count);
        }

        return count;
    }
}
