package kg.apc.perfmon.client;

import junit.framework.TestCase;

/**
 *
 * @author undera
 */
public class UDPTransportTest extends TestCase {

    private UDPTransport instance;

    public UDPTransportTest() {
    }

    public static void setUpClass() throws Exception {
    }

    public static void tearDownClass() throws Exception {
    }

    public void setUp() {
        instance = new UDPTransport("", 0);
    }

    public void tearDown() {
    }

    /**
     * Test of disconnect method, of class UDPTransport.
     */
    public void testDisconnect() {
        System.out.println("disconnect");
        instance.disconnect();
    }

    /**
     * Test of writeln method, of class UDPTransport.
     */
    public void testWriteln() {
        System.out.println("writeln");
        String string = "";
        instance.writeln(string);
    }

    /**
     * Test of readln method, of class UDPTransport.
     */
    public void testReadln() {
        System.out.println("readln");
        String expResult = "";
        String result = instance.readln();
        assertEquals(expResult, result);
    }
}
