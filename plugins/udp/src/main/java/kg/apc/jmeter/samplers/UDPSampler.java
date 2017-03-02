package kg.apc.jmeter.samplers;

import kg.apc.io.DatagramChannelWithTimeouts;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.spi.AbstractSelectableChannel;

public class UDPSampler extends AbstractIPSampler implements UDPTrafficDecoder, ThreadListener {

    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String ENCODECLASS = "encodeclass";
    public static final String WAITRESPONSE = "waitresponse";
    public static final String CLOSECHANNEL = "closechannel";
    public static final String BIND_ADDRESS = "bind_address";
    public static final String BIND_PORT = "bind_port";
    private DatagramChannel channel;
    private UDPTrafficDecoder encoder;

    public boolean isWaitResponse() {
        return getPropertyAsBoolean(WAITRESPONSE);
    }

    public boolean isCloseChannel() {
        return getPropertyAsBoolean(CLOSECHANNEL);
    }

    public String getEncoderClass() {
        return getPropertyAsString(ENCODECLASS);
    }

    public void setWaitResponse(boolean selected) {
        setProperty(WAITRESPONSE, selected);
    }

    public void setCloseChannel(boolean selected) {
        setProperty(CLOSECHANNEL, selected);
    }

    public void setEncoderClass(String text) {
        setProperty(ENCODECLASS, text);
    }

    public String getBindAddress() {
        return getPropertyAsString(BIND_ADDRESS);
    }

    public void setBindAddress(String text) {
        setProperty(BIND_ADDRESS, text);
    }

    public String getBindPort() {
        return getPropertyAsString(BIND_PORT);
    }

    public int getBindPortAsInt() {
        return getPropertyAsInt(BIND_PORT);
    }

    public void setBindPort(String text) {
        setProperty(BIND_PORT, text);
    }

    @Override
    protected AbstractSelectableChannel getChannel() throws IOException {
        DatagramChannel c;
        if (isWaitResponse()) {
            c = DatagramChannelWithTimeouts.open();
            ((DatagramChannelWithTimeouts) c).setReadTimeout(getTimeoutAsInt());
        } else {
            c = DatagramChannel.open();
        }

        String bindAddress = getBindAddress();
        if (bindAddress.isEmpty()) {
            bindAddress = "0.0.0.0";
        }
        int adr = getBindPortAsInt();
        c.bind(new InetSocketAddress(bindAddress, adr));

        int port = Integer.parseInt(getPort());
        c.connect(new InetSocketAddress(getHostName(), port));
        return c;
    }

    @Override
    public ByteBuffer encode(String data) {
        try {
            return ByteBuffer.wrap(data.getBytes("Windows-1252"));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public byte[] decode(byte[] buf) {
        return buf;
    }

    @Override
    protected byte[] processIO(SampleResult res) throws Exception {
        connect(res);
        send();

        if (!isWaitResponse()) {
            return noResponseFinish(res);
        } else {
            return readResponseFinish(res);
        }
    }

    private byte[] readResponseFinish(SampleResult res) throws IOException {
        try {
            ByteArrayOutputStream response = readResponse(res);

            if (isCloseChannel()) {
                channel.close();
            }

            return encoder.decode(response.toByteArray());
        } catch (IOException ex) {
            channel.close();
            throw ex;
        }
    }

    private byte[] noResponseFinish(SampleResult res) throws IOException {
        res.latencyEnd();
        res.sampleEnd();

        if (isCloseChannel()) {
            channel.close();
        }

        return new byte[0];
    }

    private void send() throws IOException {
        ByteBuffer sendBuf = encoder.encode(getRequestData());

        while (sendBuf.remaining() > 0) {
            channel.write(sendBuf);
        }
    }

    private void connect(SampleResult res) {
        if (channel == null || !channel.isOpen()) {
            try {
                channel = (DatagramChannel) getChannel();
            } catch (IOException ex) {
                log.error("Cannot open channel", ex);
            }
        }
        res.connectEnd();
    }

    private ByteArrayOutputStream readResponse(SampleResult res) throws IOException {
        ByteArrayOutputStream response = new ByteArrayOutputStream();
        int cnt;
        ByteBuffer recvBuf = getRecvBuf();
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
        res.latencyEnd();
        res.sampleEnd();
        res.setBytes(response.size());
        return response;
    }

    @Override
    public void threadStarted() {
        try {
            Class<?> c = Thread.currentThread().getContextClassLoader().loadClass(getEncoderClass());
            Object o = c.newInstance();
            if (!(o instanceof UDPTrafficDecoder)) {
                throw new ClassNotFoundException("Class does not implement " + UDPTrafficDecoder.class.getCanonicalName());
            }
            encoder = (UDPTrafficDecoder) o;
            log.debug("Using decoder: " + encoder);
        } catch (Exception ex) {
            if (!getEncoderClass().isEmpty()) {
                log.warn("Problem loading encoder " + getEncoderClass() + ", raw data will be used", ex);
            }
            encoder = this;
        }
    }

    @Override
    public void threadFinished() {
        try {
            if (channel != null) {
                channel.close();
            }
        } catch (IOException ex) {
            log.error("Cannot close channel", ex);
        }
    }

    @Override
    public boolean interrupt() {
        if (channel != null && channel.isOpen()) {
            try {
                channel.close();
            } catch (IOException ex) {
                log.warn("Exception while interrupting channel: ", ex);
                return false;
            }
        }
        return true;
    }
}
