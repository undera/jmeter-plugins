/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.samplers;

import kg.apc.io.SocketChannelWithTimeoutsTest;
import kg.apc.io.DatagramChannelWithTimeoutsTest;
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
@Suite.SuiteClasses({HTTPRawSamplerDirectFileTest.class, DummySamplerTest.class, HTTPRawSamplerTest.class, UDPTrafficDecoderTest.class, HexStringUDPDecoderTest.class, DummySampleResultTest.class, HTTPRawSamplerGuiTest.class, DummySamplerGuiTest.class, UDPSamplerTest.class, InfiniteGetTCPClientImplTest.class, UDPSamplerGuiTest.class, DNSJavaDecoderTest.class, AbstractIPSamplerTest.class, DNSJavaDecoderToRawDataTest.class})
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