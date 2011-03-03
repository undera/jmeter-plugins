package kg.apc.jmeter.samplers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class HTTPRawSampler extends AbstractSampler {

    public static final String CRLF = "\r\n";
    public static final String HOSTNAME = "hostname";
    public static final String PORT = "port";
    public static final String KEEPALIVE = "keepalive";
    public static final String BODY = "body";
    public static final String TIMEOUT = "timeout";
    private static final String SPACE = " ";
    // 
    private static final Logger log = LoggingManager.getLoggerForClass();
    private ByteBuffer recvBuf = ByteBuffer.allocateDirect(1024 * 4); // TODO: make benchmarks to know how it impacts sampler performance
    private SocketChannel savedSock;

    public HTTPRawSampler() {
        super();
    }

    public final String getHostName() {
        return getPropertyAsString(HOSTNAME);
    }

    void setHostName(String text) {
        setProperty(HOSTNAME, text);
    }

    public String getPort() {
        return getPropertyAsString(PORT);
    }

    public void setPort(String value) {
        setProperty(PORT, value);
    }

    public String getRawRequest() {
        return getPropertyAsString(HTTPRawSampler.BODY);
    }

    public void setRawRequest(String value) {
        setProperty(BODY, value);
    }

    public SampleResult sample(Entry e) {
        SampleResult res = new SampleResult();
        res.setSampleLabel(getName());
        res.setSamplerData(getRawRequest());
        res.sampleStart();
        res.setDataType(SampleResult.TEXT);
        res.setSuccessful(true);
        res.setResponseCode("-1");
        try {
            res.setResponseData(processIO(res));
            parseResponse(res);
        } catch (Exception ex) {
            log.error(getHostName(), ex);
            res.sampleEnd();
            res.setSuccessful(false);
            res.setResponseMessage(ex.toString());
            res.setResponseData(JMeterPluginsUtils.getStackTrace(ex).getBytes());
        }

        return res;
    }

    private void parseResponse(SampleResult res) {
        // TODO: make benchmarks - if this slows us - get rid of it, or give checkbox
        String[] parsed = res.getResponseDataAsString().split("\\r\\n");

        if (parsed.length > 0) {
            int s = parsed[0].indexOf(SPACE);
            int e = parsed[0].indexOf(SPACE, s + 1);
            if (s < e) {
                res.setResponseCode(parsed[0].substring(s, e).trim());
                res.setResponseMessage(parsed[0].substring(e).trim());
            }
        }

        if (parsed.length > 1) {
            int n = 1;
            String headers = "";
            while (n < parsed.length && parsed[n].length() > 0) {
                headers += parsed[n] + CRLF;
                n++;
            }
            res.setResponseHeaders(headers);
            String body = "";
            n++;
            while (n < parsed.length) {
                body += parsed[n] + (n + 1 < parsed.length ? CRLF : "");
                n++;
            }
            res.setResponseData(body.getBytes());
        }
    }

    private byte[] processIO(SampleResult res) throws Exception {
        //log.info("Begin IO");
        ByteArrayOutputStream response = new ByteArrayOutputStream();
        //log.info("send");
        ByteBuffer sendBuf = ByteBuffer.wrap(getRawRequest().getBytes()); // cannot cache it because of variable processing
        SocketChannel sock = getSocketChannel();
        sock.write(sendBuf);

        int cnt = 0;
        recvBuf.clear();
        boolean firstPack = true;
        while ((cnt = sock.read(recvBuf)) != -1) {
            if (firstPack) {
                res.latencyEnd();
                firstPack = false;
            }
            //log.debug("Read " + recvBuf.toString());
            recvBuf.flip();
            byte[] bytes = new byte[cnt];
            recvBuf.get(bytes);
            response.write(bytes);
            recvBuf.clear();
        }
        res.sampleEnd();

        if (!getUseKeepAlive()) {
            sock.close();
        }
        //log.info("End IO");
        return response.toByteArray();
    }

    private SocketChannel getSocketChannel() throws IOException {
        if (getUseKeepAlive() && savedSock != null) {
            return savedSock;
        }

        // FIXME: moving address outside started samplerresult could improve timings
        int port;
        try {
            port = Integer.parseInt(getPort());
        } catch (NumberFormatException ex) {
            log.warn("Wrong port number: " + getPort() + ", defaulting to 80", ex);
            port = 80;
        }
        InetSocketAddress address = new InetSocketAddress(getHostName(), port);

        //log.info("Open sock");
        savedSock = getSocketChannelImpl();
        savedSock.connect(address);
        int timeout = 0;
        try {
            timeout = Integer.parseInt(getTimeout());
        } catch (NumberFormatException e) {
            log.error("Wrong timeout: " + getTimeout(), e);
        }
        savedSock.socket().setSoTimeout(timeout);
        // TODO: have timeouts
        return savedSock;
    }

    private boolean getUseKeepAlive() {
        return getPropertyAsBoolean(KEEPALIVE);
    }

    public void setUseKeepAlive(boolean selected) {
        setProperty(KEEPALIVE, selected);
    }

    public String getTimeout() {
        return getPropertyAsString(TIMEOUT);
    }

    public void setTimeout(String value) {
        setProperty(TIMEOUT, value);
    }

    protected SocketChannel getSocketChannelImpl() throws IOException {
        return SocketChannel.open();
    }
}
