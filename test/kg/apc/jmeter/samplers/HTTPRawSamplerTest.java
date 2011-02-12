package kg.apc.jmeter.samplers;

import java.io.IOException;
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
        //HTTPRawSamplerEmul instance = new HTTPRawSamplerEmul();
        //SocketEmulatorInputStream is = (SocketEmulatorInputStream) instance.sock.getInputStream();
        //is.setBytesToRead("response");
        HTTPRawSampler instance = new HTTPRawSampler();
        SampleResult result = instance.sample(null);
        assertTrue(result.isSuccessful());
        assertEquals("GET /", result.getSamplerData().substring(0, 5));
        //System.out.println(result.getResponseDataAsString());
        //System.out.println(result.getTime());
        assertEquals("HTTP/1.1 200 OK", result.getResponseDataAsString().length()>15?result.getResponseDataAsString().substring(0, 15):result.getResponseDataAsString());
    }

   @Test
    public void testMultiSample()  {
        HTTPRawSampler instance = new HTTPRawSampler();
        SampleResult result1 = instance.sample(null);
        SampleResult result2 = instance.sample(null);
    }
   }
 