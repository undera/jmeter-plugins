package kg.apc.jmeter.samplers;

import kg.apc.emulators.SocketChannelEmul;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import org.apache.jmeter.samplers.SampleResult;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;
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
public class HTTPRawSamplerTest {

    private HTTPRawSamplerEmul instance;

    private class HTTPRawSamplerEmul extends HTTPRawSampler {

        SocketChannelEmul sockEmul = new SocketChannelEmul();

        @Override
        protected SocketChannel getSocketChannelImpl() throws IOException {
            return sockEmul;
        }
    }

    public HTTPRawSamplerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        instance = new HTTPRawSamplerEmul();
        instance.setHostName("169.254.250.25");
        instance.setPort("80");
        instance.setTimeout("0");
}

    @After
    public void tearDown() {
    }

    /**
     * Test of sample method, of class HTTPRawSampler.
     */
    @Test
    public void testSample() throws MalformedURLException, IOException {
        System.out.println("sample");
        String req="GET / HTTP/1.1\r\nHost: localhost\r\nConnection: close\r\n\r\n";
        String resp="HTTP/1.1 200 OK\r\n\r\nTEST";
        instance.setRawRequest(req);

        instance.sockEmul.setBytesToRead(ByteBuffer.wrap(resp.getBytes()));

        SampleResult result = instance.sample(null);
        //System.err.println(result.getResponseDataAsString().length());
        assertEquals(ByteBuffer.wrap(req.getBytes()), instance.sockEmul.getWrittenBytes());
        assertTrue(result.isSuccessful());
        assertEquals("200", result.getResponseCode());
        assertEquals("TEST", result.getResponseDataAsString());
        assertTrue(!instance.sockEmul.isOpen());
    }

    /**
     * Test of sample method, of class HTTPRawSampler.
     */
    @Test
    public void testSample_keepalive() throws MalformedURLException, IOException {
        System.out.println("sample");
        String req="TEST";
        String resp="TEST";
        instance.setRawRequest(req);
        instance.setUseKeepAlive(true);

        instance.sockEmul.setBytesToRead(ByteBuffer.wrap(resp.getBytes()));

        SampleResult result = instance.sample(null);
        assertTrue(instance.sockEmul.isOpen());

        instance.setUseKeepAlive(false);
        instance.sockEmul.setBytesToRead(ByteBuffer.wrap(resp.getBytes()));
        instance.sample(null);
        assertFalse(instance.sockEmul.isOpen());
    }

    /**
     * Test of getHostName method, of class HTTPRawSampler.
     */
    @Test
    public void testGetHostName() {
        System.out.println("getHostName");
        String result = instance.getHostName();
        assertTrue(result.length()>0);
    }

    /**
     * Test of setHostName method, of class HTTPRawSampler.
     */
    @Test
    public void testSetHostName() {
        System.out.println("setHostName");
        String text = "";
        instance.setHostName(text);
    }

    /**
     * Test of getPort method, of class HTTPRawSampler.
     */
    @Test
    public void testGetPort() {
        System.out.println("getPort");
        String result = instance.getPort();
        assertTrue(result.length()>0);
    }

    /**
     * Test of setPort method, of class HTTPRawSampler.
     */
    @Test
    public void testSetPort() {
        System.out.println("setPort");
        String value = "";
        instance.setPort(value);
    }

    /**
     * Test of getRawRequest method, of class HTTPRawSampler.
     */
    @Test
    public void testGetRawRequest() {
        System.out.println("getRawRequest");
        String expResult = "";
        String result = instance.getRawRequest();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRawRequest method, of class HTTPRawSampler.
     */
    @Test
    public void testSetRawRequest() {
        System.out.println("setRawRequest");
        String value = "";
        instance.setRawRequest(value);
    }

    /**
     * Test of getSocketChannel method, of class HTTPRawSampler.
     */
    @Test
    public void testGetSocketChannelImpl() throws Exception {
        System.out.println("getSocketChannel");
        SocketChannel result = instance.getSocketChannelImpl();
        assertTrue(result instanceof SocketChannelEmul);
    }

    /**
     * Test of setUseKeepAlive method, of class HTTPRawSampler.
     */
    @Test
    public void testSetUseKeepAlive() {
        System.out.println("setUseKeepAlive");
        boolean selected = false;
        instance.setUseKeepAlive(selected);
    }

    /**
     * Test of getTimeout method, of class HTTPRawSampler.
     */
    @Test
    public void testGetTimeout() {
        System.out.println("getTimeout");
        String expResult = "0";
        String result = instance.getTimeout();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTimeout method, of class HTTPRawSampler.
     */
    @Test
    public void testSetTimeout() {
        System.out.println("setTimeout");
        String value = "";
        instance.setTimeout(value);
    }

    /**
     * Test of isUseKeepAlive method, of class HTTPRawSampler.
     */
    @Test
    public void testIsUseKeepAlive() {
        System.out.println("isUseKeepAlive");
        boolean expResult = false;
        boolean result = instance.isUseKeepAlive();
        assertEquals(expResult, result);
    }

    /**
     * Test of isParseResult method, of class HTTPRawSampler.
     */
    @Test
    public void testIsParseResult() {
        System.out.println("isParseResult");
        boolean expResult = false;
        boolean result = instance.isParseResult();
        assertEquals(expResult, result);
    }

    /**
     * Test of setParseResult method, of class HTTPRawSampler.
     */
    @Test
    public void testSetParseResult() {
        System.out.println("setParseResult");
        boolean selected = false;
        instance.setParseResult(selected);
    }
}
