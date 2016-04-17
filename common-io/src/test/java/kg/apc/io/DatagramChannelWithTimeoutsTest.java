package kg.apc.io;

import kg.apc.emulators.DatagramChannelEmul;
import kg.apc.emulators.SelectorEmul;
import org.junit.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

public class DatagramChannelWithTimeoutsTest {

    private DatagramChannelWithTimeoutsEmul instance;

    private static class DatagramChannelWithTimeoutsEmul extends DatagramChannelWithTimeouts {

        protected DatagramChannelWithTimeoutsEmul() throws IOException {
            super();
            selector = new SelectorEmul();
            DatagramChannelEmul ce = (DatagramChannelEmul) DatagramChannelEmul.open();
            ce.configureBlocking(false);
            ce.setBytesToRead(ByteBuffer.wrap("test".getBytes()));
            channel = ce;
            channelKey = channel.register(selector, SelectionKey.OP_READ);
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
        DatagramChannel result = DatagramChannelWithTimeoutsEmul.open();
        Assert.assertNotNull(result);
    }

    /**
     * Test of read method, of class DatagramChannelWithTimeouts.
     */
    @Test
    public void testRead_ByteBuffer() throws Exception {
        System.out.println("read");
        instance.connect(new InetSocketAddress("204.74.112.1", 53));
        ByteBuffer dst = ByteBuffer.allocateDirect(1024);
        int expResult = 4;
        int result = instance.read(dst);
        Assert.assertEquals(expResult, result);
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
        try {
            long result = instance.read(dsts, offset, length);
            Assert.fail("exception expected");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Test of write method, of class DatagramChannelWithTimeouts.
     */
    @Test
    public void testWrite_ByteBuffer() throws Exception {
        System.out.println("write");
        ByteBuffer src = ByteBuffer.wrap("test".getBytes());
        int expResult = 4;
        int result = instance.write(src);
        Assert.assertEquals(expResult, result);
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
        try {
            long result = instance.write(srcs, offset, length);
            Assert.fail("exception expected");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Test of implCloseSelectableChannel method, of class DatagramChannelWithTimeouts.
     */
    @Test
    public void testImplCloseSelectableChannel() throws Exception {
        System.out.println("implCloseSelectableChannel");
        try{
        instance.implCloseSelectableChannel();
            Assert.fail("Exception expected");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Test of implConfigureBlocking method, of class DatagramChannelWithTimeouts.
     */
    @Test
    public void testImplConfigureBlocking() throws Exception {
        System.out.println("implConfigureBlocking");
        try {
            boolean block = false;
            instance.implConfigureBlocking(block);
            Assert.fail("Exception expected");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Test of isConnected method, of class DatagramChannelWithTimeouts.
     */
    @Test
    public void testIsConnected() throws IOException {
        System.out.println("isConnected");
        boolean expResult = true;
        boolean result = instance.isConnected();
        Assert.assertEquals(expResult, result);
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
        DatagramSocket result = instance.socket();
        Assert.assertNotNull(result);
    }

    /**
     * Test of connect method, of class DatagramChannelWithTimeouts.
     */
    @Test
    public void testConnect() throws Exception {
        System.out.println("connect");
        SocketAddress remote = new InetSocketAddress("localhost", 123);
        DatagramChannel result = instance.connect(remote);
        Assert.assertNotNull(result);
    }

    /**
     * Test of disconnect method, of class DatagramChannelWithTimeouts.
     */
    @Test
    public void testDisconnect() throws Exception {
        System.out.println("disconnect");
        DatagramChannel result = instance.disconnect();
        Assert.assertNotNull(result);
    }

    /**
     * Test of receive method, of class DatagramChannelWithTimeouts.
     */
    @Test
    public void testReceive() throws Exception {
        System.out.println("receive");
        ByteBuffer dst = null;
        SocketAddress expResult = null;
        try {
            SocketAddress result = instance.receive(dst);
            Assert.fail("exception expected");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Test of send method, of class DatagramChannelWithTimeouts.
     */
    @Test
    public void testSend() throws Exception {
        System.out.println("send");
        try {
            instance.send(null, null);
            Assert.fail("exception expected");
        } catch (UnsupportedOperationException e) {
        }
    }

    /*
    @Test
    public void testSendRecv_real() throws Exception {
        System.out.println("send real");
        String req = "892f0100000100000000000007636f6d6d6f6e730977696b696d65646961036f72670000010001";
        ByteBuffer src = ByteBuffer.wrap(BinaryTCPClientImpl.hexStringToByteArray(req));
        SocketAddress target = new InetSocketAddress("204.74.112.1", 53);
        int expResult = req.length() / 2;
        DatagramChannelWithTimeouts inst = (DatagramChannelWithTimeouts) DatagramChannelWithTimeouts.open();
        inst.setReadTimeout(500);
        inst.connect(target);
        int result = inst.write(src);
        assertEquals(expResult, result);
        ByteBuffer dst = ByteBuffer.allocateDirect(1024);
        inst.read(dst);
        System.out.println(dst);
        System.out.println(JMeterPluginsUtils.byteBufferToString(dst));
        try{
        inst.read(dst);
        fail("Exception?");
        } catch (SocketTimeoutException e)
        {}
    }
     * 
     */

    /**
     * Test of getRemoteAddress method, of class DatagramChannelWithTimeouts.
     */
    @Test
    public void testGetRemoteAddress() throws Exception {
        System.out.println("getRemoteAddress");
        DatagramChannelWithTimeouts instance = new DatagramChannelWithTimeouts();
        SocketAddress expResult = null;
        SocketAddress result = instance.getRemoteAddress();
        Assert.assertEquals(expResult, result);
    }
}
