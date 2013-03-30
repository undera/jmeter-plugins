/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc;

import kg.apc.charting.ChartingSuite;
import kg.apc.cmdtools.CmdtoolsSuite;
import kg.apc.io.IoSuite;
import kg.apc.jmeter.JmeterSuite;
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
@Suite.SuiteClasses({ChartingSuite.class, IoSuite.class, JmeterSuite.class, CmdtoolsSuite.class})
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
