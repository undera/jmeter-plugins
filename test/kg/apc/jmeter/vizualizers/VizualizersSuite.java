/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.graphs.JRowsSelectorPanelTest;
import kg.apc.jmeter.graphs.HeaderAsTextRendererTest;
import kg.apc.jmeter.graphs.HeaderClickCheckAllListenerTest;
import kg.apc.jmeter.graphs.GraphRendererInterfaceTest;
import kg.apc.jmeter.graphs.ChartRowsTableTest;
import kg.apc.jmeter.graphs.AbstractGraphPanelVisualizerTest;
import kg.apc.jmeter.graphs.AbstractVsThreadVisualizerTest;
import kg.apc.jmeter.graphs.AbstractOverTimeVisualizerTest;
import kg.apc.charting.DividerRendererTest;
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
@Suite.SuiteClasses({JSettingsPanelTest.class, AggregateReportGuiTest.class, ThroughputVsThreadsGuiTest.class, JCompositeRowsSelectorPanelTest.class, ThroughputOverTimeGuiTest.class, CompositeResultCollectorTest.class, ResponseTimesDistributionGuiTest.class, PerfMonGuiTest.class, TotalTransactionsPerSecondGuiTest.class, CompositeGraphGuiTest.class, TimesVsThreadsGuiTest.class, HitsPerSecondGuiTest.class, TransactionsPerSecondGuiTest.class, ResponseCodesPerSecondGuiTest.class, LatenciesOverTimeGuiTest.class, ResponseTimesPercentilesGuiTest.class, BytesThroughputOverTimeGuiTest.class, ResponseTimesOverTimeGuiTest.class, CompositeModelTest.class, ThreadsStateOverTimeGuiTest.class})
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
