package kg.apc.charting.plotters;

import java.awt.Graphics2D;
import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.ChartSettings;
import kg.apc.charting.CubicSpline;
import org.apache.jorphan.gui.NumberRenderer;

/**
 *
 * @author Stephane Hoblingre
 */
public class CSplineRowPlotter extends AbstractRowPlotter {

   private long splineLinesCount = 200L;

   public CSplineRowPlotter(ChartSettings chartSettings, NumberRenderer labelRenderer) {
      super(chartSettings, labelRenderer);
      allowMarkers = true;
   }

   @Override
   protected void processPoint(Graphics2D g2d, int granulation) {
      //do nothing
   }

   @Override
   protected void postPaintRow(AbstractGraphRow row, Graphics2D g2d) {
      if (row.size() >= 3) {
         CubicSpline cs = new CubicSpline(row);
         long minX = row.getMinX();
         long maxX = row.getMaxX();

         double step = (double)(maxX - minX) / splineLinesCount;
         
         double currentX = minX;
         
         while (currentX <= maxX) {
            x = chartRect.x + (int) ((currentX - minXVal) * dxForDVal);
            int yHeight = (int) ((cs.interpolate(currentX) - minYVal) * dyForDVal);
            y = chartRect.y + chartRect.height - yHeight;

            //prevent out of range
            if (y < chartRect.y) {
               y = chartRect.y;
            }
            if (y > chartRect.y + chartRect.height) {
               y = chartRect.y + chartRect.height;
            }

            currentX += step;

            if (prevX >= 0) {
               g2d.drawLine(prevX, prevY, x, y);
            }

            prevX = x;
            prevY = y;
         }
      }
   }
}
