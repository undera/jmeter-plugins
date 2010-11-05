package kg.apc.jmeter.charting;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.jmeter.util.JMeterUtils;

import org.apache.jorphan.gui.NumberRenderer;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author apc
 */
public class GraphPanelChart
      extends JComponent
      implements ClipboardOwner
{
   private static final String AD_TEXT = "http://apc.kg";
   private static final String NO_SAMPLES = "Waiting for samples...";
   private static final int spacing = 5;
   /*
    * Special type of graph were minY is forced to 0 and maxY is forced to 100
    * to display percentage charts (eg cpu monitoring)
    */
   public static final int CHART_PERCENTAGE = 0;
   public static final int CHART_DEFAULT = -1;
   private static final Logger log = LoggingManager.getLoggerForClass();
   private Rectangle legendRect;
   private Rectangle xAxisRect;
   private Rectangle yAxisRect;
   private Rectangle chartRect;
   private static final Rectangle zeroRect = new Rectangle();
   private AbstractMap<String, AbstractGraphRow> rows;
   private double maxYVal;
   private double minYVal;
   private long maxXVal;
   private long minXVal;
   private long currentXVal;
   private static final int gridLinesCount = 10;
   private NumberRenderer yAxisLabelRenderer;
   private NumberRenderer xAxisLabelRenderer;
   private boolean drawCurrentX = false;
   private int forcedMinX = -1;
   private int chartType = CHART_DEFAULT;
   // The stroke used to paint Graph's dashed lines
   private Stroke dashStroke = new BasicStroke(
         1.0f, // Width
         BasicStroke.CAP_SQUARE, // End cap
         BasicStroke.JOIN_MITER, // Join style
         10.0f, // Miter limit
         new float[]
         {
            1.0f, 4.0f
         }, // Dash pattern
         0.0f);                     			// Dash phase
   // The stroke to paint thick Graph rows
   private Stroke thickStroke = new BasicStroke(
         AbstractGraphRow.LINE_THICKNESS_BIG,
         BasicStroke.CAP_BUTT,
         BasicStroke.JOIN_BEVEL);
   // Message display in graphs. Used for perfmon error messages
   private String errorMessage = null;
   // Chart's gradient background end color
   private Color gradientColor = new Color(229, 236, 246);
   // Chart's Axis Color. For good results, use gradient color - (30, 30, 30)
   private Color axisColor = new Color(199, 206, 216);
   //save file path. We remember last folder used.
   private static String savePath = null;

   //limit the maximum number of points when drawing a line, by averaging values
   //if necessary. If -1 is assigned, no limit.
   private int maxPoints = -1;

   // Default draw options - these are default values if no property is entered in user.properties
   // List of possible properties (TODO: The explaination must be written in readme file)
   // jmeterPlugin.drawGradient=(true/false)
   // jmeterPlugin.neverDrawFinalZeroingLines=(true/false)
   // jmeterPlugin.optimizeYAxis=(true/false)
   // jmeterPlugin.neverDrawCurrentX=(true/false)
   // note to Andrey: Feel free to decide the default value!
   private static boolean drawGradient = true;
   private static boolean neverDrawFinalZeroingLines = false;
   private static boolean optimizeYAxis = true;
   private static boolean neverDrawCurrentX = false;

   //some of these preference can be overidden by the preference tab:
   private boolean settingsDrawGradient;
   private boolean settingsDrawFinalZeroingLines;
   private boolean settingsDrawCurrentX;

    public void setSettingsDrawCurrentX(boolean settingsDrawCurrentX)
    {
        this.settingsDrawCurrentX = settingsDrawCurrentX;
    }

    public void setSettingsDrawFinalZeroingLines(boolean settingsDrawFinalZeroingLines)
    {
        this.settingsDrawFinalZeroingLines = settingsDrawFinalZeroingLines;
    }

    public void setSettingsDrawGradient(boolean settingsDrawGradient)
    {
        this.settingsDrawGradient = settingsDrawGradient;
    }

    public boolean isSettingsDrawCurrentX()
    {
        return settingsDrawCurrentX;
    }

    public boolean isSettingsDrawGradient()
    {
        return settingsDrawGradient;
    }
    
    public static boolean isGlobalDrawFinalZeroingLines()
    {
        return !neverDrawFinalZeroingLines;
    }


   // If user entered configuration items in user.properties, overide default values.
   static
   {
      String cfgDrawGradient = JMeterUtils.getProperty("jmeterPlugin.drawGradient");
      if (cfgDrawGradient != null)
      {
         GraphPanelChart.drawGradient = "true".equalsIgnoreCase(cfgDrawGradient);
      }
      String cfgNeverDrawFinalZeroingLines = JMeterUtils.getProperty("jmeterPlugin.neverDrawFinalZeroingLines");
      if (cfgNeverDrawFinalZeroingLines != null)
      {
         GraphPanelChart.neverDrawFinalZeroingLines = "true".equalsIgnoreCase(cfgNeverDrawFinalZeroingLines);
      }
      String cfgOptimizeYAxis = JMeterUtils.getProperty("jmeterPlugin.optimizeYAxis");
      if (cfgOptimizeYAxis != null)
      {
         GraphPanelChart.optimizeYAxis = "true".equalsIgnoreCase(cfgOptimizeYAxis);
      }
      String cfgNeverDrawFinalCurrentX = JMeterUtils.getProperty("jmeterPlugin.neverDrawCurrentX");
      if (cfgNeverDrawFinalCurrentX != null)
      {
         GraphPanelChart.neverDrawCurrentX = "true".equalsIgnoreCase(cfgNeverDrawFinalCurrentX);
      }
   }

   /**
    * Creates new chart object with default parameters
    */
   public GraphPanelChart()
   {
      setBackground(Color.white);
      setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.lightGray, Color.darkGray));

      yAxisLabelRenderer = new NumberRenderer("#.#");
      xAxisLabelRenderer = new NumberRenderer("#.#");
      legendRect = new Rectangle();
      yAxisRect = new Rectangle();
      xAxisRect = new Rectangle();
      chartRect = new Rectangle();

      setDefaultDimensions();

      registerPopup();

      settingsDrawCurrentX = !neverDrawCurrentX;
      settingsDrawGradient = drawGradient;
      settingsDrawFinalZeroingLines = false;
   }

   public void setChartType(int type)
   {
      chartType = type;
   }

   private void drawFinalLines(AbstractGraphRow row, Graphics g, int prevX, int prevY, final double dxForDVal, Stroke oldStroke)
   {
      // draw final lines
      if (row.isDrawLine() && settingsDrawFinalZeroingLines)
      {
         if (row.isDrawThickLines())
         {
            ((Graphics2D) g).setStroke(thickStroke);
         }
         g.setColor(row.getColor());
         g.drawLine(prevX, prevY, (int) (prevX + dxForDVal), chartRect.y + chartRect.height);
         if (row.isDrawThickLines())
         {
            ((Graphics2D) g).setStroke(oldStroke);
         }
      }
   }

   private boolean drawMessages(Graphics2D g)
   {
      if (errorMessage != null)
      {
         g.setColor(Color.RED);
         g.drawString(errorMessage, getWidth() / 2 - g.getFontMetrics(g.getFont()).stringWidth(errorMessage) / 2, getHeight() / 2);
         return true;
      }
      if (rows.isEmpty())
      {
         g.setColor(Color.BLACK);
         g.drawString(NO_SAMPLES, getWidth() / 2 - g.getFontMetrics(g.getFont()).stringWidth(NO_SAMPLES) / 2, getHeight() / 2);
         return true;
      }
      return false;
   }

   private void getMinMaxDataValues()
   {
      maxXVal = 0L;
      maxYVal = 0L;
      minXVal = Long.MAX_VALUE;
      minYVal = Double.MAX_VALUE;

      Iterator<Entry<String, AbstractGraphRow>> it = rows.entrySet().iterator();
      Entry<String, AbstractGraphRow> row = null;
      AbstractGraphRow rowValue;
      while (it.hasNext())
      {
         row = it.next();
         rowValue = row.getValue();

         if (!rowValue.isDrawOnChart())
         {
            continue;
         }

         if (rowValue.getMaxY() > maxYVal)
         {
            maxYVal = rowValue.getMaxY();
         }

         if (rowValue.getMaxX() > maxXVal)
         {
            maxXVal = rowValue.getMaxX();
         }

         if (rowValue.getMinX() < minXVal)
         {
            minXVal = rowValue.getMinX();
         }

         if (rowValue.getMinY() < minYVal)
         {
            //we draw only positives values
            minYVal = rowValue.getMinY() >= 0 ? rowValue.getMinY() : 0;
         }
      }

      //maxYVal *= 1 + (double) 1 / (double) gridLinesCount;

      if (forcedMinX >= 0L)
      {
         minXVal = forcedMinX;
      }

      //prevent X axis not initialized in case of no row displayed
      //we use last known row
      if (minXVal == Long.MAX_VALUE && maxXVal == 0L && row != null)
      {
         maxXVal = row.getValue().getMaxX();
         minXVal = row.getValue().getMinX();
         minYVal = 0;
         maxYVal = 10;
      }
      else if (optimizeYAxis)
      {
         computeChartSteps();
      }
      else
      {
         minYVal = 0;
      }

   }

   /**
    * compute minY and step value to have better readable charts
    */
   private void computeChartSteps()
   {
      //if special type
      if (chartType == GraphPanelChart.CHART_PERCENTAGE)
      {
         minYVal = 0;
         maxYVal = 100;
         return;
      }

      //try to find the best range...
      //first, avoid special cases where maxY equal or close to minY
      if (maxYVal - minYVal < 0.1)
      {
         maxYVal = minYVal + 1;
      }

      //real step
      double step = (maxYVal - minYVal) / gridLinesCount;

      int pow = -1;
      double factor = -1;
      boolean found = false;

      double testStep;
      double testFactor;

      //find a step close to the real one
      while (!found)
      {
         pow++;
         //for small range (<10), don't use .5 factor.
         //we try to find integer steps as it is more easy to read
         if (pow > 0)
         {
            testFactor = 0.5;
         }
         else
         {
            testFactor = 1;
         }

         for (double f = 0; f <= 5; f = f + testFactor)
         {
            testStep = Math.pow(10, pow) * f;
            if (testStep >= step)
            {
               factor = f;
               found = true;
               break;
            }
         }
      }

      //first proposal
      double foundStep = Math.pow(10, pow) * factor;

      //we shift to the closest lower minval to align with the step
      minYVal = minYVal - minYVal % foundStep;

      //check if step is still good with minY trimmed. If not, use next factor.
      if (minYVal + foundStep * gridLinesCount < maxYVal)
      {
         foundStep = Math.pow(10, pow) * (factor + (pow > 0 ? 0.5 : 1));
      }

      //last visual optimization: find the optimal minYVal
      double trim = 10;

      while ((minYVal - minYVal % trim) + foundStep * gridLinesCount >= maxYVal && minYVal > 0)
      {
         minYVal = minYVal - minYVal % trim;
         trim = trim * 10;
      }

      //final calculation
      maxYVal = minYVal + foundStep * gridLinesCount;
   }

   private void setDefaultDimensions()
   {
      chartRect.setBounds(spacing, spacing, getWidth() - spacing * 2, getHeight() - spacing * 2);
      legendRect.setBounds(zeroRect);
      xAxisRect.setBounds(zeroRect);
      yAxisRect.setBounds(zeroRect);
   }

   private void calculateYAxisDimensions(FontMetrics fm)
   {
      // TODO: middle value labels often wider than max
      yAxisLabelRenderer.setValue(maxYVal);
      int axisWidth = fm.stringWidth(yAxisLabelRenderer.getText()) + spacing * 3;
      yAxisRect.setBounds(chartRect.x, chartRect.y, axisWidth, chartRect.height);
      chartRect.setBounds(chartRect.x + axisWidth, chartRect.y, chartRect.width - axisWidth, chartRect.height);
   }

   private void calculateXAxisDimensions(FontMetrics fm)
   {
      // FIXME: first value on X axis may take negative X coord,
      // we need to handle this and make Y axis wider
      int axisHeight = fm.getHeight() + spacing;
      xAxisLabelRenderer.setValue(maxXVal);
      int axisEndSpace = fm.stringWidth(xAxisLabelRenderer.getText()) / 2;
      xAxisRect.setBounds(chartRect.x, chartRect.y + chartRect.height - axisHeight, chartRect.width, axisHeight);
      chartRect.setBounds(chartRect.x, chartRect.y, chartRect.width - axisEndSpace, chartRect.height - axisHeight);
      yAxisRect.setBounds(yAxisRect.x, yAxisRect.y, yAxisRect.width, chartRect.height);
   }

   @Override
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);

      BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2d = image.createGraphics();
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      drawPanel(g2d);
      g.drawImage(image, 0, 0, this);
   }

   private void drawPanel(Graphics2D g)
   {
      g.setColor(Color.white);


      if (settingsDrawGradient)
      {
         GradientPaint gdp = new GradientPaint(0, 0, Color.white, 0, getHeight(), gradientColor);
         g.setPaint(gdp);
      }

      g.fillRect(0, 0, getWidth(), getHeight());
      paintAd(g);

      if (drawMessages(g))
      {
         return;
      }

      setDefaultDimensions();
      getMinMaxDataValues();

      try
      {
         paintLegend(g);
         calculateYAxisDimensions(g.getFontMetrics(g.getFont()));
         calculateXAxisDimensions(g.getFontMetrics(g.getFont()));
         paintYAxis(g);
         paintXAxis(g);
         paintChart(g);
      }
      catch (Exception e)
      {
         log.error("Error in paintComponent", e);
      }
   }

   private void paintLegend(Graphics g)
   {
      FontMetrics fm = g.getFontMetrics(g.getFont());
      int rectH = fm.getHeight();
      int rectW = rectH;

      Iterator<Entry<String, AbstractGraphRow>> it = rows.entrySet().iterator();
      Entry<String, AbstractGraphRow> row;
      int currentX = chartRect.x;
      int currentY = chartRect.y;
      int legendHeight = it.hasNext() ? rectH + spacing : 0;
      while (it.hasNext())
      {
         row = it.next();

         if (!row.getValue().isShowInLegend() || !row.getValue().isDrawOnChart())
         {
            continue;
         }

         // wrap row if overflowed
         if (currentX + rectW + spacing / 2 + fm.stringWidth(row.getKey()) > getWidth())
         {
            currentY += rectH + spacing / 2;
            legendHeight += rectH + spacing / 2;
            currentX = chartRect.x;
         }

         // draw legend color box
         g.setColor(row.getValue().getColor());
         g.fillRect(currentX, currentY, rectW, rectH);
         g.setColor(Color.black);
         g.drawRect(currentX, currentY, rectW, rectH);

         // draw legend item label
         currentX += rectW + spacing / 2;
         g.drawString(row.getKey(), currentX, (int) (currentY + rectH * 0.9));
         currentX += fm.stringWidth(row.getKey()) + spacing;
      }

      legendRect.setBounds(chartRect.x, chartRect.y, chartRect.width, legendHeight);
      chartRect.setBounds(chartRect.x, chartRect.y + legendHeight + spacing, chartRect.width, chartRect.height - legendHeight - spacing);
   }

   private void paintYAxis(Graphics g)
   {
      FontMetrics fm = g.getFontMetrics(g.getFont());

      String valueLabel;
      int labelXPos;
      int gridLineY;

      // shift 2nd and more lines
      int shift = 0;
      // for strokes swapping
      Stroke oldStroke = ((Graphics2D) g).getStroke();

      //draw markers
      g.setColor(axisColor);
      for (int n = 0; n <= gridLinesCount; n++)
      {
         gridLineY = chartRect.y + (int) ((gridLinesCount - n) * (double) chartRect.height / gridLinesCount);
         g.drawLine(chartRect.x - 3, gridLineY, chartRect.x + 3, gridLineY);
      }

      for (int n = 0; n <= gridLinesCount; n++)
      {
         //draw 2nd and more axis dashed and shifted
         if (n != 0)
         {
            ((Graphics2D) g).setStroke(dashStroke);
            shift = 7;
         }

         gridLineY = chartRect.y + (int) ((gridLinesCount - n) * (double) chartRect.height / gridLinesCount);

         // draw grid line with tick
         g.setColor(axisColor);
         g.drawLine(chartRect.x + shift, gridLineY, chartRect.x + chartRect.width, gridLineY);
         g.setColor(Color.black);

         // draw label
         yAxisLabelRenderer.setValue((minYVal * gridLinesCount + n * (maxYVal - minYVal)) / gridLinesCount);
         valueLabel = yAxisLabelRenderer.getText();
         labelXPos = yAxisRect.x + yAxisRect.width - fm.stringWidth(valueLabel) - spacing - spacing / 2;
         g.drawString(valueLabel, labelXPos, gridLineY + fm.getAscent() / 2);
      }
      //restore stroke
      ((Graphics2D) g).setStroke(oldStroke);
   }

   private void paintXAxis(Graphics g)
   {
      FontMetrics fm = g.getFontMetrics(g.getFont());

      String valueLabel;
      int labelXPos;
      int gridLineX;

      // shift 2nd and more lines
      int shift = 0;
      // for strokes swapping
      Stroke oldStroke = ((Graphics2D) g).getStroke();

      g.setColor(axisColor);
      //draw markers
      for (int n = 0; n <= gridLinesCount; n++)
      {
         gridLineX = chartRect.x + (int) (n * ((double) chartRect.width / gridLinesCount));
         g.drawLine(gridLineX, chartRect.y + chartRect.height - 3, gridLineX, chartRect.y + chartRect.height + 3);
      }

      for (int n = 0; n <= gridLinesCount; n++)
      {
         //draw 2nd and more axis dashed and shifted
         if (n != 0)
         {
            ((Graphics2D) g).setStroke(dashStroke);
            shift = 7;
         }

         gridLineX = chartRect.x + (int) (n * ((double) chartRect.width / gridLinesCount));

         // draw grid line with tick
         g.setColor(axisColor);
         g.drawLine(gridLineX, chartRect.y + chartRect.height - shift, gridLineX, chartRect.y);
         g.setColor(Color.black);

         // draw label
         xAxisLabelRenderer.setValue(minXVal + n * (double) (maxXVal - minXVal) / gridLinesCount);

         valueLabel = xAxisLabelRenderer.getText();
         labelXPos = gridLineX - fm.stringWidth(valueLabel) / 2;
         g.drawString(valueLabel, labelXPos, xAxisRect.y + fm.getAscent() + spacing);
      }

      //restore stroke
      ((Graphics2D) g).setStroke(oldStroke);

      if (drawCurrentX && settingsDrawCurrentX)
      {
         gridLineX = chartRect.x + (int) ((currentXVal - minXVal) * (double) chartRect.width / (maxXVal - minXVal));
         g.setColor(Color.GRAY);
         g.drawLine(gridLineX, chartRect.y, gridLineX, chartRect.y + chartRect.height);
         g.setColor(Color.black);
      }
   }

   private void paintChart(Graphics g)
   {
      g.setColor(Color.yellow);
      Iterator<Entry<String, AbstractGraphRow>> it = rows.entrySet().iterator();
      
      int biggestRowSize = getBiggestRowSize();

      while (it.hasNext())
      {
         Entry<String, AbstractGraphRow> row = it.next();
         if (row.getValue().isDrawOnChart())
         {
            paintRow(g, row.getValue(), biggestRowSize);
         }
      }
   }

   /*
    * Check if the point (x,y) is contained in the chart area
    * This is done to prevent line out of range if new point is added
    * during chart paint.
    */
   private boolean isChartPointValid(int x, int y)
   {
      boolean ret = true;

      //check x
      if (x < chartRect.x || x > chartRect.x + chartRect.width)
      {
         ret = false;
      }
      else //check y
      if (y < chartRect.y || y > chartRect.y + chartRect.height)
      {
         ret = false;
      }

      return ret;
   }

   public void setMaxPoints(int maxPoints)
   {
       this.maxPoints = maxPoints;
   }

    private int getBiggestRowSize()
    {
        int max = 1;
        Iterator<Entry<String, AbstractGraphRow>> it = rows.entrySet().iterator();
        while (it.hasNext())
        {
            Entry<String, AbstractGraphRow> row = it.next();
            if (row.getValue().isDrawOnChart())
            {
                int size = row.getValue().size();
                if ( size > max)
                {
                    max = size;
                }
            }
        }

        return max;
    }

   private void paintRow(Graphics g, AbstractGraphRow row, int biggestRowSize)
   {
      //how many points to average?
      int factor;

      if (maxPoints > 0)
      {
          factor = (biggestRowSize / maxPoints) + 1;
      } else
      {
          factor = 1;
      }

      FontMetrics fm = g.getFontMetrics(g.getFont());
      Iterator<Entry<Long, AbstractGraphPanelChartElement>> it = row.iterator();
      Entry<Long, AbstractGraphPanelChartElement> element;
      int radius = row.getMarkerSize();
      int x, y;
      int prevX = settingsDrawFinalZeroingLines ? chartRect.x : -1;
      int prevY = chartRect.y + chartRect.height;
      final double dxForDVal = (maxXVal <= minXVal) ? 0 : (double) chartRect.width / (maxXVal - minXVal);
      final double dyForDVal = (maxYVal <= minYVal) ? 0 : (double) chartRect.height / (maxYVal - minYVal);

      Stroke oldStroke = null;

      if (row.isDrawThickLines())
      {
         oldStroke = ((Graphics2D) g).getStroke();
      }

      //for better display, we always draw the first point
      boolean firstPoint = true;

      //how many actual points were averaged (basically, to handle last points of the collection)
      int nbAveragedValues = 0;

      while (it.hasNext())
      {
          if (!row.isDrawOnChart())
          {
             continue;
          }
          double calcPointX = 0;
          double calcPointY = 0;

          if (factor == 1 || firstPoint)
          {
              firstPoint = false;
              element = it.next();
              calcPointX = element.getKey().doubleValue();
              calcPointY = ((AbstractGraphPanelChartElement) element.getValue()).getValue();
          } else
          {
              nbAveragedValues = 0;
              for (int i = 0; i < factor; i++)
              {
                  if (it.hasNext())
                  {
                      nbAveragedValues++;
                      element = it.next();
                      calcPointX = calcPointX + element.getKey().doubleValue();
                      calcPointY = calcPointY + ((AbstractGraphPanelChartElement) element.getValue()).getValue();
                  }
              }
              calcPointX = calcPointX / nbAveragedValues;
              calcPointY = calcPointY / nbAveragedValues;
          }

         x = chartRect.x + (int) ((calcPointX - minXVal) * dxForDVal);
         int yHeight = (int) ((calcPointY - minYVal) * dyForDVal);
         y = chartRect.y + chartRect.height - yHeight;

         if (row.isDrawThickLines())
         {
            ((Graphics2D) g).setStroke(thickStroke);
         }

         // draw lines
         if (row.isDrawLine())
         {
            if (prevX > 0)
            {
               g.setColor(row.getColor());
               if (isChartPointValid(x, y))
               {
                  g.drawLine(prevX, prevY, x, y);
               }
            }
            prevX = x;
            prevY = y;
         }

         // draw bars
         if (row.isDrawBar())
         {
            g.setColor(row.getColor());
            if (isChartPointValid(x, y))
            {
               g.fillRect(x - (chartRect.width / gridLinesCount / 2) - 1, y,
                     chartRect.width / gridLinesCount - 2, yHeight);
            }
         }

         if (row.isDrawThickLines())
         {
            ((Graphics2D) g).setStroke(oldStroke);
         }

         if (row.isDrawValueLabel())
         {
            g.setColor(Color.DARK_GRAY);
            yAxisLabelRenderer.setValue(calcPointY);
            g.drawString(yAxisLabelRenderer.getText(),
                  x + row.getMarkerSize() + spacing,
                  y + fm.getAscent() / 2);
         }

         // draw markers
         if (radius != AbstractGraphRow.MARKER_SIZE_NONE)
         {
            g.setColor(row.getColor());
            if (isChartPointValid(x, y))
            {
               g.fillOval(x - radius, y - radius, (radius) * 2, (radius) * 2);
               //g.setColor(Color.black);
               //g.drawOval(x - radius, y - radius, radius * 2, radius * 2);
            }
         }
      }

      drawFinalLines(row, g, prevX, prevY, dxForDVal, oldStroke);
   }

   /**
    *
    * @param aRows
    */
   public void setRows(AbstractMap<String, AbstractGraphRow> aRows)
   {
      rows = aRows;
   }

   /**
    * @param yAxisLabelRenderer the yAxisLabelRenderer to set
    */
   public void setyAxisLabelRenderer(NumberRenderer yAxisLabelRenderer)
   {
      this.yAxisLabelRenderer = yAxisLabelRenderer;
   }

   /**
    * @param xAxisLabelRenderer the xAxisLabelRenderer to set
    */
   public void setxAxisLabelRenderer(NumberRenderer xAxisLabelRenderer)
   {
      this.xAxisLabelRenderer = xAxisLabelRenderer;
   }

   /**
    * @param drawFinalZeroingLines the drawFinalZeroingLines to set
    */
   public void setDrawFinalZeroingLines(boolean drawFinalZeroingLines)
   {
      settingsDrawFinalZeroingLines = drawFinalZeroingLines && !neverDrawFinalZeroingLines;
   }

   /**
    * @param drawCurrentX the drawCurrentX to set
    */
   public void setDrawCurrentX(boolean drawCurrentX)
   {
      this.drawCurrentX = drawCurrentX;
   }

   /**
    * @param currentX the currentX to set
    */
   public void setCurrentX(long currentX)
   {
      this.currentXVal = currentX;
   }

   /**
    *
    * @param i
    */
   public void setForcedMinX(int i)
   {
      forcedMinX = i;
   }

   //Paint the add the same color of the axis but with transparency
   private void paintAd(Graphics2D g)
   {
      Font oldFont = g.getFont();
      g.setFont(g.getFont().deriveFont(10F));
      g.setColor(axisColor);
      Composite oldComposite = g.getComposite();
      g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));

      g.drawString(AD_TEXT,
            getWidth() - g.getFontMetrics().stringWidth(AD_TEXT) - spacing,
            g.getFontMetrics().getHeight() - spacing + 1);
      g.setComposite(oldComposite);
      g.setFont(oldFont);
   }

   /*
    * Clear error messages
    */
   public void clearErrorMessage()
   {
      errorMessage = null;
   }

   /*
    * Set error message if not null and not empty
    * @param msg the error message to set
    */
   public void setErrorMessage(String msg)
   {
      if (msg != null && msg.trim().length() > 0)
      {
         errorMessage = msg;
      }
   }

   // Adding a popup menu to copy image in clipboard
   @Override
   public void lostOwnership(Clipboard clipboard, Transferable contents)
   {
      // do nothing
   }

   private Image getImage()
   {
      return (Image) getBufferedImage();
   }

   private BufferedImage getBufferedImage()
   {
      BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2 = image.createGraphics();
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      this.drawPanel(g2);

      return image;
   }

   /**
    * Thanks to stephane.hoblingre
    */
   private void registerPopup()
   {
      JPopupMenu popup = new JPopupMenu();
      this.setComponentPopupMenu(popup);
      JMenuItem itemCopy = new JMenuItem("Copy Image to Clipboard");
      JMenuItem itemSave = new JMenuItem("Save Image as...");
      itemCopy.addActionListener(new CopyAction());
      itemSave.addActionListener(new SaveAction());
      popup.add(itemCopy);
      //popup.addSeparator();
      popup.add(itemSave);
   }

   private class CopyAction
         implements ActionListener
   {
      @Override
      public void actionPerformed(final ActionEvent e)
      {
         Clipboard clipboard = getToolkit().getSystemClipboard();
         Transferable transferable = new Transferable()
         {
            @Override
            public Object getTransferData(DataFlavor flavor)
            {
               if (isDataFlavorSupported(flavor))
               {
                  return getImage();
               }
               return null;
            }

            @Override
            public DataFlavor[] getTransferDataFlavors()
            {
               return new DataFlavor[]
                     {
                        DataFlavor.imageFlavor
                     };
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor)
            {
               return DataFlavor.imageFlavor.equals(flavor);
            }
         };
         clipboard.setContents(transferable, GraphPanelChart.this);
      }
   }

   private class SaveAction
         implements ActionListener
   {
      @Override
      public void actionPerformed(final ActionEvent e)
      {
         JFileChooser chooser = savePath != null ? new JFileChooser(new File(savePath)) : new JFileChooser();
         chooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));

         int returnVal = chooser.showSaveDialog(GraphPanelChart.this);
         if (returnVal == JFileChooser.APPROVE_OPTION)
         {
            File file = chooser.getSelectedFile();
            if (!file.getAbsolutePath().toUpperCase().endsWith(".PNG"))
            {
               file = new File(file.getAbsolutePath() + ".png");
            }
            savePath = file.getParent();
            boolean doSave = true;
            if (file.exists())
            {
               int choice = JOptionPane.showConfirmDialog(GraphPanelChart.this, "Do you want to overwrite " + file.getAbsolutePath() + "?", "Save Image as", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
               doSave = (choice == JOptionPane.YES_OPTION);
            }

            if (doSave)
            {
               try
               {
                  FileOutputStream fos = new FileOutputStream(file);
                  ImageIO.write(getBufferedImage(), "png", fos);
                  fos.flush();
                  fos.close();
               }
               catch (IOException ex)
               {
                  JOptionPane.showConfirmDialog(GraphPanelChart.this, "Impossible to write the image to the file:\n" + ex.getMessage(), "Save Image as", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
               }
            }
         }
      }
   }
}
