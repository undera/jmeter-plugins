package kg.apc.jmeter.threads;

import kg.apc.jmeter.util.TestJMeterUtils;
import org.apache.jmeter.testelement.TestElement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class UltimateThreadGroupGuiTest
{
   public UltimateThreadGroupGuiTest()
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

   @Test
   public void testInit()
   {
      System.out.println("init");
      UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
      instance.init();
   }

   @Test
   public void testInitTableModel()
   {
      System.out.println("initTableModel");
      UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
      instance.initTableModel();
   }

   @Test
   public void testGetLabelResource()
   {
      System.out.println("getLabelResource");
      UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
      String expResult = "UltimateThreadGroupGui";
      String result = instance.getLabelResource();
      assertEquals(expResult, result);
   }

   @Test
   public void testGetStaticLabel()
   {
      System.out.println("getStaticLabel");
      UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
      String expResult = "Ultimate Thread Group";
      String result = instance.getStaticLabel();
      assertEquals(expResult, result);
   }

   @Test
   public void testCreateTestElement()
   {
      System.out.println("createTestElement");
      UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
      TestElement result = instance.createTestElement();
      assertTrue(result instanceof UltimateThreadGroup);
   }

   @Test
   public void testModifyTestElement()
   {
      System.out.println("modifyTestElement");
      TestElement tg = new UltimateThreadGroup();
      UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
      instance.addRowButton.doClick();
      instance.modifyTestElement(tg);
   }

   @Test
   public void testConfigure()
   {
      System.out.println("configure");
      TestElement tg = new UltimateThreadGroup();
      UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
      instance.configure(tg);
   }

   @Test
   public void testAddRow()
   {
      UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
      instance.addRowButton.doClick();
      instance.grid.editCellAt(0, 0);
      instance.addRowButton.doClick();
   }

   @Test
   public void testDeleteRow()
   {
      UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
      instance.addRowButton.doClick();
      instance.addRowButton.doClick();
      instance.grid.editCellAt(0, 0);
      instance.deleteRowButton.doClick();
      instance.deleteRowButton.doClick();
   }
}
