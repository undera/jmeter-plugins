package kg.apc.jmeter.vizualizers;

import java.awt.Color;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

class GraphPanelChartRow
{
   private ConcurrentSkipListMap<Long, GraphPanelChartElement> values;
   public static final int MARKER_SIZE_NONE = 0;
   public static final int MARKER_SIZE_SMALL = 2;
   public static final int MARKER_SIZE_BIG = 4;
   private boolean drawLine;
   private int markerSize = MARKER_SIZE_NONE;
   private long maxX;
   private long minX = System.currentTimeMillis();
   private double maxY;
   private double minY = System.currentTimeMillis();
   private Color color;
   private final String label;

   GraphPanelChartRow(String threadName, Color nextColor, boolean aDrawLine, int aMarkerSize)
   {
      values = new ConcurrentSkipListMap<Long, GraphPanelChartElement>();
      color = nextColor;
      label = threadName;
      drawLine = aDrawLine;
      markerSize = aMarkerSize;
   }

   void add(long xVal, double yVal)
   {
      if (xVal > maxX)
         maxX = xVal;
      if (yVal > maxY)
         maxY = yVal;
      if (xVal < minX)
         minX = xVal;
      if (yVal < minY)
         minY = yVal;

      if (values.containsKey(xVal))
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
      drawLine = b;
   }

   void setMarkerSize(int aMarkerSize)
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

   public Color getColor()
   {
      return color;
   }

   public void setColor(Color nextColor)
   {
      color = nextColor;
   }

   public Iterator<Entry<Long, GraphPanelChartElement>> iterator()
   {
      return values.entrySet().iterator();
   }
}
