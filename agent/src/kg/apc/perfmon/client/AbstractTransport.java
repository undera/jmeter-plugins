package kg.apc.perfmon.client;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public abstract class AbstractTransport {

    private static final Logger log = LoggingManager.getLoggerForClass();

    public static AbstractTransport getTransport(SocketAddress addr) throws IOException {
        AbstractTransport trans;
        try {
            log.debug("Connecting UDP");
            trans = new UDPTransport(addr);
            if (!trans.test()) {
                throw new IOException();
            }
            return trans;
        } catch (IOException e) {
            log.info("Can't connect UDP transport for host: " + addr.toString(), e);
            try {
                log.debug("Connecting TCP");
                trans = new TCPTransport(addr);
                if (!trans.test()) {
                    throw new IOException();
                }
                return trans;
            } catch (IOException ex) {
                log.info("Can't connect TCP transport for host: " + addr.toString(), ex);
                throw ex;
            }
        }
    }

    public AbstractTransport(SocketAddress addr) throws IOException {
    }

    public abstract void disconnect();

    public abstract boolean test();

    public abstract void startWithMetrics(String[] metricsArray) throws IOException;

    public abstract String[] readMetrics();

    protected abstract void writeln(String line) throws IOException;

    protected abstract String readln();

    public static String byteBufferToString(ByteBuffer buf) {
        byte[] dst = byteBufferToByteArray(buf);
        return new String(dst);
    }

    public static byte[] byteBufferToByteArray(ByteBuffer buf) {
        ByteBuffer str = buf.duplicate();
        //System.err.println("Before "+str);
        str.rewind();
        //str.flip();
        //System.err.println("After "+str);
        byte[] dst = new byte[str.limit()];
        str.get(dst);
        return dst;
    }
}
