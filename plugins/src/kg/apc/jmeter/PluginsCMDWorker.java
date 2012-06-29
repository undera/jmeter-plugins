package kg.apc.jmeter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import kg.apc.charting.GraphPanelChart;
import kg.apc.cmd.UniversalRunner;
import kg.apc.jmeter.graphs.AbstractGraphPanelVisualizer;
import kg.apc.jmeter.perfmon.PerfMonCollector;
import kg.apc.jmeter.vizualizers.CorrectedResultCollector;
import kg.apc.jmeter.vizualizers.PageDataExtractorOverTimeGui;
import org.apache.jmeter.JMeter;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class PluginsCMDWorker {

    private int graphWidth = 800;
    private int graphHeight = 600;
    public static final int EXPORT_PNG = 1;
    public static final int EXPORT_CSV = 2;
    private int exportMode = 0;
    private String inputFile;
    private String outputCSV;
    private String outputPNG;
    private String pluginType;
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
    private HashMap<String, String> cmdRegExps = null;

    public PluginsCMDWorker() {
        prepareJMeterEnv();
    }

    private void prepareJMeterEnv() {
        if (JMeterUtils.getJMeterHome() != null) {
            log.warn("JMeter env exists. No one should see this normally.");
            return;
        }

        String homeDir = UniversalRunner.getJARLocation();

        File dir = new File(homeDir);
        while (dir != null && dir.exists()
                && dir.getName().equals("ext")
                && dir.getParentFile().getName().equals("lib")) {
            dir = dir.getParentFile();
        }

        if (dir == null || !dir.exists()) {
            throw new IllegalArgumentException("CMDRunner.jar must be placed in <jmeter>/lib/ext directory");
        }

        homeDir = dir.getParent();

        log.debug("Creating jmeter env using JMeter home: " + homeDir);
        JMeterUtils.setJMeterHome(homeDir);
        initializeProperties();
    }

    /**
     * Had to copy this method from JMeter class 'cause they provide no ways to
     * re-use this code
     *
     * @see JMeter
     */
    private void initializeProperties() {
        JMeterUtils.loadJMeterProperties(JMeterUtils.getJMeterHome() + File.separator
                + "bin" + File.separator
                + "jmeter.properties");

        JMeterUtils.initLogging();
        JMeterUtils.initLocale();

        Properties jmeterProps = JMeterUtils.getJMeterProperties();

        // Add local JMeter properties, if the file is found
        String userProp = JMeterUtils.getPropDefault("user.properties", "");
        if (userProp.length() > 0) {
            FileInputStream fis = null;
            try {
                File file = JMeterUtils.findFile(userProp);
                if (file.canRead()) {
                    log.info("Loading user properties from: " + file.getCanonicalPath());
                    fis = new FileInputStream(file);
                    Properties tmp = new Properties();
                    tmp.load(fis);
                    jmeterProps.putAll(tmp);
                    LoggingManager.setLoggingLevels(tmp);//Do what would be done earlier
                }
            } catch (IOException e) {
                log.warn("Error loading user property file: " + userProp, e);
            } finally {
                try {
                    fis.close();
                } catch (IOException ex) {
                    log.warn("There was problem closing file stream", ex);
                }
            }
        }

        // Add local system properties, if the file is found
        String sysProp = JMeterUtils.getPropDefault("system.properties", "");
        if (sysProp.length() > 0) {
            FileInputStream fis = null;
            try {
                File file = JMeterUtils.findFile(sysProp);
                if (file.canRead()) {
                    log.info("Loading system properties from: " + file.getCanonicalPath());
                    fis = new FileInputStream(file);
                    System.getProperties().load(fis);
                }
            } catch (IOException e) {
                log.warn("Error loading system property file: " + sysProp, e);
            } finally {
                try {
                    fis.close();
                } catch (IOException ex) {
                    log.warn("There was problem closing file stream", ex);
                }
            }
        }
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
        pluginType = string;
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

        AbstractGraphPanelVisualizer gui = getGUIObject(pluginType);

        setOptions(gui);

        CorrectedResultCollector rc;
        if(gui instanceof kg.apc.jmeter.vizualizers.PerfMonGui) {
           rc = new PerfMonCollector();
        } else {
           rc = new CorrectedResultCollector();
        }
        rc.setExcludeLabels(excludeLabels);
        rc.setIncludeLabels(includeLabels);
        log.debug("Using JTL file: " + inputFile);
        rc.setFilename(inputFile);
        rc.setListener(gui);

        gui.configure(rc);

        rc.testStarted();
        rc.loadExistingFile();
        rc.testEnded();

        // to handle issue 64 and since it must be cheap - set options again
        setOptions(gui);


        
        
        if ((exportMode & EXPORT_PNG) == EXPORT_PNG) {
            File pngFile=new File(outputPNG);
            forceDir(pngFile);
            
            try {
                gui.getGraphPanelChart().saveGraphToPNG(pngFile, graphWidth, graphHeight);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        if ((exportMode & EXPORT_CSV) == EXPORT_CSV) {
            File csvFile=new File(outputCSV);
            forceDir(csvFile);
            
            try {
                gui.getGraphPanelChart().saveGraphToCSV(csvFile);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        return 0;
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
        } catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
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
        if (cmdRegExps != null) {
           if(gui instanceof PageDataExtractorOverTimeGui) {
              ((PageDataExtractorOverTimeGui)gui).setCmdRegExps(cmdRegExps);
           }
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

    public void setIncludeLabels(String string) {
        includeLabels = string;
    }

    public void setExcludeLabels(String string) {
        excludeLabels = string;
    }

    public void setAutoScaleRows(int logicValue) {
        autoScaleRows = logicValue;
    }

    public void setLineWeight(float parseInt) {
        lineWeight = parseInt;
    }

    public void setCmdRegExps (HashMap<String, String> cmdRegExps) {
        this.cmdRegExps = cmdRegExps;
    }

    private void forceDir(File resultFile) {
        File parent = resultFile.getParentFile();
        if(parent != null) {
            if (!parent.mkdirs() && !parent.exists()) {
                throw new RuntimeException("Failed to create directory for "+resultFile.getAbsolutePath());
            }
        }
    }
}
