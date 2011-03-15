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
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormatSymbols;
import java.util.AbstractMap;
import java.util.HashMap;
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
        implements ClipboardOwner {

    JPopupMenu popup = new JPopupMenu();
    private static final String AD_TEXT = "http://apc.kg";
    private static final String NO_SAMPLES = "Waiting for samples...";
    private static final int spacing = 5;
    private static final int previewInset = 4;
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
    private long forcedMinX = -1;
    private long forcedMaxY = -1;
    private int chartType = CHART_DEFAULT;
    // The stroke used to paint Graph's dashed lines
    private final static Stroke dashStroke = new BasicStroke(
            1.0f, // Width
            BasicStroke.CAP_SQUARE, // End cap
            BasicStroke.JOIN_MITER, // Join style
            10.0f, // Miter limit
            new float[]{
                1.0f, 4.0f
            }, // Dash pattern
            0.0f); // Dash phase
    // The stroke to paint thick Graph rows
    private final static Stroke thickStroke = new BasicStroke(
            AbstractGraphRow.LINE_THICKNESS_BIG,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_BEVEL);
    // Message display in graphs. Used for perfmon error messages
    private String errorMessage = null;
    // Chart's gradient background end color
    private final static Color gradientColor = new Color(229, 236, 246);
    // Chart's Axis Color. For good results, use gradient color - (30, 30, 30)
    private final static Color axisColor = new Color(199, 206, 216);
    //the composite used to draw bars
    private final static AlphaComposite barComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
    //save file path. We remember last folder used.
    private static String savePath = null;
    //limit the maximum number of points when drawing a line, by averaging values
    //if necessary. If -1 is assigned, no limit.
    private int maxPoints = -1;
    private boolean preventXAxisOverScaling = false;
    private boolean reSetColors = false;

    public void setReSetColors(boolean reSetColors) {
        this.reSetColors = reSetColors;
    }
    private String xAxisLabel = "X axis label";
    private String yAxisLabel = "Y axis label";
    private int precisionLabel = -1;
    private int factorInUse = 1;
    private boolean displayPrecision = false;

    public void setDisplayPrecision(boolean displayPrecision) {
        this.displayPrecision = displayPrecision;
    }

    public void setxAxisLabel(String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
    }

    public void setyAxisLabel(String yAxisLabel) {
        this.yAxisLabel = yAxisLabel;
    }

    public void setPrecisionLabel(int precision) {
        this.precisionLabel = precision;
    }

    public void setForcedMaxY(long forcedMaxY) {
        this.forcedMaxY = forcedMaxY;
    }

    public long getForcedMaxY() {
        return forcedMaxY;
    }

    private void autoZoom_orig() {
        //row zooming
        if (!expendRows) {
            return;
        }

        Iterator<Entry<String, AbstractGraphRow>> it = rows.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, AbstractGraphRow> row = it.next();
            double[] minMax = row.getValue().getMinMaxY(maxPoints);
            if (minMax[1] > 0) {
                double zoomFactor = 1;
                while (minMax[1] * zoomFactor <= maxYVal) {
                    rowsZoomFactor.put(row.getKey(), zoomFactor);
                    zoomFactor = zoomFactor * 10;
                }
            } else {
                rowsZoomFactor.put(row.getKey(), 1.0);
            }
        }
    }

    private void autoZoom() {
        //row zooming
        double max = 0;
        double maxPow = 0;

        if (expendRows) {
            Iterator<Entry<String, AbstractGraphRow>> it = rows.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, AbstractGraphRow> row = it.next();
                double[] minMax = row.getValue().getMinMaxY(maxPoints);
                double pow = Math.pow(10, Math.floor(Math.log10(minMax[1])));
                double val = minMax[1] / pow;
                if (val > max) {
                    max = val;
                    maxPow = pow;
                }
            }
        }

        //log.info("Got max value: "+max);
        Iterator<Entry<String, AbstractGraphRow>> it2 = rows.entrySet().iterator();
        while (it2.hasNext()) {
            Entry<String, AbstractGraphRow> row = it2.next();
            double[] minMax = row.getValue().getMinMaxY(maxPoints);
            double pow = Math.pow(10, Math.floor(Math.log10(minMax[1])));
            rowsZoomFactor.put(row.getKey(), expendRows ? (maxPow / pow) : 1);
            //log.info(row.getKey()+" Factor : "+(maxPow/pow));
        }

        // autoZoom_orig();
    }

    private String getXAxisLabel() {
        String label;
        if (!displayPrecision) {
            label = xAxisLabel;
        } else {
            long granularity;
            if (maxPoints <= 0) {
                granularity = precisionLabel;

            } else {
                granularity = precisionLabel * factorInUse;
            }

            long min = granularity / 60000;
            long sec = (granularity % 60000) / 1000;
            long ms = (granularity % 60000) % 1000;

            label = xAxisLabel + " (granularity:";

            if (min > 0) {
                label += " " + min + " min";
            }
            if (sec > 0 || (min > 0 && ms > 0)) {
                if (min > 0) {
                    label += ",";
                }
                label += " " + sec + " sec";
            }
            if (ms > 0) {
                if (sec > 0 || min > 0) {
                    label += ",";
                }
                label += " " + ms + " ms";
            }

            label += ")";
        }
        return label;
    }
    private boolean isPreview = false;

    public void setIsPreview(boolean isPreview) {
        this.isPreview = isPreview;
        if (!isPreview) {
            this.setComponentPopupMenu(popup);
        } else {
            this.setComponentPopupMenu(null);
        }

    }
    // Default draw options - these are default values if no property is entered in user.properties
    // List of possible properties
    // jmeterPlugin.drawGradient=(true/false)
    // jmeterPlugin.neverDrawFinalZeroingLines=(true/false)
    // jmeterPlugin.optimizeYAxis=(true/false)
    // jmeterPlugin.neverDrawCurrentX=(true/false)
    private static boolean drawGradient = true;
    private static boolean neverDrawFinalZeroingLines = false;
    private static boolean optimizeYAxis = true;
    private static boolean neverDrawCurrentX = false;
    private static boolean useRelativeTime = true;
    private static String csvSeparator = null;
    private static boolean useNewZoomImplementation = false;
    //some of these preference can be overidden by the preference tab:
    private boolean settingsDrawGradient;
    private boolean settingsDrawFinalZeroingLines;
    private boolean settingsDrawCurrentX;
    private int settingsHideNonRepValLimit = -1;
    private boolean settingsUseRelativeTime = false;

    // If user entered configuration items in user.properties, overide default values.
    static {
        //default configuration
        if (new DecimalFormatSymbols().getDecimalSeparator() == '.') {
            GraphPanelChart.csvSeparator = ",";
        } else {
            GraphPanelChart.csvSeparator = ";";
        }

        //properties from user.properties
        String cfgDrawGradient = JMeterUtils.getProperty("jmeterPlugin.drawGradient");
        if (cfgDrawGradient != null) {
            GraphPanelChart.drawGradient = "true".equalsIgnoreCase(cfgDrawGradient.trim());
        }
        String cfgNeverDrawFinalZeroingLines = JMeterUtils.getProperty("jmeterPlugin.neverDrawFinalZeroingLines");
        if (cfgNeverDrawFinalZeroingLines != null) {
            GraphPanelChart.neverDrawFinalZeroingLines = "true".equalsIgnoreCase(cfgNeverDrawFinalZeroingLines.trim());
        }
        String cfgOptimizeYAxis = JMeterUtils.getProperty("jmeterPlugin.optimizeYAxis");
        if (cfgOptimizeYAxis != null) {
            GraphPanelChart.optimizeYAxis = "true".equalsIgnoreCase(cfgOptimizeYAxis.trim());
        }
        String cfgNeverDrawFinalCurrentX = JMeterUtils.getProperty("jmeterPlugin.neverDrawCurrentX");
        if (cfgNeverDrawFinalCurrentX != null) {
            GraphPanelChart.neverDrawCurrentX = "true".equalsIgnoreCase(cfgNeverDrawFinalCurrentX.trim());
        }
        String cfgCsvSeparator = JMeterUtils.getProperty("jmeterPlugin.csvSeparator");
        if (cfgCsvSeparator != null) {
            GraphPanelChart.csvSeparator = cfgCsvSeparator;
        }
        String cfgUseRelativeTime = JMeterUtils.getProperty("jmeterPlugin.useRelativeTime");
        if (cfgUseRelativeTime != null) {
            GraphPanelChart.useRelativeTime = "true".equalsIgnoreCase(cfgUseRelativeTime.trim());
        }
        String cfgUseNewZoomImplementation = JMeterUtils.getProperty("jmeterPlugin.useNewZoomImplementation");
        if (cfgUseNewZoomImplementation != null) {
            GraphPanelChart.useNewZoomImplementation = "true".equalsIgnoreCase(cfgUseNewZoomImplementation.trim());
        }
    }
    //relative time
    private long testStartTime = 0;

    public void setTestStartTime(long time) {
        testStartTime = time;
    }
    //row zooming
    private HashMap<String, Double> rowsZoomFactor = new HashMap<String, Double>();
    private boolean expendRows = false;

    public void setExpendRows(boolean expendRows) {
        this.expendRows = expendRows;
    }

    /**
     * Creates new chart object with default parameters
     */
    public GraphPanelChart(boolean allowCsvExport) {
        setBackground(Color.white);
        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.lightGray, Color.darkGray));

        yAxisLabelRenderer = new NumberRenderer("#.#");
        xAxisLabelRenderer = new NumberRenderer("#.#");
        legendRect = new Rectangle();
        yAxisRect = new Rectangle();
        xAxisRect = new Rectangle();
        chartRect = new Rectangle();

        setDefaultDimensions();

        registerPopup(allowCsvExport);

        settingsDrawCurrentX = !neverDrawCurrentX;
        settingsDrawGradient = drawGradient;
        //default to false, will be initialized in each vizualiser
        settingsDrawFinalZeroingLines = false;
        settingsUseRelativeTime = useRelativeTime;
    }

    public GraphPanelChart() {
        this(true);
    }

    public void setSettingsHideNonRepValLimit(int limit) {
        this.settingsHideNonRepValLimit = limit;
    }

    public void setPreventXAxisOverScaling(boolean preventXAxisOverScaling) {
        this.preventXAxisOverScaling = preventXAxisOverScaling;
    }

    public void setSettingsDrawCurrentX(boolean settingsDrawCurrentX) {
        this.settingsDrawCurrentX = settingsDrawCurrentX;
    }

    public void setSettingsDrawFinalZeroingLines(boolean settingsDrawFinalZeroingLines) {
        this.settingsDrawFinalZeroingLines = settingsDrawFinalZeroingLines;
    }

    public void setSettingsDrawGradient(boolean settingsDrawGradient) {
        this.settingsDrawGradient = settingsDrawGradient;
    }

    public boolean isSettingsDrawCurrentX() {
        return settingsDrawCurrentX;
    }

    public boolean isSettingsDrawGradient() {
        return settingsDrawGradient;
    }

    public static boolean isGlobalDrawFinalZeroingLines() {
        return !neverDrawFinalZeroingLines;
    }

    public boolean isModelContainsRow(AbstractGraphRow row) {
        return rows.containsKey(row.getLabel());
    }

    public void setChartType(int type) {
        chartType = type;
    }

    private void drawFinalLines(AbstractGraphRow row, Graphics g, int prevX, int prevY, final double dxForDVal, Stroke oldStroke, Color color) {
        // draw final lines
        if (row.isDrawLine() && settingsDrawFinalZeroingLines) {
            if (row.isDrawThickLines()) {
                ((Graphics2D) g).setStroke(thickStroke);
            }
            g.setColor(color);
            g.drawLine(prevX, Math.max(prevY, chartRect.y), (int) (prevX + dxForDVal), chartRect.y + chartRect.height);
            if (row.isDrawThickLines()) {
                ((Graphics2D) g).setStroke(oldStroke);
            }
        }
    }

    private boolean drawMessages(Graphics2D g) {
        if (errorMessage != null) {
            g.setColor(Color.RED);
            g.drawString(errorMessage, g.getClipBounds().width / 2 - g.getFontMetrics(g.getFont()).stringWidth(errorMessage) / 2, g.getClipBounds().height / 2);
            return true;
        }
        if (rows.isEmpty()) {
            g.setColor(Color.BLACK);
            g.drawString(NO_SAMPLES, g.getClipBounds().width / 2 - g.getFontMetrics(g.getFont()).stringWidth(NO_SAMPLES) / 2, g.getClipBounds().height / 2);
            return true;
        }
        return false;
    }

    private void getMinMaxDataValues() {
        maxXVal = 0L;
        maxYVal = 0L;
        minXVal = Long.MAX_VALUE;
        minYVal = Double.MAX_VALUE;

        Iterator<Entry<String, AbstractGraphRow>> it = rows.entrySet().iterator();
        Entry<String, AbstractGraphRow> row = null;
        AbstractGraphRow rowValue;
        int barValue = 0;
        while (it.hasNext()) {
            row = it.next();
            rowValue = row.getValue();

            if (rowValue.getMaxX() > maxXVal) {
                maxXVal = rowValue.getMaxX();
            }

            if (rowValue.getMinX() < minXVal) {
                minXVal = rowValue.getMinX();
            }

            if (!rowValue.isDrawOnChart()) {
                continue;
            }

            rowValue.setExcludeOutOfRangeValues(preventXAxisOverScaling);
            double[] rowMinMaxY = rowValue.getMinMaxY(maxPoints);

            //fix old implementation compatibility, to remove if we drop one
            double zoomFactor;
            Double rowZoomFactor = rowsZoomFactor.get(row.getKey());
            if (rowZoomFactor != null) {
                zoomFactor = rowZoomFactor;
            } else {
                zoomFactor = 1;
            }

            if (rowMinMaxY[1] * zoomFactor > maxYVal) {
                maxYVal = rowMinMaxY[1] * zoomFactor;
            }
            if (rowMinMaxY[0] < minYVal) {
                //we draw only positives values
                minYVal = rowMinMaxY[0] >= 0 ? rowMinMaxY[0] : 0;
            }

            if (rowValue.isDrawBar()) {
                barValue = rowValue.getGranulationValue();
            }
        }

        if (barValue > 0) {
            maxXVal += barValue;
            //find nice X steps
            double barPerSquare = (double) (maxXVal - minXVal) / (barValue * gridLinesCount);
            double step = Math.floor(barPerSquare) + 1;
            maxXVal = (long) (minXVal + step * barValue * gridLinesCount);
        }

        //maxYVal *= 1 + (double) 1 / (double) gridLinesCount;

        if (forcedMinX >= 0L) {
            minXVal = forcedMinX;
        }

        //prevent Y axis not initialized in case of no row displayed
        if (minYVal == Double.MAX_VALUE || maxYVal == 0L) {
            minYVal = 0;
            maxYVal = 10;
        } else if (optimizeYAxis) {
            computeChartSteps();
        } else {
            minYVal = 0;
        }

        if (forcedMaxY > 0) {
            maxYVal = Math.max(forcedMaxY, minYVal + 1);
        }
    }

    /**
     * compute minY and step value to have better readable charts
     */
    private void computeChartSteps() {
        //if special type
        if (chartType == GraphPanelChart.CHART_PERCENTAGE) {
            minYVal = 0;
            maxYVal = 100;
            return;
        }

        //try to find the best range...
        //first, avoid special cases where maxY equal or close to minY
        if (maxYVal - minYVal < 0.1) {
            maxYVal = minYVal + 1;
        }

        //real step
        double step = (maxYVal - minYVal) / gridLinesCount;

        int pow = -1;
        double factor = -1;
        boolean found = false;

        double testStep;

        //find a step close to the real one
        while (!found) {
            pow++;

            for (double f = 0; f < 10; f++) {
                testStep = Math.pow(10, pow) * f;
                if (testStep >= step) {
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
        if (minYVal + foundStep * gridLinesCount < maxYVal) {
            foundStep = Math.pow(10, pow) * (factor + (pow > 0 ? 0.5 : 1));
        }

        //last visual optimization: find the optimal minYVal
        double trim = 10;

        while ((minYVal - minYVal % trim) + foundStep * gridLinesCount >= maxYVal && minYVal > 0) {
            minYVal = minYVal - minYVal % trim;
            trim = trim * 10;
        }

        //final calculation
        maxYVal = minYVal + foundStep * gridLinesCount;
    }

    private void setDefaultDimensions() {
        chartRect.setBounds(spacing, spacing, getWidth() - spacing * 2, getHeight() - spacing * 2);
        legendRect.setBounds(zeroRect);
        xAxisRect.setBounds(zeroRect);
        yAxisRect.setBounds(zeroRect);
    }

    private int getYLabelsMaxWidth(FontMetrics fm) {
        int ret = 0;
        for (int i = 0; i <= gridLinesCount; i++) {
            yAxisLabelRenderer.setValue((minYVal * gridLinesCount + i * (maxYVal - minYVal)) / gridLinesCount);
            int current = fm.stringWidth(yAxisLabelRenderer.getText());
            if (current > ret) {
                ret = current;
            }
        }
        return ret;
    }

    private void calculateYAxisDimensions(FontMetrics fm) {
        int axisWidth = getYLabelsMaxWidth(fm) + spacing * 3 + fm.getHeight();
        yAxisRect.setBounds(chartRect.x, chartRect.y, axisWidth, chartRect.height);
        if (!isPreview) {
            chartRect.setBounds(chartRect.x + axisWidth, chartRect.y, chartRect.width - axisWidth, chartRect.height);
        } else {
            chartRect.setBounds(chartRect.x + previewInset, chartRect.y, chartRect.width, chartRect.height);
        }
    }

    private void calculateXAxisDimensions(FontMetrics fm) {
        // we need to handle this and make Y axis wider
        int axisHeight;
        if (!isPreview) {
            axisHeight = 2 * fm.getHeight() + spacing;//labels plus name
        } else {
            axisHeight = spacing;
        }
        xAxisLabelRenderer.setValue(maxXVal);
        int axisEndSpace = fm.stringWidth(xAxisLabelRenderer.getText()) / 2;
        xAxisRect.setBounds(chartRect.x, chartRect.y + chartRect.height - axisHeight, chartRect.width, axisHeight);
        if (!isPreview) {
            chartRect.setBounds(chartRect.x, chartRect.y, chartRect.width - axisEndSpace, chartRect.height - axisHeight);
        } else {
            chartRect.setBounds(chartRect.x, chartRect.y, chartRect.width - 2 * previewInset, chartRect.height - (previewInset + 1)); //+1 because layout take one pixel
        }
        yAxisRect.setBounds(yAxisRect.x, yAxisRect.y, yAxisRect.width, chartRect.height);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setClip(0, 0, getWidth(), getHeight());
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawPanel(g2d);
        g.drawImage(image, 0, 0, this);
    }

    private void drawPanel(Graphics2D g) {
        g.setColor(Color.white);

        if (settingsDrawGradient) {
            GradientPaint gdp = new GradientPaint(0, 0, Color.white, 0, g.getClipBounds().height, gradientColor);
            g.setPaint(gdp);
        }

        g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
        paintAd(g);

        if (drawMessages(g)) {
            return;
        }

        setDefaultDimensions();
        if (useNewZoomImplementation) {
            autoZoom();
        } else {
            autoZoom_orig();
        }

        getMinMaxDataValues();

        paintLegend(g);
        calculateYAxisDimensions(g.getFontMetrics(g.getFont()));
        calculateXAxisDimensions(g.getFontMetrics(g.getFont()));
        paintYAxis(g);
        paintXAxis(g);
        paintChart(g);
    }

    private void paintLegend(Graphics g) {
        FontMetrics fm = g.getFontMetrics(g.getFont());
        int rectH = fm.getHeight();
        int rectW = rectH;

        Iterator<Entry<String, AbstractGraphRow>> it = rows.entrySet().iterator();
        Entry<String, AbstractGraphRow> row;
        int currentX = chartRect.x;
        int currentY = chartRect.y;
        int legendHeight = it.hasNext() ? rectH + spacing : 0;

        ColorsDispatcher colors = null;

        if (reSetColors) {
            colors = new ColorsDispatcher();
        }

        while (it.hasNext()) {
            row = it.next();
            Color color = reSetColors ? colors.getNextColor() : row.getValue().getColor();

            if (!row.getValue().isShowInLegend() || !row.getValue().isDrawOnChart()) {
                continue;
            }

            String rowLabel = row.getKey();
            if (expendRows && rowsZoomFactor.get(row.getKey()) != null) {
                double zoomFactor = rowsZoomFactor.get(row.getKey());
                if (zoomFactor != 1) {
                    if (zoomFactor > 1) {
                        int iZoomFactor = (int) zoomFactor;
                        rowLabel = rowLabel + " (x" + iZoomFactor + ")";
                    } else {
                        rowLabel = rowLabel + " (x" + zoomFactor + ")";
                    }
                }
            }

            // wrap row if overflowed
            if (currentX + rectW + spacing / 2 + fm.stringWidth(rowLabel) > g.getClipBounds().width) {
                currentY += rectH + spacing / 2;
                legendHeight += rectH + spacing / 2;
                currentX = chartRect.x;
            }

            // draw legend color box
            g.setColor(color);
            Composite oldComposite = null;
            if (row.getValue().isDrawBar()) {
                oldComposite = ((Graphics2D) g).getComposite();
                ((Graphics2D) g).setComposite(barComposite);
            }
            g.fillRect(currentX, currentY, rectW, rectH);
            if (row.getValue().isDrawBar()) {
                ((Graphics2D) g).setComposite(oldComposite);
            }
            g.setColor(Color.black);
            g.drawRect(currentX, currentY, rectW, rectH);

            // draw legend item label
            currentX += rectW + spacing / 2;
            g.drawString(rowLabel, currentX, (int) (currentY + rectH * 0.9));
            currentX += fm.stringWidth(rowLabel) + spacing;
        }

        legendRect.setBounds(chartRect.x, chartRect.y, chartRect.width, legendHeight);
        chartRect.setBounds(chartRect.x, chartRect.y + legendHeight + spacing, chartRect.width, chartRect.height - legendHeight - spacing);
    }

    private void paintYAxis(Graphics g) {
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
        for (int n = 0; n <= gridLinesCount; n++) {
            gridLineY = chartRect.y + (int) ((gridLinesCount - n) * (double) chartRect.height / gridLinesCount);
            g.drawLine(chartRect.x - 3, gridLineY, chartRect.x + 3, gridLineY);
        }

        for (int n = 0; n <= gridLinesCount; n++) {
            //draw 2nd and more axis dashed and shifted
            if (n != 0) {
                ((Graphics2D) g).setStroke(dashStroke);
                shift = 7;
            }

            gridLineY = chartRect.y + (int) ((gridLinesCount - n) * (double) chartRect.height / gridLinesCount);

            // draw grid line with tick
            g.setColor(axisColor);
            g.drawLine(chartRect.x + shift, gridLineY, chartRect.x + chartRect.width, gridLineY);
            g.setColor(Color.black);

            // draw label
            if (!isPreview) {
                yAxisLabelRenderer.setValue((minYVal * gridLinesCount + n * (maxYVal - minYVal)) / gridLinesCount);
                valueLabel = yAxisLabelRenderer.getText();
                labelXPos = yAxisRect.x + yAxisRect.width - fm.stringWidth(valueLabel) - spacing - spacing / 2;
                g.drawString(valueLabel, labelXPos, gridLineY + fm.getAscent() / 2);
            }
        }

        if (!isPreview) {
            Font oldFont = g.getFont();
            g.setFont(g.getFont().deriveFont(Font.ITALIC));

            // Create a rotation transformation for the font.
            AffineTransform fontAT = new AffineTransform();
            int delta = g.getFontMetrics(g.getFont()).stringWidth(yAxisLabel);
            fontAT.rotate(-Math.PI / 2d);
            g.setFont(g.getFont().deriveFont(fontAT));

            g.drawString(yAxisLabel, yAxisRect.x + 15, yAxisRect.y + yAxisRect.height / 2 + delta / 2);

            g.setFont(oldFont);
        }

        //restore stroke
        ((Graphics2D) g).setStroke(oldStroke);
    }

    private void paintXAxis(Graphics g) {
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
        for (int n = 0; n <= gridLinesCount; n++) {
            gridLineX = chartRect.x + (int) (n * ((double) chartRect.width / gridLinesCount));
            g.drawLine(gridLineX, chartRect.y + chartRect.height - 3, gridLineX, chartRect.y + chartRect.height + 3);
        }

        for (int n = 0; n <= gridLinesCount; n++) {
            //draw 2nd and more axis dashed and shifted
            if (n != 0) {
                ((Graphics2D) g).setStroke(dashStroke);
                shift = 7;
            }

            gridLineX = chartRect.x + (int) (n * ((double) chartRect.width / gridLinesCount));

            // draw grid line with tick
            g.setColor(axisColor);
            g.drawLine(gridLineX, chartRect.y + chartRect.height - shift, gridLineX, chartRect.y);
            g.setColor(Color.black);

            // draw label - keep decimal precision if range is too small
            double labelValue = minXVal + n * (double) (maxXVal - minXVal) / gridLinesCount;
            if ((maxXVal - minXVal < 2 * gridLinesCount) && (minXVal != maxXVal)) {
                xAxisLabelRenderer.setValue(labelValue);
            } else {
                xAxisLabelRenderer.setValue((long) labelValue);
            }

            valueLabel = xAxisLabelRenderer.getText();
            labelXPos = gridLineX - fm.stringWidth(valueLabel) / 2;
            g.drawString(valueLabel, labelXPos, xAxisRect.y + fm.getAscent() + spacing);
        }

        Font oldFont = g.getFont();
        g.setFont(g.getFont().deriveFont(Font.ITALIC));

        //axis label
        g.drawString(getXAxisLabel(), chartRect.x + chartRect.width / 2 - g.getFontMetrics(g.getFont()).stringWidth(getXAxisLabel()) / 2, xAxisRect.y + 2 * fm.getAscent() + spacing + 3);

        g.setFont(oldFont);

        //restore stroke
        ((Graphics2D) g).setStroke(oldStroke);

        if (drawCurrentX && settingsDrawCurrentX) {
            gridLineX = chartRect.x + (int) ((currentXVal - minXVal) * (double) chartRect.width / (maxXVal - minXVal));
            g.setColor(Color.GRAY);
            g.drawLine(gridLineX, chartRect.y, gridLineX, chartRect.y + chartRect.height);
            g.setColor(Color.black);
        }
    }

    private void paintChart(Graphics g) {
        g.setColor(Color.yellow);
        Iterator<Entry<String, AbstractGraphRow>> it;

        ColorsDispatcher dispatcher = null;

        if (reSetColors) {
            dispatcher = new ColorsDispatcher();
        }

        //first we get the aggregate point factor if maxpoint is > 0;
        factorInUse = 1;
        if (maxPoints > 0) {
            it = rows.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, AbstractGraphRow> row = it.next();
                int rowFactor = (int) Math.floor(row.getValue().size() / maxPoints) + 1;
                if (rowFactor > factorInUse) {
                    factorInUse = rowFactor;
                }
            }
        }

        //paint rows in 2 phases. Raws with draw label are drawn after to have label on top
        it = rows.entrySet().iterator();
        paintRows(g, dispatcher, it, false);
        it = rows.entrySet().iterator();
        paintRows(g, dispatcher, it, true);
    }

    private void paintRows(Graphics g, ColorsDispatcher dispatcher, Iterator<Entry<String, AbstractGraphRow>> it, boolean rowsWithLabel) {
        while (it.hasNext()) {
            Entry<String, AbstractGraphRow> row = it.next();
            if (row.getValue().isDrawOnChart() && row.getValue().isDrawValueLabel() == rowsWithLabel) {
                Color color = reSetColors ? dispatcher.getNextColor() : row.getValue().getColor();
                paintRow(g, row.getValue(), row.getKey(), color);
            }
        }
    }

    /*
     * Check if the point (x,y) is contained in the chart area
     * We check only minX, maxX, and maxY to avoid flickering.
     * We take max(chartRect.y, y) as redering value
     * This is done to prevent line out of range if new point is added
     * during chart paint.
     */
    private boolean isChartPointValid(int x, int y) {
        boolean ret = true;

        //check x
        if (x < chartRect.x || x > chartRect.x + chartRect.width) {
            ret = false;
        } else //check y bellow x axis
        if (y > chartRect.y + chartRect.height) {
            ret = false;
        }

        return ret;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    private void paintRow(Graphics g, AbstractGraphRow row, String rowLabel, Color color) {
        FontMetrics fm = g.getFontMetrics(g.getFont());
        Iterator<Entry<Long, AbstractGraphPanelChartElement>> it = row.iterator();
        Entry<Long, AbstractGraphPanelChartElement> element;
        int radius = row.getMarkerSize();
        int x, y;
        int prevX = -1;
        int prevY = chartRect.y + chartRect.height;
        final double dxForDVal = (maxXVal <= minXVal) ? 0 : (double) chartRect.width / (maxXVal - minXVal);
        final double dyForDVal = (maxYVal <= minYVal) ? 0 : (double) chartRect.height / (maxYVal - minYVal);

        boolean mustDrawFirstZeroingLine = settingsDrawFinalZeroingLines;

        Stroke oldStroke = null;

        if (row.isDrawThickLines()) {
            oldStroke = ((Graphics2D) g).getStroke();
        }

        boolean isPointBellowForcedMaxY = true;

        while (it.hasNext()) {
            if (!row.isDrawOnChart()) {
                continue;
            }
            double calcPointX = 0;
            double calcPointY = 0;

            if (factorInUse == 1) {

                element = it.next();
                AbstractGraphPanelChartElement elt = (AbstractGraphPanelChartElement) element.getValue();

                //not compatible with factor != 1, ie cannot be used if limit nb of point is selected.
                if (settingsHideNonRepValLimit > 0) {
                    while (!elt.isPointRepresentative(settingsHideNonRepValLimit) && it.hasNext()) {
                        element = it.next();
                        elt = (AbstractGraphPanelChartElement) element.getValue();
                    }

                    if (!elt.isPointRepresentative(settingsHideNonRepValLimit)) {
                        break;
                    }
                }

                calcPointX = element.getKey().doubleValue();
                calcPointY = elt.getValue();
            } else {
                int nbPointProcessed = 0;
                for (int i = 0; i < factorInUse; i++) {
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

            if (expendRows && rowsZoomFactor.get(rowLabel)!=null) {
                calcPointY = calcPointY * rowsZoomFactor.get(rowLabel);
            }

            x = chartRect.x + (int) ((calcPointX - minXVal) * dxForDVal);
            int yHeight = (int) ((calcPointY - minYVal) * dyForDVal);
            y = chartRect.y + chartRect.height - yHeight;
            //fix bar flickering

            if (y < chartRect.y && row.isDrawBar()) {
                y = chartRect.y;
            }

            if (row.isDrawThickLines()) {
                ((Graphics2D) g).setStroke(thickStroke);
            }

            // draw lines
            if (row.isDrawLine()) {
                boolean valid = isChartPointValid(x, y);

                if (mustDrawFirstZeroingLine && valid) {
                    mustDrawFirstZeroingLine = false;
                    prevX = x;
                }

                isPointBellowForcedMaxY = y >= chartRect.y;

                if (prevX >= 0) {
                    g.setColor(color);
                    if (valid) {
                        if (prevY >= chartRect.y && y >= chartRect.y) {
                            g.drawLine(prevX, prevY, x, y);
                        } else if (prevY >= chartRect.y && y < chartRect.y) {
                            int x1 = (x - prevX) * (chartRect.y - prevY) / (y - prevY) + prevX;
                            g.drawLine(prevX, prevY, x1, chartRect.y);
                        } else if (prevY < chartRect.y && y >= chartRect.y) {
                            int x1 = (x - prevX) * (chartRect.y - prevY) / (y - prevY) + prevX;
                            g.drawLine(x1, chartRect.y, x, y);
                        }
                    }
                }
                if (valid) {
                    prevX = x;
                    prevY = y;
                }
            }

            // draw bars
            if (row.isDrawBar()) {
                g.setColor(color);
                if (isChartPointValid(x + 1, y)) //as we draw bars, xMax values must be rejected
                {
                    int x2 = chartRect.x + (int) ((calcPointX + row.getGranulationValue() - minXVal) * dxForDVal) - x - 1;
                    Composite oldComposite = ((Graphics2D) g).getComposite();
                    ((Graphics2D) g).setComposite(barComposite);

                    g.fillRect(x, y - 1, x2, yHeight + 1);
                    ((Graphics2D) g).setComposite(oldComposite);
                }
            }

            if (row.isDrawThickLines()) {
                ((Graphics2D) g).setStroke(oldStroke);
            }

            if (row.isDrawValueLabel()) {
                g.setColor(Color.DARK_GRAY);
                Font oldFont = g.getFont();
                g.setFont(g.getFont().deriveFont(Font.BOLD));

                yAxisLabelRenderer.setValue(calcPointY);
                int labelSize = g.getFontMetrics(g.getFont()).stringWidth(yAxisLabelRenderer.getText());
                //if close to end
                if (x + row.getMarkerSize() + spacing + labelSize > chartRect.x + chartRect.width) {
                    g.drawString(yAxisLabelRenderer.getText(),
                            x - row.getMarkerSize() - spacing - labelSize,
                            y + fm.getAscent() / 2);
                } else {
                    g.drawString(yAxisLabelRenderer.getText(),
                            x + row.getMarkerSize() + spacing,
                            y + fm.getAscent() / 2);
                }
                g.setFont(oldFont);
            }

            // draw markers
            if (radius != AbstractGraphRow.MARKER_SIZE_NONE && isPointBellowForcedMaxY) {
                g.setColor(color);
                if (isChartPointValid(x, y)) {
                    g.fillOval(x - radius, y - radius, (radius) * 2, (radius) * 2);
                    //g.setColor(Color.black);
                    //g.drawOval(x - radius, y - radius, radius * 2, radius * 2);
                }
            }
        }

        drawFinalLines(row, g, prevX, prevY, dxForDVal, oldStroke, color);
    }

    /**
     *
     * @param aRows
     */
    public void setRows(AbstractMap<String, AbstractGraphRow> aRows) {
        rows = aRows;
    }

    /**
     * @param yAxisLabelRenderer the yAxisLabelRenderer to set
     */
    public void setyAxisLabelRenderer(NumberRenderer yAxisLabelRenderer) {
        this.yAxisLabelRenderer = yAxisLabelRenderer;
    }

    /**
     * @param xAxisLabelRenderer the xAxisLabelRenderer to set
     */
    public void setxAxisLabelRenderer(NumberRenderer xAxisLabelRenderer) {
        this.xAxisLabelRenderer = xAxisLabelRenderer;
    }

    /**
     * @param drawFinalZeroingLines the drawFinalZeroingLines to set
     */
    public void setDrawFinalZeroingLines(boolean drawFinalZeroingLines) {
        settingsDrawFinalZeroingLines = drawFinalZeroingLines && !neverDrawFinalZeroingLines;
    }

    /**
     * @param drawCurrentX the drawCurrentX to set
     */
    public void setDrawCurrentX(boolean drawCurrentX) {
        this.drawCurrentX = drawCurrentX;
    }

    /**
     * @param currentX the currentX to set
     */
    public void setCurrentX(long currentX) {
        this.currentXVal = currentX;
    }

    /**
     *
     * @param i
     */
    public void setForcedMinX(long minX) {
        forcedMinX = minX;
    }

    //Paint the add the same color of the axis but with transparency
    private void paintAd(Graphics2D g) {
        Font oldFont = g.getFont();
        g.setFont(g.getFont().deriveFont(10F));
        g.setColor(axisColor);
        Composite oldComposite = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));

        g.drawString(AD_TEXT,
                g.getClipBounds().width - g.getFontMetrics().stringWidth(AD_TEXT) - spacing,
                g.getFontMetrics().getHeight() - spacing + 1);
        g.setComposite(oldComposite);
        g.setFont(oldFont);
    }

    /*
     * Clear error messages
     */
    public void clearErrorMessage() {
        errorMessage = null;
    }

    /*
     * Set error message if not null and not empty
     * @param msg the error message to set
     */
    public void setErrorMessage(String msg) {
        if (msg != null && msg.trim().length() > 0) {
            errorMessage = msg;
        }
    }

    // Adding a popup menu to copy image in clipboard
    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        // do nothing
    }

    private Image getImage() {
        return (Image) getBufferedImage(this.getWidth(), this.getHeight());
    }

    private BufferedImage getBufferedImage(int w, int h) {
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        log.debug("Created image: " + image);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setClip(0, 0, w, h);
        drawPanel(g2);

        return image;
    }

    /**
     * Thanks to stephane.hoblingre
     */
    private void registerPopup(boolean allowCsvExport) {
        this.setComponentPopupMenu(popup);
        JMenuItem itemCopy = new JMenuItem("Copy Image to Clipboard");
        JMenuItem itemSave = new JMenuItem("Save Image as...");
        JMenuItem itemExport = new JMenuItem("Export to CSV...");
        itemCopy.addActionListener(new CopyAction());
        itemSave.addActionListener(new SaveAction());
        itemExport.addActionListener(new CsvExportAction());
        popup.add(itemCopy);
        popup.add(itemSave);
        if (allowCsvExport) {
            popup.addSeparator();
            popup.add(itemExport);
        }
    }

    public void setUseRelativeTime(boolean selected) {
        settingsUseRelativeTime = selected;
        // TODO: we can't notify listener from here about relative time setting, this method should be placed somewhere else
        if (selected) {
            setxAxisLabelRenderer(new DateTimeRenderer(DateTimeRenderer.HHMMSS, testStartTime));
        } else {
            setxAxisLabelRenderer(new DateTimeRenderer(DateTimeRenderer.HHMMSS));
        }
    }

    public boolean isUseRelativeTime() {
        return settingsUseRelativeTime;
    }

    private class CopyAction
            implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            Clipboard clipboard = getToolkit().getSystemClipboard();
            Transferable transferable = new Transferable() {

                @Override
                public Object getTransferData(DataFlavor flavor) {
                    if (isDataFlavorSupported(flavor)) {
                        return getImage();
                    }
                    return null;
                }

                @Override
                public DataFlavor[] getTransferDataFlavors() {
                    return new DataFlavor[]{
                                DataFlavor.imageFlavor
                            };
                }

                @Override
                public boolean isDataFlavorSupported(DataFlavor flavor) {
                    return DataFlavor.imageFlavor.equals(flavor);
                }
            };
            clipboard.setContents(transferable, GraphPanelChart.this);
        }
    }

    public void saveGraphToFile(File file, int w, int h) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        ImageIO.write(getBufferedImage(w, h), "png", fos);
        fos.flush();
        fos.close();
    }

    private class SaveAction
            implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            JFileChooser chooser = savePath != null ? new JFileChooser(new File(savePath)) : new JFileChooser(new File("."));
            chooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));

            int returnVal = chooser.showSaveDialog(GraphPanelChart.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                if (!file.getAbsolutePath().toUpperCase().endsWith(".PNG")) {
                    file = new File(file.getAbsolutePath() + ".png");
                }
                savePath = file.getParent();
                boolean doSave = true;
                if (file.exists()) {
                    int choice = JOptionPane.showConfirmDialog(GraphPanelChart.this, "Do you want to overwrite " + file.getAbsolutePath() + "?", "Save Image as", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    doSave = (choice == JOptionPane.YES_OPTION);
                }

                if (doSave) {
                    try {
                        saveGraphToFile(file, getWidth(), getHeight());
                    } catch (IOException ex) {
                        JOptionPane.showConfirmDialog(GraphPanelChart.this, "Impossible to write the image to the file:\n" + ex.getMessage(), "Save Image as", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private class CsvExportAction
            implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            JFileChooser chooser = savePath != null ? new JFileChooser(new File(savePath)) : new JFileChooser(new File("."));
            chooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));

            int returnVal = chooser.showSaveDialog(GraphPanelChart.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                if (!file.getAbsolutePath().toUpperCase().endsWith(".CSV")) {
                    file = new File(file.getAbsolutePath() + ".csv");
                }
                savePath = file.getParent();
                boolean doSave = true;
                if (file.exists()) {
                    int choice = JOptionPane.showConfirmDialog(GraphPanelChart.this, "Do you want to overwrite " + file.getAbsolutePath() + "?", "Export to CSV File", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    doSave = (choice == JOptionPane.YES_OPTION);
                }

                if (doSave) {
                    GraphModelToCsvExporter exporter = new GraphModelToCsvExporter(rows, file, csvSeparator, xAxisLabel);
                    try {
                        exporter.writeCsvFile();
                    } catch (IOException ex) {
                        JOptionPane.showConfirmDialog(GraphPanelChart.this, "Impossible to write the CSV file:\n" + ex.getMessage(), "Export to CSV File", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
}
