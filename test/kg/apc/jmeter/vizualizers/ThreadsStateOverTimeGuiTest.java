package kg.apc.jmeter.vizualizers;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.Sample;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ThreadsStateOverTimeGuiTest
{
   private ThreadsStateOverTimeGui instance;
   public ThreadsStateOverTimeGuiTest()
   {
   }

   @BeforeClass
   public static void setUpClass()
        throws Exception
   {
        File propsFile = null;
        try
        {
            propsFile = File.createTempFile("jmeter-plugins", "testProps");
        }
        catch (IOException ex)
        {
        }

        JMeterUtils.loadJMeterProperties(propsFile.getAbsolutePath());
        JMeterUtils.setLocale(new Locale("ignoreResources"));
   }

   @AfterClass
   public static void tearDownClass()
        throws Exception
   {
   }

   @Before
   public void setUp()
   {
      instance = new ThreadsStateOverTimeGui();
   }

   @After
   public void tearDown()
   {
   }

   /**
    * Test of add method, of class ThreadsStateOverTimeGui.
    */
   @Test
   public void testAdd()
   {
      System.out.println("add");
      SampleResult res = new SampleResult();
      instance.add(res);
   }

   /**
    * Test of clearData method, of class ThreadsStateOverTimeGui.
    */
   @Test
   public void testClearData()
   {
      System.out.println("clearData");
      instance.clearData();
   }

   /**
    * Test of getImage method, of class ThreadsStateOverTimeGui.
    */
   @Test
   public void testGetImage()
   {
      System.out.println("getImage");
      Image expResult = null;
      Image result = instance.getImage();
      assertEquals(expResult, result);
   }

   /**
    * Test of updateGui method, of class ThreadsStateOverTimeGui.
    */
   @Test
   public void testUpdateGui_Sample()
   {
      System.out.println("updateGui");
      Sample sample = null;
      instance.updateGui(sample);
   }

   /**
    * Test of updateGui method, of class ThreadsStateOverTimeGui.
    */
   @Test
   public void testUpdateGui_0args()
   {
      System.out.println("updateGui");
      instance.updateGui();
   }
}
