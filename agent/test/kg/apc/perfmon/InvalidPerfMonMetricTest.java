/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.perfmon;

import kg.apc.perfmon.InvalidPerfMonMetric;
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
public class InvalidPerfMonMetricTest {
    
    public InvalidPerfMonMetricTest() {
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
     * Test of getValue method, of class InvalidPerfMonMetric.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        StringBuilder res = new StringBuilder();
        InvalidPerfMonMetric instance = new InvalidPerfMonMetric();
        instance.getValue(res);
    }
}
