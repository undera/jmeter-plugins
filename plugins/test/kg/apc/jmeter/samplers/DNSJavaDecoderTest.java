package kg.apc.jmeter.samplers;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.protocol.tcp.sampler.BinaryTCPClientImpl;
import org.apache.jorphan.util.JOrphanUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.*;
import org.xbill.DNS.Record;

/**
 *
 * @author undera
 */
public class DNSJavaDecoderTest {

    public DNSJavaDecoderTest() {
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
     * Test of encode method, of class DNSJavaDecoder.
     */
    @Test
    public void testEncode() throws UnsupportedEncodingException {
        System.out.println("encode");
        // stackowerflow.com
        String data = "stackowerflow.com. A IN ";
        DNSJavaDecoder instance = new DNSJavaDecoder();
        ByteBuffer result = instance.encode(data);
        String exp = "3f3c000000010000000000000d737461636b6f776572666c6f7703636f6d0000010001";
        String res = JOrphanUtils.baToHexString(JMeterPluginsUtils.byteBufferToString(result).getBytes("cp866"));
        System.out.println(exp);
        System.out.println(res);
        assertEquals(exp.substring(8), res.substring(res.length()-exp.length()+8));
    }

    /**
     * Test of encode method, of class DNSJavaDecoder.
     */
    @Test
    public void testEncode_withFlags() throws UnsupportedEncodingException {
        System.out.println("encode f");
        // stackowerflow.com
        String data = "stackowerflow.com. A IN\r\n7\r\n-7";
        DNSJavaDecoder instance = new DNSJavaDecoder();
        ByteBuffer result = instance.encode(data);
        String exp = "3f3c000000010000000000000d737461636b6f776572666c6f7703636f6d0000010001";
        String res = JOrphanUtils.baToHexString(JMeterPluginsUtils.byteBufferToString(result).getBytes("cp866"));
        System.out.println(exp);
        System.out.println(res);
        assertEquals(exp.substring(8), res.substring(res.length()-exp.length()+8));
    }
    
    @Test
    public void testEncode_withFlagsMac() throws UnsupportedEncodingException {
        System.out.println("encode f");
        // stackowerflow.com
        String data = "stackowerflow.com. A IN\n\r7\n\r-7";
        DNSJavaDecoder instance = new DNSJavaDecoder();
        ByteBuffer result = instance.encode(data);
        String exp = "3f3c000000010000000000000d737461636b6f776572666c6f7703636f6d0000010001";
        String res = JOrphanUtils.baToHexString(JMeterPluginsUtils.byteBufferToString(result).getBytes("cp866"));
        System.out.println(exp);
        System.out.println(res);
        assertEquals(exp.substring(8), res.substring(res.length()-exp.length()+8));
    }

    /**
     * Test of decode method, of class DNSJavaDecoder.
     */
    @Test
    public void testDecode() {
        System.out.println("decode");
        String resp="567f818000010007000000000667726f75707306676f6f676c6503636f6d0000010001c00c000500010000463a000b0667726f757073016cc013c02f000100010000011a00044a7d2765c02f000100010000011a00044a7d2771c02f000100010000011a00044a7d278ac02f000100010000011a00044a7d2766c02f000100010000011a00044a7d2764c02f000100010000011a00044a7d278b";
        byte[] buf = BinaryTCPClientImpl.hexStringToByteArray(resp);
        DNSJavaDecoder instance = new DNSJavaDecoder();
        String expResult = "NOERROR";
        String result = new String(instance.decode(buf));
        System.out.println(result);
        assertTrue(result.contains(expResult));
    }

    /**
     * Test of getRecord method, of class DNSJavaDecoder.
     */
    @Test
    public void testGetRecord() {
        System.out.println("getRecord");
        String recstr = ". A IN";
        DNSJavaDecoder instance = new DNSJavaDecoder();
        Record result = instance.getRecord(recstr);
        assertTrue(result.toString().endsWith("A"));
    }
}