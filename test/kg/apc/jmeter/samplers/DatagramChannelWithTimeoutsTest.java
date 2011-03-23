package kg.apc.jmeter.samplers;

import kg.apc.emulators.SelectionKeyEmul;
import kg.apc.emulators.SelectorEmul;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import kg.apc.emulators.DatagramChannelEmul;
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
public class DatagramChannelWithTimeoutsTest {

    private DatagramChannelWithTimeoutsEmul instance;

    private static class DatagramChannelWithTimeoutsEmul extends DatagramChannelWithTimeouts {

        protected DatagramChannelWithTimeoutsEmul() throws IOException {
            super();
            selector = new SelectorEmul();
            DatagramChannel ce = new DatagramChannelEmul();
            ce.configureBlocking(false);
            //ce.setBytesToRead(ByteBuffer.wrap("test".getBytes()));
            channel = ce;
            channelKey = new SelectionKeyEmul();
        }
    }

    public DatagramChannelWithTimeoutsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws IOException {
        instance = new DatagramChannelWithTimeoutsEmul();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of open method, of class DatagramChannelWithTimeouts.
     */
    @Test
    public void testOpen() throws Exception {
        System.out.println("open");
        DatagramChannel expResult = null;
        DatagramChannel result = DatagramChannelWithTimeoutsEmul.open();
        assertEquals(expResult, result);
    }

    /**
     * Test of read method, of class DatagramChannelWithTimeouts.
     */
    @Test
    public void testRead_ByteBuffer() throws Exception {
        System.out.println("read");
        ByteBuffer dst = null;
        int expResult = 0;
        int result = instance.read(dst);
        assertEquals(expResult, result);
    }

    /**
     * Test of read method, of class DatagramChannelWithTimeouts.
     */
    @Test
    public void testRead_3args() throws Exception {
        System.out.println("read");
        ByteBuffer[] dsts = null;
        int offset = 0;
        int length = 0;
        long expResult = 0L;
        long result = instance.read(dsts, offset, length);
        assertEquals(expResult, result);
    }

    /**
     * Test of write method, of class DatagramChannelWithTimeouts.
     */
    @Test
    public void testWrite_ByteBuffer() throws Exception {
        System.out.println("write");
        ByteBuffer src = null;
        int expResult = 0;
        int result = instance.write(src);
        assertEquals(expResult, result);
    }

    /**
     * Test of write method, of class DatagramChannelWithTimeouts.
     */
    @Test
    public void testWrite_3args() throws Exception {
        System.out.println("write");
        ByteBuffer[] srcs = null;
        int offset = 0;
        int length = 0;
        long expResult = 0L;
        long result = instance.write(srcs, offset, length);
        assertEquals(expResult, result);
    }

    /**
     * Test of implCloseSelectableChannel method, of class DatagramChannelWithTimeouts.
     */
    @Test
    public void testImplCloseSelectableChannel() throws Exception {
        System.out.println("implCloseSelectableChannel");
        instance.implCloseSelectableChannel();
    }

    /**
     * Test of implConfigureBlocking method, of class DatagramChannelWithTimeouts.
     */
    @Test
    public void testImplConfigureBlocking() throws Exception {
        System.out.println("implConfigureBlocking");
        boolean block = false;
        instance.implConfigureBlocking(block);
    }

    /**
     * Test of isConnected method, of class DatagramChannelWithTimeouts.
     */
    @Test
    public void testIsConnected() throws IOException {
        System.out.println("isConnected");
        boolean expResult = false;
        boolean result = instance.isConnected();
        assertEquals(expResult, result);
    }

    /**
     * Test of setConnectTimeout method, of class DatagramChannelWithTimeouts.
     */
    @Test
    public void testSetConnectTimeout() throws IOException {
        System.out.println("setConnectTimeout");
        int t = 0;
        instance.setConnectTimeout(t);
    }

    /**
     * Test of setReadTimeout method, of class DatagramChannelWithTimeouts.
     */
    @Test
    public void testSetReadTimeout() {
        System.out.println("setReadTimeout");
        int t = 0;
        instance.setReadTimeout(t);
    }

    /**
     * Test of socket method, of class DatagramChannelWithTimeouts.
     */
    @Test
    public void testSocket() {
        System.out.println("socket");
        DatagramSocket expResult = null;
        DatagramSocket result = instance.socket();
        assertEquals(expResult, result);
    }

    /**
     * Test of connect method, of class DatagramChannelWithTimeouts.
     */
    @Test
    public void testConnect() throws Exception {
        System.out.println("connect");
        SocketAddress remote = null;
        DatagramChannel expResult = null;
        DatagramChannel result = instance.connect(remote);
        assertEquals(expResult, result);
    }

    /**
     * Test of disconnect method, of class DatagramChannelWithTimeouts.
     */
    @Test
    public void testDisconnect() throws Exception {
        System.out.println("disconnect");
        DatagramChannel expResult = null;
        DatagramChannel result = instance.disconnect();
        assertEquals(expResult, result);
    }

    /**
     * Test of receive method, of class DatagramChannelWithTimeouts.
     */
    @Test
    public void testReceive() throws Exception {
        System.out.println("receive");
        ByteBuffer dst = null;
        SocketAddress expResult = null;
        SocketAddress result = instance.receive(dst);
        assertEquals(expResult, result);
    }

    /**
     * Test of send method, of class DatagramChannelWithTimeouts.
     */
    @Test
    public void testSend() throws Exception {
        System.out.println("send");
        ByteBuffer src = null;
        SocketAddress target = null;
        int expResult = 0;
        int result = instance.send(src, target);
        assertEquals(expResult, result);
    }
}
