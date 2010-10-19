/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter;

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
@Suite.SuiteClasses({kg.apc.jmeter.dcerpc.DcerpcSuite.class,kg.apc.jmeter.perfmon.PerfmonSuite.class,kg.apc.jmeter.control.ControlSuite.class,kg.apc.jmeter.charting.ChartingSuite.class,kg.apc.jmeter.samplers.SamplersSuite.class,kg.apc.jmeter.vizualizers.VizualizersSuite.class,kg.apc.jmeter.threads.ThreadsSuite.class,kg.apc.jmeter.config.ConfigSuite.class})
public class JmeterSuite {

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