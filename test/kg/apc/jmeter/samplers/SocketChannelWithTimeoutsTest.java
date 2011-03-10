package kg.apc.jmeter.samplers;

import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author undera
 */
public class SocketChannelWithTimeoutsTest {


    public SocketChannelWithTimeoutsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of open method, of class SocketChannelWithTimeouts.
     */
    @Test
    public void testOpen() throws Exception {
        System.out.println("open");
        SocketChannel result = SocketChannelWithTimeoutsEmul.open();
        assertTrue(result instanceof SocketChannel);
    }

    /**
     * Test of connect method, of class SocketChannelWithTimeouts.
     */
    @Test
    public void testConnect() throws Exception {
        System.out.println("connect");
        SocketAddress remote = new InetSocketAddress("localhost", 80);
        SocketChannel instance = SocketChannelWithTimeoutsEmul.open();
        boolean expResult = false;
        boolean result = instance.connect(remote);
        assertEquals(expResult, result);
    }

    /**
     * Test of read method, of class SocketChannelWithTimeouts.
     */
    @Test
    public void testRead_ByteBuffer() throws Exception {
        System.out.println("read");
        ByteBuffer dst = null;
        SocketChannelWithTimeoutsEmul instance = null;
        int expResult = 0;
        int result = instance.read(dst);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of read method, of class SocketChannelWithTimeouts.
     */
    @Test
    public void testRead_3args() throws Exception {
        System.out.println("read");
        ByteBuffer[] dsts = null;
        int offset = 0;
        int length = 0;
        SocketChannelWithTimeoutsEmul instance = null;
        long expResult = 0L;
        long result = instance.read(dsts, offset, length);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of write method, of class SocketChannelWithTimeouts.
     */
    @Test
    public void testWrite_ByteBuffer() throws Exception {
        System.out.println("write");
        ByteBuffer src = null;
        SocketChannelWithTimeoutsEmul instance = null;
        int expResult = 0;
        int result = instance.write(src);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of write method, of class SocketChannelWithTimeouts.
     */
    @Test
    public void testWrite_3args() throws Exception {
        System.out.println("write");
        ByteBuffer[] srcs = null;
        int offset = 0;
        int length = 0;
        SocketChannelWithTimeoutsEmul instance = null;
        long expResult = 0L;
        long result = instance.write(srcs, offset, length);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of implCloseSelectableChannel method, of class SocketChannelWithTimeouts.
     */
    @Test
    public void testImplCloseSelectableChannel() throws Exception {
        System.out.println("implCloseSelectableChannel");
        SocketChannelWithTimeoutsEmul instance = null;
        instance.implCloseSelectableChannel();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of implConfigureBlocking method, of class SocketChannelWithTimeouts.
     */
    @Test
    public void testImplConfigureBlocking() throws Exception {
        System.out.println("implConfigureBlocking");
        boolean block = false;
        SocketChannelWithTimeoutsEmul instance = null;
        instance.implConfigureBlocking(block);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of socket method, of class SocketChannelWithTimeouts.
     */
    @Test
    public void testSocket() {
        System.out.println("socket");
        SocketChannelWithTimeoutsEmul instance = null;
        Socket expResult = null;
        Socket result = instance.socket();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isConnected method, of class SocketChannelWithTimeouts.
     */
    @Test
    public void testIsConnected() {
        System.out.println("isConnected");
        SocketChannelWithTimeoutsEmul instance = null;
        boolean expResult = false;
        boolean result = instance.isConnected();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isConnectionPending method, of class SocketChannelWithTimeouts.
     */
    @Test
    public void testIsConnectionPending() {
        System.out.println("isConnectionPending");
        SocketChannelWithTimeoutsEmul instance = null;
        boolean expResult = false;
        boolean result = instance.isConnectionPending();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of finishConnect method, of class SocketChannelWithTimeouts.
     */
    @Test
    public void testFinishConnect() throws Exception {
        System.out.println("finishConnect");
        SocketChannelWithTimeoutsEmul instance = null;
        boolean expResult = false;
        boolean result = instance.finishConnect();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setConnectTimeout method, of class SocketChannelWithTimeouts.
     */
    @Test
    public void testSetConnectTimeout() {
        System.out.println("setConnectTimeout");
        int t = 0;
        SocketChannelWithTimeoutsEmul instance = null;
        instance.setConnectTimeout(t);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setReadTimeout method, of class SocketChannelWithTimeouts.
     */
    @Test
    public void testSetReadTimeout() {
        System.out.println("setReadTimeout");
        int t = 0;
        SocketChannelWithTimeoutsEmul instance = null;
        instance.setReadTimeout(t);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
