package kg.apc.emulators;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.logging.Level;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class DatagramChannelEmul extends DatagramChannel {

    private ByteBuffer writtenBytes;
    private ByteBuffer bytesToRead;
    private static final Logger log = LoggingManager.getLoggerForClass();
    private DatagramSocket socket;

    public DatagramChannelEmul() {
        super(null);
        try {
            socket = new DatagramSocketEmulator();
        } catch (SocketException ex) {
            java.util.logging.Logger.getLogger(DatagramChannelEmul.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static DatagramChannel open() throws IOException {
        return new DatagramChannelEmul();
    }

    @Override
    public DatagramSocket socket() {
        return socket;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        if (bytesToRead == null || bytesToRead.remaining() == 0) {
            log.debug("No more data to read");
            return -1;
        }
        int cnt = dst.capacity()<bytesToRead.capacity()?dst.capacity():bytesToRead.capacity();
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
        log.debug("Emulating write: " + getString(src));
        writtenBytes = src;
        return src.capacity();
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
        log.debug("Configure blocking: "+block);
    }

    /**
     * @return the writtenBytes
     */
    public ByteBuffer getWrittenBytes() {
        ByteBuffer res = writtenBytes;
        writtenBytes = null;
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
        return src.toString();
    }

    @Override
    public DatagramChannel connect(SocketAddress remote) throws IOException {
        return this;
    }

    @Override
    public DatagramChannel disconnect() throws IOException {
        return this;
    }

    @Override
    public SocketAddress receive(ByteBuffer dst) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int send(ByteBuffer src, SocketAddress target) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
