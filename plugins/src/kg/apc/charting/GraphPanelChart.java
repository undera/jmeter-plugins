package kg.apc.charting;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import kg.apc.charting.plotters.AbstractRowPlotter;
import kg.apc.charting.plotters.BarRowPlotter;
import kg.apc.charting.plotters.CSplineRowPlotter;
import kg.apc.charting.plotters.LineRowPlotter;

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

    //cache management
    private static BufferedImage cache = null;
    private static int cacheWitdh, cacheHeight;
    private boolean cacheValid = false;
    private static int cacheOwner = -1;
    private int gpcId;
    private static int uidGenerator = 0;
    //plotters
    BarRowPlotter barRowPlotter = null;
    LineRowPlotter lineRowPlotter = null;
    CSplineRowPlotter cSplineRowPlotter = null;
    AbstractRowPlotter currentPlotter = null;
    JPopupMenu popup = new JPopupMenu();
    private static final String AD_TEXT = "http://apc.kg/plugins";
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
    private long forcedMinX = -1;
    private int chartType = CHART_DEFAULT;
    // Message display in graphs. Used for perfmon error messages
    private String errorMessage = null;
    // Chart's gradient background end color
    private final static Color gradientColor = new Color(229, 236, 246);
    // Chart's Axis Color. For good results, use gradient color - (30, 30, 30)
    private final static Color axisColor = new Color(199, 206, 216);
    //save file path. We remember last folder used.
    private static String savePath = null;
    private ChartSettings chartSettings = new ChartSettings();

    public ChartSettings getChartSettings() {
        return chartSettings;
    }
    private boolean reSetColors = false;
    //hover info
    private JWindow hoverWindow;
    //gap in pixels relative to mouse pointer position
    private final static int hoverGap = 20;
    //force positionning between 2 clicks
    private boolean forceHoverPosition = true;
    private JTextField hoverLabel;
    private int xHoverInfo = -1;
    private int yHoverInfo = -1;
    private HoverMotionListener motionListener = new HoverMotionListener();

    public void setReSetColors(boolean reSetColors) {
        this.reSetColors = reSetColors;
    }
    private String xAxisLabel = "X axis label";
    private String yAxisLabel = "Y axis label";
    private int precisionLabel = -1;
    private int limitPointFactor = 1;
    private boolean displayPrecision = false;

    public void setDisplayPrecision(boolean displayPrecision) {
        this.displayPrecision = displayPrecision;
    }

    public void setxAxisLabel(String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
    }

    public void setYAxisLabel(String yAxisLabel) {
        this.yAxisLabel = yAxisLabel;
    }

    public void setPrecisionLabel(int precision) {
        this.precisionLabel = precision;
    }

    private void autoZoom_orig() {
        //row zooming
        if (!chartSettings.isExpendRows()) {
            return;
        }

        Iterator<Entry<String, AbstractGraphRow>> it = rows.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, AbstractGraphRow> row = it.next();
            double[] minMax = row.getValue().getMinMaxY(chartSettings.getMaxPointPerRow());
            if (minMax[1] > 0) {
                double zoomFactor = 1;
                rowsZoomFactor.put(row.getKey(), zoomFactor);
                while (minMax[1] * zoomFactor <= maxYVal) {
                    rowsZoomFactor.put(row.getKey(), zoomFactor);
                    zoomFactor = zoomFactor * 10;
                }
            } else {
                rowsZoomFactor.put(row.getKey(), 1.0);
            }
        }
    }

    private String getXAxisLabel() {
        String label;
        if (!displayPrecision) {
            label = xAxisLabel;
        } else {
            long granularity;
            if (chartSettings.getMaxPointPerRow() <= 0) {
                granularity = precisionLabel;

            } else {
                granularity = precisionLabel * limitPointFactor;
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
    //relative time
    private long testStartTime = 0;

    public void setTestStartTime(long time) {
        testStartTime = time;
    }
    //row zooming
    private HashMap<String, Double> rowsZoomFactor = new HashMap<String, Double>();
    
    private static synchronized int getNextId() {
        uidGenerator++;
        return uidGenerator;
    }

    /**
     * Creates new chart object with default parameters
     */
    public GraphPanelChart(boolean allowCsvExport, boolean haveGUI) {
        gpcId = getNextId();
        setBackground(Color.white);
        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.lightGray, Color.darkGray));

        yAxisLabelRenderer = new NumberRenderer("#.#");
        xAxisLabelRenderer = new NumberRenderer("#.#");
        legendRect = new Rectangle();
        yAxisRect = new Rectangle();
        xAxisRect = new Rectangle();
        chartRect = new Rectangle();

        //no need to register anything in non GUI mode
        //second test required for unit test mode
        if (haveGUI && !GraphicsEnvironment.isHeadless()) {
            registerPopup(allowCsvExport);
            hoverLabel = new JTextField();
            hoverLabel.setEditable(false);
            hoverLabel.setOpaque(false);
            hoverLabel.setBorder(new BevelBorder(BevelBorder.RAISED));
            hoverLabel.setFont(new java.awt.Font("Tahoma", 0, 11));

            hoverWindow = new JWindow();
            hoverWindow.setBackground(gradientColor);
            hoverWindow.add(hoverLabel, BorderLayout.CENTER);
            registerHoverInfo();
        }
        barRowPlotter = new BarRowPlotter(chartSettings, yAxisLabelRenderer);
        lineRowPlotter = new LineRowPlotter(chartSettings, yAxisLabelRenderer);
        cSplineRowPlotter = new CSplineRowPlotter(chartSettings, yAxisLabelRenderer);
    }

    public GraphPanelChart(boolean haveGUI) {
        this(true, haveGUI);
    }

    public GraphPanelChart() {
        this(false);
    }

    public boolean isModelContainsRow(AbstractGraphRow row) {
        return rows.containsKey(row.getLabel());
    }

    public void setChartType(int type) {
        chartType = type;
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
        AbstractGraphRow rowValue = null;
        int barValue = 0;
        while (it.hasNext()) {
            row = it.next();
            rowValue = row.getValue();

            rowValue.setExcludeOutOfRangeValues(chartSettings.isPreventXAxisOverScaling());

            if (!rowValue.isDrawOnChart()) {
                continue;
            }

            if (rowValue.getMaxX() > maxXVal) {
                maxXVal = rowValue.getMaxX();
            }

            if (rowValue.getMinX() < minXVal) {
                minXVal = rowValue.getMinX();
            }

            double[] rowMinMaxY = rowValue.getMinMaxY(chartSettings.getMaxPointPerRow());

            if (rowMinMaxY[1] > maxYVal) {
                maxYVal = rowMinMaxY[1];
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

        //prevent X and Y axis not initialized in case of no row displayed
        if (maxXVal == 0L
                || maxYVal == 0L
                || minXVal == Long.MAX_VALUE
                || minYVal == Double.MAX_VALUE) {

            minYVal = 0;
            maxYVal = 10;
            //we take last known row to get x range
            if (rowValue != null) {
                maxXVal = rowValue.getMaxX();
                minXVal = rowValue.getMinX();
            }
        } else if (chartSettings.isConfigOptimizeYAxis()) {
            computeChartSteps();
        } else {
            minYVal = 0;
        }

        if (chartSettings.getForcedMaxY() > 0) {
            maxYVal = Math.max(chartSettings.getForcedMaxY(), minYVal + 1);
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

    private void setDefaultDimensions(Graphics g) {
        chartRect.setBounds(spacing, spacing, g.getClipBounds().width - spacing * 2, g.getClipBounds().height - spacing * 2);
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

    private void calculateYAxisDimensions(Graphics g) {
        FontMetrics fm = g.getFontMetrics(g.getFont());
        int axisWidth = getYLabelsMaxWidth(fm) + spacing * 3 + fm.getHeight();
        yAxisRect.setBounds(chartRect.x, chartRect.y, axisWidth, chartRect.height);
        if (!isPreview) {
            chartRect.setBounds(chartRect.x + axisWidth, chartRect.y, chartRect.width - axisWidth, chartRect.height);
        } else {
            chartRect.setBounds(chartRect.x + previewInset, chartRect.y, chartRect.width, chartRect.height);
        }
    }

    private void calculateXAxisDimensions(Graphics g) {
        FontMetrics fm = g.getFontMetrics(g.getFont());
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

    public void invalidateCache() {
        cacheValid = false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int witdh = this.getWidth();
        int height = this.getHeight();

        if (cacheHeight != height || cacheWitdh != witdh || gpcId != cacheOwner) {
            cacheValid = false;
        }

        if (!cacheValid) {
            if (cache == null || cacheHeight != height || cacheWitdh != witdh) {
                cache = new BufferedImage(witdh, height, BufferedImage.TYPE_INT_ARGB);
            }
            Graphics2D g2d = cache.createGraphics();
            g2d.setClip(0, 0, witdh, height);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            drawPanel(g2d);
            cacheValid = true;
            cacheHeight = height;
            cacheWitdh = witdh;
            cacheOwner = gpcId;
        }
        g.drawImage(cache, 0, 0, this);
    }

    private void drawPanel(Graphics2D g) {
        drawPanel(g, true);
    }

    private void drawPanel(Graphics2D g, boolean drawHoverInfo) {
        g.setColor(Color.white);

        if (chartSettings.isDrawGradient()) {
            GradientPaint gdp = new GradientPaint(0, 0, Color.white, 0, g.getClipBounds().height, gradientColor);
            g.setPaint(gdp);
        }

        g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
        paintAd(g);

        if (drawMessages(g)) {
            return;
        }

        setDefaultDimensions(g);

        getMinMaxDataValues();
        autoZoom_orig();

        paintLegend(g);
        calculateYAxisDimensions(g);
        calculateXAxisDimensions(g);
        paintYAxis(g);
        paintXAxis(g);
        paintChart(g);
        if (drawHoverInfo) {
            showHoverInfo();
        }
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
            if (chartSettings.isExpendRows() && rowsZoomFactor.get(row.getKey()) != null) {
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
            boolean isBarChart = row.getValue().isDrawBar() && chartSettings.getChartType() == ChartSettings.CHART_TYPE_DEFAULT || chartSettings.getChartType() == ChartSettings.CHART_TYPE_BAR;
            if (isBarChart) {
                oldComposite = ((Graphics2D) g).getComposite();
                ((Graphics2D) g).setComposite(chartSettings.getBarComposite());
            }
            g.fillRect(currentX, currentY, rectW, rectH);
            if (isBarChart) {
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
                ((Graphics2D) g).setStroke(chartSettings.getDashStroke());
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
                ((Graphics2D) g).setStroke(chartSettings.getDashStroke());
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

        if (chartSettings.isDrawCurrentX()) {
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
        limitPointFactor = 1;
        if (chartSettings.getMaxPointPerRow() > 0) {
            it = rows.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, AbstractGraphRow> row = it.next();
                int rowFactor = (int) Math.floor(row.getValue().size() / chartSettings.getMaxPointPerRow()) + 1;
                if (rowFactor > limitPointFactor) {
                    limitPointFactor = rowFactor;
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

    private void paintRow(Graphics g, AbstractGraphRow row, String rowLabel, Color color) {

        if (row.isDrawLine() && chartSettings.getChartType() == ChartSettings.CHART_TYPE_DEFAULT
                || chartSettings.getChartType() == ChartSettings.CHART_TYPE_LINE) {
            currentPlotter = lineRowPlotter;
        } else if (row.isDrawBar() && chartSettings.getChartType() == ChartSettings.CHART_TYPE_DEFAULT
                || chartSettings.getChartType() == ChartSettings.CHART_TYPE_BAR) {
            currentPlotter = barRowPlotter;
        } else if (row.isDrawSpline() && chartSettings.getChartType() == ChartSettings.CHART_TYPE_DEFAULT
                || chartSettings.getChartType() == ChartSettings.CHART_TYPE_CSPLINE) {
            currentPlotter = cSplineRowPlotter;
        }

        if (currentPlotter != null) {
            double zoomFactor = 1;
            if (chartSettings.isExpendRows() && rowsZoomFactor.get(rowLabel) != null) {
                zoomFactor = rowsZoomFactor.get(rowLabel);
            }
            currentPlotter.setBoundsValues(chartRect, minXVal, maxXVal, minYVal, maxYVal);
            currentPlotter.paintRow((Graphics2D) g, row, color, zoomFactor, limitPointFactor);
        }
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
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setClip(0, 0, w, h);
        drawPanel(g2, false);

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

    private void registerHoverInfo() {
        addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (!isPreview && evt.getButton() == MouseEvent.BUTTON1) {
                    forceHoverPosition = true;
                    addMouseMotionListener(motionListener);
                    chartMouseMoved(evt);
                }
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                if (!isPreview) {
                    hideHoverInfo();
                    removeMouseMotionListener(motionListener);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!isPreview) {
                    hideHoverInfo();
                }
            }
        });
    }

    private void hideHoverInfo() {
        hoverWindow.setVisible(false);
        xHoverInfo = -1;
        yHoverInfo = -1;
    }

    private synchronized void showHoverInfo() {

        if (isPreview
                || chartRect.width == 0 || chartRect.height == 0
                || xHoverInfo == -1 || yHoverInfo == -1) {
            return;
        }

        long realX = minXVal + (maxXVal - minXVal) * (xHoverInfo - chartRect.x) / chartRect.width;
        double realY = minYVal + (maxYVal - minYVal) * (chartRect.height - yHoverInfo + chartRect.y) / chartRect.height;
        xAxisLabelRenderer.setValue(realX);
        yAxisLabelRenderer.setValue(realY);

        StringBuilder hoverInfo = new StringBuilder("(");
        hoverInfo.append(xAxisLabelRenderer.getText());
        hoverInfo.append(" ; ");
        hoverInfo.append(yAxisLabelRenderer.getText());
        hoverInfo.append(")");

        hoverLabel.setText(hoverInfo.toString());

        int labelWidth = hoverLabel.getPreferredSize().width + 5;
        int labelHeight = hoverLabel.getPreferredSize().height;

        if (hoverWindow.getWidth() < labelWidth || hoverWindow.getHeight() < labelHeight) {
            hoverWindow.setSize(labelWidth, labelHeight);
        }

        Point mousePos = MouseInfo.getPointerInfo().getLocation();

        int hoverWindowX = mousePos.x + hoverGap;
        int hoverWindowY = mousePos.y + hoverGap;

        //we move window only if far from pointer to limit cpu
        double deltaX = Math.abs(hoverWindow.getLocation().getX() - hoverWindowX);
        double deltaY = Math.abs(hoverWindow.getLocation().getY() - hoverWindowY);

        if (forceHoverPosition || deltaX >= hoverGap || deltaY >= hoverGap) {
            //prevent out of screen
            int correctedX = Math.min(hoverWindowX, Toolkit.getDefaultToolkit().getScreenSize().width - hoverWindow.getSize().width);
            hoverWindow.setLocation(correctedX, hoverWindowY);
            forceHoverPosition = false;
        }
    }

    private void chartMouseMoved(java.awt.event.MouseEvent evt) {

        int x = evt.getX();
        int y = evt.getY();

        if (x >= chartRect.x
                && x <= (chartRect.x + chartRect.width)
                && y >= chartRect.y
                && y <= (chartRect.y + chartRect.height)) {
            xHoverInfo = x;
            yHoverInfo = y;
            showHoverInfo();
            hoverWindow.setVisible(true);
        } else {
            hoverWindow.setVisible(false);
            xHoverInfo = -1;
            yHoverInfo = -1;
        }
    }

    public void setUseRelativeTime(boolean selected) {
        chartSettings.setUseRelativeTime(selected);
        if (selected) {
            setxAxisLabelRenderer(new DateTimeRenderer(DateTimeRenderer.HHMMSS, testStartTime));
        } else {
            setxAxisLabelRenderer(new DateTimeRenderer(DateTimeRenderer.HHMMSS));
        }
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

    public void saveGraphToPNG(File file, int w, int h) throws IOException {
        log.info("Saving PNG to " + file.getAbsolutePath());
        FileOutputStream fos = new FileOutputStream(file);
        ImageIO.write(getBufferedImage(w, h), "png", fos);
        fos.flush();
        fos.close();
    }

    public void saveGraphToCSV(File file) throws IOException {
        log.info("Saving CSV to " + file.getAbsolutePath());
        GraphModelToCsvExporter exporter = new GraphModelToCsvExporter(rows, file, chartSettings.getConfigCsvSeparator(), xAxisLabel, xAxisLabelRenderer, chartSettings.getHideNonRepValLimit());
        exporter.writeCsvFile();
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
                        saveGraphToPNG(file, getWidth(), getHeight());
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
                    try {
                        saveGraphToCSV(file);
                    } catch (IOException ex) {
                        JOptionPane.showConfirmDialog(GraphPanelChart.this, "Impossible to write the CSV file:\n" + ex.getMessage(), "Export to CSV File", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private class HoverMotionListener extends java.awt.event.MouseMotionAdapter {

        @Override
        public void mouseDragged(java.awt.event.MouseEvent evt) {
            chartMouseMoved(evt);
        }
    }
}
