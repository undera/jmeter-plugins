package kg.apc.jmeter.samplers;

import java.io.File;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import kg.apc.emulators.SocketChannelEmul;
import java.io.IOException;
import org.apache.jmeter.samplers.SampleResult;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.util.JMeterUtils;
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
        TestJMeterUtils.createJmeterEnv();
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
        String req = "GET / HTTP/1.1\r\nHost: localhost\r\nConnection: close\r\n\r\n";
        String resp = "HTTP/1.1 200 OK\r\nConnection: close\r\n\r\nTEST";
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

    @Test
    public void testSample_hugeparse() throws MalformedURLException, IOException {
        System.out.println("sample");
        String req = "GET / HTTP/1.1\r\nHost: localhost\r\nConnection: close\r\n\r\n";
        String resp = "HTTP/1.1 200 OK\r\nConnection: close\r\n\r\n" + TestJMeterUtils.getTestData(10000000);
        instance.setRequestData(req);
        instance.setParseResult(true);

        instance.sockEmul.setBytesToRead(ByteBuffer.wrap(resp.getBytes()));

        SampleResult result = instance.sample(null);
        //System.err.println(result.getResponseDataAsString().length());
        assertEquals(ByteBuffer.wrap(req.getBytes()), instance.sockEmul.getWrittenBytes());
        assertTrue(result.isSuccessful());
        assertEquals("200", result.getResponseCode());
        assertTrue( result.getResponseDataAsString().length()>6000);
        assertTrue(!instance.sockEmul.isOpen());
    }

    /**
     * Test of sample method, of class HTTPRawSampler.
     */
    @Test
    public void testSample_keepalive() throws MalformedURLException, IOException {
        System.out.println("sample");
        String req = "TEST";
        String resp = "TEST";
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
        SampleResult res = new SampleResult();
        res.sampleStart();
        byte[] result = instance.processIO(res);
        assertEquals("", new String(result));
    }

    /**
     * Test of getChannel method, of class HTTPRawSampler.
     */
    @Test
    public void testGetChannel() throws Exception {
        System.out.println("getChannel");
        AbstractSelectableChannel result = instance.getChannel();
        assertNotNull(result);
    }

    /**
     * Test of readResponse method, of class HTTPRawSampler.
     */
    @Test
    public void testReadResponse() throws Exception {
        System.out.println("readResponse");
        SocketChannelEmul sock = new SocketChannelEmul();
        sock.setBytesToRead(ByteBuffer.wrap("test".getBytes()));
        SampleResult res = new SampleResult();
        res.sampleStart();
        byte[] expResult = "test".getBytes();
        byte[] result = instance.readResponse(sock, res);
        assertEquals(expResult.length, result.length);
    }

    /**
     * Test of getSocketChannel method, of class HTTPRawSampler.
     */
    @Test
    public void testGetSocketChannel() throws Exception {
        System.out.println("getSocketChannel");
        SocketChannel result = instance.getSocketChannel();
        assertNotNull(result);
    }

    /**
     * Test of interrupt method, of class HTTPRawSampler.
     */
    @Test
    public void testInterrupt() {
        System.out.println("interrupt");
        boolean expResult = true;
        boolean result = instance.interrupt();
        assertEquals(expResult, result);
    }

    @Test
    public void testSample_unparsable() throws MalformedURLException, IOException {
        System.out.println("Sample_unparsable");
        String req = "GET / HTTP/1.1\r\nHost: localhost\r\nConnection: close\r\n\r\n";
        String resp = "<html>\n"
                + "<head><title>400 Bad Request</title></head>\n"
                + "<body bgcolor=\"white\">\n"
                + "<center><h1>400 Bad Request</h1></center>\n"
                + "<hr><center>nginx</center>\n"
                + "</body></html>";
        instance.setRequestData(req);
        instance.setParseResult(true);

        instance.sockEmul.setBytesToRead(ByteBuffer.wrap(resp.getBytes()));

        SampleResult result = instance.sample(null);
        //System.err.println(result.getResponseDataAsString().length());
        assertEquals(ByteBuffer.wrap(req.getBytes()), instance.sockEmul.getWrittenBytes());
        assertTrue(result.isSuccessful());
        assertEquals("200", result.getResponseCode());
        assertEquals("", result.getResponseMessage());
        assertEquals("", result.getResponseHeaders());
        assertEquals(resp, result.getResponseDataAsString());
        assertTrue(!instance.sockEmul.isOpen());
    }

    @Test
    public void testProcessIO_long_noLimit() throws Exception {
        System.out.println("processIOnolim");
        SampleResult res = new SampleResult();
        res.sampleStart();
        String resp = TestJMeterUtils.getTestData(2048);
        instance.sockEmul.setBytesToRead(ByteBuffer.wrap(resp.getBytes()));

        byte[] result = instance.processIO(res);
        assertTrue(result.length > 1024);
    }

    @Test
    public void testProcessIO_long_Limited() throws Exception {
        JMeterUtils.setProperty(AbstractIPSampler.RECV_BUFFER_LEN_PROPERTY, Integer.toString(1024));
        JMeterUtils.setProperty(AbstractIPSampler.RESULT_DATA_LIMIT, Integer.toString(1024));
        instance = new HTTPRawSamplerEmul();
        instance.setHostName("169.254.250.25");
        instance.setPort("80");
        instance.setTimeout("0");
        System.out.println("processIOlim");
        SampleResult res = new SampleResult();
        res.sampleStart();
        String resp = TestJMeterUtils.getTestData(12048);
        instance.sockEmul.setBytesToRead(ByteBuffer.wrap(resp.getBytes()));
        byte[] result = instance.processIO(res);
        System.out.println(result.length);
        assertEquals(4096, result.length);
    }

    /**
     * Test of processIO method, of class HTTPRawSampler.
     */
    @Test
    public void testProcessIO_fileOnly() throws Exception {
        System.out.println("processIO");
        SampleResult res = new SampleResult();
        res.sampleStart();
        instance.setPort("0");
        String file = this.getClass().getResource("testSendFile.raw").getPath();
        instance.setFileToSend(file);
        instance.processIO(res);
        assertEquals(new File(file).length(), instance.sockEmul.getWrittenBytesCount());
    }

    @Test
    public void testProcessIO_field_andFile() throws Exception {
        System.out.println("processIO");
        SampleResult res = new SampleResult();
        res.sampleStart();
        instance.setPort("0");
        String file = this.getClass().getResource("testSendFile.raw").getPath();
        String prefix = "GET / HTTP/1.0\r\n";
        instance.setRequestData(prefix);
        instance.setFileToSend(file);
        instance.processIO(res);
        assertEquals(new File(file).length() + prefix.length(), instance.sockEmul.getWrittenBytesCount());
    }

    /**
     * Test of getFileToSend method, of class HTTPRawSampler.
     */
    @Test
    public void testGetFileToSend() {
        System.out.println("getFileToSend");
        String expResult = "";
        String result = instance.getFileToSend();
        assertEquals(expResult, result);
    }

    /**
     * Test of setFileToSend method, of class HTTPRawSampler.
     */
    @Test
    public void testSetFileToSend() {
        System.out.println("setFileToSend");
        String text = "";
        instance.setFileToSend(text);
    }
}
