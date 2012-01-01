package kg.apc.charting;

import kg.apc.charting.rows.GraphRowAverages;
import java.awt.Graphics;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.concurrent.ConcurrentSkipListMap;
import kg.apc.emulators.TestGraphics;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jorphan.gui.NumberRenderer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author apc
 */
public class GraphPanelChartTest {

    /**
     *
     */
    public GraphPanelChartTest() {
    }

    /**
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass()
            throws Exception {
    }

    /**
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass()
            throws Exception {
    }

    /**
     *
     */
    @Before
    public void setUp() {
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
     * Test of paintComponent method, of class GraphPanelChart.
     */
    @Test
    public void testPaintComponent() {
        System.out.println("paintComponent");
        Graphics g = new TestGraphics();
        GraphPanelChart instance = new GraphPanelChart();
        instance.setSize(500, 500);
        instance.getChartSettings().setDrawFinalZeroingLines(true);
        instance.getChartSettings().setDrawCurrentX(true);
        instance.getChartSettings().setExpendRows(true);

        final ConcurrentSkipListMap<String, AbstractGraphRow> rows = new ConcurrentSkipListMap<String, AbstractGraphRow>();
        instance.setRows(rows);
        final GraphRowAverages row1 = new GraphRowAverages();
        row1.setDrawThickLines(true);
        row1.setDrawLine(true);
        row1.setDrawBar(true);
        row1.setDrawValueLabel(true);
        row1.setMarkerSize(AbstractGraphRow.MARKER_SIZE_BIG);
        rows.put("test 1", row1);
        row1.add(System.currentTimeMillis(), 20);

        instance.paintComponent(g);

        row1.add(System.currentTimeMillis(), 540);
        instance.setxAxisLabelRenderer(new DateTimeRenderer("HH:mm:ss"));
        instance.paintComponent(g);

        row1.add(System.currentTimeMillis(), 8530);
        instance.paintComponent(g);
    }

    /**
     *
     */
    @Test
    public void testPaintComponent_empty() {
        System.out.println("paintComponent_empty");
        Graphics g = new TestGraphics();
        GraphPanelChart instance = new GraphPanelChart();
        instance.setSize(500, 500);
        instance.getChartSettings().setDrawFinalZeroingLines(false);

        final ConcurrentSkipListMap<String, AbstractGraphRow> rows = new ConcurrentSkipListMap<String, AbstractGraphRow>();
        instance.setRows(rows);
        instance.paintComponent(g);
    }

    /**
     * Test of setRows method, of class GraphPanelChart.
     */
    @Test
    public void testSetRows() {
        System.out.println("setRows");
        ConcurrentSkipListMap<String, AbstractGraphRow> aRows = null;
        GraphPanelChart instance = new GraphPanelChart();
        instance.setRows(aRows);
    }

    /**
     * Test of setyAxisLabelRenderer method, of class GraphPanelChart.
     */
    @Test
    public void testSetyAxisLabelRenderer() {
        System.out.println("setyAxisLabelRenderer");
        NumberRenderer yAxisLabelRenderer = null;
        GraphPanelChart instance = new GraphPanelChart();
        instance.setyAxisLabelRenderer(yAxisLabelRenderer);
    }

    /**
     * Test of setxAxisLabelRenderer method, of class GraphPanelChart.
     */
    @Test
    public void testSetxAxisLabelRenderer() {
        System.out.println("setxAxisLabelRenderer");
        NumberRenderer xAxisLabelRenderer = null;
        GraphPanelChart instance = new GraphPanelChart();
        instance.setxAxisLabelRenderer(xAxisLabelRenderer);
    }

    /**
     * Test of setCurrentX method, of class GraphPanelChart.
     */
    @Test
    public void testSetCurrentX() {
        System.out.println("setCurrentX");
        long currentX = 0L;
        GraphPanelChart instance = new GraphPanelChart();
        instance.setCurrentX(currentX);
    }

    /**
     * Test of setForcedMinX method, of class GraphPanelChart.
     */
    @Test
    public void testSetForcedMinX() {
        System.out.println("setForcedMinX");
        int i = 0;
        GraphPanelChart instance = new GraphPanelChart();
        instance.setForcedMinX(i);
    }

    /**
     * Test of lostOwnership method, of class GraphPanelChart.
     */
    @Test
    public void testLostOwnership() {
        System.out.println("lostOwnership");
        Clipboard clipboard = null;
        Transferable contents = null;
        GraphPanelChart instance = new GraphPanelChart();
        instance.lostOwnership(clipboard, contents);
    }

    /**
     * Test of clearErrorMessage method, of class GraphPanelChart.
     */
    @Test
    public void testClearErrorMessage() {
        System.out.println("clearErrorMessage");
        GraphPanelChart instance = new GraphPanelChart();
        instance.clearErrorMessage();
    }

    /**
     * Test of setErrorMessage method, of class GraphPanelChart.
     */
    @Test
    public void testSetErrorMessage() {
        System.out.println("setErrorMessage");
        String msg = "error";
        GraphPanelChart instance = new GraphPanelChart();
        instance.setErrorMessage(msg);
        msg = "";
        instance.setErrorMessage(msg);
        msg = null;
        instance.setErrorMessage(msg);
    }

    /**
     * Test of setChartType method, of class GraphPanelChart.
     */
    @Test
    public void testSetChartType() {
        System.out.println("setChartType");
        int type = GraphPanelChart.CHART_PERCENTAGE;
        GraphPanelChart instance = new GraphPanelChart();
        instance.setChartType(type);
    }

    /**
     * Test of isModelContainsRow method, of class GraphPanelChart.
     */
    @Test
    public void testIsModelContainsRow() {
        System.out.println("isModelContainsRow");

        ConcurrentSkipListMap<String, AbstractGraphRow> testModel = new ConcurrentSkipListMap<String, AbstractGraphRow>();

        AbstractGraphRow rowIncluded = new GraphRowAverages();
        rowIncluded.setLabel("rowIncluded");
        AbstractGraphRow rowExcluded = new GraphRowAverages();
        rowExcluded.setLabel("rowExcluded");

        testModel.put("rowIncluded", rowIncluded);

        GraphPanelChart instance = new GraphPanelChart();
        instance.setRows(testModel);
        boolean expResult = true;
        boolean result = instance.isModelContainsRow(rowIncluded);
        assertEquals(expResult, result);

        expResult = false;
        result = instance.isModelContainsRow(rowExcluded);
        assertEquals(expResult, result);
    }

    /**
     * Test of setReSetColors method, of class GraphPanelChart.
     */
    @Test
    public void testSetReSetColors() {
        System.out.println("setReSetColors");
        boolean reSetColors = false;
        GraphPanelChart instance = new GraphPanelChart();
        instance.setReSetColors(reSetColors);
    }

    /**
     * Test of setDisplayPrecision method, of class GraphPanelChart.
     */
    @Test
    public void testSetDisplayPrecision() {
        System.out.println("setDisplayPrecision");
        boolean displayPrecision = false;
        GraphPanelChart instance = new GraphPanelChart();
        instance.setDisplayPrecision(displayPrecision);
    }

    /**
     * Test of setxAxisLabel method, of class GraphPanelChart.
     */
    @Test
    public void testSetxAxisLabel() {
        System.out.println("setxAxisLabel");
        String xAxisLabel = "";
        GraphPanelChart instance = new GraphPanelChart();
        instance.setxAxisLabel(xAxisLabel);
    }

    /**
     * Test of setYAxisLabel method, of class GraphPanelChart.
     */
    @Test
    public void testSetyAxisLabel() {
        System.out.println("setyAxisLabel");
        String yAxisLabel = "";
        GraphPanelChart instance = new GraphPanelChart();
        instance.setYAxisLabel(yAxisLabel);
    }

    /**
     * Test of setPrecisionLabel method, of class GraphPanelChart.
     */
    @Test
    public void testSetPrecisionLabel() {
        System.out.println("setPrecisionLabel");
        int precision = 0;
        GraphPanelChart instance = new GraphPanelChart();
        instance.setPrecisionLabel(precision);
    }

    /**
     * Test of setIsPreview method, of class GraphPanelChart.
     */
    @Test
    public void testSetIsPreview() {
        System.out.println("setIsPreview");
        boolean isPreview = false;
        GraphPanelChart instance = new GraphPanelChart();
        instance.setIsPreview(isPreview);
    }

    @Test
    public void testSetUseRelativeTime() {
        System.out.println("setUseRelativeTime");
        boolean selected = false;
        GraphPanelChart instance = new GraphPanelChart();
        instance.setUseRelativeTime(selected);
    }

    /**
     * Test of setTestStartTime method, of class GraphPanelChart.
     */
    @Test
    public void testSetTestStartTime() {
        System.out.println("setTestStartTime");
        long time = System.currentTimeMillis();
        GraphPanelChart instance = new GraphPanelChart();
        instance.setTestStartTime(time);
    }

    /**
     * Test of saveGraphToPNG method, of class GraphPanelChart.
     */
    @Test
    public void testSaveGraphToPNG() throws Exception {
        System.out.println("saveGraphToPNG");
        File file = new File(TestJMeterUtils.getTempDir() + "/test.png");
        int w = 100;
        int h = 100;
        GraphPanelChart instance = new GraphPanelChart();
        final ConcurrentSkipListMap<String, AbstractGraphRow> rows = new ConcurrentSkipListMap<String, AbstractGraphRow>();
        instance.setRows(rows);
        instance.saveGraphToPNG(file, w, h);
    }

    /**
     * Test of saveGraphToCSV method, of class GraphPanelChart.
     */
    @Test
    public void testSaveGraphToCSV() throws Exception {
        System.out.println("saveGraphToCSV");
        File file = new File(TestJMeterUtils.getTempDir() + "/test.csv");
        GraphPanelChart instance = new GraphPanelChart();
        final ConcurrentSkipListMap<String, AbstractGraphRow> rows = new ConcurrentSkipListMap<String, AbstractGraphRow>();
        instance.setRows(rows);
        instance.saveGraphToCSV(file);
    }

   @Test
   public void testSetYAxisLabel() {
      System.out.println("setYAxisLabel");
      String yAxisLabel = "";
      GraphPanelChart instance = new GraphPanelChart();
      instance.setYAxisLabel(yAxisLabel);
   }

    /**
     * Test of getChartSettings method, of class GraphPanelChart.
     */
    @Test
    public void testGetChartSettings() {
        System.out.println("getChartSettings");
        GraphPanelChart instance = new GraphPanelChart();
        ChartSettings result = instance.getChartSettings();
        assertNotNull(result);
    }

   /**
    * Test of invalidateCache method, of class GraphPanelChart.
    */
   @Test
   public void testInvalidateCache() {
      System.out.println("invalidateCache");
      GraphPanelChart instance = new GraphPanelChart();
      instance.invalidateCache();
   }
}
