package kg.apc.jmeter.perfmon.client;

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
public class UDPTransportTest {

    private UDPTransport instance;

    public UDPTransportTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        instance = new UDPTransport("", 0);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of disconnect method, of class UDPTransport.
     */
    @Test
    public void testDisconnect() {
        System.out.println("disconnect");
        instance.disconnect();
    }

    /**
     * Test of writeln method, of class UDPTransport.
     */
    @Test
    public void testWriteln() {
        System.out.println("writeln");
        String string = "";
        instance.writeln(string);
    }

    /**
     * Test of readln method, of class UDPTransport.
     */
    @Test
    public void testReadln() {
        System.out.println("readln");
        String expResult = "";
        String result = instance.readln();
        assertEquals(expResult, result);
    }
}
