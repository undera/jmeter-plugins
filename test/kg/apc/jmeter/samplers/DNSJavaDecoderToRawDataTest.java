/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.samplers;

import java.nio.ByteBuffer;
import kg.apc.emulators.SocketEmulatorOutputStream;
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
public class DNSJavaDecoderToRawDataTest {

    private static class DNSJavaDecoderToRawDataEmul extends DNSJavaDecoderToRawData {

        public DNSJavaDecoderToRawDataEmul() {
            os = new SocketEmulatorOutputStream();
        }

        public SocketEmulatorOutputStream getOS() {
            return (SocketEmulatorOutputStream) os;
        }
    }

    public DNSJavaDecoderToRawDataTest() {
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
     * Test of encode method, of class DNSJavaDecoderToRawData.
     */
    @Test
    public void testEncode() {
        System.out.println("encode");
        String data = ". A IN";
        DNSJavaDecoderToRawDataEmul instance = new DNSJavaDecoderToRawDataEmul();
        ByteBuffer result = instance.encode(data);
        assertNotNull(result);
        assertTrue(instance.getOS().getWrittenBytesAsHexString().startsWith("33"));
    }
}
