/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.charting.plotters;

import java.awt.Color;
import java.awt.Graphics2D;
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
public class LineRowPlotterTest {

    public LineRowPlotterTest() {
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
    * Test of processPoint method, of class LineRowPlotter.
    */
   @Test
   public void testProcessPoint() {
      System.out.println("processPoint");
      Graphics2D g2d = null;
      int granulation = 0;
      LineRowPlotter instance = null;
      instance.processPoint(g2d, granulation);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

}