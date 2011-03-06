package kg.apc.jmeter.samplers;

import org.apache.jmeter.samplers.SampleResult;
import java.io.IOException;
import java.nio.channels.SocketChannel;
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
public class SocketChannelWithTimeoutsTest {

    private class HTTPRawSamplerEmul extends HTTPRawSampler {
        @Override
        protected SocketChannel getSocketChannelImpl() throws IOException {
            return SocketChannelWithTimeouts.open();
        }
    }

    public SocketChannelWithTimeoutsTest() {
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

    @Test
    public void testGeneral()
    {
        System.out.println("sample");
        HTTPRawSamplerEmul instance = new HTTPRawSamplerEmul();
        instance.setHostName("apc.kg");
        instance.setPort("80");
        instance.setTimeout("0");
        String req="GET / HTTP/1.1\r\n"
                + "Host: apc.kg\r\n"
                //+ getManyHeaders()
                + "Connection: close\r\n\r\n";
        instance.setRawRequest(req);

        SampleResult result = instance.sample(null);
        System.out.println(result.getBytes());
        System.out.println(result.getResponseDataAsString());
        assertTrue(result.isSuccessful());
        assertEquals("200", result.getResponseCode());
    }

    private String getManyHeaders() {
        String res="";
        for(int n=0; n<100; n++)
        {
            res+="Test header "+n+" : some data goes here";
        }
        return res;
    }
}