package kg.apc.jmeter;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import kg.apc.jmeter.cmd.NewDriver;
import kg.apc.jmeter.vizualizers.AbstractGraphPanelVisualizer;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.util.JMeterUtils;

/**
 *
 * @author undera
 */
public class PluginsCMDWorker {

    private int graphWidth = 800;
    private int graphHeight = 600;
    static final int EXPORT_PNG = 2 ^ 0;
    static final int EXPORT_CSV = 2 ^ 1;
    private int exportMode = 0;
    private String inputFile;
    private String outputCSV;
    private String outputPNG;
    private String pluginType;

    public PluginsCMDWorker() {
    }

    private void prepareJMeterEnv() {
        String homeDir = NewDriver.getJMeterDir();
        JMeterUtils.setJMeterHome(homeDir);
        JMeterUtils.setLocale(new Locale("ignoreResources"));
        File props = new File(homeDir + "/bin/jmeter.properties");
        if (!props.exists()) {
            try {
                props = File.createTempFile("jmeterplugins", ".properties");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        JMeterUtils.loadJMeterProperties(props.getAbsolutePath());
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
        if (pluginType==null)
        {
            throw new IllegalArgumentException("Missing plugin type specification");
        }

        if (exportMode==0)
            throw new IllegalArgumentException("Missing any export specification");

        if (inputFile==null)
            throw new IllegalArgumentException("Missing input JTL file specification");
    }

    public void setGraphWidth(int i) {
        graphWidth = i;
    }

    public void setGraphHeight(int i) {
        graphHeight = i;
    }

    public int doJob() {
        prepareJMeterEnv();

        checkParams();
    
        AbstractGraphPanelVisualizer gui = getGUIObject(pluginType);

        ResultCollector rc = new ResultCollector();
        rc.setFilename(inputFile);
        rc.setListener(gui);
        rc.loadExistingFile();

        if ((exportMode & EXPORT_PNG) == EXPORT_PNG) {
            try {
                gui.getGraphPanelChart().saveGraphToPNG(new File(outputPNG), graphWidth, graphHeight);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        if ((exportMode & EXPORT_CSV) == EXPORT_CSV) {
            try {
                gui.getGraphPanelChart().saveGraphToCSV(new File(outputCSV));
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


        //boolean isOur = AbstractGraphPanelVisualizer.class.isAssignableFrom(a);
        //if (!isOur) {
        //    throw new RuntimeException("Class name " + pluginType + " cannot be used");
        //}

        try {
            return (AbstractGraphPanelVisualizer) a.newInstance();
        } catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }
}
