package kg.apc.jmeter.gui;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CustomNumberRendererTest {

    public CustomNumberRendererTest() {
    }

   @BeforeClass
   public static void setUpClass() throws Exception {
   }

   @AfterClass
   public static void tearDownClass() throws Exception {
   }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

   /**
    * Test of setValue method, of class CustomNumberRenderer.
    */
   @Test
   public void testSetValue() {
      System.out.println("setValue");
      CustomNumberRenderer instance = new CustomNumberRenderer("#,###.#", ' ');
      instance.setValue(1000000);
      String result = instance.getText();
      assertTrue("1 000 000".equals(result));
      instance = new CustomNumberRenderer("#.#");
      instance.setValue(1000000);
      result = instance.getText();
      assertTrue("1000000".equals(result));
   }
}