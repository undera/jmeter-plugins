/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.config;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author APC
 */
public class VariablesFromCSVFileBeanInfoTest
{
   public VariablesFromCSVFileBeanInfoTest()
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
         JMeterUtils.loadJMeterProperties(propsFile.getAbsolutePath());
      }
      catch (IOException ex)
      {
         fail("err");
      }

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
   }

   @After
   public void tearDown()
   {
   }

   @Test
   public void testConstructor()
   {
      VariablesFromCSVFileBeanInfo instance = new VariablesFromCSVFileBeanInfo();
   }
}
