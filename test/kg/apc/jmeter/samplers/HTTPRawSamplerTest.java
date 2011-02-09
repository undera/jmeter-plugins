/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.samplers;

import java.net.MalformedURLException;
import java.net.URL;
import kg.apc.jmeter.util.SocketEmulatorInputStream;
import kg.apc.jmeter.util.SocketEmulatorOutputStream;
import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
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
    public void testSample() throws MalformedURLException {
        System.out.println("sample");
        URL url = new URL("http://localhost");
        String method = "GET";
        HTTPRawSampler instance = new HTTPRawSampler();
        TestHttpURLConnection conn = new TestHttpURLConnection();
        conn.setSocketEmulatorInputStream(new SocketEmulatorInputStream("response".getBytes()));
        conn.setSocketEmulatorOutputStream(new SocketEmulatorOutputStream());
        instance.persistentConnection=conn;
        HTTPSampleResult result = instance.sample(url, method, false, 0);
        assertEquals("response", result.getResponseDataAsString());
    }

}