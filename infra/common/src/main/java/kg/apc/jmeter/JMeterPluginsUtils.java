package kg.apc.jmeter;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.gui.AbstractJMeterGuiComponent;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public abstract class JMeterPluginsUtils {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final String PLUGINS_PREFIX = "jp@gc - ";
    private static boolean prefixPlugins = true;
    public static final String WIKI_BASE = "http://jmeter-plugins.org/wiki/";
    private static String version = null;

    public static final String ENCODING = JMeterUtils.getPropDefault("sampleresult.default.encoding", SampleResult.DEFAULT_HTTP_ENCODING);
    public static final Charset CHARSET = Charset.forName(ENCODING);


    static {
        getVersion();
    }

    // just prefix all the labels to be distinguished
    public static String prefixLabel(String label) {
        return prefixPlugins ? PLUGINS_PREFIX + label : label;
    }

    public static String getStackTrace(Exception ex) {
        StackTraceElement[] stack = ex.getStackTrace();
        StringBuilder res = new StringBuilder();
        for (StackTraceElement aStack : stack) {
            res.append("at ");
            res.append(aStack.toString());
            res.append("\n");
        }
        return res.toString();
    }

    static {
        String prefixPluginsCfg = JMeterUtils.getProperty("jmeterPlugin.prefixPlugins");
        if (prefixPluginsCfg != null) {
            JMeterPluginsUtils.prefixPlugins = "true".equalsIgnoreCase(prefixPluginsCfg.trim());
        }
    }

    public static CollectionProperty tableModelRowsToCollectionProperty(PowerTableModel model, String propname) {
        CollectionProperty rows = new CollectionProperty(propname, new ArrayList<>());
        for (int row = 0; row < model.getRowCount(); row++) {
            List<Object> item = getArrayListForArray(model.getRowData(row));
            rows.addItem(item);
        }
        return rows;
    }

    public static CollectionProperty tableModelRowsToCollectionPropertyEval(PowerTableModel model, String propname) {
        CollectionProperty rows = new CollectionProperty(propname, new ArrayList<>());
        for (int row = 0; row < model.getRowCount(); row++) {
            List<Object> item = getArrayListForArrayEval(model.getRowData(row));
            rows.addItem(item);
        }
        return rows;
    }

    public static void collectionPropertyToTableModelRows(CollectionProperty prop, PowerTableModel model) {
        model.clearData();
        for (int rowN = 0; rowN < prop.size(); rowN++) {
            ArrayList<String> rowObject = (ArrayList<String>) prop.get(rowN).getObjectValue();
            model.addRow(rowObject.toArray());
        }
        model.fireTableDataChanged();
    }

    public static void collectionPropertyToTableModelRows(CollectionProperty prop, PowerTableModel model, Class[] columnClasses) {
        model.clearData();
        for (int rowN = 0; rowN < prop.size(); rowN++) {
            ArrayList<StringProperty> rowStrings = (ArrayList<StringProperty>) prop.get(rowN).getObjectValue();
            ArrayList<Object> rowObject = new ArrayList<>(rowStrings.size());

            for (int i = 0; i < columnClasses.length && i < rowStrings.size(); i++) {
                rowObject.add(convertToClass(rowStrings.get(i), columnClasses[i]));
            }
            //for now work only if new fields are added at the end...
            //needed for retro compatibility if new fields added
            if (rowObject.size() < columnClasses.length) {
                for (int i = rowObject.size(); i < columnClasses.length; i++) {
                    rowObject.add(new Object());
                }
            }
            model.addRow(rowObject.toArray());
        }
        model.fireTableDataChanged();
    }

    private static List<Object> getArrayListForArray(Object[] rowData) {
        ArrayList<Object> res = new ArrayList<>();
        //noinspection ManualArrayToCollectionCopy
        for (Object aRowData : rowData) {
            res.add(aRowData);
        }

        return res;
    }

    private static List<Object> getArrayListForArrayEval(Object[] rowData) {
        ArrayList<Object> res = new ArrayList<>();
        for (Object aRowData : rowData) {
            res.add(new CompoundVariable(aRowData.toString()).execute());
        }
        return res;
    }


    public static String replaceRNT(String str) {
        // FIXME: stop using bad way...
        str = str.replaceAll("\\\\\\\\", "VERY BAD WAY");
        //System.err.println(str);
        str = str.replaceAll("\\\\t", "\t");
        //str=str.replaceAll("(^|[^\\\\])\\\\t", "$1\t");
        //System.err.println(str);
        str = str.replaceAll("\\\\n", "\n");
        //System.err.println(str);
        str = str.replaceAll("\\\\r", "\r");
        str = str.replaceAll("VERY BAD WAY", "\\\\");
        return str;
    }

    public static String getWikiLinkText(String wikiPage) {
        if (!java.awt.Desktop.isDesktopSupported()) {
            return "Plugin help available here: " + WIKI_BASE + wikiPage;
        } else {
            return "";
        }
    }

    private static Object convertToClass(StringProperty value, Class aClass) {
        if (Boolean.class.equals(aClass)) {
            return Boolean.valueOf(value.getStringValue());
        }
        return value;
    }

    /**
     * Builds full URL from wiki page name unless a URL is already passed in.
     *
     * @param helpPage wiki page name (not full URL) or URL to external wiki
     * @return full URL to helpPage
     */
    public static String buildHelpPageUrl(String helpPage) {
        try {
            if (helpPage.matches("[hH][tT][tT][pP][sS]?://.*")) {
                log.debug("Help page URL found, skipping building link to "+WIKI_BASE);
                return helpPage;
            }
        } catch (PatternSyntaxException ex) {
            log.warn("Invalid regex", ex);
        }

        if (helpPage.endsWith("Gui")) {
            helpPage = helpPage.substring(0, helpPage.length() - 3);
        }
        return WIKI_BASE + helpPage + "/?utm_source=jmeter&utm_medium=helplink&utm_campaign=" + helpPage;
    }

    /**
     * Find in panel appropriate place and put hyperlink there. I know that it
     * is stupid way. But the result is so good!
     *
     * @param panel    - supposed to be result of makeTitlePanel()
     * @param helpPage wiki page name, or full URL in case of external wiki
     * @return original panel
     * @see AbstractJMeterGuiComponent
     */
    public static Component addHelpLinkToPanel(Container panel, String helpPage) {
        if (!java.awt.Desktop.isDesktopSupported()) {
            return panel;
        }
        JLabel icon = new JLabel();
        icon.setIcon(new javax.swing.ImageIcon(JMeterPluginsUtils.class.getResource("vizualizers/information.png")));

        JLabel link = new JLabel("Help on this plugin");
        link.setForeground(Color.blue);
        link.setFont(link.getFont().deriveFont(Font.PLAIN));
        link.setCursor(new Cursor(Cursor.HAND_CURSOR));
        link.addMouseListener(new URIOpener(buildHelpPageUrl(helpPage)));
        Border border = BorderFactory.createMatteBorder(0, 0, 1, 0, java.awt.Color.blue);
        link.setBorder(border);

        JLabel version = new JLabel(""); // FIXME: what to do?
        version.setFont(version.getFont().deriveFont(Font.PLAIN).deriveFont(11F));
        version.setForeground(Color.GRAY);

        Container innerPanel = findComponentWithBorder((JComponent) panel, EtchedBorder.class);

        JPanel panelLink = new JPanel(new GridBagLayout());

        GridBagConstraints gridBagConstraints;

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 0);
        panelLink.add(icon, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 3, 0);
        panelLink.add(link, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panelLink.add(version, gridBagConstraints);

        if (innerPanel != null) {
            innerPanel.add(panelLink);
        } else {
            panel.add(panelLink);
        }
        return panel;
    }

    private static Container findComponentWithBorder(JComponent panel, Class<?> aClass) {
        for (int n = 0; n < panel.getComponentCount(); n++) {
            if (panel.getComponent(n) instanceof JComponent) {
                JComponent comp = (JComponent) panel.getComponent(n);
                if (comp.getBorder() != null && aClass.isAssignableFrom(comp.getBorder().getClass())) {
                    return comp;
                }

                Container con = findComponentWithBorder(comp, aClass);
                if (con != null) {
                    return con;
                }
            }
        }
        return null;
    }

    public static void doBestCSVSetup(SampleSaveConfiguration conf) {
        conf.setAsXml(false);
        conf.setFieldNames(true);

        conf.setSamplerData(false);
        conf.setRequestHeaders(false);
        conf.setFileName(false);
        conf.setIdleTime(false);
        conf.setSuccess(true);
        conf.setMessage(true);
        conf.setEncoding(false);
        conf.setThreadCounts(true);
        conf.setFieldNames(true);
        conf.setAssertions(false);
        conf.setResponseData(false);
        conf.setSubresults(false);
        conf.setLatency(true);
        conf.setLabel(true);

        conf.setThreadName(true);
        conf.setBytes(true);
        conf.setHostname(true);
        conf.setAssertionResultsFailureMessage(false);
        conf.setResponseHeaders(false);
        conf.setUrl(false);
        conf.setTime(true);
        conf.setTimestamp(true);
        conf.setCode(true);
        conf.setDataType(false);
        conf.setSampleCount(false);
    }

    public static void openInBrowser(String string) {
        if (java.awt.Desktop.isDesktopSupported()) {
            try {
                java.awt.Desktop.getDesktop().browse(new URI(string));
            } catch (IOException | URISyntaxException ignored) {
                log.debug("Failed to open in browser", ignored);
            }
        }
    }

    public static float getFloatFromString(String stringValue, float defaultValue) {
        float ret;
        if (stringValue != null) {
            try {
                ret = Float.valueOf(stringValue);
            } catch (NumberFormatException ex) {
                ret = defaultValue;
            }
        } else {
            ret = defaultValue;
        }

        return ret;
    }

    public static int getSecondsForShortString(String string) {
        int res = 0;
        string = string.trim();

        String c;
        String curNum = "";
        for (int n = 0; n < string.length(); n++) {
            c = String.valueOf(string.charAt(n));
            if (c.matches("\\d")) {
                curNum += c;
            } else {
                int mul;
                switch (c.charAt(0)) {
                    case 's':
                    case 'S':
                        mul = 1;
                        break;
                    case 'm':
                    case 'M':
                        mul = 60;
                        break;
                    case 'h':
                    case 'H':
                        mul = 60 * 60;
                        break;
                    case 'd':
                    case 'D':
                        mul = 60 * 60 * 24;
                        break;
                    default:
                        throw new NumberFormatException("Shorthand string does not allow using '" + c + "'");
                }
                res += Integer.parseInt(curNum) * mul;
                curNum = "";
            }
        }

        if (!curNum.isEmpty()) {
            res += Integer.parseInt(curNum);
        }

        return res;
    }

    public static String getVersion() {
        if (version == null) {
            Properties props = new Properties();
            try {
                throw new IOException("Deprecated call");
                //props.load(JMeterPluginsUtils.class.getResourceAsStream("version.properties"));
            } catch (IOException ex) {
                props.setProperty("version", "N/A");
            }
            version = props.getProperty("version");
            //log.info("JMeter-Plugins.org v." + version);
        }
        return version;
    }

    private static class URIOpener extends MouseAdapter {

        private final String uri;

        public URIOpener(String aURI) {
            uri = aURI;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
                openInBrowser(uri);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    /**
     * Get a String value (environment) with default if not present.
     *
     * @param propName   the name of the environment variable.
     * @param defaultVal the default value.
     * @return The PropDefault value
     */
    public static String getEnvDefault(String propName, String defaultVal) {
        String ans = defaultVal;
        String value = System.getenv(propName);
        if (value != null) {
            ans = value.trim();
        } else if (defaultVal != null) {
            ans = defaultVal.trim();
        }
        return ans;
    }


    public static void prepareJMeterEnv(String homeDir) {
        if (JMeterUtils.getJMeterHome() != null) {
            log.warn("JMeter env exists. No one should see this normally.");
            return;
        }

        log.debug("Orig jmeter home dir: " + homeDir);
        File dir = new File(homeDir);
        while (dir.exists() && dir.getName().equals("ext") && dir.getParentFile().getName().equals("lib")) {
            dir = dir.getParentFile();
        }

        if (!dir.exists()) {
            throw new IllegalArgumentException("CMDRunner.jar must be placed in <jmeter>/lib/ext directory");
        }

        homeDir = dir.getParent();

        if (!isJMeterHome(homeDir)) {
            homeDir = getJMeterHomeFromCP(System.getProperty("java.class.path"));
        }

        log.debug("Final jmeter home dir: " + homeDir);
        JMeterUtils.setJMeterHome(homeDir);
        initializeProperties();
    }

    /**
     * Had to copy this method from JMeter class 'cause they provide no ways to
     * re-use this code
     *
     * @see org.apache.jmeter.JMeter
     */
    private static void initializeProperties() {
        JMeterUtils.loadJMeterProperties(JMeterUtils.getJMeterHome()
                + File.separator
                + "bin" + File.separator
                + "jmeter.properties");

        //JMeterUtils.initLogging();
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
                    LoggingManager.setLoggingLevels(jmeterProps);//Do what would be done earlier
                }
            } catch (IOException e) {
                log.warn("Error loading user property file: " + userProp, e);
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
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
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException ex) {
                    log.warn("There was problem closing file stream", ex);
                }
            }
        }
    }

    private static boolean isJMeterHome(String homeDir) {
        File f = new File(homeDir + File.separator + "lib" + File.separator + "ext");
        return f.exists() && f.isDirectory();
    }

    public static String getJMeterHomeFromCP(String classpathSTR) {
        log.debug("Trying to get JMeter home from classpath");

        //FIXME: This dirty way of doing it should be changed as it is OS sensitive

        String splitter;

        if (classpathSTR.indexOf(';') != -1) {
            splitter = ";";
        } else {
            splitter = ":";
        }

        String[] paths = classpathSTR.split(splitter);
        for (String string : paths) {
            log.debug("Testing " + string);
            if (string.endsWith("ApacheJMeter_core.jar")) {
                File f = new File(string);
                return f.getParentFile().getParentFile().getParentFile().getAbsolutePath();
            }
        }
        throw new Error("Failed to find JMeter home dir from classpath");
    }

    public static String getShortHostname(String host) {
        log.debug("getShortHostname: " + host);
        try {
            String defaultRegex = "([\\w\\-]+)\\..*";
            String hostnameRegex = JMeterUtils.getPropDefault("jmeterPlugin.perfmon.label.useHostname.pattern", defaultRegex);
            log.debug("hostnameRegex: " + hostnameRegex);
            Pattern p = Pattern.compile(hostnameRegex, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(host);
            if (m.matches()) {
                String shortName = m.group(1);
                log.debug("shortName of " + host + " is: " + shortName);
                host = shortName;
            }
        } catch (Exception e) {
            log.warn("getShortHostname exception: " + e.getClass().getName() + " :: " + e.getMessage());
            log.debug("getShortHostname exception: ", e);
        }
        return host;
    }

}
