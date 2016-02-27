package kg.apc.jmeter;

import kg.apc.charting.ChartSettings;
import kg.apc.charting.GraphPanelChart;
import kg.apc.cmd.UniversalRunner;
import kg.apc.jmeter.graphs.AbstractGraphPanelVisualizer;
import kg.apc.jmeter.vizualizers.CorrectedResultCollector;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ListIterator;
import java.util.Properties;

public class PluginsCMDWorker {

    private int graphWidth = 800;
    private int graphHeight = 600;
    public static final int EXPORT_PNG = 1;
    public static final int EXPORT_CSV = 2;
    private int exportMode = 0;
    private String inputFile;
    private String outputCSV;
    private String outputPNG;
    private AbstractGraphPanelVisualizer pluginType;
    private static final Logger log = LoggingManager.getLoggerForClass();
    private int aggregate = -1;
    private int zeroing = -1;
    private int preventOutliers = -1;
    private int rowsLimit = -1;
    private int forceY = -1;
    private int lowCounts = -1;
    private int granulation = -1;
    private int relativeTimes = -1;
    private int gradient = -1;
    private int autoScaleRows = -1;
    private float lineWeight = -1;
    private String includeLabels = "";
    private String excludeLabels = "";
    private String startOffset = "";
    private String endOffset = "";
    private int includeSamplesWithRegex = -1;
    private int excludeSamplesWithRegex = -1;
    private int successFilter = -1;
    private int markers = -1;

    public PluginsCMDWorker() {
        log.info("Using JMeterPluginsCMD v. " + JMeterPluginsUtils.getVersion());
        JMeterPluginsUtils.prepareJMeterEnv(UniversalRunner.getJARLocation());
    }


    public void addExportMode(int mode) {
        exportMode |= mode;
    }

    public void setInputFile(String string) {
        inputFile = string;
    }

    public void setOutputCSVFile(String string) {
        outputCSV = string;
    }

    public void setOutputPNGFile(String string) {
        outputPNG = string;
    }

    public void setPluginType(String string) {
        pluginType = getGUIObject(string);
    }

    private void checkParams() {
        if (pluginType == null) {
            throw new IllegalArgumentException("Missing plugin type specification");
        }

        if (exportMode == 0) {
            throw new IllegalArgumentException("Missing any export specification");
        }

        if (inputFile == null) {
            throw new IllegalArgumentException("Missing input JTL file specification");
        }

        if (!(new File(inputFile).exists())) {
            throw new IllegalArgumentException("Cannot find specified JTL file: " + inputFile);
        }

    }

    public void setGraphWidth(int i) {
        graphWidth = i;
    }

    public void setGraphHeight(int i) {
        graphHeight = i;
    }

