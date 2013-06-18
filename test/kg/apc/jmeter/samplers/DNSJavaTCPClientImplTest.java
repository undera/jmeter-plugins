package kg.apc.jmeter.samplers;

import java.io.InputStream;
import java.io.OutputStream;
import kg.apc.emulators.SocketEmulatorInputStream;
import kg.apc.emulators.SocketEmulatorOutputStream;
import org.apache.jmeter.protocol.tcp.sampler.BinaryTCPClientImpl;
import org.junit.*;
import static org.junit.Assert.*;
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
        String resp = "00386a1c81800001000200000000036e6e6d0272750000010001c00c000100010000137f0004596fbd94c00c000100010000137f0004596fbd95";
        byte[] buf = BinaryTCPClientImpl.hexStringToByteArray(resp);
        SocketEmulatorInputStream in = new SocketEmulatorInputStream(buf);
        DNSJavaTCPClientImpl instance = new DNSJavaTCPClientImpl();
        String expResult = "NOERROR";
        String result = instance.read(in);
        System.out.println(result);
        assertTrue(result.contains(expResult));
    }

    @Test
    public void testRead_broken_response() {
        System.out.println("read 2");
        String resp = "a74c85000001000200000000036e6e6d0272750000010001c00c00010001000054600004596fbd95c00c";
        byte[] buf = BinaryTCPClientImpl.hexStringToByteArray(resp);
        SocketEmulatorInputStream in = new SocketEmulatorInputStream(buf);
        DNSJavaTCPClientImpl instance = new DNSJavaTCPClientImpl();
        String expResult = "NOERROR";
        try {
            instance.read(in);
            fail();
        } catch (RuntimeException e) {
        }
    }

    @Test
    public void testRead_3() {
        /*
         * System.out.println("read 3"); String resp =
         * "81800001000200000000036e6e6d0272750000010001c00c00010001000027540004596fbd95c00c00010001000027540004596fbd94";
         * byte[] buf = BinaryTCPClientImpl.hexStringToByteArray(resp);
         * SocketEmulatorInputStream in = new SocketEmulatorInputStream(buf);
         * DNSJavaTCPClientImpl instance = new DNSJavaTCPClientImpl(); String
         * expResult = "NOERROR"; String result = instance.read(in);
         * System.out.println(result); assertTrue(result.contains(expResult));
         *
         */
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

    /**
     * Test of getLengthPrefix method, of class DNSJavaTCPClientImpl.
     */
    @Test
    public void testGetLengthPrefix() {
        System.out.println("getLengthPrefix");
        int length = 0;
        DNSJavaTCPClientImpl instance = new DNSJavaTCPClientImpl();
        int expResult = 2;
        byte[] result = instance.getLengthPrefix(length);
        assertEquals(expResult, result.length);
    }

    /**
     * Test of getCharset method, of class DNSJavaTCPClientImpl.
     */
    @Test
    public void testGetCharset() {
        System.out.println("getCharset");
        DNSJavaTCPClientImpl instance = new DNSJavaTCPClientImpl();
        String expLinuxResult = "UTF-8";
        String expWinResult = "windows-1252";
        String expMacResult = "MacRoman";
        String result = instance.getCharset();
        System.out.println(result);
        assertTrue(expLinuxResult.equals(result) || expWinResult.equals(result) || expMacResult.equals(result));
    }
}
