package kg.apc.jmeter.charting;

import kg.apc.jmeter.charting.GraphPanelChartAverageElement;
import kg.apc.jmeter.charting.GraphRowOverallAverages;
import java.util.Iterator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class GraphRowOverallAveragesTest
{
   public GraphRowOverallAveragesTest()
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

   /**
    * Test of add method, of class GraphRowOverallAverages.
    */
   @Test
   public void testAdd()
   {
      System.out.println("add");
      GraphRowOverallAverages instance = new GraphRowOverallAverages();
      instance.add(0, 0);
      assertEquals(0, instance.getKey(), 0.01);
      assertEquals(0, instance.getValue().getValue(), 0.01);

      instance.add(1, 1);
      assertEquals(1, instance.getKey(), 0.01);
      assertEquals(0.5, instance.getValue().getValue(), 0.01);

      instance.add(1, 1);
      assertEquals(1, instance.getKey(), 0.01);
      assertEquals(0.66, instance.getValue().getValue(), 0.01);

      instance.add(5, 10);
      assertEquals(2, instance.getKey(), 0.01);
      assertEquals(3, instance.getValue().getValue(), 0.01);
   }

   /**
    * Test of iterator method, of class GraphRowOverallAverages.
    */
   @Test
   public void testIterator()
   {
      System.out.println("iterator");
      GraphRowOverallAverages instance = new GraphRowOverallAverages();
      Iterator expResult = instance;
      Iterator result = instance.iterator();
      assertEquals(expResult, result);
   }

   /**
    * Test of hasNext method, of class GraphRowOverallAverages.
    */
   @Test
   public void testHasNext()
   {
      System.out.println("hasNext");
      GraphRowOverallAverages instance = new GraphRowOverallAverages();
      assertTrue(instance.hasNext());
      assertEquals(instance, instance.next());
      assertFalse(instance.hasNext());
   }

   /**
    * Test of next method, of class GraphRowOverallAverages.
    */
   @Test
   public void testNext()
   {
      System.out.println("next");
      GraphRowOverallAverages instance = new GraphRowOverallAverages();
      assertEquals(instance, instance.next());
   }

   /**
    * Test of remove method, of class GraphRowOverallAverages.
    */
   @Test
   public void testRemove()
   {
      System.out.println("remove");
      GraphRowOverallAverages instance = new GraphRowOverallAverages();
      try
      {
         instance.remove();
         fail("Exception expected");
      }
      catch (UnsupportedOperationException e)
      {
      }
   }

   /**
    * Test of getKey method, of class GraphRowOverallAverages.
    */
   @Test
   public void testGetKey()
   {
      System.out.println("getKey");
      GraphRowOverallAverages instance = new GraphRowOverallAverages();
      long expResult = 0;
      long result = instance.getKey();
      assertEquals(expResult, result);
   }

   /**
    * Test of getValue method, of class GraphRowOverallAverages.
    */
   @Test
   public void testGetValue()
   {
      System.out.println("getValue");
      GraphRowOverallAverages instance = new GraphRowOverallAverages();
      GraphPanelChartAverageElement result = instance.getValue();
      assertEquals(0, result.getValue(), 0.01);
   }

   /**
    * Test of setValue method, of class GraphRowOverallAverages.
    */
   @Test
   public void testSetValue()
   {
      System.out.println("setValue");
      GraphPanelChartAverageElement value = null;
      GraphRowOverallAverages instance = new GraphRowOverallAverages();
      try
      {
         GraphPanelChartAverageElement result = instance.setValue(value);
         fail("Exception expected");
      }
      catch (UnsupportedOperationException e)
      {
      }
   }
}
