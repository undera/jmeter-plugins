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
public class UDPTrafficDecoderTest {

    public UDPTrafficDecoderTest() {
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
     * Test of encode method, of class UDPTrafficDecoder.
     */
    @Test
    public void testEncode() {
        System.out.println("encode");
        String data = "";
        UDPTrafficDecoder instance = new UDPTrafficDecoderImpl();
        ByteBuffer expResult = null;
        ByteBuffer result = instance.encode(data);
        assertEquals(expResult, result);
    }

    /**
     * Test of decode method, of class UDPTrafficDecoder.
     */
    @Test
    public void testDecode() {
        System.out.println("decode");
        byte[] data = null;
        UDPTrafficDecoder instance = new UDPTrafficDecoderImpl();
        byte[] expResult = null;
        byte[] result = instance.decode(data);
        assertEquals(expResult, result);
    }

    public class UDPTrafficDecoderImpl implements UDPTrafficDecoder {

        public ByteBuffer encode(String data) {
            return null;
        }

        public byte[] decode(byte[] data) {
            return null;
        }
    }

}