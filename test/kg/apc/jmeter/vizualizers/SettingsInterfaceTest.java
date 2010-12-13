/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.vizualizers;

import javax.swing.JPanel;
import kg.apc.jmeter.charting.GraphPanelChart;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author APC
 */
public class SettingsInterfaceTest {

    public SettingsInterfaceTest() {
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

   @Test
   public void testGetGranulation()
   {
      System.out.println("getGranulation");
      SettingsInterface instance = new SettingsInterfaceImpl();
      int expResult = 0;
      int result = instance.getGranulation();
      assertEquals(expResult, result);
   }

   @Test
   public void testSetGranulation()
   {
      System.out.println("setGranulation");
      int granulation = 0;
      SettingsInterface instance = new SettingsInterfaceImpl();
      instance.setGranulation(granulation);
   }

   @Test
   public void testGetGraphPanelChart()
   {
      System.out.println("getGraphPanelChart");
      SettingsInterface instance = new SettingsInterfaceImpl();
      GraphPanelChart result = instance.getGraphPanelChart();
      assertNotNull(result);
   }

   public class SettingsInterfaceImpl
         implements SettingsInterface
   {
      public int getGranulation()
      {
         return 0;
      }

      public void setGranulation(int granulation)
      {
      }

      public GraphPanelChart getGraphPanelChart()
      {
         return new GraphPanelChart();
      }

        @Override
        public void switchModel(boolean aggregate)
        {
        }
   }

    /**
     * Test of switchModel method, of class SettingsInterface.
     */
    @Test
    public void testSwitchModel()
    {
        System.out.println("switchModel");
        boolean aggregate = false;
        SettingsInterface instance = new SettingsInterfaceImpl();
        instance.switchModel(aggregate);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}