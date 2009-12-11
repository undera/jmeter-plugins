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
   private static String IF_UUID = "c2ce97a0-8b15-11d1-96ab-00a0c9103fcf";
   private static String TRANS_SYNTAX = "8a885d04-1ceb-11c9-9fe8-08002b104860";
   private static String TEST_RPC_HOST = "fin-virt1";
   private static int TEST_RPC_PORT = 2000;

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
      instance.setInterfaceUUID(IF_UUID);
      instance.setTransferSyntax(TRANS_SYNTAX);

      try
      {
         con = new Socket(TEST_RPC_HOST, TEST_RPC_PORT);
         con.setSoTimeout(300000);
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

      instance.setReadLimit(28);
      String result = instance.read(is);
   }
}
