package kg.apc.jmeter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.gui.AbstractJMeterGuiComponent;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.util.JMeterUtils;

/**
 *
 * @author undera
 */
public abstract class JMeterPluginsUtils {

    private static String PLUGINS_VERSION = "0.4.1 snapshot";
    private static String PLUGINS_PREFIX = "jp@gc - ";
    private static boolean prefixPlugins = true;
    private static final String WIKI_BASE = "http://code.google.com/p/jmeter-plugins/wiki/";

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

    /**
     *
     * @param model
     * @return
     */
    public static CollectionProperty tableModelToCollectionProperty(PowerTableModel model, String propname) {
        CollectionProperty rows = new CollectionProperty(propname, new ArrayList<Object>());
        for (int col = 0; col < model.getColumnCount(); col++) {
            rows.addItem(model.getColumnData(model.getColumnName(col)));
        }
        return rows;
    }

    /**
     *
     * @param model
     * @return
     */
    public static CollectionProperty tableModelToCollectionPropertyEval(PowerTableModel model, String propname) {
        CollectionProperty rows = new CollectionProperty(propname, new ArrayList<Object>());
        for (int col = 0; col < model.getColumnCount(); col++) {
            ArrayList<Object> tmp = new ArrayList<Object>();
            Iterator iter = model.getColumnData(model.getColumnName(col)).iterator();
            while (iter.hasNext()) {
                String value = iter.next().toString();
                tmp.add(new CompoundVariable(value).execute());
            }

            rows.addItem(tmp);
        }
        return rows;
    }

    public static String byteBufferToString(ByteBuffer buf) {
        ByteBuffer str = buf.duplicate();
        //System.err.println(str);
        str.rewind();
        //System.err.println(str);
        byte[] dst = new byte[str.limit()];
        str.get(dst);
        return new String(dst);
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

    /**
     * Find in panel appropriate place and put hyperlink there.
     * I know that it is stupid way. But the result is so good!
     * @param panel - supposed to be result of makeTitlePanel()
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
        link.addMouseListener(new URIOpener(WIKI_BASE + helpPage+"?utm_source=jmeter&utm_medium=helplink&utm_campaigh="+helpPage));
        Border border = BorderFactory.createMatteBorder(0, 0, 1, 0, java.awt.Color.blue);
        link.setBorder(border);

        JLabel version = new JLabel("v" + PLUGINS_VERSION);
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

    private static Container findComponentWithBorder(JComponent panel, Class aClass) {
        for (int n = 0; n < panel.getComponentCount(); n++) {
            if (panel.getComponent(n) instanceof JComponent) {
                JComponent comp=(JComponent) panel.getComponent(n);
                if (comp.getBorder()!=null && aClass.isAssignableFrom(comp.getBorder().getClass()))
                    return comp;

                Container con=findComponentWithBorder(comp, aClass);
                if (con!=null)
                    return con;
            }
        }
        return null;
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

    public static int getSecondsForShortString(String string) {
        int res=0;
        string=string.trim();

        String c;
        String curNum="";
        for (int n=0; n<string.length(); n++)
        {
            c=String.valueOf(string.charAt(n));
            if (c.matches("\\d"))
            {
                curNum+=c;
            }
            else
            {
                int mul;
                switch(c.charAt(0))
                {
                    case 's':
                    case 'S':
                        mul=1;
                        break;
                    case 'm':
                    case 'M':
                        mul=60;
                        break;
                    case 'h':
                    case 'H':
                        mul=60*60;
                        break;
                    case 'd':
                    case 'D':
                        mul=60*60*24;
                        break;
                    default:
                        throw new NumberFormatException("Shorthand string does not allow using '"+c+"'");
                }
                res+=Integer.parseInt(curNum)*mul;
                curNum="";
            }
        }

        if (!curNum.isEmpty())
            res+=Integer.parseInt(curNum);

        return res;
    }

    private static class URIOpener implements MouseListener {

        private final String uri;

        public URIOpener(String aURI) {
            uri = aURI;
        }

        public void mouseClicked(MouseEvent e) {
            if((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) openInBrowser(uri);
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }
}
