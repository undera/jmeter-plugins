package kg.apc.jmeter.samplers;

import java.io.InputStream;
import java.io.OutputStream;
import kg.apc.emulators.SocketEmulatorInputStream;
import kg.apc.emulators.SocketEmulatorOutputStream;
import org.apache.jmeter.protocol.tcp.sampler.BinaryTCPClientImpl;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author undera
 */
public class DNSJavaTCPClientImplTest {

    public DNSJavaTCPClientImplTest() {
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
     * Test of setupTest method, of class DNSJavaTCPClientImpl.
     */
    @Test
    public void testSetupTest() {
        System.out.println("setupTest");
        DNSJavaTCPClientImpl instance = new DNSJavaTCPClientImpl();
        instance.setupTest();
    }

    /**
     * Test of teardownTest method, of class DNSJavaTCPClientImpl.
     */
    @Test
    public void testTeardownTest() {
        System.out.println("teardownTest");
        DNSJavaTCPClientImpl instance = new DNSJavaTCPClientImpl();
        instance.teardownTest();
    }

    /**
     * Test of write method, of class DNSJavaTCPClientImpl.
     */
    @Test
    public void testWrite_OutputStream_InputStream() {
        System.out.println("write");
        OutputStream out = null;
        InputStream in = null;
        DNSJavaTCPClientImpl instance = new DNSJavaTCPClientImpl();
        try {
            instance.write(out, in);
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Test of write method, of class DNSJavaTCPClientImpl.
     */
    @Test
    public void testWrite_OutputStream_String() {
        System.out.println("write");
        SocketEmulatorOutputStream out = new SocketEmulatorOutputStream();
        String string = "nnm.ru. A IN\n7";
        DNSJavaTCPClientImpl instance = new DNSJavaTCPClientImpl();
        instance.write(out, string);
        // dig +tcp @8.8.8.8 nnm.ru IN A 
        String exp = "01000001000000000000036e6e6d0272750000010001";
        String res = out.getWrittenBytesAsHexString();
        System.out.println(exp);
        System.out.println(res.substring(8));
        //assertEquals(26, res.length()/2);
        assertTrue(res.startsWith("0018"));
        assertTrue(res.endsWith(exp));
    }

    /**
     * Test of read method, of class DNSJavaTCPClientImpl.
     */
    @Test
    public void testRead() {
        System.out.println("read");
        String resp="00386a1c81800001000200000000036e6e6d0272750000010001c00c000100010000137f0004596fbd94c00c000100010000137f0004596fbd95";
        byte[] buf = BinaryTCPClientImpl.hexStringToByteArray(resp);
        SocketEmulatorInputStream in = new  SocketEmulatorInputStream(buf);
        DNSJavaTCPClientImpl instance = new DNSJavaTCPClientImpl();
        String expResult = "NOERROR";
        String result = instance.read(in);
        System.out.println(result);
        assertTrue(result.contains(expResult));
    }

    /**
     * Test of getEolByte method, of class DNSJavaTCPClientImpl.
     */
    @Test
    public void testGetEolByte() {
        System.out.println("getEolByte");
        DNSJavaTCPClientImpl instance = new DNSJavaTCPClientImpl();
        byte expResult = 0;
        try {
            byte result = instance.getEolByte();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Test of setEolByte method, of class DNSJavaTCPClientImpl.
     */
    @Test
    public void testSetEolByte() {
        System.out.println("setEolByte");
        int i = 0;
        DNSJavaTCPClientImpl instance = new DNSJavaTCPClientImpl();
        instance.setEolByte(i);
    }
}
