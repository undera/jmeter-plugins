package kg.apc.perfmon.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import kg.apc.emulators.DatagramSocketEmulator;

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
        DatagramSocketEmulator emul = new DatagramSocketEmulator();
        DatagramSocket sock = emul;
        sock.setSoTimeout(100);
        UDPInputStream instance = new UDPInputStream(sock);

        emul.setDatagramToReceive(new DatagramPacket("\r\n".getBytes(), 1));
        int expResult = '\r';
        int result = instance.read();
        assertEquals(expResult, result);

        int expResult2 = '\n';
        int result2 = instance.read();
        assertEquals(expResult2, result2);

        int expResult3 = ' ';
        emul.setDatagramToReceive(new DatagramPacket(" ".getBytes(), 1));
        int result3 = instance.read();
        assertEquals(expResult3, result3);
    }
}
