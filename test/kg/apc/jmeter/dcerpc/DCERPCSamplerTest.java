package kg.apc.jmeter.dcerpc;

import kg.apc.emulators.SocketEmulatorInputStream;
import kg.apc.emulators.SocketEmulatorOutputStream;
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

/**
 *
 * @author apc
 */
public class DCERPCSamplerTest
{
   private SocketEmulatorOutputStream os;
   private SocketEmulatorInputStream is;
   private DCERPCSampler instance;
   private static String SERVER_UUID = "80d7862a-6160-4596-aaa9-1743e4c27638";
   private static String ABSTRACT_SYNTAX = "8a885d04-1ceb-11c9-9fe8-08002b104860";

   /**
    *
    */
   public DCERPCSamplerTest()
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
      instance = new DCERPCSampler();

      os = new SocketEmulatorOutputStream();
      is = new SocketEmulatorInputStream();
   }

   /**
    *
    */
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

   /**
    *
    */
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

   /**
    *
    */
   @Test
   public void testWrite_OutputStream_String()
   {
      System.out.println("write");
      String str = "bind "
           + SERVER_UUID + " "
           + ABSTRACT_SYNTAX;

      instance.write(os, str);
      String expBytes = "0500b031000000048000000010000000ffffffd0160fffff" +
           "fd0160000000001000000000001002a0ffffff860ffffffd70ffffff8060610" +
           "ffffff96450ffffffaa0ffffffa917430ffffffe40ffffffc27638010000000" +
           "45d0ffffff880ffffff8a0ffffffeb1c0ffffffc9110ffffff9f0ffffffe8080" +
           "02b10486002000000";
      String gotBytes = os.getWrittenBytesAsHexString();
      assertEquals(expBytes, gotBytes);
   }

   /**
    *
    */
   @Test
   public void testWrite_OutputStream_MultiPDU()
   {
      System.out.println("write MultiPDU");

      StringBuffer buf=new StringBuffer();
      for (int n = 0; n < 10000; n++)
         buf.append("ff");

      String str = "1 0\n"
           + buf.toString();

      instance.write(os, str);
      String gotBytes = os.getWrittenBytesAsHexString();
      assertEquals(90110, gotBytes.length());
   }

   /**
    *
    */
   @Test
   public void testRead()
   {
      System.out.println("read");
      String header = "05000203100000009c0000000b0000008400000000000000";
      String expStr = "00000000630000006300000000000200630000003" +
           "c3f786d6c2076657273696f6e3d22312e30223f3e0d0a3c526573706f6e" +
           "643e3c6e466f756e643e2020202020202020202020303c2f6e466f756e643" +
           "e3c737472526573706f6e643e3c2f737472526573706f6e643e3c2f52657370" +
           "6f6e643e0d0a0000000000000000000000000000";

      is.setBytesToRead(BinaryTCPClientImpl.hexStringToByteArray(header + expStr));
      String result = instance.read(is);
      assertEquals(expStr, result);
   }

   /**
    *
    */
   @Test
   public void testRead_WithUnmarshal_strings()
   {
      System.out.println("read unm strs");
      instance.write(os, "1 1 S:3\n00");

      String expStr = "05000203100000003000000042000000180000000000000000000000e903000000000000040000004b47530000000000";
      is.setBytesToRead(BinaryTCPClientImpl.hexStringToByteArray(expStr));
      String result = instance.read(is);
      assertEquals("00000000e90300000000000004000000{KGS}0000000000", result);
   }
}
