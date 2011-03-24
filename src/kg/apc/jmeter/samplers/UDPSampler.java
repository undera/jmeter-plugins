package kg.apc.jmeter.samplers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class UDPSampler extends AbstractIPSampler implements UDPTrafficDecoder, ThreadListener {

    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String ENCODECLASS = "encodeclass";
    public static final String WAITRESPONSE = "waitresponse";
    private DatagramChannel channel;
    private UDPTrafficDecoder encoder;

    public boolean isWaitResponse() {
        return getPropertyAsBoolean(WAITRESPONSE);
    }

    public String getEncoderClass() {
        return getPropertyAsString(ENCODECLASS);
    }

    public void setWaitResponse(boolean selected) {
        setProperty(WAITRESPONSE, selected);
    }

    public void setEncoderClass(String text) {
        setProperty(ENCODECLASS, text);
    }

    protected AbstractSelectableChannel getChannel() throws IOException {
        DatagramChannel c;
        if (isWaitResponse()) {
            c = DatagramChannelWithTimeouts.open();
            ((DatagramChannelWithTimeouts) c).setReadTimeout(getTimeoutAsInt());
        } else {
            c = DatagramChannel.open();
        }
        int port = Integer.parseInt(getPort());
        c.connect(new InetSocketAddress(getHostName(), port));
        return c;
    }

    public ByteBuffer encode(String data) {
        try {
            return ByteBuffer.wrap(data.getBytes("Windows-1252"));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public byte[] decode(byte[] buf) {
        return buf;
    }

    @Override
    protected byte[] processIO(SampleResult res) throws Exception {
        ByteBuffer sendBuf = encoder.encode(getRequestData());
        channel.write(sendBuf);

        if (!isWaitResponse()) {
            res.latencyEnd();
            res.sampleEnd();
            return new byte[0];
        }

        ByteArrayOutputStream response = new ByteArrayOutputStream();
        int cnt = 0;
        recvBuf.clear();
        if ((cnt = channel.read(recvBuf)) != -1) {
            res.latencyEnd();
            //log.debug("Read " + recvBuf.toString());
            recvBuf.flip();
            byte[] bytes = new byte[cnt];
            recvBuf.get(bytes);
            response.write(bytes);
            recvBuf.clear();
        }
        res.sampleEnd();

        res.setBytes(response.size());
        return encoder.decode(response.toByteArray());
    }

    public void threadStarted() {
        try {
            channel = (DatagramChannel) getChannel();
        } catch (IOException ex) {
            log.error("Cannot open channel", ex);
        }

        try {
            Class<?> c = Thread.currentThread().getContextClassLoader().loadClass(getEncoderClass());
            Object o = c.newInstance();
            if (!(o instanceof UDPTrafficDecoder)) {
                throw new ClassNotFoundException("Class does not implement " + UDPTrafficDecoder.class.getCanonicalName());
            }
            encoder = (UDPTrafficDecoder) o;
            log.debug("Using decoder: " + encoder);
        } catch (Exception ex) {
            log.warn("Problem loading encoder " + getEncoderClass() + ", raw data will be used", ex);
            encoder=this;
        }
    }

    public void threadFinished() {
        try {
            if (channel != null) {
                channel.close();
            }
        } catch (IOException ex) {
            log.error("Cannot close channel", ex);
        }
    }
}
