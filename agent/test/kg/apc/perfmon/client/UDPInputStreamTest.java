package kg.apc.perfmon.client;

import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author undera
 */
public class UDPInputStreamTest extends TestCase {

    public UDPInputStreamTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(UDPInputStreamTest.class);
        return suite;
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of read method, of class UDPInputStream.
     */
    public void testRead() throws Exception {
        System.out.println("read");
        DatagramSocket sock = new DatagramSocket();
        sock.setSoTimeout(100);
        UDPInputStream instance = new UDPInputStream(sock);
        int expResult = 0;
        try {
            int result = instance.read();
            assertEquals(expResult, result);
        } catch (SocketTimeoutException e) {
        }
    }
}
