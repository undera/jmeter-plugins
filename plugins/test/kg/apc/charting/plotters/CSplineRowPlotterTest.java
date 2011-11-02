/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.charting.plotters;

import java.awt.Color;
import java.awt.Graphics2D;
import kg.apc.charting.AbstractGraphRow;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author z000205
 */
public class CSplineRowPlotterTest {

    public CSplineRowPlotterTest() {
    }

   @BeforeClass
   public static void setUpClass() throws Exception {
   }

   @AfterClass
   public static void tearDownClass() throws Exception {
   }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

   /**
    * Test of processPoint method, of class CSplineRowPlotter.
    */
   @Test
   public void testProcessPoint() {
      System.out.println("processPoint");
      Graphics2D g2d = null;
      Color color = null;
      int granulation = 0;
      CSplineRowPlotter instance = null;
      instance.processPoint(g2d, granulation);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   /**
    * Test of postPaintRow method, of class CSplineRowPlotter.
    */
   @Test
   public void testPostPaintRow() {
      System.out.println("postPaintRow");
      AbstractGraphRow row = null;
      Graphics2D g2d = null;
      Color color = null;
      CSplineRowPlotter instance = null;
      instance.postPaintRow(row, g2d);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

}