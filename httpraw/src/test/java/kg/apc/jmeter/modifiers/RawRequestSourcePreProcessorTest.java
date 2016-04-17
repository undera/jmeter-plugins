package kg.apc.jmeter.modifiers;

import kg.apc.emulators.TestJMeterUtils;
import kg.apc.jmeter.RuntimeEOFException;
import org.apache.jmeter.protocol.tcp.sampler.BinaryTCPClientImpl;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static org.junit.Assert.*;

public class RawRequestSourcePreProcessorTest {

    private String basedir;

    public RawRequestSourcePreProcessorTest() {
        String file = RawRequestSourcePreProcessorTest.class.getResource("/rawdata_broken.txt").getPath();
        basedir = file.substring(0, file.lastIndexOf("/"));
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        JMeterContextService.getContext().setVariables(new JMeterVariables());
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testProcess_crlf_metaline() {
        System.out.println("crlf_metaline");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.testStarted();
        instance.setFileName(basedir + "/rawdata_crlf_metaline.txt");
        instance.setRewindOnEOF(true);
        for (int n = 1; n < 10; n++) {
            instance.process();
            String result = JMeterContextService.getContext().getVariables().get(instance.getVarName());
            //System.out.println(n+"["+result+"]");
            assertTrue(result.length() > 0);
            assertTrue(!(result.startsWith("\n")));
            assertTrue(!(result.startsWith("\r")));
        }
    }

    /**
     * Test of process method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testProcess() {
        System.out.println("process zeroterm looped");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.testStarted();
        instance.setFileName(basedir + "/rawdata_zeroterm_looped.txt");
        instance.setRewindOnEOF(true);
        for (int n = 1; n < 10; n++) {
            instance.process();
            String result = JMeterContextService.getContext().getVariables().get(instance.getVarName());
            //System.out.println(n+" "+result);
            assertTrue(result.length() > 0);
        }
    }

    /**
     * Test of process method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testProcess_zeroterminated() {
        System.out.println("zeroterm");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.testStarted();
        instance.setFileName(basedir + "/rawdata_zeroterm_looped.txt");
        int n = 1;
        try {
            for (; n < 20; n++) {
                instance.process();
                JMeterContextService.getContext().getVariables().get(instance.getVarName());
                //System.out.println(n);
            }
            fail("EOF expected");
        } catch (RuntimeEOFException ex) {
            assertEquals(6, n);
        }
    }

    /**
     * Test of process method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testProcess_nonzeroterminated() {
        System.out.println("nonzeroterm");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.testStarted();
        instance.setFileName(basedir + "/rawdata_nonzeroterm.txt");
        int n = 1;
        try {
            for (; n < 20; n++) {
                instance.process();
                JMeterContextService.getContext().getVariables().get(instance.getVarName());
            }
            fail("EOF expected");
        } catch (RuntimeEOFException ex) {
            assertEquals(6, n);
        }
    }

    /**
     * Test of process method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testProcess_broken() {
        System.out.println("broken");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.testStarted();
        instance.setFileName(basedir + "/rawdata_broken.txt");
        int n = 1;
        try {
            for (; n < 20; n++) {
                instance.process();
                JMeterContextService.getContext().getVariables().get(instance.getVarName());
            }
        } catch (RuntimeException ex) {
            assertEquals(3, n);
        }
    }

    @Test
    public void testProcess_file_not_found() {
        System.out.println("file_not_found");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.setFileName("/nofilefortest");
        instance.process();
        String result = JMeterContextService.getContext().getVariables().get(instance.getVarName());
        assertNull(result);
    }

    /**
     * Test of getVarName method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testGetVarName() {
        System.out.println("getVarName");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        String expResult = "";
        String result = instance.getVarName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setVarName method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testSetVarName() {
        System.out.println("setVarName");
        String name = "";
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.setVarName(name);
    }

    /**
     * Test of getFileName method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testGetFileName() {
        System.out.println("getFileName");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        String expResult = "";
        String result = instance.getFileName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setFileName method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testSetFileName() {
        System.out.println("setFileName");
        String filename = "";
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.setFileName(filename);
    }

    /**
     * Test of setRewindOnEOF method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testSetRewindOnEOF() {
        System.out.println("setRewindOnEOF");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.setRewindOnEOF(false);
    }

    /**
     * Test of getRewindOnEOF method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testGetRewindOnEOF() {
        System.out.println("getRewindOnEOF");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        boolean result = instance.getRewindOnEOF();
        assertEquals(false, result);
    }

    @Test
    public void testProcess_bug2() {
        System.out.println("bug with binary data");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.testStarted();
        instance.setEncodeHex(true);
        instance.setFileName(basedir + "/protobuf.one.ammo");
        instance.setVarName("rawData");
        instance.setRewindOnEOF(false);
        instance.process();
        byte[] ar = BinaryTCPClientImpl.hexStringToByteArray(JMeterContextService.getContext().getVariables().get(instance.getVarName()));

        assertTrue(63 != ar[210]);
        assertEquals(3130, ar.length);
        assertEquals(12, ar[1]);
    }

    @Test
    public void testSerialization() throws IOException {
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(instance);
    }

    /**
     * Test of isHexEncode method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testIsHexEncode() {
        System.out.println("isHexEncode");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        boolean result = instance.isHexEncode();
        assertEquals(result, false);
    }

    /**
     * Test of setEncodeHex method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testSetEncodeHex() {
        System.out.println("setEncodeHex");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.setEncodeHex(false);
    }

    /**
     * Test of testStarted method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testTestStarted_0args() {
        System.out.println("testStarted");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.testStarted();
    }

    /**
     * Test of testStarted method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testTestStarted_String() {
        System.out.println("testStarted");
        String host = "";
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.testStarted(host);
    }

    /**
     * Test of testEnded method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testTestEnded_0args() {
        System.out.println("testEnded");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.testEnded();
    }

    /**
     * Test of testEnded method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testTestEnded_String() {
        System.out.println("testEnded");
        String host = "";
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.testEnded(host);
    }
}