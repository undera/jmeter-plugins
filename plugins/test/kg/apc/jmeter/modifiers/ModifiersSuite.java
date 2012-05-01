/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.modifiers;

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
@Suite.SuiteClasses({FifoPopPreProcessorGuiTest.class, CheckConsistencyActionTest.class, AnchorModifierGuiTest.class, RawRequestSourcePreProcessorTest.class, RawRequestSourcePreProcessorGuiTest.class, AnchorModifierTest.class, FifoPutPostProcessorTest.class, FifoMapTest.class, FifoPopPreProcessorTest.class})
public class ModifiersSuite {

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