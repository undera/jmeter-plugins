package kg.apc.jmeter.functions;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

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
        assertNull(FifoMap.getInstance().get("testget"));
        assertEquals("testget", FifoMap.getInstance().get("testget"));
    }

    /**
     * Test of pop method, of class FifoMap.
     */
    @Test
    public void testPop() throws Exception {
        System.out.println("pop");
        String fifoName = "test";
        FifoMap instance = FifoMap.getInstance();
        // we rely on testPut method, since we have a singletone
        assertEquals("val1", instance.pop(fifoName));
        assertEquals("val2", instance.pop(fifoName));
        assertEquals("test", instance.pop(""));
    }
}
