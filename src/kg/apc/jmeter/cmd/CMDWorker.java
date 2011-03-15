package kg.apc.jmeter.cmd;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import kg.apc.jmeter.vizualizers.AbstractGraphPanelVisualizer;
import kg.apc.jmeter.vizualizers.ResponseTimesOverTimeGui;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.util.JMeterUtils;

/**
 *
 * @author undera
 */
class CMDWorker {

    private int graphWidth = 800;
    private int graphHeight = 600;
    static final int EXPORT_PNG = 2 ^ 0;
    static final int EXPORT_CSV = 2 ^ 1;
    private int exportMode = 0;
    private String inputFile;
    private String outputCSV;
    private String outputPNG;
    private String pluginType;

    public CMDWorker() {
        prepareJMeterEnv();
    }

    private void prepareJMeterEnv() {
        String homeDir="/home/undera/NetBeansProjects/jmeter/trunk";
        JMeterUtils.setJMeterHome(homeDir);
        JMeterUtils.setLocale(new Locale("ignoreResources"));
        JMeterUtils.loadJMeterProperties(homeDir+"/bin/jmeter.properties");

        /*
        File savePropsFile = new File(propsFile.getParent() + "/bin");
        if (!savePropsFile.mkdirs())
        {
            throw new RuntimeException("Cannot create SaveService properties dir: "+savePropsFile.getAbsolutePath());
        }

        savePropsFile=new File(savePropsFile.getAbsolutePath()+"/saveservice.properties");
        try {
            savePropsFile.createNewFile();
        } catch (IOException ex) {
            throw new RuntimeException("Cannot create temporary SaveService properties file: "+ex.toString(), ex);
        }
        JMeterUtils.setJMeterHome(propsFile.getAbsolutePath());
         * 
         */
    }

    void addExportMode(int mode) {
        exportMode |= mode;
    }

    void setInputFile(String string) {
        inputFile = string;
    }

    void setOutputCSVFile(String string) {
        outputCSV = string;
    }

    void setOutputPNGFile(String string) {
        outputPNG = string;
    }

    void setPluginType(String string) {
        pluginType = string;
    }

    private void checkParams() {
        // TODO: check here that parameters are consistent
    }

    int doJob() {
        checkParams();

        AbstractGraphPanelVisualizer gui = new ResponseTimesOverTimeGui();
        gui.setBounds(0, 0, graphWidth, graphHeight);

        ResultCollector rc = new ResultCollector();
        rc.setFilename(inputFile);
        rc.setListener(gui);
        rc.loadExistingFile();
        try {
            gui.getGraphPanelChart().saveGraphToFile(new File(outputPNG), graphWidth, graphHeight);
        } catch (IOException ex) {
            ex.printStackTrace();
            return 1;
        }

        return 0;
    }

    void setGraphWidth(int i) {
        graphWidth = i;
    }

    void setGraphHeight(int i) {
        graphHeight = i;
    }
}
