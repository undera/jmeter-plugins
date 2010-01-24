package kg.apc.jmeter.vizualizers;

import java.awt.Color;
import java.util.concurrent.ConcurrentHashMap;

class GraphPanelChartRow
{
   private ConcurrentHashMap<Double, GraphPanelChartElement> values;
   public static final int MARKER_SIZE_SMALL = 3;
   private boolean drawLine;
   private int markerSize;
   private double maxX;
   private double maxY;
   private double minX;
   private double minY;
   private Color color;
   private final String label;

   GraphPanelChartRow(String threadName, Color nextColor)
   {
      values = new ConcurrentHashMap<Double, GraphPanelChartElement>();
      color=nextColor;
      label=threadName;
   }

   void add(double xVal, double yVal)
   {
      if (xVal>maxX) maxX=xVal;
      if (yVal>maxY) maxY=yVal;
      if (xVal<minX) minX=xVal;
      if (yVal<minY) minY=yVal;

      if (values.contains(xVal))
      {
         values.get(xVal).add(yVal);
      }
      else
      {
         values.put(xVal, new GraphPanelChartElement(yVal));
      }
   }

   void setDrawLine(boolean b)
   {
      drawLine=b;
   }

   void setMarkerSize(int aMarkerSize)
   {
      markerSize=aMarkerSize;
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

   /**
    * @return the maxX
    */
   public double getMaxX()
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
   public double getMinX()
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

   public Color getColor()
   {
      return color;
   }

   void setColor(Color nextColor)
   {
      color=nextColor;
   }
}
