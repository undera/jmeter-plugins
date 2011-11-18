package kg.apc.perfmon.client;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
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
public class NIOTransport {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private final PipedOutputStream pos;
    private final PipedInputStream pis;
    private ReadableByteChannel readChannel;
    private WritableByteChannel writeChannel;

    public NIOTransport() throws IOException {
        pos = new PipedOutputStream();
        pis = new PipedInputStream(pos, 256 * 1024);
    }

    public void setChannels(ReadableByteChannel reader, WritableByteChannel writer) {
        readChannel = reader;
        writeChannel = writer;
    }

    public void disconnect() {
        try {
            writeln("exit");
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

    public boolean test() {
        try {
            writeln("test");
        } catch (IOException ex) {
            log.error("Failed to send command", ex);
            return false;
        }
        return readln().startsWith("Yep");
    }

    public void startWithMetrics(String[] metricsArray) throws IOException {
        String cmd = "metrics:";
        for (int n = 0; n < metricsArray.length; n++) {
            cmd += metricsArray[n] + PerfMonMetricGetter.TAB;
        }

        writeln(cmd);
    }

    public String[] readMetrics() {
        String str = readln();
        return str.split(PerfMonMetricGetter.TAB);
    }

    public void writeln(String line) throws IOException {
        writeChannel.write(ByteBuffer.wrap(line.concat(PerfMonMetricGetter.NEWLINE).getBytes()));
    }

    public String readln() {
        ByteBuffer buf = ByteBuffer.allocateDirect(4096); // FIXME: magic constants are bad

        try {
            readChannel.read(buf);
            buf.flip();
            return readlnFromByteBuffer(buf);
        } catch (IOException e) {
            return "";
        }
    }

    private String readlnFromByteBuffer(ByteBuffer buf) throws IOException {
        int nlCount = 0;
        while (buf.position() < buf.limit()) {
            try {
                byte b = buf.get();
                pos.write(b);
                if (b == '\n') {
                    nlCount++;
                }
            } catch (IOException ex) {
                log.debug("Failed to read from buffer", ex);
            }
        }

        if (nlCount == 0) {
            return "";
        }

        StringBuilder str = new StringBuilder();
        int b;
        while (pis.available() > 0) {
            b = pis.read();
            if (b == -1) {
                return "";
            }

            if (b == '\n') {
                nlCount--;
                if (nlCount == 0) {
                    log.debug("Read lines: " + str.toString());
                    String[] lines = str.toString().split("\n");
                    return lines[lines.length - 1];
                }
            }

            str.append((char) b);
        }

        return "";
    }

    public void setInterval(long interval) {
        try {
            writeln("interval:" + interval);
        } catch (IOException ex) {
            log.error("Error setting interval", ex);
        }
    }

    public void shutdownAgent() {
        try {
            writeln("shutdown");
        } catch (IOException ex) {
            log.error("Error setting interval", ex);
        }
    }
}
