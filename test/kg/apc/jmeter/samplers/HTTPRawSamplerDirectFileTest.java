package kg.apc.jmeter.samplers;

import java.io.File;
import java.io.IOException;
import java.nio.channels.spi.AbstractSelectableChannel;
import kg.apc.emulators.SocketChannelEmul;
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
public class HTTPRawSamplerDirectFileTest {

    private class HTTPRawSamplerDirectFileEmul extends HTTPRawSamplerDirectFile {

        SocketChannelEmul sockEmul = new SocketChannelEmul();

        @Override
        protected AbstractSelectableChannel getChannel() throws IOException {
            return sockEmul;
        }
    }

    public HTTPRawSamplerDirectFileTest() {
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
     * Test of processIO method, of class HTTPRawSamplerDirectFile.
     */
    @Test
    public void testProcessIO() throws Exception {
        System.out.println("processIO");
        SampleResult res = new SampleResult();
        res.sampleStart();
        HTTPRawSamplerDirectFileEmul instance = new HTTPRawSamplerDirectFileEmul();
        instance.setPort("0");
        String file = this.getClass().getResource("testSendFile.raw").getPath();
        instance.setRequestData("FileToInclude:"+file);
        instance.processIO(res);
        assertEquals(new File(file).length(), instance.sockEmul.getWrittenBytesCount());
    }

    @Test
    public void testProcessIO_with_headers() throws Exception {
        System.out.println("processIO");
        SampleResult res = new SampleResult();
        res.sampleStart();
        HTTPRawSamplerDirectFileEmul instance = new HTTPRawSamplerDirectFileEmul();
        instance.setPort("0");
        String file = this.getClass().getResource("testSendFile.raw").getPath();
        String prefix="GET / HTTP/1.0\r\n";
        instance.setRequestData(prefix+"FileToInclude:"+file);
        instance.processIO(res);
        assertEquals(new File(file).length()+prefix.length(), instance.sockEmul.getWrittenBytesCount());
    }
}
