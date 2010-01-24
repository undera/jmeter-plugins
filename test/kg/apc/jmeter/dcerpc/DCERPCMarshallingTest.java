package kg.apc.jmeter.dcerpc;

import org.apache.jmeter.protocol.tcp.sampler.BinaryTCPClientImpl;
import org.apache.jorphan.util.JOrphanUtils;
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
public class DCERPCMarshallingTest
{
   public DCERPCMarshallingTest()
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
   }

   @After
   public void tearDown()
   {
   }

   /**
    * Test of marshalData method, of class DCERPCSampler.
    */
   @Test
   public void testHexEncodeTextParts()
        throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts");
      String in_str = "00{t-adm-1}00";
      String expResult = "00742d61646d2d3100";
      String result = DCERPCMarshalling.marshalData(in_str);
      assertEquals(expResult, result);
   }

   @Test
   public void testHexEncodeTextParts2()
        throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts2");
      String in_str = "00{t-adm-1}01{t-\r\n-1}02";
      String expResult = "00742d61646d2d3101742d0d0a2d3102";
      String result = DCERPCMarshalling.marshalData(in_str);
      assertEquals(expResult, result);
   }

   @Test
   public void testHexEncodeTextParts_empty()
        throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts2");
      String in_str = "00{t-adm-1}0{}1{t-\r\n-1}02";
      String expResult = "00742d61646d2d3101742d0d0a2d3102";
      String result = DCERPCMarshalling.marshalData(in_str);
      assertEquals(expResult, result);
   }

   @Test
   public void testHexEncodeTextParts_russian()
        throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts2");
      String in_str = "00{тест}02";
      String expResult = "00f2e5f1f202";
      String result = DCERPCMarshalling.marshalData(in_str);
      assertEquals(expResult, result);
   }

   @Test
   public void testHexEncodeTextParts_empty2()
        throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts2");
      String in_str = "00{t-adm-1}0{-:}1{t-\r\n-1}02";
      String expResult = "00742d61646d2d3102d3a1742d0d0a2d3102";
      String result = DCERPCMarshalling.marshalData(in_str);
      assertEquals(expResult, result);
   }

   @Test
   public void testHexEncodeTextParts_marshal_length_prefixed()
        throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts_lp");
      String in_str = "00{L:test length prefixed}02";
      String expResult = "001400000074657374206c656e67746820707265666978656402";
      String result = DCERPCMarshalling.marshalData(in_str);
      assertEquals(expResult, result);
   }

   @Test
   public void testHexEncodeTextParts_marshal_null_terminated()
        throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts_lp");
      String in_str = "00{Z:test zero terminated}02";
      String expResult = "0074657374207a65726f207465726d696e617465640002";
      String result = DCERPCMarshalling.marshalData(in_str);
      assertEquals(expResult, result);
   }

   @Test
   public void testHexEncodeTextParts_marshal_length_prefixed_null_terminated()
        throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts_lpnt");
      String in_str = "00{N:test length prefixed}02";
      String expResult = "001500000074657374206c656e6774682070726566697865640002";
      String result = DCERPCMarshalling.marshalData(in_str);
      assertEquals(expResult, result);
   }

   @Test
   public void testHexEncodeTextParts_marshal_bpp_invoke_xml()
        throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts_lp");
      String in_str = "00{X:test length prefixed}02";
      String expResult = "0015000000140000001400000074657374206c656e67746820707265666978656402";
      String result = DCERPCMarshalling.marshalData(in_str);
      assertEquals(expResult, result);
   }

   @Test
   public void testHexEncodeTextParts_marshal_fixed_length()
        throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts_fl");
      String in_str = "01{F:32:test length prefixed}02";
      String expResult = "0174657374206c656e67746820707265666978656400000000000000000000000002";
      String result = DCERPCMarshalling.marshalData(in_str);
      assertEquals(expResult, result);
   }

   @Test
   public void testHexEncodeTextParts_marshal_fixed_length2()
        throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts_fl2");
      String in_str = "01{F:4:test}02";
      String expResult = "017465737402";
      String result = DCERPCMarshalling.marshalData(in_str);
      assertEquals(expResult, result);
   }

   @Test
   public void testHexEncodeTextParts_marshal_integer()
        throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts_int");
      String in_str = "01{I:23040978}02";
      String expResult = "01d2935f0102";
      String result = DCERPCMarshalling.marshalData(in_str);
      assertEquals(expResult, result);
   }

   @Test
   public void testHexEncodeTextParts_marshal_integer2()
        throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts_int");
      String in_str = "01{I:0069252}02";
      String expResult = "01840e010002";
      String result = DCERPCMarshalling.marshalData(in_str);
      assertEquals(expResult, result);
   }

   @Test
   public void testHexEncodeTextParts_MARSHAL_DOUBLE_LENGTH_PREFIXED_NULL_TERMINATED()
        throws RPCMarshallingException
   {
      System.out.println("hexEncodeTextParts_lp");
      String in_str = "00{D:test length prefixed}02";
      String expResult = "0015000000000000001500000074657374206c656e6774682070726566697865640002";
      String result = DCERPCMarshalling.marshalData(in_str);
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
      String result = null;
      try
      {
         result = DCERPCMarshalling.unmarshalData(ba, "S");
      }
      catch (RPCMarshallingException ex)
      {
         fail(ex.getMessage());
      }
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
      String result = null;
      try
      {
         result = DCERPCMarshalling.unmarshalData(ba, "S");
      }
      catch (RPCMarshallingException ex)
      {
         fail(ex.getMessage());
      }
      assertEquals(expResult, result);
   }

   @Test
   public void testBaToHexString_russian()
   {
      System.out.println("baToHexString русский");
      byte[] ba = BinaryTCPClientImpl.hexStringToByteArray("00f2e5f1f2f2e5f1f202");
      String expResult = "00{тесттест}02";
      String result = null;
      try
      {
         result = DCERPCMarshalling.unmarshalData(ba, "S");
      }
      catch (RPCMarshallingException ex)
      {
         fail(ex.getMessage());
      }
      assertEquals(expResult, result);
   }

   @Test
   public void testBaToHexString_ASCII()
        throws RPCMarshallingException
   {
      //if (true) return;
      System.out.println("baToHexString2");
      byte[] ASCII = new byte[256];
      for (int n = 0; n < ASCII.length; n++)
      {
         ASCII[n] = (byte) n;
      }
      String result = DCERPCMarshalling.unmarshalData(ASCII, "");
      String baCheck = DCERPCMarshalling.marshalData(result);
      //System.out.println(result);
      assertEquals(JOrphanUtils.baToHexString(ASCII), baCheck);
   }

   @Test
   public void testBaToHexString_shorts()
   {
      System.out.println("baToHexString4");
      byte[] ba = String.valueOf("1{23{456{7890{abcde{fg").getBytes();
      String expResult = "317b32337b3435367b373839307b{abcde}7b6667";
      String result = null;
      try
      {
         result = DCERPCMarshalling.unmarshalData(ba, "S");
      }
      catch (RPCMarshallingException ex)
      {
         fail(ex.getMessage());
      }
      assertEquals(expResult, result);
   }

   /**
    * Test of unmarshalData method, of class DCERPCMarshalling.
    */
   @Test
   public void testUnmarshalData()
        throws Exception
   {
      System.out.println("unmarshalData");
      byte[] ba = new byte[0];
      String unmarshalOptions = "";
      String expResult = "";
      String result = DCERPCMarshalling.unmarshalData(ba, unmarshalOptions);
      assertEquals(expResult, result);
   }

   /**
    * Test of marshalData method, of class DCERPCMarshalling.
    */
   @Test
   public void testMarshalData()
        throws Exception
   {
      System.out.println("marshalData");
      String in_str = "";
      String expResult = "";
      String result = DCERPCMarshalling.marshalData(in_str);
      assertEquals(expResult, result);
   }
}
