package kg.apc.perfmon.client;

import junit.framework.TestCase;

/**
 *
 * @author undera
 */
public class TCPTransportTest extends TestCase {

    private TCPTransport instance;

    public TCPTransportTest() {
    }

    public static void setUpClass() throws Exception {
    }

    public static void tearDownClass() throws Exception {
    }

    public void setUp() {
        instance = new TCPTransport("", 0);
    }

    public void tearDown() {
    }

    /**
     * Test of disconnect method, of class TCPTransport.
     */
    public void testDisconnect() {
        System.out.println("disconnect");
        instance.disconnect();
    }

    /**
     * Test of writeln method, of class TCPTransport.
     */
    public void testWriteln() {
        System.out.println("writeln");
        String string = "";
        instance.writeln(string);
    }

    /**
     * Test of readln method, of class TCPTransport.
     */
    public void testReadln() {
        System.out.println("readln");
        String expResult = "";
        String result = instance.readln();
        assertEquals(expResult, result);
    }
}
