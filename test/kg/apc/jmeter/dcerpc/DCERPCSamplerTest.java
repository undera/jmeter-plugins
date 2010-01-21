package kg.apc.jmeter.dcerpc;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.jmeter.protocol.tcp.sampler.BinaryTCPClientImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DCERPCSamplerTest
{
   private SocketEmulatorOutputStream os;
   private SocketEmulatorInputStream is;
   private DCERPCSampler instance;
   private static String SERVER_UUID = "80d7862a-6160-4596-aaa9-1743e4c27638";
   private static String ABSTRACT_SYNTAX = "8a885d04-1ceb-11c9-9fe8-08002b104860";

   public DCERPCSamplerTest()
   {
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
   public void setUp()
   {
      instance = new DCERPCSampler();

      os = new SocketEmulatorOutputStream();
      is = new SocketEmulatorInputStream();
   }

   @After
   public void tearDown()
   {
      try
      {
         os.close();
         is.close();
      }
      catch (IOException ex)
      {
         Logger.getLogger(DCERPCSamplerTest.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   @Test
   public void testWrite_OutputStream_InputStream()
   {
      System.out.println("write");
      try
      {
         instance.write(os, is);
         fail("Operation must throw exception!");
      }
      catch (UnsupportedOperationException e)
      {
         System.out.println("Ok, we got our exception");
      }
   }

   @Test
   public void testWrite_OutputStream_String()
   {
      System.out.println("write");
      String str = "bind\n"
           + SERVER_UUID + "\n"
           + ABSTRACT_SYNTAX;

      instance.write(os, str);
      String expBytes = "0500b031000000048000000010000000ffffffd0160ffffffd0160000000001000000000001002a0ffffff860ffffffd70ffffff8060610ffffff96450ffffffaa0ffffffa917430ffffffe40ffffffc2763801000000045d0ffffff880ffffff8a0ffffffeb1c0ffffffc9110ffffff9f0ffffffe808002b10486002000000";
      String gotBytes = os.getWrittenBytes();
      assertEquals(expBytes, gotBytes);
   }

   @Test
   public void testWrite_OutputStream_MultiPDU()
   {
      System.out.println("write large");

      String large = "ff";
      for (int n = 0; n < 13; n++)
         large += large;

      String str = "1\n"
           + "0\n"
           + large;

      instance.write(os, str);
      String gotBytes = os.getWrittenBytes();
      assertEquals(73838, gotBytes.length());
   }

   @Test
   public void testRead()
   {
      System.out.println("read");
      String header = "05000203100000009c0000000b0000008400000000000000";
      String expStr = "00000000630000006300000000000200630000003c3f786d6c2076657273696f6e3d22312e30223f3e0d0a3c526573706f6e643e3c6e466f756e643e2020202020202020202020303c2f6e466f756e643e3c737472526573706f6e643e3c2f737472526573706f6e643e3c2f526573706f6e643e0d0a0000000000000000000000000000";

      is.setBytesToRead(BinaryTCPClientImpl.hexStringToByteArray(header + expStr));
      String result = instance.read(is);
      assertEquals(expStr, result);
   }
}
