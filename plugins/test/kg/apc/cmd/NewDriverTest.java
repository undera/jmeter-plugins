/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.cmd;

import java.net.URL;
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
public class NewDriverTest {
    
    public NewDriverTest() {
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
     * Test of addURL method, of class NewDriver.
     */
    @Test
    public void testAddURL() {
        System.out.println("addURL");
        URL url = null;
        NewDriver.addURL(url);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addPath method, of class NewDriver.
     */
    @Test
    public void testAddPath() throws Exception {
        System.out.println("addPath");
        String path = "";
        NewDriver.addPath(path);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getJMeterDir method, of class NewDriver.
     */
    @Test
    public void testGetJMeterDir() {
        System.out.println("getJMeterDir");
        String expResult = "";
        String result = NewDriver.getJMeterDir();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class NewDriver.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        NewDriver.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
