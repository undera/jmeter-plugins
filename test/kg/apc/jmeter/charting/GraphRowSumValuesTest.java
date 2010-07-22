/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.charting;

import java.util.Iterator;
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
public class GraphRowSumValuesTest
{
   public GraphRowSumValuesTest()
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

   @Test
   public void testAdd()
   {
      System.out.println("add");
      long xVal = 0L;
      double yVal = 0.0;
      GraphRowSumValues instance = new GraphRowSumValues();
      instance.add(xVal, yVal);
   }

   @Test
   public void testIterator()
   {
      System.out.println("iterator");
      GraphRowSumValues instance = new GraphRowSumValues();
      Iterator result = instance.iterator();
      assertTrue(result instanceof Iterator);
   }
}
