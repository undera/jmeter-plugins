package kg.apc.charting.plotters;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Map.Entry;
import kg.apc.charting.AbstractGraphPanelChartElement;
import kg.apc.charting.ChartSettings;

/**
 *
 * @author Stephane Hoblingre
 */
public class LineRowPlotter extends AbstractRowPlotter {

   public LineRowPlotter(ChartSettings chartSettings) {
      super(chartSettings);
   }

   @Override
   protected void processPoint(Graphics2D g2d, Entry<Long, AbstractGraphPanelChartElement> element, String rowLabel, Color color) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

}
