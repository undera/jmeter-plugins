/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.dcerpc;

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
public class RPCCallRequestTest
{
   /**
    *
    */
   public RPCCallRequestTest()
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
    * Test of getBodyBytes method, of class RPCCallRequest.
    */
   @Test
   public void testGetBodyBytes()
   {
      System.out.println("getBodyBytes");
      final byte flags = RPCPacket.PACKET_FLAG_FIRST | RPCPacket.PACKET_FLAG_LAST;
      RPCCallRequest instance = new RPCCallRequest(15, (short) 354, String.valueOf("34523452345").getBytes(), flags);
      String expResult = "3334353233343532333435";
      String result = JOrphanUtils.baToHexString(instance.getBodyBytes());
      assertEquals(expResult, result);
   }

   /**
    * Test of getHeaderBytes method, of class RPCCallRequest.
    */
   @Test
   public void testGetHeaderBytes()
   {
      System.out.println("getHeaderBytes");
      final byte flags = RPCPacket.PACKET_FLAG_FIRST | RPCPacket.PACKET_FLAG_LAST;
      RPCCallRequest instance = new RPCCallRequest(15, (short) 354, String.valueOf("34523452345").getBytes(), flags);
      String expResult = "0b00000000006201";
      String result = JOrphanUtils.baToHexString(instance.getHeaderBytes());
      assertEquals(expResult, result);
   }

   /**
    *
    */
   @Test
   public void testConstructorException()
   {
      System.out.println("constr_exception");
      final byte flags = RPCPacket.PACKET_FLAG_FIRST | RPCPacket.PACKET_FLAG_LAST;

      try
      {
         RPCCallRequest instance = new RPCCallRequest(15, (short) 123, null, flags);
         fail("Exception expected");
      }
      catch (IllegalArgumentException e)
      {
      }
   }

   /**
    * Test of setAllocHint method, of class RPCCallRequest.
    */
   @Test
   public void testSetAllocHint()
   {
      System.out.println("setAllocHint");
      int allocHint = 0;
      byte flags=0;
      RPCCallRequest instance = new RPCCallRequest(15, (short) 123, new byte[0], flags);
      instance.setAllocHint(allocHint);
   }
}
