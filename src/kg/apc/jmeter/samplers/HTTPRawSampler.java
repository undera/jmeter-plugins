package kg.apc.jmeter.samplers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;


// FIXME: actually keep-alive does not work!
/**
 *
 * @author undera
 */
public class HTTPRawSampler extends AbstractIPSampler {

    private static final String KEEPALIVE = "keepalive";
    private static final String PARSE = "parse";
    private static final String RNpattern = "\\r\\n";
    private static final String SPACE = " ";
    // 
    protected static final Logger log = LoggingManager.getLoggerForClass();
    private SocketChannel savedSock;

    public HTTPRawSampler() {
        super();
    }

    @Override
    public SampleResult sample(Entry entry) {
        SampleResult res = super.sample(entry);
        if (isParseResult()) {
            parseResponse(res);
        }
        return res;
    }

    protected byte[] readResponse(SocketChannel sock, SampleResult res) throws IOException {
        ByteArrayOutputStream response = new ByteArrayOutputStream();
        recvBuf.clear();
        boolean firstPack = true;
        int cnt = 0;

        try {
            while ((cnt = sock.read(recvBuf)) != -1) {
                if (firstPack) {
                    res.latencyEnd();
                    firstPack = false;
                }
                recvBuf.flip();
                byte[] bytes = new byte[cnt];
                recvBuf.get(bytes);
                response.write(bytes);
                recvBuf.clear();
            }
        } catch (IOException ex) {
            sock.close();
            throw ex;
        }

        res.sampleEnd();
        if (!isUseKeepAlive()) {
            sock.close();
        }
        res.setBytes(response.size());
        return response.toByteArray();
    }

    private void parseResponse(SampleResult res) {
        String[] parsed = res.getResponseDataAsString().split(RNpattern);

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
            String headers = EMPTY;
            while (n < parsed.length && parsed[n].length() > 0) {
                headers += parsed[n] + CRLF;
                n++;
            }
            res.setResponseHeaders(headers);
            String body = EMPTY;
            n++;
            while (n < parsed.length) {
                body += parsed[n] + (n + 1 < parsed.length ? CRLF : EMPTY);
                n++;
            }
            res.setResponseData(body.getBytes());
        }
    }

    protected byte[] processIO(SampleResult res) throws Exception {
        //log.info("Begin IO");
        //log.info("send");
        ByteBuffer sendBuf = ByteBuffer.wrap(getRequestData().getBytes()); // cannot cache it because of variable processing
        SocketChannel sock = (SocketChannel) getSocketChannel();
        sock.write(sendBuf);
        return readResponse(sock, res);
    }

    protected SocketChannel getSocketChannel() throws IOException {
        if (isUseKeepAlive() && savedSock != null) {
            return savedSock;
        }

        int port;
        try {
            port = Integer.parseInt(getPort());
        } catch (NumberFormatException ex) {
            log.warn("Wrong port number: " + getPort() + ", defaulting to 80", ex);
            port = 80;
        }
        InetSocketAddress address = new InetSocketAddress(getHostName(), port);

        //log.info("Open sock");
        savedSock = (SocketChannel) getChannel();
        savedSock.connect(address);
        return savedSock;
    }

    public boolean isUseKeepAlive() {
        return getPropertyAsBoolean(KEEPALIVE);
    }

    public void setUseKeepAlive(boolean selected) {
        setProperty(KEEPALIVE, selected);
    }

    protected AbstractSelectableChannel getChannel() throws IOException {
        int t = getTimeoutAsInt();
        if (t > 0) {
            SocketChannelWithTimeouts res = (SocketChannelWithTimeouts) SocketChannelWithTimeouts.open();
            res.setConnectTimeout(t);
            res.setReadTimeout(t);
            return res;
        } else {
            return SocketChannel.open();
        }
    }

    boolean isParseResult() {
        return getPropertyAsBoolean(PARSE);
    }

    void setParseResult(boolean selected) {
        setProperty(PARSE, selected);
    }
}
