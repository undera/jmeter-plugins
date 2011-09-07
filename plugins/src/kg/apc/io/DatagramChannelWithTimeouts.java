package kg.apc.io;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class DatagramChannelWithTimeouts extends DatagramChannel {

    protected DatagramChannel channel;
    protected Selector selector;
    private long readTimeout = 10000;
    protected SelectionKey channelKey;
    private static final Logger log = LoggingManager.getLoggerForClass();
    private boolean fastFirstPacketRead;

    protected DatagramChannelWithTimeouts() throws IOException {
        super(null);
        log.debug("Creating DatagramChannel");
        selector = Selector.open();
        channel = DatagramChannel.open();
        channel.configureBlocking(false);
        channelKey = channel.register(selector, SelectionKey.OP_READ);
    }

    public static DatagramChannel open() throws IOException {
        return new DatagramChannelWithTimeouts();
    }

    public int read(ByteBuffer dst) throws IOException {
        int bytesRead = 0;
        while (selector.select(readTimeout) > 0) {
            if (log.isDebugEnabled()) {
                log.debug("Loop " + bytesRead);
            }
            // damn NPE in unit tests...
            if (selector.selectedKeys() != null) {
                selector.selectedKeys().remove(channelKey);
            }
            int cnt = channel.read(dst);
            if (cnt < 1) {
                if (bytesRead < 1) {
                    bytesRead = -1;
                }
                return bytesRead;
            } else {
                bytesRead += cnt;
                if (!fastFirstPacketRead) {
                    fastFirstPacketRead = true;
                    return bytesRead;
                }
            }
        }

        if (bytesRead < 1) {
            throw new SocketTimeoutException("Timeout exceeded while reading from socket");
        }
        return bytesRead;
    }

    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int write(ByteBuffer src) throws IOException {
        fastFirstPacketRead = false;
        int res = 0;
        int size = src.remaining();
        while (res < size) {
            res += channel.write(src);
        }
        return res;
    }

    public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected void implCloseSelectableChannel() throws IOException {
        channel.close();
        selector.close();
    }

    protected void implConfigureBlocking(boolean block) throws IOException {
        throw new UnsupportedOperationException("This class is blocking implementation of SocketChannel");
    }

    public boolean isConnected() {
        return channel.isConnected();
    }

    public void setReadTimeout(int t) {
        readTimeout = t;
    }

    public DatagramSocket socket() {
        return channel.socket();
    }

    public DatagramChannel disconnect() throws IOException {
        return channel.disconnect();
    }

    public SocketAddress receive(ByteBuffer dst) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int send(ByteBuffer src, SocketAddress target) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public DatagramChannel connect(SocketAddress remote) throws IOException {
        return channel.connect(remote);
    }
}
