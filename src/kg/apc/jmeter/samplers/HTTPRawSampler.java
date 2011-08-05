// TODO: add property to limit data stored in sampleresults
package kg.apc.jmeter.samplers;

import kg.apc.io.SocketChannelWithTimeouts;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
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

    private static final String FILE_NAME = "fileName";
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

    protected byte[] readResponse(SocketChannel channel, SampleResult res) throws IOException {
        ByteArrayOutputStream response = new ByteArrayOutputStream();
        recvBuf.clear();
        boolean firstPack = true;
        int cnt = 0;

        try {
            while ((cnt = channel.read(recvBuf)) != -1) {
                if (firstPack) {
                    res.latencyEnd();
                    firstPack = false;
                }
                recvBuf.flip();
                if (recvDataLimit < 0 || response.size() <= recvDataLimit) {
                    byte[] bytes = new byte[cnt];
                    recvBuf.get(bytes);
                    response.write(bytes);
                }

                recvBuf.clear();
            }

            if (response.size() < 1) {
                log.warn("Read no bytes from socket, seems it was closed. Let it be so.");
                channel.close();
            }
        } catch (IOException ex) {
            channel.close();
            throw ex;
        }

        res.sampleEnd();
        if (!isUseKeepAlive()) {
            channel.close();
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
                String rc = parsed[0].substring(s, e).trim();
                try {
                    int rcInt = Integer.parseInt(rc);
                    if (rcInt < 100 || rcInt > 599) {
                        return;
                    }
                    res.setResponseCode(rc);
                    res.setResponseMessage(parsed[0].substring(e).trim());
                } catch (NumberFormatException ex) {
                    return;
                }
            } else {
                return;
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

    @Override
    protected byte[] processIO(SampleResult res) throws Exception {
        SocketChannel sock = (SocketChannel) getSocketChannel();

        if (!getRequestData().isEmpty()) {
            ByteBuffer sendBuf = ByteBuffer.wrap(getRequestData().getBytes()); // cannot cache it because of variable processing
            sock.write(sendBuf);
        }
        sendFile(getFileToSend(), sock);
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

    @Override
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

    public boolean isParseResult() {
        return getPropertyAsBoolean(PARSE);
    }

    public void setParseResult(boolean selected) {
        setProperty(PARSE, selected);
    }

    public String getFileToSend() {
        return getPropertyAsString(FILE_NAME);
    }

    public void setFileToSend(String text) {
        setProperty(FILE_NAME, text);
    }

    // TODO: make something with test stopping in JMeter
    @Override
    public boolean interrupt() {
        if (savedSock != null && savedSock.isOpen()) {
            try {
                savedSock.close();
            } catch (IOException ex) {
                log.warn("Exception while interrupting channel: ", ex);
                return false;
            }
        }
        return true;
    }

    private void sendFile(String filename, SocketChannel sock) throws IOException {
        if (filename.isEmpty()) {
            return;
        }

        FileInputStream is = new FileInputStream(new File(filename));
        FileChannel source = is.getChannel();

        ByteBuffer sendBuf = ByteBuffer.allocateDirect(1024);// is it efficient enough?
        while (source.read(sendBuf) > 0) {
            sendBuf.flip();
            if (log.isDebugEnabled()) {
                log.debug("Sending " + sendBuf);
            }
            sock.write(sendBuf);
            sendBuf.rewind();
        }
    }
}
