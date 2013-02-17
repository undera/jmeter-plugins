/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author undera
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({kg.apc.io.IoSuite.class, kg.apc.charting.ChartingSuite.class, kg.apc.jmeter.JmeterSuite.class})
public class ApcSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
