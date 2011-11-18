package kg.apc.perfmon.client;

import java.io.IOException;
import java.net.SocketAddress;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class TransportFactory {
    private static final Logger log = LoggingManager.getLoggerForClass();

    public AbstractTransport getTransport(SocketAddress addr) throws IOException {
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
}
