package kg.apc.jmeter.samplers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import kg.apc.emulators.DatagramChannelEmul;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.engine.event.LoopIterationEvent;
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

    private static class UDPSamplerEmul extends UDPSampler {

        @Override
        protected DatagramChannel getChannel() throws IOException {
            return DatagramChannelEmul.open();
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
        UDPSampler instance = new UDPSamplerEmul();
        SampleResult result = instance.sample(null);
        assertTrue(result.isSuccessful());
    }

    /**
     * Test of getHostName method, of class UDPSampler.
     */
    @Test
    public void testGetHostName() {
        System.out.println("getHostName");
        UDPSampler instance = new UDPSampler();
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
        UDPSampler instance = new UDPSampler();
        instance.setHostName(text);
    }

    /**
     * Test of getPort method, of class UDPSampler.
     */
    @Test
    public void testGetPort() {
        System.out.println("getPort");
        UDPSampler instance = new UDPSampler();
        String expResult = "";
        String result = instance.getPort();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTimeout method, of class UDPSampler.
     */
    @Test
    public void testGetTimeout() {
        System.out.println("getTimeout");
        UDPSampler instance = new UDPSampler();
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
        UDPSampler instance = new UDPSampler();
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
        UDPSampler instance = new UDPSampler();
        String expResult = "";
        String result = instance.getEncoderClass();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRequestData method, of class UDPSampler.
     */
    @Test
    public void testGetRequestData() {
        System.out.println("getRequestData");
        UDPSampler instance = new UDPSampler();
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
        UDPSampler instance = new UDPSampler();
        instance.setPort(text);
    }

    /**
     * Test of setWaitResponse method, of class UDPSampler.
     */
    @Test
    public void testSetWaitResponse() {
        System.out.println("setWaitResponse");
        boolean selected = false;
        UDPSampler instance = new UDPSampler();
        instance.setWaitResponse(selected);
    }

    /**
     * Test of setTimeout method, of class UDPSampler.
     */
    @Test
    public void testSetTimeout() {
        System.out.println("setTimeout");
        String text = "";
        UDPSampler instance = new UDPSampler();
        instance.setTimeout(text);
    }

    /**
     * Test of setRequestData method, of class UDPSampler.
     */
    @Test
    public void testSetRequestData() {
        System.out.println("setRequestData");
        String text = "";
        UDPSampler instance = new UDPSampler();
        instance.setRequestData(text);
    }

    /**
     * Test of setEncoderClass method, of class UDPSampler.
     */
    @Test
    public void testSetEncoderClass() {
        System.out.println("setEncoderClass");
        String text = "";
        UDPSampler instance = new UDPSampler();
        instance.setEncoderClass(text);
    }

    /**
     * Test of testStarted method, of class UDPSampler.
     */
    @Test
    public void testTestStarted_0args() {
        System.out.println("testStarted");
        UDPSampler instance = new UDPSampler();
        instance.setPort("53");
        instance.testStarted();
    }

    /**
     * Test of testStarted method, of class UDPSampler.
     */
    @Test
    public void testTestStarted_String() {
        System.out.println("testStarted");
        String string = "";
        UDPSampler instance = new UDPSampler();
        instance.setPort("53");
        instance.testStarted(string);
    }

    /**
     * Test of testEnded method, of class UDPSampler.
     */
    @Test
    public void testTestEnded_0args() {
        System.out.println("testEnded");
        UDPSampler instance = new UDPSampler();
        instance.setPort("53");
        instance.testStarted();
        instance.testEnded();
    }

    /**
     * Test of testEnded method, of class UDPSampler.
     */
    @Test
    public void testTestEnded_String() {
        System.out.println("testEnded");
        String string = "";
        UDPSampler instance = new UDPSampler();
        instance.setPort("53");
        instance.testStarted();
        instance.testEnded(string);
    }

    /**
     * Test of testIterationStart method, of class UDPSampler.
     */
    @Test
    public void testTestIterationStart() {
        System.out.println("testIterationStart");
        LoopIterationEvent lie = null;
        UDPSampler instance = new UDPSampler();
        instance.testIterationStart(lie);
    }

    /**
     * Test of getChannel method, of class UDPSampler.
     */
    @Test
    public void testGetChannel() throws Exception {
        System.out.println("getChannel");
        UDPSampler instance = new UDPSampler();
        instance.setPort("53");
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
        UDPSampler instance = new UDPSampler();
        ByteBuffer result = instance.encode(data);
        assertEquals(data, JMeterPluginsUtils.byteBufferToString(result));
    }

    /**
     * Test of decode method, of class UDPSampler.
     */
    @Test
    public void testDecode() {
        System.out.println("decode");
        ByteBuffer data = ByteBuffer.wrap("test".getBytes());
        UDPSampler instance = new UDPSampler();
        byte[] result = instance.decode(data);
        assertEquals("test".getBytes(), result);
    }
}
