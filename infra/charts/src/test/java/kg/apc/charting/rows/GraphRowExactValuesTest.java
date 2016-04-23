package kg.apc.charting.rows;

import kg.apc.charting.AbstractGraphPanelChartElement;
import kg.apc.charting.elements.GraphPanelChartExactElement;
import kg.apc.charting.rows.GraphRowExactValues;
import java.util.Iterator;
import java.util.Map.Entry;

import org.junit.*;

import static org.junit.Assert.*;

public class GraphRowExactValuesTest
{
   /**
    *
    */
   public GraphRowExactValuesTest()
   {
   }

   /**
    *
    * @throws Exception
    */
   @BeforeClass
   public static void setUpClass() throws Exception
   {
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
    * Test of add method, of class GraphRowExactValues.
    */
   @Test
   public void testAdd()
   {
      System.out.println("add");
      long xVal = 0L;
      GraphRowExactValues instance = new GraphRowExactValues();
      instance.add(xVal, 1);
      instance.add(xVal, 2);
      instance.add(xVal, 3);
   }

   /**
    * Test of iterator method, of class GraphRowExactValues.
    */
   @Test
   public void testIterator()
   {
      System.out.println("iterator");
      GraphRowExactValues instance = new GraphRowExactValues();
      instance.add(1, 1);
      instance.add(1, 2);
      instance.add(1, 3);
      Iterator<Entry<Long, AbstractGraphPanelChartElement>> result = instance.iterator();
      assertEquals(1, ((AbstractGraphPanelChartElement) result.next().getValue()).getValue(), 0.001);
      assertEquals(2, ((AbstractGraphPanelChartElement) result.next().getValue()).getValue(), 0.001);
      assertEquals(3, ((AbstractGraphPanelChartElement) result.next().getValue()).getValue(), 0.001);
   }

   /**
    * Test of hasNext method, of class GraphRowExactValues.
    */
   @Test
   public void testHasNext()
   {
      System.out.println("hasNext");
      GraphRowExactValues instance = new GraphRowExactValues();
      boolean expResult = false;
      boolean result = instance.hasNext();
      Assert.assertEquals(expResult, result);
   }

   /**
    * Test of next method, of class GraphRowExactValues.
    */
   @Test
   public void testNext()
   {
      System.out.println("next");
      GraphRowExactValues instance = new GraphRowExactValues();
      instance.add(1, 1);
      Iterator it = instance.iterator();
      Entry result = (Entry) it.next();
      Assert.assertEquals(1L, result.getKey());
   }

   /**
    * Test of remove method, of class GraphRowExactValues.
    */
   @Test
   public void testRemove()
   {
      System.out.println("remove");
      GraphRowExactValues instance = new GraphRowExactValues();
      try
      {
         instance.remove();
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
      GraphRowExactValues instance = new GraphRowExactValues();
      int expResult = 0;
      int result = instance.size();
      Assert.assertEquals(expResult, result);
   }

    /**
     * Test of getElement method, of class GraphRowExactValues.
     */
    @Test
    public void testGetElement()
    {
        System.out.println("getElement");
        
        long value = 100L;
        GraphRowExactValues instance = new GraphRowExactValues();
        AbstractGraphPanelChartElement expResult = new GraphPanelChartExactElement(value, 2);
        instance.add(value, 2);
        AbstractGraphPanelChartElement result = instance.getElement(value);
        Assert.assertTrue(instance.getElement(value).getValue() == expResult.getValue());
    }
}
