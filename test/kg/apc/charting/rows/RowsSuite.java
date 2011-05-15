/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.charting.rows;

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
@Suite.SuiteClasses({kg.apc.charting.rows.GraphRowSumValuesTest.class, kg.apc.charting.rows.GraphRowSimpleTest.class, kg.apc.charting.rows.GraphRowExactValuesTest.class, kg.apc.charting.rows.GraphRowAveragesTest.class, kg.apc.charting.rows.GraphRowOverallAveragesTest.class, kg.apc.charting.rows.GraphRowPercentilesTest.class})
public class RowsSuite {

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
