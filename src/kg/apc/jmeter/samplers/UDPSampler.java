package kg.apc.jmeter.samplers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class UDPSampler extends AbstractIPSampler implements TestListener, UDPTrafficEncoder {

    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String ENCODECLASS = "encodeclass";
    public static final String WAITRESPONSE = "waitresponse";
    private AbstractSelectableChannel channel;
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

    public void testStarted() {
        try {
            channel = getChannel();
        } catch (IOException ex) {
            log.error("Cannot open channel", ex);
        }

        try {
            Object e = Thread.currentThread().getContextClassLoader().loadClass(getEncoderClass());
            if (!(e instanceof UDPTrafficEncoder)) {
                throw new ClassNotFoundException("Class does not implement " + UDPTrafficEncoder.class.getCanonicalName());
            }
            encoder = (UDPTrafficEncoder) e;
        } catch (ClassNotFoundException ex) {
            log.error("Problem loading encoder " + getEncoderClass() + ", raw data will be used", ex);
            encoder = this;
        }
    }

    public void testStarted(String string) {
        testStarted();
    }

    public void testEnded() {
        try {
            channel.close();
        } catch (IOException ex) {
            log.error("Cannot close channel", ex);
        }

    }

    public void testEnded(String string) {
        testEnded();
    }

    public void testIterationStart(LoopIterationEvent lie) {
    }

    protected AbstractSelectableChannel getChannel() throws IOException {
        DatagramChannel c;
        if (isWaitResponse()) {
            c = DatagramChannelWithTimeouts.open();
            (( DatagramChannelWithTimeouts)c).setReadTimeout(getTimeoutAsInt());
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

    public byte[] decode(ByteBuffer buf) {
        ByteBuffer str = buf.duplicate();
        str.rewind();
        byte[] dst = new byte[str.limit()];
        str.get(dst);
        return dst;
    }

    @Override
    protected byte[] processIO(SampleResult res) throws Exception {
        ByteBuffer sendBuf = ByteBuffer.wrap(getRequestData().getBytes()); // cannot cache it because of variable processing
        DatagramChannel sock = (DatagramChannel) getChannel();
        sock.write(sendBuf);

        if (!isWaitResponse())
        {
            res.latencyEnd();
            res.sampleEnd();
            return new byte[0];
        }

        ByteArrayOutputStream response = new ByteArrayOutputStream();
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

        return response.toByteArray();
    }

}
