package com.blazemeter.api;

import com.blazemeter.jmeter.StatusNotifierCallback;
import org.apache.commons.httpclient.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.jmeter.JMeter;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.net.InetAddress;

public class BlazemeterAPIClient {


    private static final Logger log = LoggingManager.getLoggerForClass();

    private final AbstractHttpClient httpClient;
    private final StatusNotifierCallback notifier;



//            if isinstance(self.token, string_types) and ':' in self.token:
//    headers['Authorization'] = 'Basic ' + base64.b64encode(self.token)
//    elif self.token:
//    headers["X-Api-Key"] = self.token

    public BlazemeterAPIClient(StatusNotifierCallback notifier) {
        this.notifier = notifier;
        httpClient = getHTTPClient();
    }

    public String startOnline() throws IOException {


        return "";
    }

    public void sendOnlineData(JSONArray data) throws IOException {

    }

    public void endOnline() throws IOException {

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

