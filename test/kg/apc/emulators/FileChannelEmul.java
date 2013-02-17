package kg.apc.emulators;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class FileChannelEmul extends FileChannel {

    private ByteBuffer writtenBytes;
    private ByteBuffer bytesToRead;
    private static final Logger log = LoggingManager.getLoggerForClass();

    public FileChannelEmul() {
        super();
        log.debug("Creating file channel emulator");
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
        return JMeterPluginsUtils.byteBufferToString(src);
    }

    @Override
    public long position() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FileChannel position(long newPosition) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long size() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FileChannel truncate(long size) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void force(boolean metaData) throws IOException {
        log.debug("Force flush");
    }

    @Override
    public long transferTo(long position, long count, WritableByteChannel target) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long transferFrom(ReadableByteChannel src, long position, long count) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int read(ByteBuffer dst, long position) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int write(ByteBuffer src, long position) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MappedByteBuffer map(MapMode mode, long position, long size) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FileLock lock(long position, long size, boolean shared) throws IOException {
        return new FileLockEmul();
    }

    @Override
    public FileLock tryLock(long position, long size, boolean shared) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void implCloseChannel() throws IOException {
        log.debug("Emulating close file channel");
    }
}
