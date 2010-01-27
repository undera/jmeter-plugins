package kg.apc.jmeter.vizualizers;

import java.awt.Color;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ColorsDispatcherTest {

    public ColorsDispatcherTest() {
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
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

   /**
    * Test of getNextColor method, of class ColorsDispatcher.
    */
   @Test
   public void testGetNextColor()
   {
      System.out.println("getNextColor");
      ColorsDispatcher instance = new ColorsDispatcher();
      assertEquals(Color.red, instance.getNextColor());
      assertEquals(Color.green, instance.getNextColor());
      assertEquals(Color.blue, instance.getNextColor());
   }

   /**
    * Test of reset method, of class ColorsDispatcher.
    */
   @Test
   public void testReset()
   {
      System.out.println("reset");
      ColorsDispatcher instance = new ColorsDispatcher();
      assertEquals(Color.red, instance.getNextColor());
      instance.getNextColor();
      instance.getNextColor();
      instance.reset();
      assertEquals(Color.red, instance.getNextColor());
   }

}