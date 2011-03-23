package kg.apc.jmeter.samplers;

import java.beans.Encoder;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.logging.Level;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class UDPSampler extends AbstractSampler implements TestListener, UDPTrafficEncoder {

    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String HOSTNAME = "hostname";
    public static final String PORT = "port";
    public static final String ENCODECLASS = "encodeclass";
    public static final String TIMEOUT = "timeout";
    public static final String WAITRESPONSE = "waitresponse";
    public static final String DATA = "data";
    private DatagramChannel channel;
    private UDPTrafficEncoder encoder;

    public SampleResult sample(Entry entry) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public final String getHostName() {
        return getPropertyAsString(HOSTNAME);
    }

    public void setHostName(String text) {
        setProperty(HOSTNAME, text);
    }

    public String getPort() {
        return getPropertyAsString(PORT);
    }

    public String getTimeout() {
        return getPropertyAsString(TIMEOUT);
    }

    public boolean isWaitResponse() {
        return getPropertyAsBoolean(WAITRESPONSE);
    }

    public String getEncoderClass() {
        return getPropertyAsString(ENCODECLASS);
    }

    public String getRequestData() {
        return getPropertyAsString(DATA);
    }

    public void setPort(String text) {
        setProperty(PORT, text);
    }

    public void setWaitResponse(boolean selected) {
        setProperty(WAITRESPONSE, selected);
    }

    public void setTimeout(String text) {
        setProperty(TIMEOUT, text);
    }

    public void setRequestData(String text) {
        setProperty(DATA, text);
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
            log.error("Problem loading encoder " + getEncoderClass(), ex);
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected DatagramChannel getChannel() throws IOException {
        DatagramChannel c;
        if (isWaitResponse()) {
            c = DatagramChannelWithTimeouts.open();
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

    public String decode(ByteBuffer data) {
        return JMeterPluginsUtils.byteBufferToString(data);
    }
}
