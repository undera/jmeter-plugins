package kg.apc.jmeter.threads;

import java.util.List;
import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.threads.AbstractThreadGroup;
import kg.apc.jmeter.util.TestJMeterUtils;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.ObjectProperty;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author apc
 */
public class UltimateThreadGroupGuiTest
{
   /**
    *
    */
   public UltimateThreadGroupGuiTest()
   {
   }

   /**
    *
    * @throws Exception
    */
   @BeforeClass
   public static void setUpClass() throws Exception
   {
      TestJMeterUtils.createJmeterEnv();
   }

   /**
    *
    * @throws Exception
    */
   @AfterClass
   public static void tearDownClass() throws Exception
   {
   }

   /**
    *
    */
   @Before
   public void setUp()
   {
   }

   /**
    *
    */
   @After
   public void tearDown()
   {
   }

   /**
    *
    */
   @Test
   public void testInit()
   {
      System.out.println("init");
      UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
      instance.init();
   }

   /**
    *
    */
   @Test
   public void testGetLabelResource()
   {
      System.out.println("getLabelResource");
      UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
      String expResult = "UltimateThreadGroupGui";
      String result = instance.getLabelResource();
      assertEquals(expResult, result);
   }

   /**
    *
    */
   @Test
   public void testGetStaticLabel()
   {
      System.out.println("getStaticLabel");
      UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
      String result = instance.getStaticLabel();
      assertTrue(result.length()>0);
   }

   /**
    *
    */
   @Test
   public void testCreateTestElement()
   {
      System.out.println("createTestElement");
      UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
      TestElement result = instance.createTestElement();
      assertTrue(result instanceof UltimateThreadGroup);
   }

   /**
    *
    */
   @Test
   public void testModifyTestElement()
   {
      System.out.println("modifyTestElement");
      UltimateThreadGroup tg = new UltimateThreadGroup();
      UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
      //instance.addRowButton.doClick();
      instance.modifyTestElement(tg);
      CollectionProperty data = (CollectionProperty) tg.getData();
      assertEquals(instance.grid.getModel().getColumnCount(), data.size());
      assertEquals(instance.grid.getModel().getRowCount(), ((List<?>) data.get(0).getObjectValue()).size());
   }

   /**
    *
    */
   @Test
   public void testConfigure()
   {
      System.out.println("configure");
      UltimateThreadGroup tg = new UltimateThreadGroup();
      List<Integer> list = new ArrayList();
      list.add(1);
      CollectionProperty rows = new CollectionProperty(UltimateThreadGroup.DATA_PROPERTY,
            new ArrayList<Object>());
      rows.addItem(list);
      rows.addItem(list);
      rows.addItem(list);
      rows.addItem(list);
      rows.addItem(list);
      tg.setData(rows);
      UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
      tg.setProperty(new ObjectProperty(AbstractThreadGroup.MAIN_CONTROLLER, tg));
      instance.configure(tg);
   }

 
   /**
    *
    */
   @Test
   public void testClearGui()
   {
      System.out.println("clearGui");
      UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
      instance.clearGui();
   }

   /**
    *
    */
   @Test
   public void testTableChanged()
   {
      System.out.println("tableChanged");
      TableModelEvent e = null;
      UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
      instance.tableChanged(e);
   }

   /**
    *
    */
   @Test
   public void testEditingStopped()
   {
      System.out.println("editingStopped");
      ChangeEvent e = null;
      UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
      instance.editingStopped(e);
   }

   /**
    *
    */
   @Test
   public void testEditingCanceled()
   {
      System.out.println("editingCanceled");
      ChangeEvent e = null;
      UltimateThreadGroupGui instance = new UltimateThreadGroupGui();
      instance.editingCanceled(e);
   }
}
