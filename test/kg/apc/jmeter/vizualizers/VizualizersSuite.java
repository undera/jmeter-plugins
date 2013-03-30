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
@Suite.SuiteClasses({AggregateReportGuiTest.class, CompositeModelTest.class, ResponseCodesPerSecondGuiTest.class, DbMonGuiTest.class, PageDataExtractorOverTimeGuiTest.class, ResponseTimesDistributionGuiTest.class, CompositeResultCollectorTest.class, JCompositeRowsSelectorPanelTest.class, JPerfmonParamsPanelTest.class, ThroughputOverTimeGuiTest.class, TotalTransactionsPerSecondGuiTest.class, ThroughputVsThreadsGuiTest.class, JSettingsPanelTest.class, PerfMonGuiTest.class, HitsPerSecondGuiTest.class, TransactionsPerSecondGuiTest.class, ResponseTimesOverTimeGuiTest.class, CorrectedResultCollectorTest.class, BytesThroughputOverTimeGuiTest.class, LatenciesOverTimeGuiTest.class, ThreadsStateOverTimeGuiTest.class, ResponseTimesPercentilesGuiTest.class, TimesVsThreadsGuiTest.class, CompositeGraphGuiTest.class})
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
