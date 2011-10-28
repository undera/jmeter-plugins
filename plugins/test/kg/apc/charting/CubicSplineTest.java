/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.charting;

import kg.apc.charting.rows.GraphRowSumValues;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Stephane Hoblingre
 */
public class CubicSplineTest {

    public CubicSplineTest() {
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
    * Test of init method, of class CubicSpline.
    */
   @Test
   public void testInit() {
      System.out.println("init");
      AbstractGraphRow row = new GraphRowSumValues(false);
      row.add(1, 1);
      row.add(2, 4);
      row.add(3, 2);
      CubicSpline instance = new CubicSpline(row);
      instance.init(row);
   }

   /**
    * Test of interpolate method, of class CubicSpline.
    */
   @Test
   public void testInterpolate() {
      System.out.println("interpolate");
      AbstractGraphRow row = new GraphRowSumValues(false);
      row.add(1, 1);
      row.add(2, 4);
      row.add(4, 2);
      
      CubicSpline instance = new CubicSpline(row);
      double xx = 1.0;
      double expResult = 1.0;
      double result = instance.interpolate(xx);
      assertEquals(expResult, result, 0.0);

      xx = 3.5;
      expResult = 3.125;
      result = instance.interpolate(xx);
      assertEquals(expResult, result, 0.0);
   }

}