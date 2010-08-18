/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.vizualizers;

import java.util.Calendar;
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
public class DateTimeRendererTest
{
   /**
    *
    */
   public DateTimeRendererTest()
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
    * Test of setValue method, of class DateTimeRenderer.
    */
   @Test
   public void testSetValue()
   {
      System.out.println("setValue");
      DateTimeRenderer instance = new DateTimeRenderer("HH:mm:ss");

      Calendar test = Calendar.getInstance();

      test.set(Calendar.HOUR_OF_DAY, 3);
      test.set(Calendar.MINUTE, 16);
      test.set(Calendar.SECOND, 40);
      test.set(Calendar.MILLISECOND, 0);
      
      instance.setValue(null);
      assertEquals("", instance.getText());

      instance.setValue(test.getTimeInMillis());
      String text = instance.getText();
      assertEquals("03:16:40", text);
   }


   /**
    *
    */
   @Test
   public void testConstructors()
   {
      DateTimeRenderer i1=new DateTimeRenderer();
      DateTimeRenderer i2=new DateTimeRenderer("HH");
   }
}
