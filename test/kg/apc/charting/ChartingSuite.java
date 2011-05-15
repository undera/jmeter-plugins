package kg.apc.charting;

import kg.apc.jmeter.graphs.ColorRendererTest;
import kg.apc.charting.rows.GraphRowExactValuesTest;
import kg.apc.charting.rows.GraphRowSumValuesTest;
import kg.apc.charting.rows.GraphRowOverallAveragesTest;
import kg.apc.charting.rows.GraphRowPercentilesTest;
import kg.apc.charting.rows.GraphRowSimpleTest;
import kg.apc.charting.rows.GraphRowAveragesTest;
import kg.apc.charting.elements.GraphPanelChartAverageElementTest;
import kg.apc.charting.elements.GraphPanelChartSumElementTest;
import kg.apc.charting.elements.GraphPanelChartSimpleElementTest;
import kg.apc.charting.elements.GraphPanelChartExactElementTest;
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
@Suite.SuiteClasses({GraphModelToCsvExporterTest.class, ColorsDispatcherTest.class, GraphPanelChartTest.class, GraphPanelChartAverageElementTest.class, GraphRowPercentilesTest.class, AbstractGraphRowTest.class, DateTimeRendererTest.class, GraphPanelChartExactElementTest.class, AbstractGraphPanelChartElementTest.class, GraphRowSimpleTest.class, GraphPanelChartSumElementTest.class, GraphRowAveragesTest.class, GraphRowExactValuesTest.class, GraphRowOverallAveragesTest.class, GraphPanelChartSimpleElementTest.class, ColorRendererTest.class, DividerRendererTest.class, GraphRowSumValuesTest.class})
public class ChartingSuite {

    /**
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    /**
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
    }
}
