/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.charting;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author apc
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({AbstractGraphRowTest.class, GraphRowExactValuesTest.class, GraphPanelChartTest.class, AbstractGraphPanelChartElementTest.class, GraphRowSumValuesTest.class, GraphPanelChartAverageElementTest.class, GraphPanelChartSumElementTest.class, GraphRowAveragesTest.class, GraphPanelChartExactElementTest.class, GraphRowOverallAveragesTest.class})
public class ChartingSuite {

   /**
    *
    * @throws Exception
    */
   @BeforeClass
   public static void setUpClass() throws Exception
   {
   }

   /**
    *
    * @throws Exception
    */
   @AfterClass
   public static void tearDownClass() throws Exception
   {
   }

   /**
    * 
    * @throws Exception
    */
   @Before
   public void setUp() throws Exception
   {
   }

   /**
    *
    * @throws Exception
    */
   @After
   public void tearDown() throws Exception
   {
   }

}