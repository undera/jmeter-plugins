package kg.apc.jmeter.modifiers;

import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 *
 * @author undera
 */
public class FifoMapTest {

    public FifoMapTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        FifoMap.getInstance().clear();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getInstance method, of class FifoMap.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        FifoMap result = FifoMap.getInstance();
        assertNotNull(result);
    }

    /**
     * Test of put method, of class FifoMap.
     */
    @Test
    public void testPut() throws Exception {
        System.out.println("put");
        String k = "test";
        FifoMap instance = FifoMap.getInstance();
        instance.put(k, "val1");
        instance.put(k, "val2");
        instance.put("", "test");
    }

    /**
     * Test of get method, of class FifoMap.
     */
    @Test
    public void testGet() throws Exception {
        System.out.println("get");
        assertEquals("", FifoMap.getInstance().get("testget"));
        FifoMap.getInstance().put("testget", "testget");
        assertEquals("testget", FifoMap.getInstance().get("testget"));
    }

    /**
     * Test of pop method, of class FifoMap.
     */
    @Test
    public void testPop() throws Exception {
        System.out.println("pop");
        // setup data
        String fifoName = "test";
        FifoMap instance = FifoMap.getInstance();
        instance.put(fifoName, "val1");
        instance.put(fifoName, "val2");
        instance.put("", "test");
        // test and assert
        assertEquals("val1", instance.pop(fifoName, 0));
        assertEquals("val2", instance.pop(fifoName, 0));
        assertEquals("test", instance.pop("", 0));
    }

    /**
     * Test of length method, of class FifoMap.
     */
    @Test
    public void testLength() {
        System.out.println("length");
        String fifoName = "";
        FifoMap instance = FifoMap.getInstance();
        int expResult = 0;
        int result = instance.length(fifoName);
        assertEquals(expResult, result);
    }
}
