package kg.apc.io;

import kg.apc.emulators.SelectionKeyEmul;
import java.util.Set;
import java.io.IOException;
import kg.apc.emulators.SelectorEmul;
import kg.apc.emulators.SocketChannelEmul;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
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

    private class SocketChannelWithTimeoutsEmul extends SocketChannelWithTimeouts {

        protected SocketChannelWithTimeoutsEmul() throws IOException {
            super();
            selector = new SelectorEmul();
            SocketChannelEmul ce = new SocketChannelEmul();
            ce.configureBlocking(false);
            ce.setBytesToRead(ByteBuffer.wrap("test".getBytes()));
            socketChannel = ce;
            channelKey=new SelectionKeyEmul();
        }

        @Override
        public boolean finishConnect() throws IOException {
            return true;
        }

        private void setSelectedKeys(Set<SelectionKey> linkedList) {
            ((SelectorEmul) selector).setSelectedKeys(linkedList);
        }
    }

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
        SocketChannel result = new SocketChannelWithTimeoutsEmul();
        assertTrue(result instanceof SocketChannel);
    }

    /**
     * Test of connect method, of class SocketChannelWithTimeouts.
     */
    @Test
    public void testConnect() throws Exception {
        System.out.println("connect");
        SocketAddress remote = new InetSocketAddress("localhost", 80);
        SocketChannelWithTimeoutsEmul instance = new SocketChannelWithTimeoutsEmul();
        instance.setSelectedKeys(new HashSet<SelectionKey>());
        boolean expResult = true;
        boolean result = instance.connect(remote);
        assertEquals(expResult, result);
    }

    /**
     * Test of read method, of class SocketChannelWithTimeouts.
     */
    @Test
    public void testRead_ByteBuffer() throws Exception {
        System.out.println("read");
        ByteBuffer dst = ByteBuffer.allocateDirect(1024);
        SocketChannelWithTimeoutsEmul instance = new SocketChannelWithTimeoutsEmul();
        instance.setSelectedKeys(new HashSet<SelectionKey>());
        assertEquals(4, instance.read(dst));
        assertEquals(-1, instance.read(dst));
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
        SocketChannel instance = new SocketChannelWithTimeoutsEmul();
        try {
            long result = instance.read(dsts, offset, length);
            fail("Unimplemented expected");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Test of write method, of class SocketChannelWithTimeouts.
     */
    @Test
    public void testWrite_ByteBuffer() throws Exception {
        System.out.println("write");
        ByteBuffer src = ByteBuffer.wrap("TEST".getBytes());
        SocketChannel instance = new SocketChannelWithTimeoutsEmul();
        int expResult = 4;
        int result = instance.write(src);
        assertEquals(expResult, result);
    }

    @Test
    public void testWrite_Empty() throws Exception {
        System.out.println("write");
        SocketChannelWithTimeoutsEmul instance = new SocketChannelWithTimeoutsEmul();
        SocketChannelEmul sc = (SocketChannelEmul) instance.socketChannel;
        assertEquals(4, instance.write(ByteBuffer.wrap("test".getBytes())));
        assertEquals(4, sc.getWrittenBytesCount());
        assertEquals(0, instance.write(ByteBuffer.wrap("".getBytes())));
        assertEquals(0, sc.getWrittenBytesCount());
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
        SocketChannel instance = new SocketChannelWithTimeoutsEmul();
        try {
            long result = instance.write(srcs, offset, length);
            fail("This function is unimplemented yet");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Test of implCloseSelectableChannel method, of class SocketChannelWithTimeouts.
     */
    @Test
    public void testImplCloseSelectableChannel() throws Exception {
        System.out.println("implCloseSelectableChannel");
        SocketChannelWithTimeoutsEmul instance = new SocketChannelWithTimeoutsEmul();
        instance.implCloseSelectableChannel();
    }

    /**
     * Test of implConfigureBlocking method, of class SocketChannelWithTimeouts.
     */
    @Test
    public void testImplConfigureBlocking() throws Exception {
        System.out.println("implConfigureBlocking");
        boolean block = false;
        SocketChannelWithTimeoutsEmul instance = new SocketChannelWithTimeoutsEmul();
        try {
            instance.implConfigureBlocking(block);
            fail("Exception expected");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Test of socket method, of class SocketChannelWithTimeouts.
     */
    @Test
    public void testSocket() throws IOException {
        System.out.println("socket");
        SocketChannelWithTimeoutsEmul instance = new SocketChannelWithTimeoutsEmul();
        Socket result = instance.socket();
        assertNotNull(result);
    }

    /**
     * Test of isConnected method, of class SocketChannelWithTimeouts.
     */
    @Test
    public void testIsConnected() throws IOException {
        System.out.println("isConnected");
        SocketChannelWithTimeoutsEmul instance = new SocketChannelWithTimeoutsEmul();
        boolean expResult = true;
        boolean result = instance.isConnected();
        assertEquals(expResult, result);
    }

    /**
     * Test of isConnectionPending method, of class SocketChannelWithTimeouts.
     */
    @Test
    public void testIsConnectionPending() throws IOException {
        System.out.println("isConnectionPending");
        SocketChannelWithTimeoutsEmul instance = new SocketChannelWithTimeoutsEmul();
        boolean expResult = false;
        boolean result = instance.isConnectionPending();
        assertEquals(expResult, result);
    }

    /**
     * Test of finishConnect method, of class SocketChannelWithTimeouts.
     */
    @Test
    public void testFinishConnect() throws Exception {
        System.out.println("finishConnect");
        SocketChannelWithTimeoutsEmul instance = new SocketChannelWithTimeoutsEmul();
        boolean expResult = true;
        boolean result = instance.finishConnect();
        assertEquals(expResult, result);
    }

    /**
     * Test of setConnectTimeout method, of class SocketChannelWithTimeouts.
     */
    @Test
    public void testSetConnectTimeout() throws IOException {
        System.out.println("setConnectTimeout");
        int t = 0;
        SocketChannelWithTimeoutsEmul instance = new SocketChannelWithTimeoutsEmul();
        instance.setConnectTimeout(t);
    }

    /**
     * Test of setReadTimeout method, of class SocketChannelWithTimeouts.
     */
    @Test
    public void testSetReadTimeout() throws IOException {
        System.out.println("setReadTimeout");
        int t = 0;
        SocketChannelWithTimeoutsEmul instance = new SocketChannelWithTimeoutsEmul();
        instance.setReadTimeout(t);
    }
}
