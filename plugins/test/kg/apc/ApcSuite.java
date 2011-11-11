package kg.apc;

import kg.apc.charting.ChartingSuite;
import kg.apc.cmd.CmdSuite;
import kg.apc.io.IoSuite;
import kg.apc.jmeter.JmeterSuite;
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
@Suite.SuiteClasses({IoSuite.class, CmdSuite.class, ChartingSuite.class, JmeterSuite.class})
public class ApcSuite {

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