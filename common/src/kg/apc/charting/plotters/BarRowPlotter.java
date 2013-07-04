package kg.apc.charting.plotters;

import java.awt.Composite;
import java.awt.Graphics2D;
import kg.apc.charting.ChartSettings;
import org.apache.jorphan.gui.NumberRenderer;

/**
 *
 * @author Stephane Hoblingre
 */
public class BarRowPlotter extends AbstractRowPlotter {

   public BarRowPlotter(ChartSettings chartSettings, NumberRenderer labelRenderer) {
      super(chartSettings, labelRenderer);
      allowMarkers = false;
   }

   @Override
   protected void processPoint(Graphics2D g2d, int granulation) {
      //fix bar flickering
      if (y < chartRect.y) {
         y = chartRect.y;
      }
      if (isChartPointValid(x + 1, y)) { //as we draw bars, xMax values must be rejected
         int x2 = chartRect.x + (int) ((calcPointX + granulation - minXVal) * dxForDVal) - x - 1;
         Composite oldComposite = g2d.getComposite();
         g2d.setComposite(chartSettings.getBarComposite());

         int yHeight = chartRect.y + chartRect.height - y;

         g2d.fillRect(x, y - 1, x2, yHeight + 1);
         g2d.setComposite(oldComposite);
      }
   }
}
