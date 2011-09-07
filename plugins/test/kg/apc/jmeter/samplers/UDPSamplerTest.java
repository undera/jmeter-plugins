package kg.apc.jmeter.samplers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import kg.apc.emulators.DatagramChannelEmul;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.samplers.SampleResult;
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
public class UDPSamplerTest {

    private UDPSampler instance;

    private static class UDPSamplerEmul extends UDPSampler {

        DatagramChannel c;

        @Override
        protected DatagramChannel getChannel() throws IOException {
            c = DatagramChannelEmul.open();
            c.connect(new InetSocketAddress(53));
            return c;
        }
    }

    public UDPSamplerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        instance = new UDPSamplerEmul();
        instance.setPort("53");
        instance.setEncoderClass(HexStringUDPDecoder.class.getCanonicalName());
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of sample method, of class UDPSampler.
     */
    @Test
    public void testSample() {
        System.out.println("sample");
        instance.threadStarted();
        SampleResult result = instance.sample(null);
        assertTrue(result.isSuccessful());
    }

    /**
     * Test of getHostName method, of class UDPSampler.
     */
    @Test
    public void testGetHostName() {
        System.out.println("getHostName");
        String expResult = "";
        String result = instance.getHostName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setHostName method, of class UDPSampler.
     */
    @Test
    public void testSetHostName() {
        System.out.println("setHostName");
        String text = "";
        instance.setHostName(text);
    }

    /**
     * Test of getPort method, of class UDPSampler.
     */
    @Test
    public void testGetPort() {
        System.out.println("getPort");
        String expResult = "53";
        String result = instance.getPort();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTimeout method, of class UDPSampler.
     */
    @Test
    public void testGetTimeout() {
        System.out.println("getTimeout");
        String expResult = "";
        String result = instance.getTimeout();
        assertEquals(expResult, result);
    }

    /**
     * Test of isWaitResponse method, of class UDPSampler.
     */
    @Test
    public void testIsWaitResponse() {
        System.out.println("isWaitResponse");
        boolean expResult = false;
        boolean result = instance.isWaitResponse();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEncoderClass method, of class UDPSampler.
     */
    @Test
    public void testGetEncoderClass() {
        System.out.println("getEncoderClass");
        String result = instance.getEncoderClass();
        assertNotNull(result);
    }

    /**
     * Test of getRequestData method, of class UDPSampler.
     */
    @Test
    public void testGetRequestData() {
        System.out.println("getRequestData");
        String expResult = "";
        String result = instance.getRequestData();
        assertEquals(expResult, result);
    }

    /**
     * Test of setPort method, of class UDPSampler.
     */
    @Test
    public void testSetPort() {
        System.out.println("setPort");
        String text = "";
        instance.setPort(text);
    }

    /**
     * Test of setWaitResponse method, of class UDPSampler.
     */
    @Test
    public void testSetWaitResponse() {
        System.out.println("setWaitResponse");
        boolean selected = false;
        instance.setWaitResponse(selected);
    }

    /**
     * Test of setTimeout method, of class UDPSampler.
     */
    @Test
    public void testSetTimeout() {
        System.out.println("setTimeout");
        String text = "";
        instance.setTimeout(text);
    }

    /**
     * Test of setRequestData method, of class UDPSampler.
     */
    @Test
    public void testSetRequestData() {
        System.out.println("setRequestData");
        String text = "";
        instance.setRequestData(text);
    }

    /**
     * Test of setEncoderClass method, of class UDPSampler.
     */
    @Test
    public void testSetEncoderClass() {
        System.out.println("setEncoderClass");
        String text = "";
        instance.setEncoderClass(text);
    }

    /**
     * Test of getChannel method, of class UDPSampler.
     */
    @Test
    public void testGetChannel() throws Exception {
        System.out.println("getChannel");
        DatagramChannel result = (DatagramChannel) instance.getChannel();
        assertNotNull(result);
    }

    /**
     * Test of encode method, of class UDPSampler.
     */
    @Test
    public void testEncode() {
        System.out.println("encode");
        String data = "test";
        ByteBuffer result = instance.encode(data);
        assertEquals(data, JMeterPluginsUtils.byteBufferToString(result));
    }

    /**
     * Test of decode method, of class UDPSampler.
     */
    @Test
    public void testDecode() {
        System.out.println("decode");
        byte[] result = instance.decode("test".getBytes());
        assertEquals(4, result.length);
    }

    /*
    @Test
    public void testReal() {
        System.out.println("real");
        instance = new UDPSampler();
        instance.setHostName("204.74.112.1");
        instance.setPort("53");
        instance.setRequestData("f11b0100000100000000000004636f646506676f6f676c6503636f6d00001c0001");
        instance.setEncoderClass(HexStringUDPDecoder.class.getCanonicalName());
        instance.threadStarted();
        instance.setWaitResponse(true);
        instance.setTimeout("500");
        SampleResult res = instance.sample(null);
        assertTrue(res.isSuccessful());
        assertTrue(res.getResponseDataAsString().length()>0);
        SampleResult res2 = instance.sample(null);
        assertTrue(res2.isSuccessful());
        assertTrue(res2.getResponseDataAsString().length()>0);
    }
     * 
     */

    /**
     * Test of processIO method, of class UDPSampler.
     */
    @Test
    public void testProcessIO() throws Exception {
        System.out.println("processIO");
        instance.threadStarted();
        SampleResult res = new SampleResult();
        instance.setRequestData("453555");
        byte[] result = instance.processIO(res);
        assertEquals("", new String(result));
    }

    /**
     * Test of threadStarted method, of class UDPSampler.
     */
    @Test
    public void testThreadStarted() {
        System.out.println("threadStarted");
        instance.threadStarted();
    }

    /**
     * Test of threadFinished method, of class UDPSampler.
     */
    @Test
    public void testThreadFinished() {
        System.out.println("threadFinished");
        instance.threadFinished();
    }

    /**
     * Test of interrupt method, of class UDPSampler.
     */
    @Test
    public void testInterrupt() {
        System.out.println("interrupt");
        boolean expResult = true;
        boolean result = instance.interrupt();
        assertEquals(expResult, result);
    }
}
