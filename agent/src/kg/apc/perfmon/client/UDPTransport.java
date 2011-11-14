package kg.apc.perfmon.client;

import java.io.IOException;
import java.net.SocketAddress;

/**
 *
 * @author undera
 */
class UDPTransport extends AbstractTransport {

    public UDPTransport(SocketAddress addr) throws IOException {
        super(addr);
    }

    public void disconnect() {
    }

    public void writeln(String string) {
    }

    public String readln() {
        return "";
    }

    public boolean test() {
        return false;
    }
}
