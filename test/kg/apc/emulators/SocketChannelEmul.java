package kg.apc.emulators;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class SocketChannelEmul extends SocketChannel {

    private int writtenBytesCount;
    private ByteBuffer writtenBytes;
    private ByteBuffer bytesToRead;
    private static final Logger log = LoggingManager.getLoggerForClass();
    private Socket socket = new SocketEmulator();

    public SocketChannelEmul() {
        super(null);
    }

    @Override
    public Socket socket() {
        return socket;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public boolean isConnectionPending() {
        return false;
    }

    @Override
    public boolean connect(SocketAddress remote) throws IOException {
        log.debug("Emulating connect to " + remote.toString());
        return true;
    }

    @Override
    public boolean finishConnect() throws IOException {
        return true;
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        if (bytesToRead == null || bytesToRead.remaining() == 0) {
            log.debug("No more data to read");
            return -1;
        }
        int cnt = dst.capacity() < bytesToRead.capacity() ? dst.capacity() : bytesToRead.capacity();
        ByteBuffer chunk = bytesToRead.duplicate();
        if (cnt < chunk.capacity()) {
            log.debug("Setting limit to " + cnt);
            chunk.limit(cnt);
        }
        log.debug("Emulating read: " + getString(chunk));
        chunk.rewind();
        dst.put(chunk);
        bytesToRead = null;
        return cnt;
    }

    @Override
    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        log.debug("Emulating write: " + src.toString());
        //log.debug("Emulating write verbose: " + getString(src));
        writtenBytes = src.duplicate();
        int written = 0;
        while (src.hasRemaining()) {
            src.get();
            written++;
        }

        writtenBytesCount += written;

        return written;
    }

    @Override
    public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void implCloseSelectableChannel() throws IOException {
        log.debug("Close selectable channel");
    }

    @Override
    protected void implConfigureBlocking(boolean block) throws IOException {
        log.debug("Configure blocking: " + block);
    }

    /**
     * @return the writtenBytes
     */
    public ByteBuffer getWrittenBytes() {
        ByteBuffer res = writtenBytes;
        writtenBytes = null;
        getWrittenBytesCount();
        return res;
    }

    public void setBytesToRead(ByteBuffer wrap) {
        log.debug("Set bytes to read: " + getString(wrap));
        bytesToRead = wrap;
    }

    private String getString(ByteBuffer src) {
        if (src == null) {
            log.error("Null buffer!");
            return "";
        }
        return JMeterPluginsUtils.byteBufferToString(src);
    }

    /**
     * @return the writtenBytesCount
     */
    public int getWrittenBytesCount() {
        int res = writtenBytesCount;
        writtenBytesCount = 0;
        return res;
    }
}
