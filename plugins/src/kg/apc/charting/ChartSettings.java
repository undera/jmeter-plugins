package kg.apc.charting;

import java.text.DecimalFormatSymbols;

/**
 * GraphPanelChart configuration and settings holder
 * @author Stephane Hoblingre
 */
public class ChartSettings {
    // Default draw options - these are default values if no property is entered in user.properties
    // List of possible properties:
    // jmeterPlugin.drawGradient=(true/false)
    // jmeterPlugin.neverDrawFinalZeroingLines=(true/false)
    // jmeterPlugin.optimizeYAxis=(true/false)
    // jmeterPlugin.neverDrawCurrentX=(true/false)
    // jmeterPlugin.useRelativeTime=(true/false)
    // jmeterPlugin.csvSeparator=(.?)
   
    //Global config
    private boolean configNeverDrawFinalZeroingLines = false;
    private boolean configOptimizeYAxis = true;
    private boolean configNeverDrawCurrentX = false;
    private String  configCsvSeparator;

    //Live settings
    private boolean drawGradient = true;
    private boolean drawFinalZeroingLines = false;
    private boolean drawCurrentX = false;
    private boolean useRelativeTime = true;
    private boolean preventXAxisOverScaling = false;
    private int hideNonRepValLimit = -1;
    private int maxPointPerRow = -1;
    private long forcedMaxY = -1;

    public ChartSettings() {
       if (new DecimalFormatSymbols().getDecimalSeparator() == '.') {
            configCsvSeparator = ",";
        } else {
            configCsvSeparator = ";";
        }
    }

   //configuration accessors
   public String getConfigCsvSeparator() {
      return configCsvSeparator;
   }

   public void setConfigCsvSeparator(String configCsvSeparator) {
      this.configCsvSeparator = configCsvSeparator;
   }

   public void setConfigNeverDrawCurrentX(boolean configNeverDrawCurrentX) {
      this.configNeverDrawCurrentX = configNeverDrawCurrentX;
   }

   public void setConfigNeverDrawFinalZeroingLines(boolean configNeverDrawFinalZeroingLines) {
      this.configNeverDrawFinalZeroingLines = configNeverDrawFinalZeroingLines;
   }

   public boolean isConfigOptimizeYAxis() {
      return configOptimizeYAxis;
   }

   public void setConfigOptimizeYAxis(boolean configOptimizeYAxis) {
      this.configOptimizeYAxis = configOptimizeYAxis;
   }

   //settings accessors

   public boolean isDrawCurrentX() {
      return drawCurrentX;
   }

   public void setDrawCurrentX(boolean drawCurrentX) {
      this.drawCurrentX = drawCurrentX;
   }

   public boolean isDrawFinalZeroingLines() {
      return drawFinalZeroingLines;
   }

   public void setDrawFinalZeroingLines(boolean drawFinalZeroingLines) {
      this.drawFinalZeroingLines = drawFinalZeroingLines;
   }

   public boolean isDrawGradient() {
      return drawGradient;
   }

   public void setDrawGradient(boolean drawGradient) {
      this.drawGradient = drawGradient;
   }

   public int getHideNonRepValLimit() {
      return hideNonRepValLimit;
   }

   public void setHideNonRepValLimit(int hideNonRepValLimit) {
      this.hideNonRepValLimit = hideNonRepValLimit;
   }

   public boolean isPreventXAxisOverScaling() {
      return preventXAxisOverScaling;
   }

   public void setPreventXAxisOverScaling(boolean preventXAxisOverScaling) {
      this.preventXAxisOverScaling = preventXAxisOverScaling;
   }

   public boolean isUseRelativeTime() {
      return useRelativeTime;
   }

   public void setUseRelativeTime(boolean useRelativeTime) {
      this.useRelativeTime = useRelativeTime;
   }

   public int getMaxPointPerRow() {
      return maxPointPerRow;
   }

   public void setMaxPointPerRow(int maxPointPerRow) {
      this.maxPointPerRow = maxPointPerRow;
   }

   public long getForcedMaxY() {
      return forcedMaxY;
   }

   public void setForcedMaxY(long forcedMaxY) {
      this.forcedMaxY = forcedMaxY;
   }
   
   // initialisation
   public void enableDrawFinalZeroingLines() {
      drawFinalZeroingLines = !configNeverDrawFinalZeroingLines;
   }

   public void enableDrawCurrentX() {
      drawCurrentX = !configNeverDrawCurrentX;
   }
}
