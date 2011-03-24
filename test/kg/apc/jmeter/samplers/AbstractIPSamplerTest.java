/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.samplers;

import java.io.IOException;
import java.nio.channels.spi.AbstractSelectableChannel;
import org.apache.jmeter.samplers.Entry;
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
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of sample method, of class AbstractIPSampler.
     */
    @Test
    public void testSample() {
        System.out.println("sample");
        Entry entry = null;
        AbstractIPSampler instance = new AbstractIPSamplerImpl();
        SampleResult expResult = null;
        SampleResult result = instance.sample(entry);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class AbstractIPSamplerImpl extends AbstractIPSampler {

        public AbstractSelectableChannel getChannel() throws IOException {
            return null;
        }

        public byte[] processIO(SampleResult res) throws Exception {
            return null;
        }
    }

}