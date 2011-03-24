package kg.apc.jmeter.samplers;

import java.nio.ByteBuffer;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.protocol.tcp.sampler.BinaryTCPClientImpl;
import org.apache.jorphan.util.JOrphanUtils;
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
    public void testEncode() {
        System.out.println("encode");
        // stackowerflow.com
        String data = "stackowerflow.com. A IN ";
        DNSJavaDecoder instance = new DNSJavaDecoder();
        ByteBuffer result = instance.encode(data);
        String exp = "XXXX010000010000000000000d737461636b6f766572666c6f7703636f6d0000010001";
        String res = JOrphanUtils.baToHexString(JMeterPluginsUtils.byteBufferToString(result).getBytes());
        System.out.println(exp);
        System.out.println(res);
        assertEquals(exp.substring(4), res.substring(4));
    }

    /**
     * Test of decode method, of class DNSJavaDecoder.
     */
    @Test
    public void testDecode() {
        System.out.println("decode");
        byte[] data = null;
        DNSJavaDecoder instance = new DNSJavaDecoder();
        byte[] expResult = null;
        byte[] result = instance.decode(data);
        assertEquals(expResult, result);
    }

}