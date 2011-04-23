/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.samplers;

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
@Suite.SuiteClasses({HTTPRawSamplerGuiTest.class, DummySamplerTest.class, UDPSamplerTest.class, DatagramChannelWithTimeoutsTest.class, HTTPRawSamplerDirectFileTest.class, DummySamplerGuiTest.class, UDPTrafficDecoderTest.class, SocketChannelWithTimeoutsTest.class, UDPSamplerGuiTest.class, AbstractIPSamplerTest.class, HTTPRawSamplerTest.class, DNSJavaDecoderToRawDataTest.class, HexStringUDPDecoderTest.class, DNSJavaDecoderTest.class})
public class SamplersSuite {

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