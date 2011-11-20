package kg.apc.perfmon.client;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
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

    public static Transport getTransport(SocketAddress addr) throws IOException {
        Transport trans;
        try {
            log.debug("Connecting UDP");
            trans = UDPInstance(addr);
            if (!trans.test()) {
                throw new IOException("Agent is unreachable via UDP");
            }
            return trans;
        } catch (IOException e) {
            log.info("Can't connect UDP transport for host: " + addr.toString(), e);
            try {
                log.debug("Connecting TCP");
                trans = TCPInstance(addr);
                if (!trans.test()) {
                    throw new IOException("Agent is unreachable via TCP");
                }
                return trans;
            } catch (IOException ex) {
                log.info("Can't connect TCP transport for host: " + addr.toString(), ex);
                throw ex;
            }
        }
    }

    public static Transport NIOUDPInstance(SocketAddress addr) throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        channel.connect(addr);

        NIOTransport ret = new NIOTransport();
        ret.setChannels(channel, channel);
        return ret;
    }

    public static Transport NIOTCPInstance(SocketAddress addr) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.connect(addr);
        NIOTransport ret = new NIOTransport();
        ret.setChannels(channel, channel);
        return ret;
    }

    public static Transport TCPInstance(SocketAddress addr) throws IOException {
        Socket sock = new Socket();
        sock.setSoTimeout(getTimeout());
        sock.connect(addr);

        StreamTransport trans = new StreamTransport();
        trans.setStreams(sock.getInputStream(), sock.getOutputStream());
        return trans;
    }

    private static int getTimeout() {
        return 2000;
    }

    public static Transport UDPInstance(SocketAddress addr) throws IOException {
        DatagramSocket sock = new DatagramSocket();
        sock.setSoTimeout(getTimeout());
        sock.connect(addr);

        StreamTransport trans = new StreamTransport();
        trans.setStreams(new UDPInputStream(sock), new UDPOutputStream(sock));
        return trans;
    }
}
