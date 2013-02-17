package kg.apc.charting;

import kg.apc.charting.DividerRenderer;
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
public class DividerRendererTest {

    public DividerRendererTest() {
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
     * Test of setValue method, of class DividerRenderer.
     */
    @Test
    public void testSetValue() {
        
    }

    /**
     * Test of getFactor method, of class DividerRenderer.
     */
    @Test
    public void testGetFactor() {
        System.out.println("testGetFactor");
        DividerRenderer instance = new DividerRenderer(10);
        assertTrue(instance.getFactor() == 10);
    }

}