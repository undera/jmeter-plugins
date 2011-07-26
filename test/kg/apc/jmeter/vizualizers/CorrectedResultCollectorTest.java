/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class CorrectedResultCollectorTest {
    
    public CorrectedResultCollectorTest() {
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
     * Test of testStarted method, of class CorrectedResultCollector.
     */
    @Test
    public void testTestStarted() {
        System.out.println("testStarted");
        CorrectedResultCollector instance = new CorrectedResultCollector();
        instance.testStarted();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
