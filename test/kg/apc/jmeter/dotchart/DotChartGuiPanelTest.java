/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.dotchart;

import java.awt.event.ItemEvent;
import javax.swing.JCheckBox;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author APC
 */
public class DotChartGuiPanelTest
{
   public DotChartGuiPanelTest()
   {
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
   public void setUp()
   {
   }

   @After
   public void tearDown()
   {
   }

   @Test
   public void testItemStateChanged()
   {
      System.out.println("itemStateChanged");
      int id = 0;
      int stateChange = ItemEvent.ITEM_STATE_CHANGED;
      DotChartGuiPanel instance = new DotChartGuiPanel(new DotChart());

      JCheckBox checkbox = new JCheckBox();
      ItemEvent e = new ItemEvent(checkbox, id, this, stateChange);
      instance.itemStateChanged(e);
   }
}
