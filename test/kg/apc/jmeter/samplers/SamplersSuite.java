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
@Suite.SuiteClasses({HTTPRawSamplerTest.class, InfiniteGetTCPClientImplTest.class, DNSJavaDecoderToRawDataTest.class, HTTPRawSamplerGuiTest.class, DummySamplerTest.class, UDPSamplerGuiTest.class, AbstractIPSamplerTest.class, DNSJavaDecoderTest.class, DummySamplerGuiTest.class, HexStringUDPDecoderTest.class, UDPTrafficDecoderTest.class, DNSJavaTCPClientImplTest.class, UDPSamplerTest.class})
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