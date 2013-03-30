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
@Suite.SuiteClasses({RPCBindRequestTest.class, BinaryUtilsTest.class, RPCPacketTest.class, DCERPCSamplerUtilsTest.class, DCERPCSamplerTest.class, DCERPCMarshallingTest.class, RPCCallExceptionTest.class, RPCCallRequestTest.class, RPCMarshallingExceptionTest.class})
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