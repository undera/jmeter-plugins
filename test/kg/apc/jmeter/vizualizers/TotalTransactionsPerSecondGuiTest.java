/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.vizualizers;

import kg.apc.emulators.TestJMeterUtils;
import java.util.Collection;
import org.apache.jmeter.testelement.TestElement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author z000205
 */
public class TotalTransactionsPerSecondGuiTest
{

    public TotalTransactionsPerSecondGuiTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        TestJMeterUtils.createJmeterEnv();
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of getMenuCategories method, of class TotalTransactionsPerSecondGui.
     */
    @Test
    public void testGetMenuCategories()
    {
        System.out.println("getMenuCategories");
        TotalTransactionsPerSecondGui instance = new TotalTransactionsPerSecondGui();
        Collection result = instance.getMenuCategories();
        assertTrue(result.isEmpty());
    }

    /**
     * Test of createTestElement method, of class TotalTransactionsPerSecondGui.
     */
    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        TotalTransactionsPerSecondGui instance = new TotalTransactionsPerSecondGui();
        TestElement result = instance.createTestElement();
        assertNotNull(result);
    }
}
