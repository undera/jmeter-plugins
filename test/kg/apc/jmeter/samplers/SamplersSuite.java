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
@Suite.SuiteClasses({HTTPRawSamplerTest.class, UDPSamplerTest.class, InfiniteGetTCPClientImplTest.class, DummySamplerTest.class, UDPSamplerGuiTest.class, DNSJavaDecoderToRawDataTest.class, HexStringUDPDecoderTest.class, DNSJavaDecoderTest.class, UDPTrafficDecoderTest.class, AbstractIPSamplerTest.class, DummySamplerGuiTest.class, HTTPRawSamplerGuiTest.class, DNSJavaTCPClientImplTest.class})
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