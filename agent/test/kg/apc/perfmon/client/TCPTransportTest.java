package kg.apc.perfmon.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import junit.framework.TestCase;

/**
 *
 * @author undera
 */
public class TCPTransportTest extends TestCase {

    public TCPTransportTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of disconnect method, of class TCPTransport.
     */
    public void testDisconnect() throws IOException {
        System.out.println("disconnect");
        TCPTransport instance = new TCPTransport(new InetSocketAddress("localhost", 4444));
        instance.disconnect();
    }

    /**
     * Test of test method, of class TCPTransport.
     */
    public void testTest() throws IOException {
        System.out.println("test");
        TCPTransport instance = new TCPTransport(new InetSocketAddress("localhost", 4444));
        boolean expResult = true;
        boolean result = instance.test();
        assertEquals(expResult, result);
    }

    /**
     * Test of startWithMetrics method, of class TCPTransport.
     */
    public void testStartWithMetrics() throws Exception {
        System.out.println("startWithMetrics");
        String[] metricsArray = {"cpu"};
        TCPTransport instance = new TCPTransport(new InetSocketAddress("localhost", 4444));
        instance.startWithMetrics(metricsArray);
    }

    /**
     * Test of readMetrics method, of class TCPTransport.
     */
    public void testReadMetrics() throws IOException {
        System.out.println("readMetrics");
        TCPTransport instance = new TCPTransport(new InetSocketAddress("localhost", 4444));
        String[] metricsArray = {"cpu", "exec:echo 12"};
        instance.startWithMetrics(metricsArray);
        String[] result = instance.readMetrics();
        instance.disconnect();
        assertTrue(Double.parseDouble(result[0]) > 0);
    }

    /**
     * Test of writeln method, of class TCPTransport.
     */
    public void testWriteln() throws Exception {
        System.out.println("writeln");
        String line = "";
        TCPTransport instance = new TCPTransport(new InetSocketAddress("localhost", 4444));
        instance.writeln(line);
    }

    /**
     * Test of readln method, of class TCPTransport.
     */
    public void testReadln() throws IOException {
        System.out.println("readln");
        TCPTransport instance = new TCPTransport(new InetSocketAddress("localhost", 4444));
        String expResult = "Yep";
        instance.writeln("test");
        String result = instance.readln();
        assertEquals(expResult, result);
    }
}
