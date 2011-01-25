package kg.apc.jmeter.charting;

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
    private static final String HHMMSS = "HH:mm:ss";
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
      DateTimeRenderer instance = new DateTimeRenderer(HHMMSS);

      Calendar test = Calendar.getInstance();

      test.set(Calendar.HOUR_OF_DAY, 3);
      test.set(Calendar.MINUTE, 16);
      test.set(Calendar.SECOND, 40);
      test.set(Calendar.MILLISECOND, 0);
      
      instance.setValue(test.getTimeInMillis());
      String text = instance.getText();
      assertEquals("03:16:40", text);
   }


   /**
    *
    */
   @Test
   public void testConstructor_null()
   {
      DateTimeRenderer instance=new DateTimeRenderer();
      instance.setValue(null);
      assertEquals("", instance.getText());
   }

   @Test
   public void testConstructor2()
   {
      assertNotNull(new DateTimeRenderer(HHMMSS));
   }

   @Test
   public void testConstructor3()
   {
      System.out.println("relTime");
      Calendar test = Calendar.getInstance();

      test.set(Calendar.HOUR_OF_DAY, 3);
      test.set(Calendar.MINUTE, 16);
      test.set(Calendar.SECOND, 40);
      test.set(Calendar.MILLISECOND, 0);
      Long end=test.getTimeInMillis();

      test.set(Calendar.MINUTE, 6);
      test.set(Calendar.SECOND, 40);
      Long start=test.getTimeInMillis();

      DateTimeRenderer instance=new DateTimeRenderer( HHMMSS, start);

      instance.setValue(end);
      String text = instance.getText();
      System.out.println(text);
      assertEquals("10:00", text.substring(3));
   }
}
