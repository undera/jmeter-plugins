/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.dotchart;

import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
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

   /**
    * Test of keyTyped method, of class DotChartGuiPanel.
    */
   @Test
   public void testKeyTyped()
   {
      System.out.println("keyTyped");
      DotChartGuiPanel instance = new DotChartGuiPanel(new DotChart());
      KeyEvent event = new KeyEvent(new JTextField(), KeyEvent.KEY_TYPED, 1, 0, KeyEvent.VK_UNDEFINED, (char) 200);
      instance.keyTyped(event);
   }

   /**
    * Test of keyPressed method, of class DotChartGuiPanel.
    */
   @Test
   public void testKeyPressed()
   {
   }

   /**
    * Test of keyReleased method, of class DotChartGuiPanel.
    */
   @Test
   public void testKeyReleased()
   {
   }

   /**
    * Test of actionPerformed method, of class DotChartGuiPanel.
    */
   @Test
   public void testActionPerformed()
   {
   }
}
