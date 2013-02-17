package kg.apc.io;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * SocketChannel with timeouts.
 * This class performs blocking operations for connect and IO.
 * Make note that some of methods are not implemeted yet.
 * Also selector usage kills scalability
 * @author apc@apc.kg
 */
public class SocketChannelWithTimeouts extends SocketChannel {

    protected SocketChannel socketChannel;
    protected Selector selector;
    private long connectTimeout = 5000;
    private long readTimeout = 10000;
    protected SelectionKey channelKey;
    private static final Logger log = LoggingManager.getLoggerForClass();
    private boolean fastFirstPacketRead;

    protected SocketChannelWithTimeouts() throws IOException {
        super(null);
        log.debug("Creating socketChannel");
        selector = Selector.open();
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        channelKey = socketChannel.register(selector, SelectionKey.OP_CONNECT);
    }

    public static SocketChannel open() throws IOException {
        return new SocketChannelWithTimeouts();
    }

    @Override
    public boolean connect(SocketAddress remote) throws IOException {

        long start = System.currentTimeMillis();
        //log.debug("trying to connect");
        socketChannel.connect(remote);
        while (selector.select(connectTimeout) > 0) {
            selector.selectedKeys().remove(channelKey);
            //log.debug("selected connect");
            //log.debug("Spent " + (System.currentTimeMillis() - start));
            if (!channelKey.isConnectable()) {
                throw new IllegalStateException("Socket channel is in not connectable state");
            }

            socketChannel.finishConnect();
            channelKey = socketChannel.register(selector, SelectionKey.OP_READ);
            if (log.isDebugEnabled()) {
                log.debug("Connected socket in " + (System.currentTimeMillis() - start));
            }
            if (!socketChannel.isConnected()) {
                throw new SocketException("SocketChannel not connected on some reason");
            }
            return true;
        }
        //log.debug("Spent " + (System.currentTimeMillis() - start));
        throw new SocketTimeoutException("Failed to connect to " + remote.toString());
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        int bytesRead = 0;
        while (selector.select(readTimeout) > 0) {
            selector.selectedKeys().remove(channelKey);
            int cnt = socketChannel.read(dst);
            if (cnt < 1) {
                if (bytesRead < 1) {
                    bytesRead = -1;
                }
                //log.info("Bytes read: "+bytesRead);
                return bytesRead;
            } else {
                bytesRead += cnt;
                if (!fastFirstPacketRead) {
                    fastFirstPacketRead = true;
                    return bytesRead;
                }
            }
        }

        throw new SocketTimeoutException("Timeout exceeded while reading from socket");
    }

    @Override
    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        fastFirstPacketRead = false;
        int res = 0;
        int size = src.remaining();
        while (res < size) {
            res += socketChannel.write(src);
        }
        return res;
    }

    @Override
    public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void implCloseSelectableChannel() throws IOException {
        socketChannel.close();
        selector.close();
    }

    @Override
    protected void implConfigureBlocking(boolean block) throws IOException {
        throw new UnsupportedOperationException("This class is blocking implementation of SocketChannel");
    }

    @Override
    public Socket socket() {
        return socketChannel.socket();
    }

    @Override
    public boolean isConnected() {
        return socketChannel.isConnected();
    }

    @Override
    public boolean isConnectionPending() {
        return socketChannel.isConnectionPending();
    }

    @Override
    public boolean finishConnect() throws IOException {
        return socketChannel.finishConnect();
    }

    public void setConnectTimeout(int t) {
        connectTimeout = t;
    }

    public void setReadTimeout(int t) {
        readTimeout = t;
    }
}
