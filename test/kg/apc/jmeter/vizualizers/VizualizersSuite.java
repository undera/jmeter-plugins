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
@Suite.SuiteClasses({CompositeGraphTest.class, DateTimeRendererTest.class, ResponseTimesPercentilesGuiTest.class, HitsPerSecondGuiTest.class, ThreadsStateOverTimeGuiTest.class, ColorRendererTest.class, ThroughputVsThreadsGuiTest.class, ResponseTimesOverTimeGuiTest.class, JSettingsPanelTest.class, TimesVsThreadsGuiTest.class, ThroughputOverTimeGuiTest.class, TransactionsPerSecondGuiTest.class, BytesThroughputOverTimeGuiTest.class, HeaderClickCheckAllListenerTest.class, GraphPanelTest.class, SettingsInterfaceTest.class, AbstractGraphPanelVisualizerTest.class, ColorsDispatcherTest.class, ResponseTimesDistributionGuiTest.class, ChartRowsTableTest.class, HeaderAsTextRendererTest.class})
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