package kg.apc.jmeter.samplers;

import java.nio.channels.spi.AbstractSelectableChannel;
import kg.apc.emulators.SocketChannelEmul;
import java.io.IOException;
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
        protected AbstractSelectableChannel getChannel() throws IOException {
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
        instance.setRequestData(req);
        instance.setParseResult(true);

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
        instance.setRequestData(req);
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
     * Test of setUseKeepAlive method, of class HTTPRawSampler.
     */
    @Test
    public void testSetUseKeepAlive() {
        System.out.println("setUseKeepAlive");
        boolean selected = false;
        instance.setUseKeepAlive(selected);
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

    /**
     * Test of processIO method, of class HTTPRawSampler.
     */
    @Test
    public void testProcessIO() throws Exception {
        System.out.println("processIO");
        SampleResult res = null;
        HTTPRawSampler instance = new HTTPRawSampler();
        byte[] expResult = null;
        byte[] result = instance.processIO(res);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getChannel method, of class HTTPRawSampler.
     */
    @Test
    public void testGetChannel() throws Exception {
        System.out.println("getChannel");
        HTTPRawSampler instance = new HTTPRawSampler();
        AbstractSelectableChannel expResult = null;
        AbstractSelectableChannel result = instance.getChannel();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
