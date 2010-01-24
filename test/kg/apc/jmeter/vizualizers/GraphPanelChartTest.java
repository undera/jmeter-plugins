package kg.apc.jmeter.vizualizers;

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.ConcurrentHashMap;
import kg.apc.jmeter.util.TestGraphics;
import org.apache.jorphan.gui.NumberRenderer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GraphPanelChartTest
{
   public GraphPanelChartTest()
   {
   }

   @BeforeClass
   public static void setUpClass()
        throws Exception
   {
   }

   @AfterClass
   public static void tearDownClass()
        throws Exception
   {
   }

   @Before
   public void setUp()
   {
   }

   @After
   public void tearDown()
   {
   }

   /**
    * Test of paintComponent method, of class GraphPanelChart.
    */
   @Test
   public void testPaintComponent()
   {
      System.out.println("paintComponent");
      Graphics g = new TestGraphics();
      GraphPanelChart instance = new GraphPanelChart();
      instance.setSize(500, 500);

      final ConcurrentHashMap<String, GraphPanelChartRow> rows = new ConcurrentHashMap<String, GraphPanelChartRow>();
      instance.setRows(rows);
      final GraphPanelChartRow row1 = new GraphPanelChartRow("test", Color.black);
      rows.put("test 1", row1);
      row1.add(System.currentTimeMillis(), 20);

      instance.paintComponent(g);

      row1.add(System.currentTimeMillis(), 40);
      instance.setxAxisLabelRenderer(new DateTimeRenderer("HH:mm:ss"));
      instance.paintComponent(g);
   }

   /**
    * Test of setRows method, of class GraphPanelChart.
    */
   @Test
   public void testSetRows()
   {
      System.out.println("setRows");
      ConcurrentHashMap<String, GraphPanelChartRow> aRows = null;
      GraphPanelChart instance = new GraphPanelChart();
      instance.setRows(aRows);
   }

   /**
    * Test of setyAxisLabelRenderer method, of class GraphPanelChart.
    */
   @Test
   public void testSetyAxisLabelRenderer()
   {
      System.out.println("setyAxisLabelRenderer");
      NumberRenderer yAxisLabelRenderer = null;
      GraphPanelChart instance = new GraphPanelChart();
      instance.setyAxisLabelRenderer(yAxisLabelRenderer);
   }

   /**
    * Test of setxAxisLabelRenderer method, of class GraphPanelChart.
    */
   @Test
   public void testSetxAxisLabelRenderer()
   {
      System.out.println("setxAxisLabelRenderer");
      NumberRenderer xAxisLabelRenderer = null;
      GraphPanelChart instance = new GraphPanelChart();
      instance.setxAxisLabelRenderer(xAxisLabelRenderer);
   }
}
