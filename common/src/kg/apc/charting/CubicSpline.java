package kg.apc.charting;

import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Cubic interpolation utility
 * @author Stephane Hoblingre
 */
public class CubicSpline {

   //nb of reference points
   private int size = 0;
   //reference points
   private double[] x = null;
   private double[] y = null;
   //second derivates
   private double[] d2ydx2 = null;

   public CubicSpline(AbstractGraphRow row) {
      this.size = row.size();
      this.x = new double[size];
      this.y = new double[size];
      this.d2ydx2 = new double[size];
      init(row);
   }

   public final void init(AbstractGraphRow row) {
      Iterator<Entry<Long, AbstractGraphPanelChartElement>> it = row.iterator();
      Entry<Long, AbstractGraphPanelChartElement> element;
      
      int index = 0;
      while (it.hasNext() && index < size) {
         element = it.next();
         AbstractGraphPanelChartElement elt = (AbstractGraphPanelChartElement) element.getValue();
         x[index] = element.getKey().doubleValue();
         y[index] = elt.getValue();
         index++;
      }
      calcD2ydx2();
   }

   private void calcD2ydx2() {
      double p = 0.0D, qn = 0.0D, sig = 0.0D, un = 0.0D;
      double[] u = new double[size];

      d2ydx2[0] = u[0] = 0.0;
      for (int i = 1; i <= size - 2; i++) {
         sig = (x[i] - x[i - 1]) / (x[i + 1] - x[i - 1]);
         p = sig * d2ydx2[i - 1] + 2.0;
         d2ydx2[i] = (sig - 1.0) / p;
         u[i] = (y[i + 1] - y[i]) / (x[i + 1] - x[i]) - (y[i] - y[i - 1]) / (x[i] - x[i - 1]);
         u[i] = (6.0 * u[i] / (x[i + 1] - x[i - 1]) - sig * u[i - 1]) / p;
      }

      qn = un = 0.0;
      d2ydx2[size - 1] = (un - qn * u[size - 2]) / (qn * d2ydx2[size - 2] + 1.0);
      for (int k = size - 2; k >= 0; k--) {
         d2ydx2[k] = d2ydx2[k] * d2ydx2[k + 1] + u[k];
      }
   }

   public double interpolate(double xx) {
      double h = 0.0D, b = 0.0D, a = 0.0D, yy = 0.0D;
      int k = 0;
      int klo = 0;
      int khi = size - 1;
      while (khi - klo > 1) {
         k = (khi + klo) >> 1;
         if (x[k] > xx) {
            khi = k;
         } else {
            klo = k;
         }
      }
      h = x[khi] - x[klo];
      a = (x[khi] - xx) / h;
      b = (xx - x[klo]) / h;
      yy = a * y[klo] + b * y[khi] + ((a * a * a - a) * d2ydx2[klo] + (b * b * b - b) * d2ydx2[khi]) * (h * h) / 6.0;
      return yy;
   }
}
