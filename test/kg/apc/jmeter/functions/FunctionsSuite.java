/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.functions;

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
@Suite.SuiteClasses({UpperCaseTest.class, DoubleSumTest.class, MD5Test.class, ChooseRandomTest.class, FifoPutTest.class, StrLenTest.class, IsDefinedTest.class, FifoSizeTest.class, FifoGetTest.class, SubstringTest.class, FifoPopTest.class, LowerCaseTest.class})
public class FunctionsSuite {

   @BeforeClass
   public static void setUpClass() throws Exception {
   }

   @AfterClass
   public static void tearDownClass() throws Exception {
   }

   @Before
   public void setUp() throws Exception {
   }

   @After
   public void tearDown() throws Exception {
   }

}