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
public class RPCBindRequestTest
{
   /**
    *
    */
   public RPCBindRequestTest()
   {
   }

   /**
    *
    * @throws Exception
    */
   @BeforeClass
   public static void setUpClass() throws Exception
   {
   }

   /**
    *
    * @throws Exception
    */
   @AfterClass
   public static void tearDownClass() throws Exception
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
    *
    */
   @Test
   public void testGetBytes()
   {
      System.out.println("getBytes");
      RPCBindRequest instance = new RPCBindRequest("c2ce97a0-8b15-11d1-0000-00a0c9103fcf", "8a885d04-1ceb-11c9-9fe8-08002b104860");
      String ExpectedHex = "05000b03100000004800000001000000d016d016000000000100000000000100a097cec2158bd111000000a0c9103fcf01000000045d888aeb1cc9119fe808002b10486002000000";
      String result = JOrphanUtils.baToHexString(instance.getBytes());
      assertEquals(ExpectedHex, result);
   }

   /**
    * Test of getHeaderBytes method, of class RPCBindRequest.
    */
   @Test
   public void testGetHeaderBytes()
   {
      System.out.println("getHeaderBytes");
      RPCBindRequest instance = new RPCBindRequest("c2ce97a0-8b15-11d1-0000-00a0c9103fcf", "8a885d04-1ceb-11c9-9fe8-08002b104860");
      String ExpectedHex = "d016d016000000000100000000000100a097cec2158bd111000000a0c9103fcf01000000045d888aeb1cc9119fe808002b10486002000000";
      String result = JOrphanUtils.baToHexString(instance.getHeaderBytes());
      assertEquals(ExpectedHex, result);
   }

   /**
    * Test of getBodyBytes method, of class RPCBindRequest.
    */
   @Test
   public void testGetBodyBytes()
   {
      System.out.println("getBodyBytes");
      RPCBindRequest instance = new RPCBindRequest("c2ce97a0-8b15-11d1-0000-00a0c9103fcf", "8a885d04-1ceb-11c9-9fe8-08002b104860");
      String ExpectedHex = "";
      String result = JOrphanUtils.baToHexString(instance.getBodyBytes());
      assertEquals(ExpectedHex, result);
   }
}
