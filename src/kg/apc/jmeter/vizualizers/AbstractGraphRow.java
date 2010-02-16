package kg.apc.jmeter.vizualizers;

import java.awt.Color;
import java.util.Iterator;
import java.util.Map.Entry;

public abstract class AbstractGraphRow
{
   public static final int MARKER_SIZE_NONE = 0;
   public static final int MARKER_SIZE_SMALL = 3;
   public static final int MARKER_SIZE_BIG = 5;
   protected boolean drawLine = false;
   private boolean drawValueLabel = false;
   private boolean showInLegend = true;
   protected int markerSize = MARKER_SIZE_NONE;
   protected Color color = Color.BLACK;
   protected String label = "";
   protected long maxX = Long.MIN_VALUE;
   protected long minX = Long.MAX_VALUE;
   protected double maxY = Double.MIN_VALUE;
   protected double minY = Double.MAX_VALUE;
   private boolean drawOnChart=true;

   public void setDrawLine(boolean b)
   {
      drawLine = b;
   }

   public void setMarkerSize(int aMarkerSize)
   {
      markerSize = aMarkerSize;
   }

   /**
    * @return the drawLine
    */
   public boolean isDrawLine()
   {
      return drawLine;
   }

   /**
    * @return the markerSize
    */
   public int getMarkerSize()
   {
      return markerSize;
   }

   public Color getColor()
   {
      return color;
   }

   public void setColor(Color nextColor)
   {
      color = nextColor;
   }

   /**
    * @return the label
    */
   public String getLabel()
   {
      return label;
   }

   /**
    * @param label the label to set
    */
   public void setLabel(String label)
   {
      this.label = label;
   }

   /**
    * @return the maxX
    */
   public long getMaxX()
   {
      return maxX;
   }

   /**
    * @return the maxY
    */
   public double getMaxY()
   {
      return maxY;
   }

   /**
    * @return the minX
    */
   public long getMinX()
   {
      return minX;
   }

   /**
    * @return the minY
    */
   public double getMinY()
   {
      return minY;
   }

   public void add(long xVal, double yVal)
   {
      if (xVal > maxX)
         maxX = xVal;
      if (yVal > maxY)
         maxY = yVal;
      if (xVal < minX)
         minX = xVal;
      if (yVal < minY)
         minY = yVal;
   }

   public abstract Iterator<Entry<Long, GraphPanelChartElement>> iterator();

   /**
    * @return the drawValueLabel
    */
   public boolean isDrawValueLabel()
   {
      return drawValueLabel;
   }

   /**
    * @param drawValueLabel the drawValueLabel to set
    */
   public void setDrawValueLabel(boolean drawValueLabel)
   {
      this.drawValueLabel = drawValueLabel;
   }

   /**
    * @return the showInLegend
    */
   public boolean isShowInLegend()
   {
      return showInLegend;
   }

   /**
    * @param showInLegend the showInLegend to set
    */
   public void setShowInLegend(boolean showInLegend)
   {
      this.showInLegend = showInLegend;
   }

   /**
    * @return the drawOnChart
    */
   public boolean isDrawOnChart()
   {
      return drawOnChart;
   }

   /**
    * @param drawOnChart the drawOnChart to set
    */
   public void setDrawOnChart(boolean drawOnChart)
   {
      this.drawOnChart = drawOnChart;
   }
}
