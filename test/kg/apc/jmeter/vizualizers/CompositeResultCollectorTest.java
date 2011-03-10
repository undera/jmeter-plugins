/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.vizualizers;

import kg.apc.emulators.TestJMeterUtils;
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
public class CompositeResultCollectorTest {

    public CompositeResultCollectorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        TestJMeterUtils.createJmeterEnv();
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setCompositeModel method, of class CompositeResultCollector.
     */
    @Test
    public void testSetCompositeModel()
    {
        System.out.println("setCompositeModel");
        CompositeModel model = new CompositeModel();
        CompositeResultCollector instance = new CompositeResultCollector();
        instance.setCompositeModel(model);
    }

    /**
     * Test of getCompositeModel method, of class CompositeResultCollector.
     */
    @Test
    public void testGetCompositeModel()
    {
        System.out.println("getCompositeModel");
        CompositeResultCollector instance = new CompositeResultCollector();
        CompositeModel expResult = null;
        CompositeModel result = instance.getCompositeModel();
        assertEquals(expResult, result);
    }

}