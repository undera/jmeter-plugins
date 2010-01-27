package kg.apc.jmeter.vizualizers;

import java.awt.Color;
import java.util.Iterator;
import java.util.Map.Entry;

public abstract class AbstractGraphRow
{
   public static final int MARKER_SIZE_NONE = 0;
   public static final int MARKER_SIZE_SMALL = 2;
   public static final int MARKER_SIZE_BIG = 4;
   protected boolean drawLine = false;
   protected int markerSize = MARKER_SIZE_NONE;
   protected Color color = Color.BLACK;
   protected String label = "";
   protected long maxX = Long.MIN_VALUE;
   protected long minX = Long.MAX_VALUE;
   protected double maxY = Double.MIN_VALUE;
   protected double minY = Double.MAX_VALUE;

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

   public abstract void add(long X, double Y);

   public abstract Iterator<Entry<Long, GraphPanelChartElement>> iterator();
}
