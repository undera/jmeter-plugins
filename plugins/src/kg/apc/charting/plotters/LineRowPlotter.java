package kg.apc.charting.plotters;

import java.awt.Graphics2D;
import kg.apc.charting.ChartSettings;
import org.apache.jorphan.gui.NumberRenderer;

/**
 *
 * @author Stephane Hoblingre
 */
public class LineRowPlotter extends AbstractRowPlotter {

   public LineRowPlotter(ChartSettings chartSettings, NumberRenderer labelRenderer) {
      super(chartSettings, labelRenderer);
      allowMarkers = true;
   }

   @Override
   protected void processPoint(Graphics2D g2d, int granulation) {
      boolean valid = isChartPointValid(x, y);
      if (mustDrawFirstZeroingLine && valid) {
         mustDrawFirstZeroingLine = false;
         prevX = x;
      }
      if (prevX >= 0) {
         if (valid) {
            if (prevY >= chartRect.y && y >= chartRect.y) {
               g2d.drawLine(prevX, prevY, x, y);
            } else if (prevY >= chartRect.y && y < chartRect.y) {
               int x1 = (x - prevX) * (chartRect.y - prevY) / (y - prevY) + prevX;
               g2d.drawLine(prevX, prevY, x1, chartRect.y);
            } else if (prevY < chartRect.y && y >= chartRect.y) {
               int x1 = (x - prevX) * (chartRect.y - prevY) / (y - prevY) + prevX;
               g2d.drawLine(x1, chartRect.y, x, y);
            }
         }
      }
   }
}
