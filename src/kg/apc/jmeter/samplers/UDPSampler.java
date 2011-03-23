package kg.apc.jmeter.samplers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
public class UDPSampler extends AbstractIPSampler implements UDPTrafficEncoder, ThreadListener {

    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String ENCODECLASS = "encodeclass";
    public static final String WAITRESPONSE = "waitresponse";
    private DatagramChannel channel;
    private UDPTrafficEncoder encoder;

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
        return ByteBuffer.wrap(data.getBytes());
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
            if (log.isDebugEnabled()) {
                log.debug("read " + cnt + " bytes");
            }
            res.latencyEnd();
            //log.debug("Read " + recvBuf.toString());
            recvBuf.flip();
            byte[] bytes = new byte[cnt];
            recvBuf.get(bytes);
            response.write(bytes);
            recvBuf.clear();
        }
        res.sampleEnd();

        log.debug("Resp " + response.toString());
        return encoder.decode(response.toByteArray());
    }

    public void threadStarted() {
        try {
            channel = (DatagramChannel) getChannel();
        } catch (IOException ex) {
            log.error("Cannot open channel", ex);
        }

        /*
        if (isWaitResponse()) {
        try {
        ((DatagramChannel) channel).socket().bind(new InetSocketAddress(getHostName(), getPortAsInt()));
        } catch (SocketException ex) {
        log.error("Cannot bind socket", ex);
        }
        }
         * 
         */

        try {
            Class<?> c = Thread.currentThread().getContextClassLoader().loadClass(getEncoderClass());
            Object o = c.newInstance();
            if (!(o instanceof UDPTrafficEncoder)) {
                throw new ClassNotFoundException("Class does not implement " + UDPTrafficEncoder.class.getCanonicalName());
            }
            encoder = (UDPTrafficEncoder) o;
            log.debug("Using decoder: " + encoder);
        } catch (Exception ex) {
            log.error("Problem loading encoder " + getEncoderClass() + ", raw data will be used", ex);
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
