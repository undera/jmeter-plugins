package kg.apc.emulators;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.gui.tree.JMeterTreeListener;
import org.apache.jmeter.gui.tree.JMeterTreeModel;
import org.apache.jmeter.threads.*;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

public abstract class TestJMeterUtils {

    public static void createJmeterEnv() {
        JMeterUtils.setJMeterHome(getTempDir());

        File dst = new File(JMeterUtils.getJMeterHome() + "/ss.props");
        InputStream src = DirectoryAnchor.class.getResourceAsStream("/kg/apc/jmeter/bin/saveservice.properties");
        try {
            Files.copy(src, dst.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy file " + src + " to " + dst, e);
        }

        JMeterUtils.loadJMeterProperties(dst.getAbsolutePath());
        JMeterUtils.setLocale(new Locale("ignoreResources"));

        JMeterTreeModel jMeterTreeModel = new JMeterTreeModel();
        JMeterTreeListener jMeterTreeListener = new JMeterTreeListener();
        jMeterTreeListener.setModel(jMeterTreeModel);
        JMeterContextService.getContext().setVariables(new JMeterVariables());
        StandardJMeterEngine engine = new EmulatorJmeterEngine();
        JMeterThreadMonitor monitor = new EmulatorThreadMonitor();
        JMeterContextService.getContext().setEngine(engine);
        HashTree hashtree = new HashTree();
        hashtree.add(new LoopController());
        JMeterThread thread = new JMeterThread(hashtree, monitor, null);
        thread.setThreadName("test thread");
        JMeterContextService.getContext().setThread(thread);
        ThreadGroup threadGroup = new org.apache.jmeter.threads.ThreadGroup();
        threadGroup.setName("test thread group");
        JMeterContextService.getContext().setThreadGroup(threadGroup);
        JMeterUtils.setProperty("sample_variables", "TEST1,TEST2,TEST3"); // for Flexible File Writer Test
        JMeterUtils.setProperty("saveservice_properties", "/ss.props");
        JMeterUtils.setProperty("upgrade_properties", "/ss.props");
        JMeterUtils.setProperty("sampleresult.default.encoding", "UTF-8"); // enable multibyte
    }

    public static String getTempDir() {
        Path f;
        try {
            File path = new File(System.getProperty("java.io.tmpdir") + "/jpgc");
            path.mkdirs();
            f = Files.createTempDirectory(path.toPath(), "ut");
        } catch (IOException e) {
            throw new RuntimeException("Failed to get new temp dir", e);
        }
        assert f != null;
        return f.toString();
    }

    public static String getTestData(int i) {
        return RandomStringUtils.randomAlphanumeric(i);
    }


    public static String fixWinPath(String path) {
        String ret = path;
        //test if win os
        boolean isWinOs = System.getProperty("os.name").toLowerCase().contains("win");

        //fix only files in "document and settings", for computer with no admin / C: drive access...
        if (isWinOs) {
            ret = ret.replace("Documents%20and%20Settings", "DOCUME~1");
            ret = ret.replace("Local%20Settings", "LOCALS~1");
            ret = ret.replace("Application%20Data", "APPLIC~1");
        }

        return ret;
    }

    public static String convertStreamToString(InputStream is)
            throws IOException {
        /*
         * To convert the InputStream to String we use the Reader.read(char[]
         * buffer) method. We iterate until the Reader return -1 which means
         * there's no more data to read. We use the StringWriter class to
         * produce the string.
         */
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }
}
