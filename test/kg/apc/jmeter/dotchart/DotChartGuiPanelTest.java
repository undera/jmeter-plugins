/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.dotchart;

import java.awt.event.ItemEvent;
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
public class DotChartGuiPanelTest {

    public DotChartGuiPanelTest() {
    }

   @BeforeClass
   public static void setUpClass()
        throws Exception
   {
   }

   @AfterClass
   public static void tearDownClass()
        throws Exception
   {
   }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

   @Test
   public void testItemStateChanged()
   {
      System.out.println("itemStateChanged");
      ItemEvent e = null;
      DotChartGuiPanel instance = null;
      instance.itemStateChanged(e);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

}