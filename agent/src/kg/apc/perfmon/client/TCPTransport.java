package kg.apc.perfmon.client;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import kg.apc.perfmon.PerfMonMetricGetter;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
class TCPTransport extends AbstractTransport {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private SocketChannel channel;

    public TCPTransport(SocketAddress addr) throws IOException {
        super(addr);
        channel = SocketChannel.open(addr);
    }

    public void disconnect() {
        try {
            writeln("exit");
            channel.close();
        } catch (IOException ex) {
            log.error("Problems closing connection", ex);
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected void writeln(String line) throws IOException {
        channel.configureBlocking(true);
        channel.write(ByteBuffer.wrap(line.concat("\n").getBytes()));
    }

    protected String readln() {
        // todo: make proper reading with buffer
        ByteBuffer buf = ByteBuffer.allocateDirect(4096); // FIXME: magic constants are bad

        try {
            channel.configureBlocking(false);
            channel.read(buf);
        } catch (IOException e) {
            return null;
        }

        buf.flip();
        return byteBufferToString(buf);
    }
}