    public int doJob() {
        checkParams();

        AbstractGraphPanelVisualizer pluginInstance = pluginType;
        pluginType.setIgnoreCurrentTestStartTime();
        setOptions(pluginInstance);

        CorrectedResultCollector rc;
        rc = (CorrectedResultCollector) pluginInstance.createTestElement();
        rc.setExcludeLabels(excludeLabels);
        rc.setIncludeLabels(includeLabels);
        rc.setStartOffset(startOffset);
        rc.setEndOffset(endOffset);

        if (includeSamplesWithRegex >= 0) {
            rc.setEnabledIncludeRegex(includeSamplesWithRegex != 0);
        }
        if (excludeSamplesWithRegex >= 0) {
            rc.setEnabledExcludeRegex(excludeSamplesWithRegex != 0);
        }

        if (successFilter >= 0) {
            rc.setErrorLogging(successFilter == 0);
            rc.setSuccessOnlyLogging(successFilter != 0);
        }

        if (pluginType.getStaticLabel().equals(JMeterPluginsUtils.prefixLabel("Merge Results"))) {
            mergeResults(pluginInstance, rc);
        } else {
            log.debug("Using JTL file: " + inputFile);
            rc.setFilename(inputFile);
            rc.setListener(pluginInstance);
            pluginInstance.configure(rc);

            // rc.testStarted();
            rc.loadExistingFile();
            // rc.testEnded();
        }

        // to handle issue 64 and since it must be cheap - set options again
        setOptions(pluginInstance);

        if ((exportMode & EXPORT_PNG) == EXPORT_PNG) {
            File pngFile = new File(outputPNG);
            forceDir(pngFile);

            try {
                pluginInstance.getGraphPanelChart().saveGraphToPNG(pngFile, graphWidth, graphHeight);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        if ((exportMode & EXPORT_CSV) == EXPORT_CSV) {
            File csvFile = new File(outputCSV);
            forceDir(csvFile);

            try {
                pluginInstance.getGraphPanelChart().saveGraphToCSV(csvFile);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        return 0;
    }

    private void mergeResults(AbstractGraphPanelVisualizer pluginInstance, CorrectedResultCollector rc) {
        log.debug("Using properties file with MergeResults plugin: " + inputFile);
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream(inputFile);

            // load a properties file
            prop.load(input);

            for (int i = 1; i < 5; i++) {
                rc.setFilename(null == prop.getProperty("inputJtl" + i) ? ""
                        : prop.getProperty("inputJtl" + i));
                if (rc.getFilename().isEmpty()) {
                    break;
                }
                rc.setPrefixLabel(null == prop.getProperty("prefixLabel"
                        + i) ? "" : prop.getProperty("prefixLabel" + i));
                rc.setIncludeLabels(null == prop
                        .getProperty("includeLabels" + i) ? "" : prop
                        .getProperty("includeLabels" + i));
                rc.setExcludeLabels(null == prop
                        .getProperty("excludeLabels" + i) ? "" : prop
                        .getProperty("excludeLabels" + i));
                rc.setEnabledIncludeRegex(Boolean.valueOf(prop
                        .getProperty("includeLabelRegex" + i)));
                rc.setEnabledExcludeRegex(Boolean.valueOf(prop
                        .getProperty("excludeLabelRegex" + i)));
                rc.setStartOffset(null == prop.getProperty("startOffset"
                        + i) ? "" : prop.getProperty("startOffset" + i));
                rc.setEndOffset(null == prop.getProperty("endOffset" + i) ? ""
                        : prop.getProperty("endOffset" + i));
                rc.setListener(pluginInstance);
                pluginInstance.configure(rc);

                // rc.testStarted();
                rc.loadExistingFile();
                // rc.testEnded();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private AbstractGraphPanelVisualizer getGUIObject(String pluginType) {
        Class a;
        try {
            a = Class.forName(pluginType);
        } catch (ClassNotFoundException ex) {
            if (!pluginType.endsWith("Gui")) {
                return getGUIObject(pluginType + "Gui");
            }

            if (!pluginType.startsWith("kg.apc.jmeter.vizualizers.")) {
                return getGUIObject("kg.apc.jmeter.vizualizers." + pluginType);
            }

            throw new RuntimeException(ex);
        }

        try {
            return (AbstractGraphPanelVisualizer) a.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void setOptions(AbstractGraphPanelVisualizer gui) {
        GraphPanelChart graph = gui.getGraphPanelChart();

        if (aggregate >= 0) {
            gui.switchModel(aggregate > 0);
        }
        if (granulation >= 0) {
            gui.setGranulation(granulation);
        }
        if (relativeTimes >= 0) {
            graph.setUseRelativeTime(relativeTimes > 0);
        }
        if (lineWeight >= 0) {
            graph.getChartSettings().setLineWidth(lineWeight);
        }


        if (gradient >= 0) {
            graph.getChartSettings().setDrawGradient(gradient > 0);
        }
        if (zeroing >= 0) {
            graph.getChartSettings().setDrawFinalZeroingLines(zeroing > 0);
        }
        if (rowsLimit >= 0) {
            graph.getChartSettings().setMaxPointPerRow(rowsLimit);
        }
        if (preventOutliers >= 0) {
            graph.getChartSettings().setPreventXAxisOverScaling(preventOutliers > 0);
        }
        if (lowCounts >= 0) {
            graph.getChartSettings().setHideNonRepValLimit(lowCounts);
        }
        if (forceY >= 0) {
            graph.getChartSettings().setForcedMaxY(forceY);
        }
        if (autoScaleRows >= 0) {
            graph.getChartSettings().setExpendRows(autoScaleRows > 0);
        }
        if (markers >= 0) {
            graph.getChartSettings().setChartMarkers(markers > 0 ? ChartSettings.CHART_MARKERS_YES : ChartSettings.CHART_MARKERS_NO);
        }

    }

    public void setAggregate(int logicValue) {
        aggregate = logicValue;
    }

    public void setZeroing(int logicValue) {
        zeroing = logicValue;
    }

    public void setPreventOutliers(int logicValue) {
        preventOutliers = logicValue;
    }

    public void setRowsLimit(int parseInt) {
        rowsLimit = parseInt;
    }

    public void setForceY(int parseInt) {
        forceY = parseInt;
    }

    public void setHideLowCounts(int parseInt) {
        lowCounts = parseInt;
    }

    public void setGranulation(int parseInt) {
        granulation = parseInt;
    }

    public void setRelativeTimes(int logicValue) {
        relativeTimes = logicValue;
    }

    public void setGradient(int logicValue) {
        gradient = logicValue;
    }

    public void setMarkers(int logicValue) {
        markers = logicValue;
    }

    public void setIncludeLabels(String string) {
        includeLabels = string;
    }

    public void setExcludeLabels(String string) {
        excludeLabels = string;
    }

    public void setIncludeSamplesWithRegex(int logicValue) {
        includeSamplesWithRegex = logicValue;
    }

    public void setExcludeSamplesWithRegex(int logicValue) {
        excludeSamplesWithRegex = logicValue;
    }

    public void setStartOffset(String string) {
        startOffset = string;
    }

    public void setEndOffset(String string) {
        endOffset = string;
    }

    public void setAutoScaleRows(int logicValue) {
        autoScaleRows = logicValue;
    }

    public void setLineWeight(float parseInt) {
        lineWeight = parseInt;
    }

    public void setSuccessFilter(int logicValue) {
        successFilter = logicValue;
    }

    private void forceDir(File resultFile) {
        File parent = resultFile.getParentFile();
        if (parent != null) {
            if (!parent.mkdirs() && !parent.exists()) {
                throw new RuntimeException("Failed to create directory for " + resultFile.getAbsolutePath());
            }
        }
    }

    public void processUnknownOption(String nextArg, ListIterator args) {
        if (pluginType != null && pluginType instanceof CMDLineArgumentsProcessor) {
            log.debug("Trying to process unknown option using CMDLineArgumentsProcessor: " + nextArg);
            CMDLineArgumentsProcessor obj = (CMDLineArgumentsProcessor) pluginType;
            obj.processCMDOption(nextArg, args);
        } else {
            throw new UnsupportedOperationException("Unrecognized option: " + nextArg);
        }
    }
}
