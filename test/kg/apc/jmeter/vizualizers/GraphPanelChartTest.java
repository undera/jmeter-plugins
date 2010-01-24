/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.vizualizers;

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.ConcurrentHashMap;
import kg.apc.jmeter.util.TestGraphics;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author APC
 */
public class GraphPanelChartTest {

    public GraphPanelChartTest() {
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
    public void setUp() {
    }

    @After
    public void tearDown() {
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
      final ConcurrentHashMap<String, GraphPanelChartRow> rows = new ConcurrentHashMap<String, GraphPanelChartRow>();
      instance.setRows(rows);
      final GraphPanelChartRow row1 = new GraphPanelChartRow("test", Color.black);
      rows.put("test 1", row1);
      row1.add(10, 20);
      instance.setSize(500, 500);
      instance.paintComponent(g);
   }
}