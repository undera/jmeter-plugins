package kg.apc.perfmon.client;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author undera
 */
public class TransportFactoryTest extends TestCase {

    public TransportFactoryTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(TransportFactoryTest.class);
        return suite;
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
        SocketAddress addr = new InetSocketAddress("localhost", 4445);
        try {
            Transport result = TransportFactory.getTransport(addr);
            fail();
        } catch (IOException e) {
        }
    }

    /**
     * Test of UDPInstance method, of class TransportFactory.
     */
    public void testUDPInstance() throws Exception {
        System.out.println("UDPInstance");
        SocketAddress addr = new InetSocketAddress("localhost", 4445);
        Transport result = TransportFactory.UDPInstance(addr);
        assertEquals(false, result.test());
    }

    /**
     * Test of TCPInstance method, of class TransportFactory.
     */
    public void testTCPInstance() throws Exception {
        System.out.println("TCPInstance");
        SocketAddress addr = new InetSocketAddress("localhost", 4445);
        try {
            Transport result = TransportFactory.TCPInstance(addr);
            fail();
        } catch (IOException e) {
        }
    }

    /**
     * Test of NIOUDPInstance method, of class TransportFactory.
     */
    public void testNIOUDPInstance() throws Exception {
        System.out.println("NIOUDPInstance");
        SocketAddress addr = new InetSocketAddress("localhost", 4445);
        Transport result = TransportFactory.NIOUDPInstance(addr);
        assertNotNull(result);
    }

    /**
     * Test of NIOTCPInstance method, of class TransportFactory.
     */
    public void testNIOTCPInstance() throws Exception {
        System.out.println("NIOTCPInstance");
        SocketAddress addr = new InetSocketAddress("localhost", 4445);
        try {
            Transport result = TransportFactory.NIOTCPInstance(addr);
            fail();
        } catch (ConnectException e) {
        }
    }

    /*
    public void testAll() throws Exception {
        Transport trans = TransportFactory.getTransport(new InetSocketAddress("localhost", 4444));
        assertTrue(trans.test());
        trans.writeln("metrics: cpu\n");
        String res=trans.readln();
        assertNotNull(res);
        assertTrue(Double.parseDouble(res)>0);
        trans.disconnect();
    }
     * 
     */
}
