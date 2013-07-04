package kg.apc.charting;

import kg.apc.charting.elements.ElementsSuite;
import kg.apc.charting.plotters.PlottersSuite;
import kg.apc.charting.rows.RowsSuite;
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
@Suite.SuiteClasses({ElementsSuite.class, PlottersSuite.class, ColorsDispatcherTest.class, GraphModelToCsvExporterTest.class, DateTimeRendererTest.class, GraphPanelChartTest.class, AbstractGraphPanelChartElementTest.class, CubicSplineTest.class, DividerRendererTest.class, AbstractGraphRowTest.class, RowsSuite.class, ChartSettingsTest.class})
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
