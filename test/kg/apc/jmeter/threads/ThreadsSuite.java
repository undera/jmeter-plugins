/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.threads;

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
@Suite.SuiteClasses({kg.apc.jmeter.threads.SteppingThreadGroupTest.class,kg.apc.jmeter.threads.ThreadScheduleParamsTest.class,kg.apc.jmeter.threads.SteppingThreadGroupGuiTest.class,kg.apc.jmeter.threads.UltimateThreadGroupTest.class,kg.apc.jmeter.threads.UltimateThreadGroupGuiTest.class})
public class ThreadsSuite {

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