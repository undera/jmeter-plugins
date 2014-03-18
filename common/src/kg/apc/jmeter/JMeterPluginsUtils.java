package kg.apc.jmeter;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.gui.AbstractJMeterGuiComponent;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.util.JMeterUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class JMeterPluginsUtils {

    private static String PLUGINS_PREFIX = "jp@gc - ";
    private static boolean prefixPlugins = true;
    public static final String WIKI_BASE = "http://jmeter-plugins.org/wiki/";

    // just prefix all the labels to be distinguished
    public static String prefixLabel(String label) {
        return prefixPlugins ? PLUGINS_PREFIX + label : label;
    }

    public static String getStackTrace(Exception ex) {
        StackTraceElement[] stack = ex.getStackTrace();
        StringBuilder res = new StringBuilder();
        for (int n = 0; n < stack.length; n++) {
            res.append("at ");
            res.append(stack[n].toString());
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
        CollectionProperty rows = new CollectionProperty(propname, new ArrayList<Object>());
        for (int row = 0; row < model.getRowCount(); row++) {
            List<Object> item = getArrayListForArray(model.getRowData(row));
            rows.addItem(item);
        }
        return rows;
    }

    public static CollectionProperty tableModelRowsToCollectionPropertyEval(PowerTableModel model, String propname) {
        CollectionProperty rows = new CollectionProperty(propname, new ArrayList<Object>());
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
            ArrayList<Object> rowObject = new ArrayList<Object>(rowStrings.size());

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
        ArrayList<Object> res = new ArrayList<Object>();
        for (int n = 0; n < rowData.length; n++) // note that we MUST use ArrayList
        {
            res.add(rowData[n]);
        }

        return res;
    }

    private static List<Object> getArrayListForArrayEval(Object[] rowData) {
        ArrayList<Object> res = new ArrayList<Object>();
        for (int n = 0; n < rowData.length; n++) // note that we must use ArrayList
        {
            res.add(new CompoundVariable(rowData[n].toString()).execute());
        }
        return res;
    }

    public static String byteBufferToString(ByteBuffer buf) {
        byte[] dst = byteBufferToByteArray(buf);
        return new String(dst);
    }

    public static byte[] byteBufferToByteArray(ByteBuffer buf) {
        ByteBuffer str = buf.duplicate();
        //System.err.println("Before "+str);
        str.rewind();
        //str.flip();
        //System.err.println("After "+str);
        byte[] dst = new byte[str.limit()];
        str.get(dst);
        return dst;
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
     * Find in panel appropriate place and put hyperlink there. I know that it
     * is stupid way. But the result is so good!
     *
     * @param panel    - supposed to be result of makeTitlePanel()
     * @param helpPage wiki page name, not full URL
     * @return original panel
     * @see AbstractJMeterGuiComponent
     */
    public static Component addHelpLinkToPanel(Container panel, String helpPage) {
        if (!java.awt.Desktop.isDesktopSupported()) {
            return panel;
        }

        JLabel icon = new JLabel();
        icon.setIcon(new javax.swing.ImageIcon(JMeterPluginsUtils.class.getResource("/kg/apc/jmeter/vizualizers/information.png")));

        JLabel link = new JLabel("Help on this plugin");
        link.setForeground(Color.blue);
        link.setFont(link.getFont().deriveFont(Font.PLAIN));
        link.setCursor(new Cursor(Cursor.HAND_CURSOR));
        link.addMouseListener(new URIOpener(WIKI_BASE + helpPage + "/?utm_source=jmeter&utm_medium=helplink&utm_campaign=" + helpPage));
        Border border = BorderFactory.createMatteBorder(0, 0, 1, 0, java.awt.Color.blue);
        link.setBorder(border);

        JLabel version = new JLabel("v" + getVersion());
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

        conf.setFormatter(null);
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

        conf.setThreadName(false);
        conf.setBytes(true);
        conf.setHostname(false);
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
            } catch (IOException ex) {
            } catch (URISyntaxException ex) {
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
        Properties props = new Properties();
        try {
            props.load(JMeterPluginsUtils.class.getResourceAsStream("version.properties"));
        } catch (IOException ex) {
            props.setProperty("version", "N/A");
        }
        return props.getProperty("version");
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
     * @param propName
     *            the name of the environment variable.
     * @param defaultVal
     *            the default value.
     * @return The PropDefault value
     */
    public static String getEnvDefault(String propName, String defaultVal) {
        String ans = defaultVal;
        String value = System.getenv(propName);
        if(value != null) {
            ans = value.trim();
        } else if (defaultVal != null) {
            ans = defaultVal.trim();
        }
        return ans;
    }
}
