package kg.apc.jmeter.samplers;

import kg.apc.jmeter.util.SocketChannelEmul;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import org.apache.jmeter.samplers.SampleResult;
import java.net.MalformedURLException;
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
        protected SocketChannel getSocketChannel(SocketAddress address) throws IOException {
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
        instance.setHostName("169.254.250.25");
        instance.setPort("80");
        instance.setRawRequest("GET / HTTP/1.1\r\nHost: localhost\r\nConnection: close\r\n\r\n");

        SampleResult result = instance.sample(null);
        //System.err.println(result.getResponseDataAsString().length());
        assertTrue(result.isSuccessful());
        assertEquals("GET /", result.getSamplerData().substring(0, 5));
        assertEquals("HTTP/1.1 200 OK", result.getResponseDataAsString().length() > 15 ? result.getResponseDataAsString().substring(0, 15) : result.getResponseDataAsString());
    }

    @Test
    public void testMultiSample() {
        SampleResult result1 = instance.sample(null);
        SampleResult result2 = instance.sample(null);
    }

    /**
     * Test of getHostName method, of class HTTPRawSampler.
     */
    @Test
    public void testGetHostName() {
        System.out.println("getHostName");
        String expResult = "";
        String result = instance.getHostName();
        assertEquals(expResult, result);
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
        String expResult = "";
        String result = instance.getPort();
        assertEquals(expResult, result);
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
}
