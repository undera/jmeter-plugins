package kg.apc.jmeter.perfmon.client;

/**
 *
 * @author undera
 */
public class UDPTransport implements AbstractTransport {

    public UDPTransport(String host, int port) {
    }

    public void disconnect() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void writeln(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String readln() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
