package kg.apc.jmeter.samplers;

import kg.apc.emulators.DatagramChannelEmul;
import kg.apc.emulators.FileChannelEmul;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import static org.junit.Assert.*;

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

    @Before
    public void setUp() {
        instance = new UDPSamplerEmul();
        instance.setPort("53");
        instance.setEncoderClass(HexStringUDPDecoder.class.getCanonicalName());
    }

    //@Test
    public void testReal() {
        System.out.println("real");
        instance = new UDPSampler();
        instance.setHostName("8.8.8.8");
        instance.setPort("53");
        instance.setRequestData("f11b0100000100000000000004636f646506676f6f676c6503636f6d00001c0001");
        instance.setEncoderClass(HexStringUDPDecoder.class.getCanonicalName());
        instance.threadStarted();
        instance.setWaitResponse(true);
        instance.setTimeout("500");
        SampleResult res = instance.sample(null);
        assertTrue(res.isSuccessful());
        assertTrue(res.getResponseDataAsString().length() > 0);
        SampleResult res2 = instance.sample(null);
        assertTrue(res2.isSuccessful());
        assertTrue(res2.getResponseDataAsString().length() > 0);
    }

    @Test
    public void testSample() {
        System.out.println("sample");
        instance.threadStarted();
        SampleResult result = instance.sample(null);
        assertTrue(result.isSuccessful());
    }

    @Test
    public void testGetHostName() {
        System.out.println("getHostName");
        String expResult = "";
        String result = instance.getHostName();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetHostName() {
        System.out.println("setHostName");
        String text = "";
        instance.setHostName(text);
    }

    @Test
    public void testGetPort() {
        System.out.println("getPort");
        String expResult = "53";
        String result = instance.getPort();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetTimeout() {
        System.out.println("getTimeout");
        String expResult = "";
        String result = instance.getTimeout();
        assertEquals(expResult, result);
    }

    @Test
    public void testIsWaitResponse() {
        System.out.println("isWaitResponse");
        boolean result = instance.isWaitResponse();
        assertEquals(false, result);
    }

    @Test
    public void testGetEncoderClass() {
        System.out.println("getEncoderClass");
        String result = instance.getEncoderClass();
        assertNotNull(result);
    }

    @Test
    public void testGetRequestData() {
        System.out.println("getRequestData");
        String expResult = "";
        String result = instance.getRequestData();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetPort() {
        System.out.println("setPort");
        String text = "";
        instance.setPort(text);
    }

    @Test
    public void testSetWaitResponse() {
        System.out.println("setWaitResponse");
        instance.setWaitResponse(false);
    }

    @Test
    public void testSetTimeout() {
        System.out.println("setTimeout");
        String text = "";
        instance.setTimeout(text);
    }

    @Test
    public void testSetRequestData() {
        System.out.println("setRequestData");
        String text = "";
        instance.setRequestData(text);
    }

    @Test
    public void testSetEncoderClass() {
        System.out.println("setEncoderClass");
        String text = "";
        instance.setEncoderClass(text);
    }

    @Test
    public void testGetChannel() throws Exception {
        System.out.println("getChannel");
        DatagramChannel result = (DatagramChannel) instance.getChannel();
        assertNotNull(result);
    }

    @Test
    public void testEncode() {
        System.out.println("encode");
        String data = "test";
        ByteBuffer result = instance.encode(data);
        Assert.assertEquals(data, FileChannelEmul.byteBufferToString(result));
    }

    @Test
    public void testDecode() {
        System.out.println("decode");
        byte[] result = instance.decode("test".getBytes());
        assertEquals(4, result.length);
    }

    @Test
    public void testThreadStarted() {
        System.out.println("threadStarted");
        instance.threadStarted();
    }


    @Test
    public void testThreadFinished() {
        System.out.println("threadFinished");
        instance.threadFinished();
    }


    @Test
    public void testInterrupt() {
        System.out.println("interrupt");
        boolean result = instance.interrupt();
        assertEquals(true, result);
    }


    @Test
    public void testIsCloseChannel() {
        System.out.println("isCloseChannel");
        UDPSampler instance = new UDPSampler();
        boolean result = instance.isCloseChannel();
        assertEquals(false, result);
    }


    @Test
    public void testSetCloseChannel() {
        System.out.println("setCloseChannel");
        UDPSampler instance = new UDPSampler();
        instance.setCloseChannel(false);
    }
}
