package kg.apc.perfmon.client;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import kg.apc.perfmon.PerfMonMetricGetter;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public abstract class AbstractTransport {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private final PipedOutputStream pos;
    private final PipedInputStream pis;

    public AbstractTransport(SocketAddress addr) throws IOException {
        pos = new PipedOutputStream();
        pis = new PipedInputStream(pos, 256 * 1024);
    }

    public void disconnect() {
        try {
            writeln("exit");
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

    protected abstract void writeln(String line) throws IOException;

    protected abstract String readln();

    protected String readln(ByteBuffer buf) throws IOException {
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
            return null;
        }

        StringBuilder str = new StringBuilder();
        int b;
        while (pis.available() > 0) {
            b = pis.read();
            if (b == -1) {
                return null;
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

        return null;
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
