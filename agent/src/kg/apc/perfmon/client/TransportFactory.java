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
 * Factory used to obtain instances of PerfMon Agent Connections (Transports)
 * @author undera
 * @see Transport
 */
public class TransportFactory {

    private static final Logger log = LoggingManager.getLoggerForClass();

    /**
     * Primary transport getting method. Tries to connect and test UDP Transport.
     * If UDP failed, tries TCP transport. If it fails, too, throws IOException
     * @param addr
     * @return Transport instance
     * @throws IOException 
     */
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

    /**
     * @deprecated because of instability
     * @param addr 
     * @return
     * @throws IOException 
     */
    public static Transport NIOUDPInstance(SocketAddress addr) throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        channel.connect(addr);

        NIOTransport ret = new NIOTransport();
        ret.setChannels(channel, channel);
        return ret;
    }

    /**
     * @deprecated because of instability
     * @param addr
     * @return
     * @throws IOException 
     */
    public static Transport NIOTCPInstance(SocketAddress addr) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.connect(addr);
        NIOTransport ret = new NIOTransport();
        ret.setChannels(channel, channel);
        return ret;
    }

    /**
     * Returns TCP transport instance, connected to specified address
     * @param addr
     * @return Transport instance
     * @throws IOException 
     */
    public static Transport TCPInstance(SocketAddress addr) throws IOException {
        Socket sock = new Socket();
        sock.setSoTimeout(getTimeout());
        sock.connect(addr);

        StreamTransport trans = new StreamTransport();
        trans.setStreams(sock.getInputStream(), sock.getOutputStream());
        trans.setAddressLabel(addr.toString());
        return trans;
    }

    private static int getTimeout() {
        return 500;
    }

    /**
     * Returns new UDP Transport instance
     * connected to specified socket address
     * @param addr
     * @return connected Transport
     * @throws IOException 
     */
    public static Transport UDPInstance(SocketAddress addr) throws IOException {
        DatagramSocket sock = new DatagramSocket();
        sock.setSoTimeout(getTimeout());
        sock.connect(addr);

        StreamTransport trans = new StreamTransport();
        trans.setStreams(new UDPInputStream(sock), new UDPOutputStream(sock));
        trans.setAddressLabel(addr.toString());
        return trans;
    }
}
