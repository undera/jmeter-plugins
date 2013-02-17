package kg.apc.jmeter.samplers;

import java.io.IOException;
import kg.apc.emulators.SocketEmulatorInputStream;
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
public class InfiniteGetTCPClientImplTest {

    public InfiniteGetTCPClientImplTest() {
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
     * Test of write method, of class InfiniteGetTCPClientImpl.
     */
    @Test
    public void testWrite_OutputStream_InputStream() throws IOException {
        System.out.println("write");
        SocketEmulatorOutputStream os = new SocketEmulatorOutputStream();
        SocketEmulatorInputStream is = new SocketEmulatorInputStream();
        InfiniteGetTCPClientImpl instance = new InfiniteGetTCPClientImpl();
        instance.write(os, is);
    }

    /**
     * Test of write method, of class InfiniteGetTCPClientImpl.
     */
    @Test
    public void testWrite_OutputStream_String() throws IOException {
        System.out.println("write");
        SocketEmulatorOutputStream out = new SocketEmulatorOutputStream();
        String string = "";
        InfiniteGetTCPClientImpl instance = new InfiniteGetTCPClientImpl();
        instance.write(out, string);
    }

    /**
     * Test of read method, of class InfiniteGetTCPClientImpl.
     */
    @Test
    public void testRead() {
        System.out.println("read");
        SocketEmulatorInputStream in = new SocketEmulatorInputStream();
        StringBuilder str=new StringBuilder();
        for(int i=0; i<6000; i++)
        {
            str.append('0');
        }
        in.setBytesToRead(str.toString());
        InfiniteGetTCPClientImpl instance = new InfiniteGetTCPClientImpl();
        String result = instance.read(in);
        assertEquals(5120, result.length());
        String result2 = instance.read(in);
        assertEquals(880, result2.length());
    }

}