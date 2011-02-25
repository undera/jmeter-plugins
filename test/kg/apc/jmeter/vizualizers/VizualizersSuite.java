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
@Suite.SuiteClasses({TimesVsThreadsGuiTest.class, ThreadsStateOverTimeGuiTest.class, CompositeNotifierInterfaceTest.class, GraphPanelTest.class, ResponseTimesPercentilesGuiTest.class, kg.apc.jmeter.vizualizers.ChartRowsTableTest.class, AbstractGraphPanelVisualizerTest.class, ThroughputVsThreadsGuiTest.class, SettingsInterfaceTest.class, ResponseTimesDistributionGuiTest.class, HeaderAsTextRendererTest.class, ResponseTimesOverTimeGuiTest.class, JRowsSelectorPanelTest.class, JSettingsPanelTest.class, AbstractOverTimeVisualizerTest.class, ThroughputOverTimeGuiTest.class, TotalTransactionsPerSecondGuiTest.class, LatenciesOverTimeGuiTest.class, HitsPerSecondGuiTest.class, GraphRendererInterfaceTest.class, JCompositeRowsSelectorPanelTest.class, CompositeResultCollectorTest.class, CompositeModelTest.class, ResponseCodesPerSecondGuiTest.class, CompositeGraphGuiTest.class, HeaderClickCheckAllListenerTest.class, BytesThroughputOverTimeGuiTest.class})
public class VizualizersSuite {

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
