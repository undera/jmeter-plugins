package kg.apc.charting.rows;

import kg.apc.charting.AbstractGraphPanelChartElement;
import kg.apc.charting.elements.GraphPanelChartAverageElement;
import kg.apc.charting.rows.GraphRowOverallAverages;
import java.util.Iterator;

import org.junit.*;

import static org.junit.Assert.*;

public class GraphRowOverallAveragesTest
{
   /**
    *
    */
   public GraphRowOverallAveragesTest()
   {
   }

   /**
    * 
    * @throws Exception
    */
   @BeforeClass
   public static void setUpClass()
        throws Exception
   {
   }

   /**
    *
    * @throws Exception
    */
   @AfterClass
   public static void tearDownClass()
        throws Exception
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
      Assert.assertEquals(expResult, result);
   }

   /**
    * Test of hasNext method, of class GraphRowOverallAverages.
    */
   @Test
   public void testHasNext()
   {
      System.out.println("hasNext");
      GraphRowOverallAverages instance = new GraphRowOverallAverages();
      Assert.assertTrue(instance.hasNext());
      assertEquals(instance, instance.next());
      Assert.assertFalse(instance.hasNext());
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
         Assert.fail("Exception expected");
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
      Assert.assertEquals(expResult, result);
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
         AbstractGraphPanelChartElement result = instance.setValue(value);
         Assert.fail("Exception expected");
      }
      catch (UnsupportedOperationException e)
      {
      }
   }

   @Test
   public void testSize()
   {
      System.out.println("size");
      GraphRowOverallAverages instance = new GraphRowOverallAverages();
      int expResult = 1;
      int result = instance.size();
      Assert.assertEquals(expResult, result);
   }

    /**
     * Test of getElement method, of class GraphRowOverallAverages.
     */
    @Test
    public void testGetElement()
    {
        System.out.println("getElement");

        long value = 100L;
        GraphRowOverallAverages instance = new GraphRowOverallAverages();
        AbstractGraphPanelChartElement expResult = new GraphPanelChartAverageElement(10);
        instance.add(value, 10);
        AbstractGraphPanelChartElement result = instance.getElement(value);
        Assert.assertTrue(instance.getElement(value).getValue() == expResult.getValue());
    }
}
