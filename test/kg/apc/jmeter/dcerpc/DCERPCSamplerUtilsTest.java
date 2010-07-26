package kg.apc.jmeter.dcerpc;

import org.apache.jmeter.protocol.tcp.sampler.BinaryTCPClientImpl;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
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
public class DCERPCSamplerUtilsTest
{
   private static String SERVER_UUID = "80d7862a-6160-4596-aaa9-1743e4c27638";
   private static String ABSTRACT_SYNTAX = "8a885d04-1ceb-11c9-9fe8-08002b104860";

   /**
    *
    */
   public DCERPCSamplerUtilsTest()
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
    * Test of getRequestByString method, of class DCERPCSampler.
    */
   @Test
   public void testGetRequestByString()
   {
      System.out.println("getRequestByString");
      String str = "bind "
           + SERVER_UUID + "\t"
           + ABSTRACT_SYNTAX;

      String expResult = "05000b03100000004800000001000000d016d0160000000001000000000001002a86d78060619645aaa91743e4c2763801000000045d888aeb1cc9119fe808002b10486002000000";
      RPCPacket[] result = DCERPCSamplerUtils.getRequestsArrayByString(str, "");
      assertEquals(1, result.length);
      assertEquals(expResult, JOrphanUtils.baToHexString(result[0].getBytes()));
   }

   /**
    *
    */
   @Test
   public void testGetRequestByString2()
   {
      System.out.println("getRequestByString2");
      String expResult = "05000003100000001c00000001000000040000000000000000000000";
      RPCPacket[] result = DCERPCSamplerUtils.getRequestsArrayByString("1 0", "00000000");
      assertEquals(1, result.length);
      assertEquals(expResult, JOrphanUtils.baToHexString(result[0].getBytes()));
   }

   /**
    *
    */
   @Test
   public void testGetRequestByString3()
   {
      System.out.println("getRequestByString3");
      String expResult = "05000003100000001f00000001000000070000000000000000544553540000";
      RPCPacket[] result = DCERPCSamplerUtils.getRequestsArrayByString("1 0", "00{TEST}\r\n0000");
      assertEquals(1, result.length);
      assertEquals(expResult, JOrphanUtils.baToHexString(result[0].getBytes()));
   }

   /**
    *
    */
   @Test
   public void testGetRequestByString_large1()
   {
      System.out.println("getRequestByString_large");
      String large = "ff";
      for (int n = 0; n < 15; n++)
         large += large;

      RPCPacket[] result = DCERPCSamplerUtils.getRequestsArrayByString("1 1", large);
      assertEquals(6, result.length);
   }

   /**
    *
    */
   @Test
   public void testGetRequestByString_large2()
   {
      System.out.println("getRequestByString_large");
      final int len = RPCBindRequest.maxXmitFrag - RPCCallRequest.HEADER_LENGTH;

      // border condition 0
      String large = JOrphanUtils.baToHexString(new byte[len]);

      RPCPacket[] result = DCERPCSamplerUtils.getRequestsArrayByString("1\t1", large);
      assertEquals(1, result.length);
   }

   /**
    *
    */
   @Test
   public void testGetRequestByString_large3()
   {
      System.out.println("getRequestByString_large");
      final int len = RPCBindRequest.maxXmitFrag - RPCCallRequest.HEADER_LENGTH + 1;

      // border condition +1
      String large = JOrphanUtils.baToHexString(new byte[len]);

      RPCPacket[] result = DCERPCSamplerUtils.getRequestsArrayByString("1 1", large);
      assertEquals(2, result.length);
   }

   /**
    *
    */
   public void testGetRequestByString_large4()
   {
      System.out.println("getRequestByString_large");
      final int len = RPCBindRequest.maxXmitFrag - RPCCallRequest.HEADER_LENGTH - 1;

      // border condition -1
      String large = JOrphanUtils.baToHexString(new byte[len]);

      RPCPacket[] result = DCERPCSamplerUtils.getRequestsArrayByString("1 1", large);
      assertEquals(1, result.length);
   }

   /**
    * Test of getOpNum method, of class DCERPCSamplerUtils.
    */
   @Test
   public void testGetOpNum()
   {
      System.out.println("getOpNum");
      String str = "23456";
      short expResult = 23456;
      short result = DCERPCSamplerUtils.getOpNum(str);
      assertEquals(expResult, result);
   }

   /**
    *
    */
   @Test
   public void testGetOpNum_exception()
   {
      System.out.println("getOpNum");
      JMeterVariables vars = new JMeterVariables();
      vars.put("test", "test");
      JMeterContextService.getContext().setVariables(vars);
      String str = "error!";
      short expResult = Short.MAX_VALUE;
      short result = DCERPCSamplerUtils.getOpNum(str);
      assertEquals(expResult, result);
   }

   /**
    * Test of getPacketsArray method, of class DCERPCSamplerUtils.
    */
   @Test
   public void testGetPacketsArray()
   {
      System.out.println("getPacketsArray");
      byte[] stubDataByteArray = BinaryTCPClientImpl.hexStringToByteArray(new String("123456"));
      int callID = 345;
      short opNum = 21554;
      String expResult = "05000003100000001b000000590100000300000000003254123456";
      RPCPacket[] result = DCERPCSamplerUtils.getPacketsArray(stubDataByteArray, callID, opNum);
      assertEquals(1, result.length);
      assertEquals(expResult, JOrphanUtils.baToHexString(result[0].getBytes()));
   }

   /**
    * Test of getPacketFlags method, of class DCERPCSamplerUtils.
    */
   @Test
   public void testGetPacketFlags()
   {
      System.out.println("getPacketFlags");
      assertEquals(1, DCERPCSamplerUtils.getPacketFlags(0, 3));
      assertEquals(0, DCERPCSamplerUtils.getPacketFlags(1, 3));
      assertEquals(2, DCERPCSamplerUtils.getPacketFlags(2, 3));
   }

   /**
    * Test of getStubDataHex method, of class DCERPCSamplerUtils.
    */
   @Test
   public void testGetStubDataHex()
   {
      System.out.println("getStubDataHex");
      String joinedStr = "{F:32:somedata!}";
      String expResult = "736f6d6564617461210000000000000000000000000000000000000000000000";
      String result = DCERPCSamplerUtils.getStubDataHex(joinedStr);
      assertEquals(expResult, result);
   }

   /**
    *
    */
   @Test
   public void testGetStubDataHex_broken()
   {
      System.out.println("getStubDataHex");
      String joinedStr = "{head${somevar}tail}";
      String expResult = "68656164247b736f6d657661722121214441544120574153205452494d4d4544212121";
      String result = DCERPCSamplerUtils.getStubDataHex(joinedStr);
      assertEquals(expResult, result);
   }

   /**
    * Test of getRequestsArrayByString method, of class DCERPCSamplerUtils.
    */
   @Test
   public void testGetRequestsArrayByString()
   {
      System.out.println("getRequestsArrayByString");
      String s = "55{TEST}66";
      String expResult = "05000003100000001e000000010000000600000000000100555445535466";
      RPCPacket[] result = DCERPCSamplerUtils.getRequestsArrayByString("1 1", s);
      assertEquals(expResult, JOrphanUtils.baToHexString(result[0].getBytes()));
   }
}
