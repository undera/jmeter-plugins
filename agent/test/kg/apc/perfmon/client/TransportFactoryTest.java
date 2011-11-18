package kg.apc.perfmon.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import junit.framework.TestCase;

/**
 *
 * @author undera
 */
public class TransportFactoryTest extends TestCase {

    public TransportFactoryTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getTransport method, of class TransportFactory.
     */
    public void testGetTransport() throws Exception {
        System.out.println("getTransport");
        SocketAddress addr = new InetSocketAddress("localhost", 4444);
        try {
            NIOTransport result = TransportFactory.getTransport(addr);
            fail();
        } catch (IOException e) {
        }
    }

    /**
     * Test of UDPInstance method, of class TransportFactory.
     */
    public void testUDPInstance() throws Exception {
        System.out.println("UDPInstance");
        SocketAddress addr = new InetSocketAddress("localhost", 4444);
        NIOTransport result = TransportFactory.UDPInstance(addr);
        assertEquals(false, result.test());
    }

    /**
     * Test of TCPInstance method, of class TransportFactory.
     */
    public void testTCPInstance() throws Exception {
        System.out.println("TCPInstance");
        SocketAddress addr = new InetSocketAddress("localhost", 4444);
        try {
            NIOTransport result = TransportFactory.TCPInstance(addr);
            fail();
        } catch (IOException e) {
        }
    }
}
