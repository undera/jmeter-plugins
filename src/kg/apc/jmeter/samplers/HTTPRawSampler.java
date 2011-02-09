package kg.apc.jmeter.samplers;

import java.io.IOException;
import java.net.HttpURLConnection;
import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class HTTPRawSampler extends HTTPSampler {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private String rawRequest;

    @Override
    protected HTTPSampleResult sample(java.net.URL url, String method, boolean areFollowingRedirect, int frameDepth) {
        //return super.sample(url, method, areFollowingRedirect, frameDepth);
        HttpURLConnection conn = null;

        String urlStr = url.toString();
        log.debug("Start : sample " + urlStr);

        HTTPSampleResult res = new HTTPSampleResult();
        res.setMonitor(isMonitor());

        res.setSampleLabel(urlStr);
        res.setURL(url);
        res.setHTTPMethod(method);
        res.setQueryString(getRawRequest());

        res.sampleStart(); // Count the retries as well in the time

        try {
            conn = setupConnection(url, method, res);
            // Attempt the connection:
            conn.connect();
            sendRawData(conn);

            // Request sent. Now get the response:
            byte[] responseData = readResponse(conn, res);

            res.sampleEnd();

            // Now collect the results into the HTTPSampleResult:

            res.setResponseData(responseData);

            int errorLevel = conn.getResponseCode();
            String respMsg = conn.getResponseMessage();
            res.setResponseCode(Integer.toString(errorLevel));            
            res.setSuccessful(isSuccessCode(errorLevel));
            res.setResponseMessage(conn.getResponseMessage());

            String ct = conn.getContentType();
            if (ct != null) {
                res.setContentType(ct);// e.g. text/html; charset=ISO-8859-1
                res.setEncodingAndType(ct);
            }

            res.setResponseHeaders(getResponseHeaders(conn));
            if (res.isRedirect()) {
                res.setRedirectLocation(conn.getHeaderField(HEADER_LOCATION));
            }

            // If we redirected automatically, the URL may have changed
            if (getAutoRedirects()) {
                res.setURL(conn.getURL());
            }

            res = resultProcessing(areFollowingRedirect, frameDepth, res);

            log.debug("End : sample");
            return res;
        } catch (IOException e) {
            res.sampleEnd();
            // We don't want to continue using this connection, even if KeepAlive is set
            if (conn != null) { // May not exist
                conn.disconnect();
            }
            conn = null; // Don't process again
            return errorResult(e, res);
        } finally {
            // calling disconnect doesn't close the connection immediately,
            // but indicates we're through with it. The JVM should close
            // it when necessary.
            disconnect(conn); // Disconnect unless using KeepAlive
        }
    }

    private void sendRawData(HttpURLConnection conn) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private String getRawRequest() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
