package kg.apc.jmeter.threads;

import java.util.ArrayList;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jorphan.collections.HashTree;
import org.apache.jmeter.threads.JMeterThread;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class UltimateThreadGroupTest
{
   private UltimateThreadGroup instance;
   private PowerTableModel dataModel;

   public UltimateThreadGroupTest()
   {
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
   public void setUp()
   {
      instance = new UltimateThreadGroup();
      dataModel = new PowerTableModel(UltimateThreadGroupGui.columnIdentifiers, UltimateThreadGroupGui.columnClasses);
      dataModel.addRow(new Integer[]
            {
               1, 2, 3, 4
            });
      dataModel.addRow(new Integer[]
            {
               5, 6, 7, 8
            });
      dataModel.addRow(new Integer[]
            {
               9, 10, 11, 12
            });
   }

   @After
   public void tearDown()
   {
   }

   @Test
   public void testScheduleThread()
   {
      System.out.println("scheduleThread");
      HashTree hashtree = new HashTree();
      hashtree.add(new LoopController());
      JMeterThread thread = new JMeterThread(hashtree, null, null);

      instance.scheduleThread(thread);
   }

   @Test
   public void testSetData()
   {
      System.out.println("setSchedule");
      CollectionProperty prop = new CollectionProperty(UltimateThreadGroup.DATA_PROPERTY, new ArrayList<Object>());
      instance.setData(prop);
   }

   @Test
   public void testGetData()
   {
      System.out.println("getSchedule");
      CollectionProperty prop = new CollectionProperty(UltimateThreadGroup.DATA_PROPERTY, new ArrayList<Object>());
      prop.addItem(dataModel.getColumnData(dataModel.getColumnName(0)));
      CollectionProperty expResult = prop;
      instance.setData(prop);
      JMeterProperty result = instance.getData();
      assertFalse(result instanceof NullProperty);
      assertEquals(expResult.getStringValue(), result.getStringValue());
   }

   @Test
   public void testGetNumThreads()
   {
      System.out.println("getNumThreads");
      CollectionProperty prop = new CollectionProperty(UltimateThreadGroup.DATA_PROPERTY, new ArrayList<Object>());
      prop.addItem(dataModel.getColumnData(dataModel.getColumnName(0)));
      instance.setData(prop);

      int expResult = 15;
      int result = instance.getNumThreads();
      assertEquals(expResult, result);
   }
}
