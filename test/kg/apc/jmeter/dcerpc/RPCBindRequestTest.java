package kg.apc.jmeter.dcerpc;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class RPCBindRequestTest
{
   // took here http://stackoverflow.com/questions/140131/convert-a-string-representation-of-a-hex-dump-to-a-byte-array-using-java

   public static byte[] hexStringToByteArray(String s)
   {
      int len = s.length();
      byte[] data = new byte[len / 2];
      for (int i = 0; i < len; i += 2)
      {
         data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
      }
      return data;
   }

   public RPCBindRequestTest()
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

   @Test
   public void testGetBytes()
   {
      // Interface UUID: c2ce97a0-8b15-11d1-96ab-00a0c9103fcf
      // Transfer Syntax: 8a885d04-1ceb-11c9-9fe8-08002b104860
      System.out.println("getBodyBytes");
      RPCBindRequest instance = new RPCBindRequest("c2ce97a0-8b15-11d1-96ab-00a0c9103fcf", "8a885d04-1ceb-11c9-9fe8-08002b104860");
      String ExpectedHex = "05000b03100000004800000001000000d016d016000000000100000000000100a097cec2158bd11196ab00a0c9103fcf01000000045d888aeb1cc9119fe808002b10486002000000";
      byte[] expResult = hexStringToByteArray(ExpectedHex);
      int ExpectedLen=expResult.length;
      System.out.println("Expected packet len: "+Integer.toString(ExpectedLen));
      byte[] result = instance.getBytes();

      //System.out.println("EXP: " + ExpectedHex);
      //System.out.println("ACT: " + getHexString(result));
      assertEquals(ExpectedLen, result.length);
      assertArrayEquals(expResult, result);
   }

   // took here http://www.rgagnon.com/javadetails/java-0596.html
   public static String getHexString(byte[] b)
   {
      String result = "";
      for (int i = 0; i < b.length; i++)
      {
         result +=
            Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
      }
      return result;
   }
}
