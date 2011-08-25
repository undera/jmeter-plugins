/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter;

import java.io.PrintStream;
import java.util.ListIterator;
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
public class AbstractCMDToolTest {
    
    public AbstractCMDToolTest() {
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
     * Test of getLogicValue method, of class AbstractCMDTool.
     */
    @Test
    public void testGetLogicValue() {
        System.out.println("getLogicValue");
        String string = "";
        AbstractCMDTool instance = new AbstractCMDToolImpl();
        int expResult = 0;
        int result = instance.getLogicValue(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of processParams method, of class AbstractCMDTool.
     */
    @Test
    public void testProcessParams() {
        System.out.println("processParams");
        ListIterator<String> args = null;
        AbstractCMDTool instance = new AbstractCMDToolImpl();
        int expResult = 0;
        int result = instance.processParams(args);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of showHelp method, of class AbstractCMDTool.
     */
    @Test
    public void testShowHelp() {
        System.out.println("showHelp");
        PrintStream os = null;
        AbstractCMDTool instance = new AbstractCMDToolImpl();
        instance.showHelp(os);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class AbstractCMDToolImpl extends AbstractCMDTool {

        public int processParams(ListIterator<String> args) throws UnsupportedOperationException, IllegalArgumentException {
            return 0;
        }

        public void showHelp(PrintStream os) {
        }
    }
}
