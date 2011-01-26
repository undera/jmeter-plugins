/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.charting.DateTimeRendererTest;
import kg.apc.jmeter.charting.ColorsDispatcherTest;
import kg.apc.jmeter.charting.ColorRendererTest;
import kg.apc.jmeter.charting.ChartRowsTableTest;
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
@Suite.SuiteClasses({LatenciesOverTimeGuiTest.class, HitsPerSecondGuiTest.class, ResponseTimesPercentilesGuiTest.class, ThreadsStateOverTimeGuiTest.class, JRowsSelectorPanelTest.class, ThroughputVsThreadsGuiTest.class, ResponseTimesOverTimeGuiTest.class, AbstractOverTimeVisualizerTest.class, JSettingsPanelTest.class, TimesVsThreadsGuiTest.class, CompositeNotifierInterfaceTest.class, ThroughputOverTimeGuiTest.class, TransactionsPerSecondGuiTest.class, BytesThroughputOverTimeGuiTest.class, CompositeGraphGuiTest.class, CompositeModelTest.class, HeaderClickCheckAllListenerTest.class, GraphPanelTest.class, SettingsInterfaceTest.class, AbstractGraphPanelVisualizerTest.class, TotalTransactionsPerSecondGuiTest.class, JCompositeRowsSelectorPanelTest.class, GraphRendererInterfaceTest.class, ResponseTimesDistributionGuiTest.class, CompositeResultCollectorTest.class, kg.apc.jmeter.vizualizers.ChartRowsTableTest.class, HeaderAsTextRendererTest.class})
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