/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.vizualizers;

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
@Suite.SuiteClasses({ColorsDispatcherTest.class, JSettingsPanelTest.class, ThroughputVsThreadsGuiTest.class, HeaderClickCheckAllListenerTest.class, ThroughputOverTimeGuiTest.class, ResponseTimesDistributionGuiTest.class, GraphPanelTest.class, ChartRowsTableTest.class, TotalTransactionsPerSecondGuiTest.class, TimesVsThreadsGuiTest.class, TransactionsPerSecondGuiTest.class, ResponseTimesOverTimeGuiTest.class, SettingsInterfaceTest.class, ColorRendererTest.class, AbstractGraphPanelVisualizerTest.class, DateTimeRendererTest.class, ThreadsStateOverTimeGuiTest.class, HeaderAsTextRendererTest.class})
public class VizualizersSuite {

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