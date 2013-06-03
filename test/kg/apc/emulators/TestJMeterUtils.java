package kg.apc.emulators;

import kg.apc.jmeter.DirectoryAnchor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.tree.JMeterTreeListener;
import org.apache.jmeter.gui.tree.JMeterTreeModel;
import org.apache.jmeter.threads.*;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.junit.Test;

import java.io.*;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author apc
 */
public abstract class TestJMeterUtils {

    private static JMeterTreeListener jMeterTreeListener;
    private static JMeterTreeModel jMeterTreeModel;

    /**
     *
     */
    public static void createJmeterEnv() {
        File propsFile = null;
        try {
            propsFile = File.createTempFile("jmeter-plugins", ".properties");
            propsFile.deleteOnExit();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }

        //propsFile=new File("/home/undera/NetBeansProjects/jmeter/trunk/bin/jmeter.properties");

        JMeterUtils.loadJMeterProperties(propsFile.getAbsolutePath());
        JMeterUtils.setJMeterHome(new DirectoryAnchor().toString());
        JMeterUtils.setLocale(new Locale("ignoreResources"));

        jMeterTreeModel = new JMeterTreeModel();
        jMeterTreeListener = new JMeterTreeListener();
        jMeterTreeListener.setModel(jMeterTreeModel);
        GuiPackage.getInstance(jMeterTreeListener, jMeterTreeModel);
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
    }

    public static String getTempDir() {
        File f = null;
        try {
            f = File.createTempFile("jmeterplugins", ".tmp");
        } catch (IOException ex) {
            Logger.getLogger(TestJMeterUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return f.getParent();
    }

    public static String getTestData(int i) {
        return RandomStringUtils.randomAlphanumeric(i);
    }

    /**
     *
     */
    @Test
    public void testEnv() {
        TestJMeterUtils.createJmeterEnv();
        GuiPackage.getInstance().updateCurrentNode();
    }

    public static String fixWinPath(String path) {
        String ret = path;
        //test if win os
        boolean isWinOs = System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;

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
