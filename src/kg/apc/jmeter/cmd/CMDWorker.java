package kg.apc.jmeter.cmd;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import kg.apc.jmeter.vizualizers.ThreadsStateOverTimeGui;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.util.JMeterUtils;

/**
 *
 * @author undera
 */
class CMDWorker {

    public CMDWorker() {
        JMeterUtils.setLocale(new Locale("ignoreResources"));
        File propsFile = null;
        try {
            propsFile = File.createTempFile("jmeter-plugins", "testProps");
        } catch (IOException ex) {
        }

        JMeterUtils.loadJMeterProperties(propsFile.getAbsolutePath());
        JMeterUtils.setJMeterHome(".");
    }
    static final int EXPORT_PNG = 2 ^ 0;
    static final int EXPORT_CSV = 2 ^ 1;
    private int exportMode = 0;
    private String inputFile;
    private String outputCSV;
    private String outputPNG;
    private String pluginType;

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

        ThreadsStateOverTimeGui gui = new ThreadsStateOverTimeGui();
        ResultCollector rc = new ResultCollector();
        rc.setFilename(inputFile);
        rc.setListener(gui);
        rc.loadExistingFile();

        return 0;
    }
}
