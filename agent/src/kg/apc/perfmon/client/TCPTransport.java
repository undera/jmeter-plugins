package kg.apc.perfmon.client;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
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
        channel.configureBlocking(true);
    }

    public void disconnect() {
        super.disconnect();

        try {
            channel.close();
        } catch (IOException ex) {
            log.error("Problems closing TCP connection", ex);
        }
    }

    protected void writeln(String line) throws IOException {
        channel.write(ByteBuffer.wrap(line.concat("\n").getBytes()));
    }

    protected String readln() {
        // todo: make proper reading with buffer
        ByteBuffer buf = ByteBuffer.allocateDirect(4096); // FIXME: magic constants are bad

        try {
            channel.read(buf);
            buf.flip();
            return readln(buf);
        } catch (IOException e) {
            return null;
        }
    }
}
