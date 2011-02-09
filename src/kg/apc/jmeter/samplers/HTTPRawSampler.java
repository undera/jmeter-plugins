package kg.apc.jmeter.samplers;

import java.io.IOException;
import java.io.OutputStream;
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
    protected HttpURLConnection persistentConnection;
    private static final Logger log = LoggingManager.getLoggerForClass();

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
            conn = persistentConnection==null?setupConnection(url, method, res):persistentConnection;
            makeHTTPRequest(res, conn);
            return res;
        } catch (IOException e) {
            log.error("Error during request", e);
            res.sampleEnd();
            // We don't want to continue using this connection, even if KeepAlive is set
            if (conn != null) { // May not exist
                conn.disconnect();
            }
            conn = null; // Don't process again
            return errorResult(e, res);
        } finally {
            if (conn != null) { // May not exist
                disconnect(conn); // Disconnect unless using KeepAlive
            }
        }
    }

    private void makeHTTPRequest(HTTPSampleResult res, HttpURLConnection conn) throws IOException {
        // Attempt the connection:
        conn.connect();
        sendRawData(conn);
        // Request sent. Now get the response:
        byte[] responseData = readResponse(conn, res);
        res.sampleEnd();
        // Now collect the results into the HTTPSampleResult:
        res.setResponseData(responseData);
        int errorLevel = conn.getResponseCode();
        res.setResponseCode(Integer.toString(errorLevel));
        res.setSuccessful(isSuccessCode(errorLevel));
        res.setResponseMessage(conn.getResponseMessage());
        res.setResponseHeaders(getResponseHeaders(conn));
        log.debug("End : sample");
    }

    private void sendRawData(HttpURLConnection conn) throws IOException {
        byte[] data = getRawRequest().getBytes();
        OutputStream out = conn.getOutputStream();
        try {
            out.write(data);
        } catch (IOException ex) {
            out.close();
        } finally {
            out.close();
        }
    }

    private String getRawRequest() {
        return "test";
    }
}
