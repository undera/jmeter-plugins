package kg.apc.charting.plotters;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.Iterator;
import java.util.Map.Entry;
import kg.apc.charting.AbstractGraphPanelChartElement;
import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.ChartSettings;

/**
 *
 * @author Stephane Hoblingre
 */
public abstract class AbstractRowPlotter {

   ChartSettings chartSettings = null;
   Rectangle chartRect = null;
   protected long minXVal;
   protected long maxXVal;
   protected double minYVal;
   protected double maxYVal;
   //live values to paint rows
   protected double dxForDVal;
   protected double dyForDVal;

   protected int x, y;
   protected int prevX;
   protected int prevY;

   public AbstractRowPlotter(ChartSettings chartSettings) {
      this.chartSettings = chartSettings;
   }

   public void setBoundsValues(Rectangle chartRect, long minXVal, long maxXVal, double minYVal, double maxYVal) {
      this.chartRect = chartRect;
      this.minXVal = minXVal;
      this.maxXVal = maxXVal;
      this.minYVal = minYVal;
      this.maxYVal = maxYVal;

      dxForDVal = (maxXVal <= minXVal) ? 0 : (double) chartRect.width / (maxXVal - minXVal);
      dyForDVal = (maxYVal <= minYVal) ? 0 : (double) chartRect.height / (maxYVal - minYVal);
   }

   protected boolean isChartPointValid(int xx, int yy) {
      boolean ret = true;

      //check x
      if (xx < chartRect.x || xx > chartRect.x + chartRect.width) {
         ret = false;
      } else //check y bellow x axis
      if (yy > chartRect.y + chartRect.height) {
         ret = false;
      }

      return ret;
   }

   public void paintRow(Graphics2D g2d, AbstractGraphRow row, String rowLabel, Color color) {
      FontMetrics fm = g2d.getFontMetrics(g2d.getFont());
      Iterator<Entry<Long, AbstractGraphPanelChartElement>> it = row.iterator();
      Entry<Long, AbstractGraphPanelChartElement> element;

      prevX = -1;
      prevY = chartRect.y + chartRect.height;

      while (it.hasNext()) {
         if (!row.isDrawOnChart()) {
            continue;
         }
         element = it.next();
         processPoint(g2d, element, rowLabel, color);
      }

      postPaintRow();
   }

   protected abstract void processPoint(Graphics2D g2d, Entry<Long, AbstractGraphPanelChartElement> element, String rowLabel, Color color);

   protected void postPaintRow() {

   }

   protected void drawMarker(Graphics2D g2d, AbstractGraphRow row, Color color, int x, int y) {
      int radius = row.getMarkerSize();

      //check if forced via settings
      if(chartSettings.getChartMarkers() == ChartSettings.CHART_MARKERS_YES) radius = Math.max(AbstractGraphRow.MARKER_SIZE_SMALL, row.getMarkerSize());
      if(chartSettings.getChartMarkers() == ChartSettings.CHART_MARKERS_NO) radius = AbstractGraphRow.MARKER_SIZE_NONE;

      // draw markers
      if (radius != AbstractGraphRow.MARKER_SIZE_NONE && (y >= chartRect.y)) {
         g2d.setColor(color);
         if (isChartPointValid(x, y)) {
            g2d.fillOval(x - radius, y - radius, (radius) * 2, (radius) * 2);
         }
      }
   }

   protected void drawFinalLines(AbstractGraphRow row, Graphics2D g2d, int prevX, int prevY, Color color) {
      Stroke oldStroke = null;

      if (row.isDrawLine() && chartSettings.isDrawFinalZeroingLines()) {
         if (row.isDrawThickLines()) {
            oldStroke = g2d.getStroke();
            g2d.setStroke(chartSettings.getThickStroke());
         }
         g2d.setColor(color);
         g2d.drawLine(prevX, Math.max(prevY, chartRect.y), (int) (prevX + dxForDVal), chartRect.y + chartRect.height);
         if (row.isDrawThickLines()) {
            g2d.setStroke(oldStroke);
         }
      }
   }
}
