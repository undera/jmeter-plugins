/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter;

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
public class JMeterPluginsUtilsTest {

    public JMeterPluginsUtilsTest() {
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
     * Test of prefixLabel method, of class JMeterPluginsUtils.
     */
    @Test
    public void testPrefixLabel() {
        System.out.println("prefixLabel");
        String string = "TEST";
        String result = JMeterPluginsUtils.prefixLabel(string);
        assertTrue(result.indexOf(string) != -1);
    }

}