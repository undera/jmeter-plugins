package kg.apc.jmeter.reporters;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Stephane Hoblingre
 */
public class JAutoStopPanelTest {

    public JAutoStopPanelTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
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
     * Test of configure method, of class JAutoStopPanel.
     */
    @Test
    public void testConfigure()
    {
        System.out.println("configure");
        AutoStop testElement = new AutoStop();
        JAutoStopPanel instance = new JAutoStopPanel();
        instance.configure(testElement);
    }

    /**
     * Test of modifyTestElement method, of class JAutoStopPanel.
     */
    @Test
    public void testModifyTestElement()
    {
        System.out.println("modifyTestElement");
        AutoStop testElement = new AutoStop();
        JAutoStopPanel instance = new JAutoStopPanel();
        instance.modifyTestElement(testElement);
    }

    /**
     * Test of initFields method, of class JAutoStopPanel.
     */
    @Test
    public void testInitFields()
    {
        System.out.println("initFields");
        JAutoStopPanel instance = new JAutoStopPanel();
        instance.initFields();
    }

}