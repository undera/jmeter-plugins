package kg.apc.jmeter.samplers;

import java.nio.ByteBuffer;
import kg.apc.jmeter.JMeterPluginsUtils;
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
public class HexStringUDPDecoderTest {

    public HexStringUDPDecoderTest() {
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
     * Test of encode method, of class HexStringUDPDecoder.
     */
    @Test
    public void testEncode() {
        System.out.println("encode");
        String data = "74657374";
        HexStringUDPDecoder instance = new HexStringUDPDecoder();
        ByteBuffer result = instance.encode(data);
        assertEquals("test", JMeterPluginsUtils.byteBufferToString(result));
    }

    /**
     * Test of decode method, of class HexStringUDPDecoder.
     */
    @Test
    public void testDecode() {
        System.out.println("decode");
        byte[] buf = "test".getBytes();
        HexStringUDPDecoder instance = new HexStringUDPDecoder();
        byte[] result = instance.decode(buf);
        assertEquals("74657374", new String(result));
    }
}
