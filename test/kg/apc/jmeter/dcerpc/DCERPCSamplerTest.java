package kg.apc.jmeter.dcerpc;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DCERPCSamplerTest
{

   private Socket con;
   private OutputStream os;
   private InputStream is;
   private DCERPCSampler instance;

   public DCERPCSamplerTest()
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
      instance = new DCERPCSampler();

      try
      {
         con = new Socket("fin-virt2", 2000);
         con.setSoTimeout(10000);
      }
      catch (UnknownHostException ex)
      {
         Logger.getLogger(DCERPCSamplerTest.class.getName()).log(Level.SEVERE, null, ex);
      }
      catch (IOException ex)
      {
         Logger.getLogger(DCERPCSamplerTest.class.getName()).log(Level.SEVERE, null, ex);
      }
      try
      {
         os = con.getOutputStream();
      }
      catch (IOException ex)
      {
         Logger.getLogger(DCERPCSamplerTest.class.getName()).log(Level.SEVERE, null, ex);
      }
      try
      {
         is = con.getInputStream();
      }
      catch (IOException ex)
      {
         Logger.getLogger(DCERPCSamplerTest.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   @After
   public void tearDown()
   {
      try
      {
         con.close();
      }
      catch (IOException ex)
      {
         Logger.getLogger(DCERPCSamplerTest.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   /**
    * Test of write method, of class DCERPCSampler.
    */
   @Test
   public void testWrite_OutputStream_InputStream()
   {
      System.out.println("write");
      instance.write(os, is);
   }

   /**
    * Test of write method, of class DCERPCSampler.
    */
   @Test
   public void testWrite_OutputStream_String()
   {
      System.out.println("write");

      try
      {
         instance.write(os, "");
         fail("Operation must throw exception!");
      }
      catch (UnsupportedOperationException e)
      {
         System.out.println("Ok, we got our exception");
      }
   }

   /**
    * Test of read method, of class DCERPCSampler.
    */
   @Test
   public void testRead()
   {
      System.out.println("read");

      instance.write(os, is);

      String expResult = "05000c03100000003c00000001000000d016d0162b9c874705003230303000370100000000000000045d888aeb1cc9119fe808002b10486002000000";
      String result = instance.read(is);
      System.out.println(expResult);
      System.out.println(result);
      System.out.println(result.substring(48, 62));
      assertEquals(expResult.substring(0, 40), result.substring(0, 40));
      assertEquals(expResult.substring(48, 62), result.substring(48, 62));
   }
}
