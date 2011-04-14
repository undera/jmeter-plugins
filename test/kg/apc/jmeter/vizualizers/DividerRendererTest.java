package kg.apc.jmeter.vizualizers;

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
        System.out.println("setValue");
        Object value = new Long(54);
        DividerRenderer instance = new DividerRenderer(10);
        instance.setValue(value);
        assertEquals("5.4", instance.getText());
    }

}