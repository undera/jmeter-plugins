package kg.apc.jmeter.charting;

import kg.apc.jmeter.charting.GraphRowExactValues;
import kg.apc.jmeter.charting.AbstractGraphPanelChartElement;
import java.util.Iterator;
import java.util.Map.Entry;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class GraphRowExactValuesTest
{
   public GraphRowExactValuesTest()
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
   }

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
      Iterator<Entry<Long, Object>> result = instance.iterator();
      assertEquals(1, ((AbstractGraphPanelChartElement) result.next().getValue()).getValue(), 0.001);
      assertEquals(2, ((AbstractGraphPanelChartElement) result.next().getValue()).getValue(), 0.001);
      assertEquals(3, ((AbstractGraphPanelChartElement) result.next().getValue()).getValue(), 0.001);
   }
}
