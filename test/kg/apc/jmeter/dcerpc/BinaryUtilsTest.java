package kg.apc.jmeter.dcerpc;

import java.io.BufferedReader;
import java.io.FileReader;
import org.apache.jmeter.protocol.tcp.sampler.BinaryTCPClientImpl;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.util.JOrphanUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryUtilsTest
{
   private SampleResult res;

   public BinaryUtilsTest()
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
      res = new SampleResult();
      String data = "05000003100000003f0000002a000000270000000000040031353965313237372d343964632d343932642d393830642d386530663034363739303862000000";
      res.setResponseData(data.getBytes());
      res.setResponseHeaders("Header1: Value1\\nHeader2: Value2");
      res.setResponseCode("abcd");
      res.setResponseMessage("The quick brown fox");
   }

   @After
   public void tearDown()
   {
   }

   /**
    * Test of hexEncodeTextParts method, of class DCERPCSampler.
    */
   @Test
   public void testHexEncodeTextParts() throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts");
      String in_str = "00{t-adm-1}00";
      String expResult = "00742d61646d2d3100";
      String result = BinaryUtils.hexEncodeTextParts(in_str);
      assertEquals(expResult, result);
   }

   @Test
   public void testHexEncodeTextParts2() throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts2");
      String in_str = "00{t-adm-1}01{t-\r\n-1}02";
      String expResult = "00742d61646d2d3101742d0d0a2d3102";
      String result = BinaryUtils.hexEncodeTextParts(in_str);
      assertEquals(expResult, result);
   }

   @Test
   public void testHexEncodeTextParts_empty() throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts2");
      String in_str = "00{t-adm-1}0{}1{t-\r\n-1}02";
      String expResult = "00742d61646d2d3101742d0d0a2d3102";
      String result = BinaryUtils.hexEncodeTextParts(in_str);
      assertEquals(expResult, result);
   }

   @Test
   public void testHexEncodeTextParts_russian() throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts2");
      String in_str = "00{тест}02";
      String expResult = "00f2e5f1f202";
      String result = BinaryUtils.hexEncodeTextParts(in_str);
      assertEquals(expResult, result);
   }

   @Test
   public void testHexEncodeTextParts_empty2() throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts2");
      String in_str = "00{t-adm-1}0{-:}1{t-\r\n-1}02";
      String expResult = "00742d61646d2d3102d3a1742d0d0a2d3102";
      String result = BinaryUtils.hexEncodeTextParts(in_str);
      assertEquals(expResult, result);
   }

   @Test
   public void testHexEncodeTextParts_marshal_length_prefixed() throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts_lp");
      String in_str = "00{L:test length prefixed}02";
      String expResult = "001400000074657374206c656e67746820707265666978656402";
      String result = BinaryUtils.hexEncodeTextParts(in_str);
      assertEquals(expResult, result);
   }

   @Test
   public void testHexEncodeTextParts_marshal_null_terminated() throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts_lp");
      String in_str = "00{Z:test zero terminated}02";
      String expResult = "0074657374207a65726f207465726d696e617465640002";
      String result = BinaryUtils.hexEncodeTextParts(in_str);
      assertEquals(expResult, result);
   }

   @Test
   public void testHexEncodeTextParts_marshal_length_prefixed_null_terminated() throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts_lpnt");
      String in_str = "00{N:test length prefixed}02";
      String expResult = "001500000074657374206c656e6774682070726566697865640002";
      String result = BinaryUtils.hexEncodeTextParts(in_str);
      assertEquals(expResult, result);
   }

   @Test
   public void testHexEncodeTextParts_marshal_bpp_invoke_xml() throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts_lp");
      String in_str = "00{X:test length prefixed}02";
      String expResult = "0015000000140000001400000074657374206c656e67746820707265666978656402";
      String result = BinaryUtils.hexEncodeTextParts(in_str);
      assertEquals(expResult, result);
   }

   @Test
   public void testHexEncodeTextParts_marshal_fixed_length() throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts_fl");
      String in_str = "01{F:32:test length prefixed}02";
      String expResult = "0174657374206c656e67746820707265666978656400000000000000000000000002";
      String result = BinaryUtils.hexEncodeTextParts(in_str);
      assertEquals(expResult, result);
   }

   @Test
   public void testHexEncodeTextParts_marshal_fixed_length2() throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts_fl2");
      String in_str = "01{F:4:test}02";
      String expResult = "017465737402";
      String result = BinaryUtils.hexEncodeTextParts(in_str);
      assertEquals(expResult, result);
   }

   @Test
   public void testHexEncodeTextParts_marshal_integer() throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts_int");
      String in_str = "01{I:23040978}02";
      String expResult = "01d2935f0102";
      String result = BinaryUtils.hexEncodeTextParts(in_str);
      assertEquals(expResult, result);
   }

   @Test
   public void testHexEncodeTextParts_marshal_integer2() throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts_int");
      String in_str = "01{I:0069252}02";
      String expResult = "01840e010002";
      String result = BinaryUtils.hexEncodeTextParts(in_str);
      assertEquals(expResult, result);
   }

   @Test
   public void testHexEncodeTextParts_MARSHAL_DOUBLE_LENGTH_PREFIXED_NULL_TERMINATED() throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts_lp");
      String in_str = "00{D:test length prefixed}02";
      String expResult = "0015000000000000001500000074657374206c656e6774682070726566697865640002";
      String result = BinaryUtils.hexEncodeTextParts(in_str);
      assertEquals(expResult, result);
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
    * Test of baToHexString method, of class BinaryUtils.
    */
   @Test
   public void testBaToHexString()
   {
      System.out.println("baToHexString");
      byte[] ba = String.valueOf("24352345").getBytes();
      String expResult = "{24352345}";
      String result = BinaryUtils.baToHexStringWithText(ba);
      assertEquals(expResult, result);
   }

   @Test
   public void testBaToHexString_tabs_newlines()
   {
      System.out.println("baToHexString3");
      byte[] ba = String.valueOf("1234567890").getBytes();
      ba[2] = 9;
      ba[4] = 10;
      ba[6] = 13;
      String expResult = "{12\t4\n6\r890}";
      String result = BinaryUtils.baToHexStringWithText(ba);
      assertEquals(expResult, result);
   }

   @Test
   public void testBaToHexString_russian()
   {
      System.out.println("baToHexString русский");
      byte[] ba = BinaryTCPClientImpl.hexStringToByteArray("00f2e5f1f2f2e5f1f202");
      String expResult = "00{тесттест}02";
      String result = BinaryUtils.baToHexStringWithText(ba);
      assertEquals(expResult, result);
   }

   @Test
   public void testBaToHexString_ASCII() throws RPCMarshallingException
   {
      //if (true) return;
      System.out.println("baToHexString2");
      byte[] ASCII = new byte[256];
      for (int n = 0; n < ASCII.length; n++)
      {
         ASCII[n] = (byte) n;
      }
      String result = BinaryUtils.baToHexStringWithText(ASCII);
      String baCheck = BinaryUtils.hexEncodeTextParts(result);
      System.out.println(result);
      assertEquals(JOrphanUtils.baToHexString(ASCII), baCheck);
   }

   @Test
   public void testBaToHexString_shorts()
   {
      System.out.println("baToHexString4");
      byte[] ba = String.valueOf("1{23{456{7890{abcde{fg").getBytes();
      String expResult = "317b32337b3435367b373839307b{abcde}7b6667";
      String result = BinaryUtils.baToHexStringWithText(ba);
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
    * Test of isNotVisibleChar method, of class BinaryUtils.
    */
   @Test
   public void testIsNotVisibleChar()
   {
      System.out.println("isNotVisibleChar");
      assertFalse(BinaryUtils.isNotVisibleChar(9));
      assertFalse(BinaryUtils.isNotVisibleChar(10));
      assertTrue(BinaryUtils.isNotVisibleChar(11));
      assertFalse(BinaryUtils.isNotVisibleChar(13));
      assertFalse(BinaryUtils.isNotVisibleChar(32));
      assertFalse(BinaryUtils.isNotVisibleChar(126));
      assertTrue(BinaryUtils.isNotVisibleChar(127));
   }
}
