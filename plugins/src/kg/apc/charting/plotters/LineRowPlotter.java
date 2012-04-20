package kg.apc.charting.plotters;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
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
      if(chartSettings.getLineWidth() == 0) return;
      Stroke oldStroke = g2d.getStroke();
      BasicStroke newStroke;
      if(chartSettings.getLineWidth() > 1) {
         newStroke = new BasicStroke(chartSettings.getLineWidth(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
      } else {
         newStroke = new BasicStroke(chartSettings.getLineWidth());
      }
      g2d.setStroke(newStroke);

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
      g2d.setStroke(oldStroke);
   }
}
