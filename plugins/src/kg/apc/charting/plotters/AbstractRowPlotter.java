package kg.apc.charting.plotters;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.Iterator;
import java.util.Map.Entry;
import kg.apc.charting.AbstractGraphPanelChartElement;
import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.ChartSettings;
import org.apache.jorphan.gui.NumberRenderer;

/**
 *
 * @author Stephane Hoblingre
 */
public abstract class AbstractRowPlotter {

   private final static int labelSpacing = 5;

   protected ChartSettings chartSettings = null;
   protected NumberRenderer labelRenderer = null;
   protected Rectangle chartRect = null;
   protected long minXVal;
   protected long maxXVal;
   protected double minYVal;
   protected double maxYVal;
   //live values to paint rows
   protected double dxForDVal;
   protected double dyForDVal;
   protected double calcPointX = 0;
   protected double calcPointY = 0;
   protected int x, y;
   protected int prevX;
   protected int prevY;
   protected boolean allowMarkers = false;

   protected boolean mustDrawFirstZeroingLine;

   public AbstractRowPlotter(ChartSettings chartSettings, NumberRenderer labelRenderer) {
      this.chartSettings = chartSettings;
      this.labelRenderer = labelRenderer;
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

   /*
    * Check if the point (x,y) is contained in the chart area
    * We check only minX, maxX, and maxY to avoid flickering.
    * We take max(chartRect.y, y) as redering value
    * This is done to prevent line out of range if new point is added
    * during chart paint.
    */
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

   //this method is responsible to maintain x, y, prevX, prevY
   public synchronized void paintRow(Graphics2D g2d, AbstractGraphRow row, Color color, double zoomFactor, int limitPointFactor) {
      
      Iterator<Entry<Long, AbstractGraphPanelChartElement>> it = row.iterator();
      Entry<Long, AbstractGraphPanelChartElement> element;

      Stroke olStroke = null;

      prevX = -1;
      prevY = chartRect.y + chartRect.height;

      mustDrawFirstZeroingLine = chartSettings.isDrawFinalZeroingLines();

      if (row.isDrawThickLines()) {
         olStroke = g2d.getStroke();
         g2d.setStroke(chartSettings.getThickStroke());
      }

      g2d.setColor(color);

      while (it.hasNext()) {
         if (!row.isDrawOnChart()) {
            continue;
         }
         if (limitPointFactor == 1) {

            element = it.next();
            AbstractGraphPanelChartElement elt = (AbstractGraphPanelChartElement) element.getValue();

            //not compatible with factor != 1, ie cannot be used if limit nb of point is selected.
            if (chartSettings.getHideNonRepValLimit() > 0) {
               while (!elt.isPointRepresentative(chartSettings.getHideNonRepValLimit()) && it.hasNext()) {
                  element = it.next();
                  elt = (AbstractGraphPanelChartElement) element.getValue();
               }

               if (!elt.isPointRepresentative(chartSettings.getHideNonRepValLimit())) {
                  break;
               }
            }

            calcPointX = element.getKey().doubleValue();
            calcPointY = elt.getValue();
         } else {
            calcPointX = 0;
            calcPointY = 0;
            int nbPointProcessed = 0;
            for (int i = 0; i < limitPointFactor; i++) {
               if (it.hasNext()) {
                  element = it.next();
                  calcPointX = calcPointX + element.getKey().doubleValue();
                  calcPointY = calcPointY + ((AbstractGraphPanelChartElement) element.getValue()).getValue();
                  nbPointProcessed++;
               }
            }
            calcPointX = calcPointX / (double) nbPointProcessed;
            calcPointY = calcPointY / (double) nbPointProcessed;
         }

         calcPointY = calcPointY * zoomFactor;

         x = chartRect.x + (int) ((calcPointX - minXVal) * dxForDVal);
         int yHeight = (int) ((calcPointY - minYVal) * dyForDVal);
         y = chartRect.y + chartRect.height - yHeight;

         //now x and y are set, we can call plotter
         processPoint(g2d, row.getGranulationValue());
         
         //set prevX, prevY
         if (isChartPointValid(x, y)) {
            if(allowMarkers) {
               processMarker(g2d, row);
            }
            prevX = x;
            prevY = y;
         }

         if (row.isDrawValueLabel() && isChartPointValid(x, y) && y >= chartRect.y) {
            drawLabels(g2d, row, calcPointY);
         }
      }

      processFinalLines(row, g2d);

      x=0;
      y=0;
      prevX = -1;
      prevY = chartRect.y + chartRect.height;

      postPaintRow(row, g2d);

      if(olStroke != null) {
         g2d.setStroke(olStroke);
      }
   }

   protected abstract void processPoint(Graphics2D g2d, int granulation);

   protected void postPaintRow(AbstractGraphRow row, Graphics2D g2d) {
   }

   private void processMarker(Graphics2D g2d, AbstractGraphRow row) {
      int radius = row.getMarkerSize();

      //check if forced via settings
      if (chartSettings.getChartMarkers() == ChartSettings.CHART_MARKERS_YES) {
         radius = Math.max(AbstractGraphRow.MARKER_SIZE_SMALL, row.getMarkerSize());
      }
      if (chartSettings.getChartMarkers() == ChartSettings.CHART_MARKERS_NO) {
         radius = AbstractGraphRow.MARKER_SIZE_NONE;
      }

      // draw markers
      if (radius != AbstractGraphRow.MARKER_SIZE_NONE && (y >= chartRect.y)) {
         if (isChartPointValid(x, y)) {
            g2d.fillOval(x - radius, y - radius, (radius) * 2, (radius) * 2);
         }
      }
   }

   private void drawLabels(Graphics2D g2d, AbstractGraphRow row, double yValue) {
      Color oldColor = g2d.getColor();
      g2d.setColor(Color.DARK_GRAY);
      Font oldFont = g2d.getFont();
      g2d.setFont(g2d.getFont().deriveFont(Font.BOLD));

      FontMetrics fm = g2d.getFontMetrics(g2d.getFont());

      labelRenderer.setValue(yValue);
      int labelSize = g2d.getFontMetrics(g2d.getFont()).stringWidth(labelRenderer.getText());
      //if close to end
      if (x + row.getMarkerSize() + labelSpacing + labelSize > chartRect.x + chartRect.width) {
         g2d.drawString(labelRenderer.getText(),
                 x - row.getMarkerSize() - labelSpacing - labelSize,
                 y + fm.getAscent() / 2);
      } else {
         g2d.drawString(labelRenderer.getText(),
                 x + row.getMarkerSize() + labelSpacing,
                 y + fm.getAscent() / 2);
      }
      g2d.setFont(oldFont);
      g2d.setColor(oldColor);
   }

   private void processFinalLines(AbstractGraphRow row, Graphics2D g2d) {
      Stroke oldStroke = null;

      if (row.isDrawLine() && chartSettings.isDrawFinalZeroingLines()) {
         if (row.isDrawThickLines()) {
            oldStroke = g2d.getStroke();
            g2d.setStroke(chartSettings.getThickStroke());
         }
         g2d.drawLine(prevX, Math.max(prevY, chartRect.y), (int) (prevX + dxForDVal), chartRect.y + chartRect.height);
         if (row.isDrawThickLines()) {
            g2d.setStroke(oldStroke);
         }
      }
   }
}
