package kg.apc.jmeter.dcerpc;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author APC
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({BinaryUtilsTest.class, DCERPCMarshallingTest.class, RPCPacketTest.class, RPCCallExceptionTest.class, RPCCallRequestTest.class, DCERPCSamplerTest.class, RPCBindRequestTest.class, DCERPCSamplerUtilsTest.class, RPCMarshallingExceptionTest.class})
public class DcerpcSuite {

   @BeforeClass
   public static void setUpClass() throws Exception
   {
   }

   @AfterClass
   public static void tearDownClass() throws Exception
   {
   }

   @Before
   public void setUp() throws Exception
   {
   }

   @After
   public void tearDown() throws Exception
   {
   }

}