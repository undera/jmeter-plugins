package kg.apc.jmeter.samplers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
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

    static final String HOSTNAME = "hostname";
    static final String PORT = "port";
    static String BODY = "body";
    private static final String SPACE = " ";
    // 
    private static final Logger log = LoggingManager.getLoggerForClass();
    private ByteBuffer recvBuf = ByteBuffer.allocateDirect(1024 * 1); // TODO: make benchmarks to know how it impacts sampler performance
    private SocketAddress addr;
    //private SocketChannel sock;

    public HTTPRawSampler() {
        super();
    }

    public final String getHostName() {
        return getPropertyAsString(HOSTNAME);
    }

    void setHostName(String text) {
        addr = null; // reset cached addr and SendBuf
        setProperty(HOSTNAME, text);
    }

    public String getPort() {
        return getPropertyAsString(PORT);
    }

    public void setPort(String value) {
        addr = null; // reset cached addr and SendBuf
        setProperty(PORT, value);
    }

    public String getRawRequest() {
        return getPropertyAsString(HTTPRawSampler.BODY);
    }

    public void setRawRequest(String value) {
        addr = null; // reset cached addr and SendBuf
        setProperty(BODY, value);
    }

    public SampleResult sample(Entry e) {
        if (addr == null) {
            log.debug("Create cached values");
            // TODO: can we get them from HTTP Config?
            int port;
            try {
                port = Integer.parseInt(getPort());
            } catch (NumberFormatException ex) {
                log.warn("Wrong port number: " + getPort() + ", defaulting to 80", ex);
                port=80;
            }
            addr = new InetSocketAddress(getHostName(), port);
        }

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
        // TODO: make benchmarks - if this slows us - get rid of it
        String[] parsed = res.getResponseDataAsString().split("\\r\\n");

        if (parsed.length > 0) {
            int s=parsed[0].indexOf(SPACE);
            int e=parsed[0].indexOf(SPACE, s+1);
            res.setResponseCode(parsed[0].substring(s, e).trim());
            res.setResponseMessage(parsed[0].substring(e).trim());
        }

        if (parsed.length > 1) {
            int n=1;
            String headers="";
            while(n<parsed.length && parsed[n].length()>0)
            {
                headers+=parsed[n]+"\r\n";
                n++;
            }
            res.setResponseHeaders(headers);
            String body="";
            while(n<parsed.length)
            {
                body+=parsed[n]+"\r\n";
                n++;
            }
            res.setResponseData(body.getBytes());
        }
    }

    private byte[] processIO(SampleResult res) throws Exception {
        //log.info("Begin IO");
        SocketChannel sock = getSocketChannel(addr);
        ByteArrayOutputStream response = new ByteArrayOutputStream();
        //log.info("send");
        ByteBuffer sendBuf = ByteBuffer.wrap(getRawRequest().getBytes()); // cannot cache it because of variable processing
        sock.write(sendBuf);

        int cnt = 0;
        recvBuf.clear();
        while ((cnt = sock.read(recvBuf)) != -1) {
            //log.info("Read "+cnt);
            recvBuf.flip();
            byte[] bytes = new byte[cnt];
            recvBuf.get(bytes);
            response.write(bytes);
            recvBuf.clear();
        }
        res.sampleEnd();

        sock.close();
        //log.info("End IO");
        return response.toByteArray();
    }

    protected SocketChannel getSocketChannel(SocketAddress address) throws IOException {
        //log.info("Open sock");
        // TODO: add keep-alive ability
        SocketChannel sock = SocketChannel.open();
        sock.connect(address);
        // TODO: have timeouts
        return sock;
    }
}
