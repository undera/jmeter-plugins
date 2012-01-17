package kg.apc.jmeter.samplers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.spi.AbstractSelectableChannel;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author undera
 */
public class AbstractIPSamplerTest {

    public AbstractIPSamplerTest() {
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
     * Test of getHostName method, of class AbstractIPSampler.
     */
    @Test
    public void testGetHostName() {
        System.out.println("getHostName");
        AbstractIPSampler instance = new AbstractIPSamplerImpl();
        String expResult = "";
        String result = instance.getHostName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPort method, of class AbstractIPSampler.
     */
    @Test
    public void testGetPort() {
        System.out.println("getPort");
        AbstractIPSampler instance = new AbstractIPSamplerImpl();
        String expResult = "";
        String result = instance.getPort();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRequestData method, of class AbstractIPSampler.
     */
    @Test
    public void testGetRequestData() {
        System.out.println("getRequestData");
        AbstractIPSampler instance = new AbstractIPSamplerImpl();
        String expResult = "";
        String result = instance.getRequestData();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTimeout method, of class AbstractIPSampler.
     */
    @Test
    public void testGetTimeout() {
        System.out.println("getTimeout");
        AbstractIPSampler instance = new AbstractIPSamplerImpl();
        String expResult = "";
        String result = instance.getTimeout();
        assertEquals(expResult, result);
    }

    /**
     * Test of setHostName method, of class AbstractIPSampler.
     */
    @Test
    public void testSetHostName() {
        System.out.println("setHostName");
        String text = "";
        AbstractIPSampler instance = new AbstractIPSamplerImpl();
        instance.setHostName(text);
    }

    /**
     * Test of setPort method, of class AbstractIPSampler.
     */
    @Test
    public void testSetPort() {
        System.out.println("setPort");
        String text = "";
        AbstractIPSampler instance = new AbstractIPSamplerImpl();
        instance.setPort(text);
    }

    /**
     * Test of setRequestData method, of class AbstractIPSampler.
     */
    @Test
    public void testSetRequestData() {
        System.out.println("setRequestData");
        String text = "";
        AbstractIPSampler instance = new AbstractIPSamplerImpl();
        instance.setRequestData(text);
    }

    /**
     * Test of setTimeout method, of class AbstractIPSampler.
     */
    @Test
    public void testSetTimeout() {
        System.out.println("setTimeout");
        String text = "";
        AbstractIPSampler instance = new AbstractIPSamplerImpl();
        instance.setTimeout(text);
    }

    /**
     * Test of getPortAsInt method, of class AbstractIPSampler.
     */
    @Test
    public void testGetPortAsInt() {
        System.out.println("getPortAsInt");
        AbstractIPSampler instance = new AbstractIPSamplerImpl();
        int expResult = 0;
        int result = instance.getPortAsInt();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTimeoutAsInt method, of class AbstractIPSampler.
     */
    @Test
    public void testGetTimeoutAsInt() {
        System.out.println("getTimeoutAsInt");
        AbstractIPSampler instance = new AbstractIPSamplerImpl();
        int expResult = 0;
        int result = instance.getTimeoutAsInt();
        assertEquals(expResult, result);
    }

    /**
     * Test of sample method, of class AbstractIPSampler.
     */
    @Test
    public void testSample() {
        System.out.println("sample");
        Entry entry = null;
        AbstractIPSampler instance = new AbstractIPSamplerImpl();
        SampleResult result = instance.sample(entry);
        assertNotNull(result);
    }

    /**
     * Test of getChannel method, of class AbstractIPSampler.
     */
    @Test
    public void testGetChannel() throws Exception {
        System.out.println("getChannel");
        AbstractIPSampler instance = new AbstractIPSamplerImpl();
        AbstractSelectableChannel expResult = null;
        AbstractSelectableChannel result = instance.getChannel();
        assertEquals(expResult, result);
    }

    /**
     * Test of processIO method, of class AbstractIPSampler.
     */
    @Test
    public void testProcessIO() throws Exception {
        System.out.println("processIO");
        SampleResult res = null;
        AbstractIPSampler instance = new AbstractIPSamplerImpl();
        byte[] expResult = null;
        byte[] result = instance.processIO(res);
        assertEquals(expResult, result);
    }

    public class AbstractIPSamplerImpl extends AbstractIPSampler {

        public AbstractSelectableChannel getChannel() throws IOException {
            return null;
        }

        public byte[] processIO(SampleResult res) throws Exception {
            return null;
        }

        public boolean interrupt() {
            return true;
        }
    }

   /**
    * Test of getRecvBuf method, of class AbstractIPSampler.
    */
   @Test
   public void testGetRecvBuf() {
      System.out.println("getRecvBuf");
      AbstractIPSampler instance = new AbstractIPSamplerImpl();
      ByteBuffer result = instance.getRecvBuf();
      assertNotNull(result);
   }

}