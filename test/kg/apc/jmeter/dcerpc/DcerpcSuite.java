/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
@Suite.SuiteClasses({kg.apc.jmeter.dcerpc.RPCCallRequestTest.class,kg.apc.jmeter.dcerpc.DCERPCSamplerTest.class,kg.apc.jmeter.dcerpc.RPCPacketTest.class,kg.apc.jmeter.dcerpc.RPCBindRequestTest.class,kg.apc.jmeter.dcerpc.DCERPCSamplerUtilsTest.class,kg.apc.jmeter.dcerpc.DCERPCMarshallingTest.class,kg.apc.jmeter.dcerpc.RPCCallExceptionTest.class,kg.apc.jmeter.dcerpc.RPCMarshallingExceptionTest.class,kg.apc.jmeter.dcerpc.BinaryUtilsTest.class})
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