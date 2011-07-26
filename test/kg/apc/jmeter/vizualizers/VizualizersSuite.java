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
@Suite.SuiteClasses({TimesVsThreadsGuiTest.class, ThreadsStateOverTimeGuiTest.class, TransactionsPerSecondGuiTest.class, ResponseTimesPercentilesGuiTest.class, CorrectedResultCollectorTest.class, ThroughputVsThreadsGuiTest.class, ResponseTimesDistributionGuiTest.class, ResponseTimesOverTimeGuiTest.class, JSettingsPanelTest.class, ThroughputOverTimeGuiTest.class, TotalTransactionsPerSecondGuiTest.class, LatenciesOverTimeGuiTest.class, HitsPerSecondGuiTest.class, JCompositeRowsSelectorPanelTest.class, CompositeResultCollectorTest.class, PerfMonGuiTest.class, CompositeModelTest.class, CompositeGraphGuiTest.class, ResponseCodesPerSecondGuiTest.class, AggregateReportGuiTest.class, BytesThroughputOverTimeGuiTest.class})
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
