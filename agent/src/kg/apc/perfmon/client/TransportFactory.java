package kg.apc.perfmon.client;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class TransportFactory {

    private static final Logger log = LoggingManager.getLoggerForClass();

    public NIOTransport getTransport(SocketAddress addr) throws IOException {
        NIOTransport trans;
        try {
            log.debug("Connecting UDP");
            trans = UDPInstance(addr);
            if (!trans.test()) {
                throw new IOException();
            }
            return trans;
        } catch (IOException e) {
            log.info("Can't connect UDP transport for host: " + addr.toString(), e);
            try {
                log.debug("Connecting TCP");
                trans = TCPInstance(addr);
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

    public static NIOTransport UDPInstance(SocketAddress addr) throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        channel.socket().setSoTimeout(1000);
        channel.connect(addr);

        NIOTransport ret = new NIOTransport();
        ret.setChannels(channel, channel);
        return ret;
    }

    private NIOTransport TCPInstance(SocketAddress addr) throws IOException {
        SocketChannel channel = SocketChannel.open(addr);
        NIOTransport ret = new NIOTransport();
        ret.setChannels(channel, channel);
        return ret;
    }
}
