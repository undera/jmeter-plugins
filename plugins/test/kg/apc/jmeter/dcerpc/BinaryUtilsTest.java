package kg.apc.jmeter.dcerpc;

import java.io.BufferedReader;
import java.io.FileReader;
import org.apache.jorphan.util.JOrphanUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author apc
 */
public class BinaryUtilsTest
{
   /**
    *
    */
   public BinaryUtilsTest()
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
   }

   /**
    *
    */
   @After
   public void tearDown()
   {
   }

   /**
    * Test of intToByteArray method, of class RPCRequest.
    */
   @Test
   public void testIntToByteArray()
   {
      System.out.println("intToByteArray");
      int value = 65535;
      String expResult = "ffff0000";
      String result = JOrphanUtils.baToHexString(BinaryUtils.intToByteArray(value));
      assertEquals(expResult, result);
   }

   /**
    * Test of shortToByteArray method, of class RPCRequest.
    */
   @Test
   public void testShortToByteArray()
   {
      System.out.println("shortToByteArray");
      short value = 6535;
      String expResult = "8719";
      String result = JOrphanUtils.baToHexString(BinaryUtils.shortToByteArray(value));
      assertEquals(expResult, result);
   }

   /**
    * Test of twoHexCharsToIntVal method, of class RPCPacket.
    */
   @Test
   public void testTwoHexCharsToIntVal()
   {
      System.out.println("TwoHexCharsToIntVal");
      char byte1 = 'A';
      char byte2 = 'F';
      int expResult = 175;
      int result = BinaryUtils.twoHexCharsToIntVal(byte1, byte2);
      assertEquals(expResult, result);
   }

   /**
    *
    */
   @Test
   public void testTwoHexCharsToIntVal_Exception()
   {
      System.out.println("TwoHexCharsToIntVal");
      char byte1 = 'A';
      char byte2 = 'Z';
      try
      {
         int result = BinaryUtils.twoHexCharsToIntVal(byte1, byte2);
         fail("Exception expected");
      }
      catch (Exception e)
      {
         assertEquals("Hex-encoded binary string contains an invalid hex digit in 'AZ'", e.getMessage());
      }
   }

   /**
    * Test of UUIDToByteArray method, of class RPCRequest.
    */
   @Test
   public void testUUIDToByteArray()
   {
      System.out.println("UUIDToByteArray");
      String ainterfaceUUID = "8a885d04-1ceb-11c9-9fe8-08002b104860";
      String expResult = "045d888aeb1cc9119fe808002b104860";
      String result = JOrphanUtils.baToHexString(BinaryUtils.UUIDToByteArray(ainterfaceUUID));
      assertEquals(expResult, result);
   }

   /**
    * Test of twoHexCharsToByteVal method, of class BinaryUtils.
    */
   @Test
   public void testTwoHexCharsToByteVal()
   {
      System.out.println("twoHexCharsToByteVal");
      char c = 'c';
      char c0 = '6';
      byte expResult = -58;
      byte result = BinaryUtils.twoHexCharsToByteVal(c, c0);
      assertEquals(expResult, result);
   }

   /**
    * Test of twoBytesToShortVal method, of class BinaryUtils.
    */
   @Test
   public void testTwoBytesToShortVal()
   {
      System.out.println("twoBytesToShortVal");
      byte byte1 = 120;
      byte byte2 = 56;
      short expResult = 14456;
      short result = BinaryUtils.twoBytesToShortVal(byte1, byte2);
      assertEquals(expResult, result);
   }

   /**
    * Test of intToHexString method, of class BinaryUtils.
    */
   @Test
   public void testIntToHexString()
   {
      System.out.println("intToHexString");
      int i = 234567;
      String expResult = "47940300";
      String result = BinaryUtils.intToHexString(i);
      assertEquals(expResult, result);
   }

   private static String readFileAsString(String filePath)
         throws java.io.IOException
   {
      StringBuffer fileData = new StringBuffer(1000);
      BufferedReader reader = new BufferedReader(
            new FileReader(filePath));
      char[] buf = new char[1024];
      int numRead = 0;
      while ((numRead = reader.read(buf)) != -1)
      {
         fileData.append(buf, 0, numRead);
      }
      reader.close();
      return fileData.toString();
   }

   /**
    * Test of baToHexStringWithText method, of class BinaryUtils.
    */
   @Test
   public void testBaToHexStringWithText()
   {
      // actual test in other method
      String pat = "^00000000([^\\00]+)$";
      String test1 = "0000000001";
      String test = "00000000\r\n11";
      assertTrue(test1.matches(pat));
      assertTrue(test.matches(pat));
   }

   /**
    * Test of fourBytesToIntVal method, of class BinaryUtils.
    */
   @Test
   public void testFourBytesToIntVal()
   {
      System.out.println("fourBytesToIntVal");
      assertEquals(0, BinaryUtils.fourBytesToIntVal((byte) 0, (byte) 0, (byte) 0, (byte) 0));
      assertEquals(1, BinaryUtils.fourBytesToIntVal((byte) 1, (byte) 0, (byte) 0, (byte) 0));
      assertEquals(256, BinaryUtils.fourBytesToIntVal((byte) 0, (byte) 1, (byte) 0, (byte) 0));
      assertEquals(65536, BinaryUtils.fourBytesToIntVal((byte) 0, (byte) 0, (byte) 1, (byte) 0));
      assertEquals(16777216, BinaryUtils.fourBytesToIntVal((byte) 0, (byte) 0, (byte) 0, (byte) 1));
      assertEquals(16843009, BinaryUtils.fourBytesToIntVal((byte) 1, (byte) 1, (byte) 1, (byte) 1));
   }

   /**
    *
    */
   @Test
   public void testDoubleToHexString()
   {
      assertEquals("0000006012db6541", BinaryUtils.doubleToHexString(11458707.00));
   }

   /**
    *
    */
   @Test
   public void testHexToDouble()
   {
      assertEquals(11458707.00, BinaryUtils.hexToDouble("0000006012db6541"), 0.001);
   }
}
