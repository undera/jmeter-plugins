package kg.apc.jmeter.dotchart;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Graphics;
import org.apache.jmeter.samplers.SampleResult;
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
public class DotChartTest
{
   public DotChartTest()
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
    * Test of clearData method, of class DotChart.
    */
   @Test
   public void testClearData()
   {
      System.out.println("clearData");
      DotChartModel model = new DotChartModel();
      fillSamples(model);
      DotChart instance = new DotChart(model);
      instance.clearData();
      assertEquals(0, model.size());
   }

   /**
    * Test of paintComponent method, of class DotChart.
    */
   @Test
   public void testPaintComponent()
   {
      System.out.println("paintComponent");
      DotChartModel model = new DotChartModel();
      DotChart parent = new DotChart(model);
      DotChart instance = new DotChart(model);
      parent.add(instance);
      Graphics g = new TestGraphics();

      instance.paintComponent(g);
      fillSamples(model);
      assertEquals(2, model.get("Test1").getCount());

      instance.paintComponent(g);
   }

   /**
    * Test of getScrollableTracksViewportWidth method, of class DotChart.
    */
   @Test
   public void testGetScrollableTracksViewportWidth()
   {
      System.out.println("getScrollableTracksViewportWidth");
      DotChart instance = new DotChart();
      boolean expResult = true;
      boolean result = instance.getScrollableTracksViewportWidth();
      assertEquals(expResult, result);
   }

   /**
    * Test of getScrollableTracksViewportHeight method, of class DotChart.
    */
   @Test
   public void testGetScrollableTracksViewportHeight()
   {
      System.out.println("getScrollableTracksViewportHeight");
      DotChart instance = new DotChart();
      boolean expResult = true;
      boolean result = instance.getScrollableTracksViewportHeight();
      assertEquals(expResult, result);
   }

   /**
    * Test of getPreferredScrollableViewportSize method, of class DotChart.
    */
   @Test
   public void testGetPreferredScrollableViewportSize()
   {
      System.out.println("getPreferredScrollableViewportSize");
      DotChart instance = new DotChart();
      Dimension expResult = new Dimension(100, 100);
      Dimension result = instance.getPreferredScrollableViewportSize();
      assertEquals(expResult, result);
   }

   /**
    * Test of getScrollableUnitIncrement method, of class DotChart.
    */
   @Test
   public void testGetScrollableUnitIncrement()
   {
      System.out.println("getScrollableUnitIncrement");
      Rectangle visibleRect = null;
      int orientation = 0;
      int direction = 0;
      DotChart instance = new DotChart();
      int expResult = 5;
      int result = instance.getScrollableUnitIncrement(visibleRect, orientation, direction);
      assertEquals(expResult, result);
   }

   /**
    * Test of getScrollableBlockIncrement method, of class DotChart.
    */
   @Test
   public void testGetScrollableBlockIncrement()
   {
      System.out.println("getScrollableBlockIncrement");
      Rectangle visibleRect = new Rectangle();
      int orientation = 0;
      int direction = 0;
      DotChart instance = new DotChart();
      int expResult = 0;
      int result = instance.getScrollableBlockIncrement(visibleRect, orientation, direction);
      assertEquals(expResult, result);
   }

   private void fillSamples(DotChartModel model)
   {
      SampleResult sr = new SampleResult();
      sr.setTime(100);
      sr.setSampleLabel("Test1");
      sr.setAllThreads(1);
      sr.setGroupThreads(1);
      model.addSample(sr);

      sr = new SampleResult();
      sr.setTime(110);
      sr.setSampleLabel("Test1");
      sr.setAllThreads(3);
      sr.setGroupThreads(1);
      model.addSample(sr);

      sr = new SampleResult();
      sr.setTime(90);
      sr.setSampleLabel("Test1");
      sr.setAllThreads(2);
      model.addSample(sr);
      
      sr = new SampleResult();
      sr.setTime(0);
      sr.setSampleLabel("Test1");
      model.addSample(sr);
   }

   @Test
   public void testSetDrawSamples()
   {
      System.out.println("setDrawSamples");
      boolean b = false;
      DotChart instance = new DotChart();
      instance.setDrawSamples(b);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   @Test
   public void testIsDrawSamples()
   {
      System.out.println("isDrawSamples");
      DotChart instance = new DotChart();
      boolean expResult = false;
      boolean result = instance.isDrawSamples();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   @Test
   public void testIsDrawThreadAverages()
   {
      System.out.println("isDrawThreadAverages");
      DotChart instance = new DotChart();
      boolean expResult = false;
      boolean result = instance.isDrawThreadAverages();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   @Test
   public void testSetDrawThreadAverages()
   {
      System.out.println("setDrawThreadAverages");
      boolean drawThreadAverages = false;
      DotChart instance = new DotChart();
      instance.setDrawThreadAverages(drawThreadAverages);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   @Test
   public void testIsDrawAverages()
   {
      System.out.println("isDrawAverages");
      DotChart instance = new DotChart();
      boolean expResult = false;
      boolean result = instance.isDrawAverages();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   @Test
   public void testSetDrawAverages()
   {
      System.out.println("setDrawAverages");
      boolean drawAverages = false;
      DotChart instance = new DotChart();
      instance.setDrawAverages(drawAverages);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }
}
