package kg.apc.perfmon.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import junit.framework.TestCase;

/**
 *
 * @author undera
 */
public class UDPTransportTest extends TestCase {

    public UDPTransportTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of disconnect method, of class UDPTransport.
     */
    public void testDisconnect() throws IOException {
        System.out.println("disconnect");
        UDPTransport instance = new UDPTransport(new InetSocketAddress("localhost", 4444));
        instance.disconnect();
    }

    /**
     * Test of test method, of class UDPTransport.
     */
    public void testTest() throws IOException {
        System.out.println("test");
        UDPTransport instance = new UDPTransport(new InetSocketAddress("localhost", 4444));
        boolean expResult = true;
        boolean result = instance.test();
        assertEquals(expResult, result);
    }

    /**
     * Test of startWithMetrics method, of class UDPTransport.
     */
    public void testStartWithMetrics() throws Exception {
        System.out.println("startWithMetrics");
        String[] metricsArray = {"cpu"};
        UDPTransport instance = new UDPTransport(new InetSocketAddress("localhost", 4444));
        instance.startWithMetrics(metricsArray);
    }

    /**
     * Test of readMetrics method, of class UDPTransport.
     */
    public void testReadMetrics() throws IOException {
        System.out.println("readMetrics");
        UDPTransport instance = new UDPTransport(new InetSocketAddress("localhost", 4444));
        String[] metricsArray = {"cpu", "exec:echo 12"};
        instance.startWithMetrics(metricsArray);
        String[] result = instance.readMetrics();
        instance.disconnect();
        assertTrue(Double.parseDouble(result[0]) > 0);
    }

    /**
     * Test of writeln method, of class UDPTransport.
     */
    public void testWriteln() throws Exception {
        System.out.println("writeln");
        String line = "";
        UDPTransport instance = new UDPTransport(new InetSocketAddress("localhost", 4444));
        instance.writeln(line);
    }

    /**
     * Test of readln method, of class UDPTransport.
     */
    public void testReadln() throws IOException {
        System.out.println("readln");
        UDPTransport instance = new UDPTransport(new InetSocketAddress("localhost", 4444));
        String expResult = "Yep";
        instance.writeln("test");
        String result = instance.readln();
        assertEquals(expResult, result);
    }
}
