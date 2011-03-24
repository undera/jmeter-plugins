/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.samplers;

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
public class HexStringUDPDecoderTest {

    public HexStringUDPDecoderTest() {
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
     * Test of encode method, of class HexStringUDPDecoder.
     */
    @Test
    public void testEncode() {
        System.out.println("encode");
        String data = "";
        HexStringUDPDecoder instance = new HexStringUDPDecoder();
        ByteBuffer expResult = null;
        ByteBuffer result = instance.encode(data);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of decode method, of class HexStringUDPDecoder.
     */
    @Test
    public void testDecode() {
        System.out.println("decode");
        byte[] buf = null;
        HexStringUDPDecoder instance = new HexStringUDPDecoder();
        byte[] expResult = null;
        byte[] result = instance.decode(buf);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}