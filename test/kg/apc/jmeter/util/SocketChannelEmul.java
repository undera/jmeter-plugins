package kg.apc.jmeter.util;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class SocketChannelEmul extends SocketChannel {
    private ByteBuffer writtenBytes;
    private ByteBuffer bytesToRead;
    private static final Logger log = LoggingManager.getLoggerForClass();

    public SocketChannelEmul() {
        super(null);
    }
    

    @Override
    public Socket socket() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isConnected() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isConnectionPending() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean connect(SocketAddress remote) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean finishConnect() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        log.debug("Emulating read: "+getString(bytesToRead));
        dst.put(bytesToRead);
        bytesToRead=null;
        return dst.position();
    }

    @Override
    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        log.debug("Emulating write: "+getString(src));
        writtenBytes=src;
        return src.capacity();
    }

    @Override
    public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void implCloseSelectableChannel() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void implConfigureBlocking(boolean block) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the writtenBytes
     */
    public ByteBuffer getWrittenBytes() {
        ByteBuffer res = writtenBytes;
        writtenBytes=null;
        return res;
    }

    private String getString(ByteBuffer src) {
        if (src==null)
        {
            log.error("Null buffer!");
            return "";
        }
        return src.toString();
    }

}
