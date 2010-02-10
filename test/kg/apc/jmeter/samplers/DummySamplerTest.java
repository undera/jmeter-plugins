package kg.apc.jmeter.samplers;

import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class DummySamplerTest
{
   public DummySamplerTest()
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
    * Test of sample method, of class DummySampler.
    */
   @Test
   public void testSample()
   {
      System.out.println("sample");
      Entry e = null;
      String data="test";
      DummySampler instance = new DummySampler();
      instance.setResponseData(data);
      SampleResult result = instance.sample(e);
      assertEquals(data, result.getResponseDataAsString());
   }

   /**
    * Test of setSuccessful method, of class DummySampler.
    */
   @Test
   public void testSetSuccessful()
   {
      System.out.println("setSuccessful");
      boolean selected = false;
      DummySampler instance = new DummySampler();
      instance.setSuccessful(selected);
   }

   /**
    * Test of setResponseCode method, of class DummySampler.
    */
   @Test
   public void testSetResponseCode()
   {
      System.out.println("setResponseCode");
      String text = "";
      DummySampler instance = new DummySampler();
      instance.setResponseCode(text);
   }

   /**
    * Test of setResponseMessage method, of class DummySampler.
    */
   @Test
   public void testSetResponseMessage()
   {
      System.out.println("setResponseMessage");
      String text = "";
      DummySampler instance = new DummySampler();
      instance.setResponseMessage(text);
   }

   /**
    * Test of setResponseData method, of class DummySampler.
    */
   @Test
   public void testSetResponseData()
   {
      System.out.println("setResponseData");
      String text = "";
      DummySampler instance = new DummySampler();
      instance.setResponseData(text);
   }
}
