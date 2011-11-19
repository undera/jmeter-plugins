package kg.apc.perfmon.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import kg.apc.perfmon.PerfMonMetricGetter;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
class NIOTransport extends AbstractTransport {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private ReadableByteChannel readChannel;
    private WritableByteChannel writeChannel;

    public NIOTransport() throws IOException {
        super();
    }

    public void setChannels(ReadableByteChannel reader, WritableByteChannel writer) {
        readChannel = reader;
        writeChannel = writer;
    }

    public void disconnect() {
        super.disconnect();
        try {
            if (readChannel.isOpen()) {
                readChannel.close();
            }
            if (writeChannel.isOpen()) {
                writeChannel.close();
            }
        } catch (IOException ex) {
            log.error("Error closing transport", ex);
        }
    }

    public void writeln(String line) throws IOException {
        writeChannel.write(ByteBuffer.wrap(line.concat(PerfMonMetricGetter.NEWLINE).getBytes()));
    }

    public String readln() {
        ByteBuffer buf = ByteBuffer.allocateDirect(4096); // FIXME: magic constants are bad
        int nlCount = 0;

        try {
            readChannel.read(buf);
            buf.flip();
            while (buf.position() < buf.limit()) {
                byte b = buf.get();
                pos.write(b);
                if (b == '\n') {
                    nlCount++;
                }
            }

            return getNextLine(nlCount);
        } catch (IOException e) {
            log.error("Problem reading next line", e);
            return "";
        }
    }
}
