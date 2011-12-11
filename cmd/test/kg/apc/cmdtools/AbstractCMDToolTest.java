package kg.apc.cmdtools;

import kg.apc.cmdtools.AbstractCMDTool;
import java.io.PrintStream;
import java.util.ListIterator;
import junit.framework.TestCase;

/**
 *
 * @author undera
 */
public class AbstractCMDToolTest extends TestCase {

    public AbstractCMDToolTest() {
    }

    public static void setUpClass() throws Exception {
    }

    public static void tearDownClass() throws Exception {
    }

    public void setUp() {
    }

    public void tearDown() {
    }

    /**
     * Test of getLogicValue method, of class AbstractCMDTool.
     */
    public void testGetLogicValue() {
        System.out.println("getLogicValue");
        String string = "";
        AbstractCMDTool instance = new AbstractCMDToolImpl();
        int expResult = 0;
        int result = instance.getLogicValue(string);
        assertEquals(expResult, result);
    }

    /**
     * Test of processParams method, of class AbstractCMDTool.
     */
    public void testProcessParams() {
        System.out.println("processParams");
        ListIterator args = null;
        AbstractCMDTool instance = new AbstractCMDToolImpl();
        int expResult = 0;
        int result = instance.processParams(args);
        assertEquals(expResult, result);
    }

    /**
     * Test of showHelp method, of class AbstractCMDTool.
     */
    public void testShowHelp() {
        System.out.println("showHelp");
        PrintStream os = null;
        AbstractCMDTool instance = new AbstractCMDToolImpl();
        instance.showHelp(os);
    }

    public class AbstractCMDToolImpl extends AbstractCMDTool {

        public int processParams(ListIterator args) throws UnsupportedOperationException, IllegalArgumentException {
            return 0;
        }

        public void showHelp(PrintStream os) {
        }
    }
}
