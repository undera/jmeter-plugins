package kg.apc.jmeter.vizualizers;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;

import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.gui.JAbsrtactDialogPanel;

/**
 * @author Stephane Hoblingre
 */
public class JPerfmonParamsPanel extends JAbsrtactDialogPanel {

    private final static int OPTION_PRIMARY_METRIC = 1;
    private final static int OPTION_ADDITIONAL_METRIC = 1 << 1;
    private final static int OPTION_PROCESS_SCOPE = 1 << 2;
    private final static int OPTION_CPU_CORE_SCOPE = 1 << 3;
    private final static int OPTION_FILESYSTEM_SCOPE = 1 << 4;
    private final static int OPTION_NET_INTERFACE_SCOPE = 1 << 5;
    private final static int OPTION_EXEC = 1 << 6;
    private final static int OPTION_TAIL = 1 << 7;
    private final static int OPTION_JMX = 1 << 8;
    private final static int OPTION_UNIT = 1 << 9;
    private JTextField parent = null;
    private String type = null;

    private final static String defaultMarker = " (default)";
    private final static String separator = ":";
    private final static String METRIC_CPU = "CPU";
    private final static String METRIC_MEM = "Memory";
    private final static String METRIC_SWAP = "Swap";
    private final static String METRIC_DISKIO = "Disks I/O";
    private final static String METRIC_NETIO = "Network I/O";
    private final static String METRIC_TCP = "TCP";
    private final static String METRIC_EXEC = "EXEC";
    private final static String METRIC_TAIL = "TAIL";
    private final static String METRIC_JMX = "JMX";

    private HashMap<String, Integer> rules = new HashMap<String, Integer>();

    private ArrayList<String> unitRules = new ArrayList<String>();

    //CPU metrics
    private static String[] cpuMetricsPrimary = {
            "combined" + defaultMarker, "Get the combined CPU usage, in percent (%)",
            "idle", "Get the idle CPU usage, in percent (%)",
            "system", "Get the system CPU usage, in percent (%)",
            "user", "Get the user CPU usage, in percent (%)",
            "iowait", "Get the iowait CPU usage, in percent (%)"
    };
    private static String[] cpuProcessMetricsPrimary = {
            "percent" + defaultMarker, "Get the process combined CPU usage, in percent (%)",
            "total", "Get the process cpu time (sum of User and System) per second, in ms",
            "system", "Get the process cpu kernel time per second, in ms",
            "user", "Get the process cpu user time per second, in ms"
    };
    private static String[] cpuMetricsAdditional = {
            "irq", "Get the irq CPU usage, in percent (%)",
            "nice", "Get the nice CPU usage, in percent (%)",
            "softirq", "Get the softirq CPU usage, in percent (%)",
            "stolen", "Get the stolen CPU usage, in percent (%)",};
    //Memory Metrics
    private static String[] memMetricsPrimary = {
            "usedperc" + defaultMarker, "Relative memory usage, in percent (%)",
            "freeperc", "Relative free memory, in percent (%)",
            "used", "Size of Memory used",
            "free", "Size of Free memory"};
    private static String[] memMetricsAdditional = {
            "actualused", "Size of Actual memory usage",
            "actualfree", "Size of Actual free memory",
            "ram", "Server physical memory in Mb",
            "total", "Size of Total memory"};
    private static String[] memProcessMetricsPrimary = {
            "resident" + defaultMarker, "Size of Process resident memory usage",
            "virtual", "Size of Process virtual memory usage",
            "shared", "Size of Process shared memory usage"
    };
    private static String[] memProcessMetricsAdditional = {
            "pagefaults", "Process page faults count",
            "majorfaults", "Process major faults count",
            "minorfaults", "Process minor faults count"
    };
    //Disk I/O
    private static String[] diskIOMetricsPrimary = {
            "queue" + defaultMarker, "Description to update",
            "reads", "Number of read access",
            "writes", "Number of write access",
            "readbytes", "Number of bytes read",
            "writebytes", "Number of bytes written"
    };
    private static String[] diskIOMetricsAdditional = {
            "available", "Description to update",
            "service", "Description to update",
            "files", "Description to update",
            "free", "Description to update",
            "freefiles", "Description to update",
            "total", "Description to update",
            "useperc", "Description to update",
            "used", "Description to update"
    };
    //Network I/O
    private static String[] netIOMetricsPrimary = {
            "bytesrecv" + defaultMarker, "Number of bytes received",
            "bytessent", "Number of bytes sent",
            "rx", "Description to update",
            "tx", "Description to update"
    };
    private static String[] netIOMetricsAdditional = {
            "used", "Description to update",
            "speed", "Description to update",
            "rxdrops", "Description to update",
            "rxerr", "Description to update",
            "rxframe", "Description to update",
            "rxoverruns", "Description to update",
            "txcarrier", "Description to update",
            "txcollisions", "Description to update",
            "txdrops", "Description to update",
            "txerr", "Description to update",
            "txoverruns", "Description to update"
    };
    //TCP
    private static String[] tcpMetricsPrimary = {
            "estab" + defaultMarker, "Number of established connections",
            "time_wait", "Description to update",
            "close_wait", "Description to update"
    };
    private static String[] tcpMetricsAdditional = {
            "bound", "Description to update",
            "close", "Description to update",
            "closing", "Description to update",
            "fin_wait1", "Description to update",
            "fin_wait2", "Description to update",
            "idle", "Description to update",
            "inbound", "Description to update",
            "last_ack", "Description to update",
            "listen", "Description to update",
            "outbound", "Description to update",
            "syn_recv", "Description to update"
    };
    //SWAP
    private static String[] swapMetricsPrimary = {
            "used" + defaultMarker, "Size of used swap",
            "pagein", "Number of page in",
            "pageout", "Number of page out",
            "free", "Size of free swap",
            "total", "Size of total swap"
    };

    //JMX
    private static String[] jmxMetricsPrimary = {
            "gc-time", "Time spent in garbage collection, milliseconds",
            "memory-usage", "Heap memory used by VM, bytes",
            "memory-committed", "Heap memory committed by VM, bytes",
            "memorypool-usage", "Heap memory pool usage, bytes",
            "memorypool-committed", "Heap memory pool committed size, bytes",
            "class-count", "Loaded class count in VM",
            "compile-time", "Time spent in compilation, milliseconds"
    };

    /**
     * Creates new form JPerfmonParamsDialog
     */
    public JPerfmonParamsPanel(String type, JTextField parentField) {
        this.parent = parentField;
        this.type = type;
        initRules();
        initUnitRules();
        initComponents();
        initMetrics(type);
        showProcessScopePanels();
        makePtqlLink();
        initFields();
    }

    private void addUnitRule(String type, String metric) {
        if (metric.endsWith(defaultMarker)) metric = metric.substring(0, metric.length() - defaultMarker.length());
        unitRules.add(type + metric);
    }

    private void initUnitRules() {
        //TODO: map all metrics...
        //memory
        addUnitRule(METRIC_MEM, memMetricsPrimary[4]);
        addUnitRule(METRIC_MEM, memMetricsPrimary[6]);
        addUnitRule(METRIC_MEM, memMetricsAdditional[0]);
        addUnitRule(METRIC_MEM, memMetricsAdditional[2]);
        addUnitRule(METRIC_MEM, memMetricsAdditional[6]);

        addUnitRule(METRIC_MEM, memProcessMetricsPrimary[0]);
        addUnitRule(METRIC_MEM, memProcessMetricsPrimary[2]);
        addUnitRule(METRIC_MEM, memProcessMetricsPrimary[4]);

        //swap
        addUnitRule(METRIC_SWAP, swapMetricsPrimary[0]);
        addUnitRule(METRIC_SWAP, swapMetricsPrimary[6]);
        addUnitRule(METRIC_SWAP, swapMetricsPrimary[8]);

        //diskio
        addUnitRule(METRIC_DISKIO, diskIOMetricsPrimary[6]);
        addUnitRule(METRIC_DISKIO, diskIOMetricsPrimary[8]);

        addUnitRule(METRIC_DISKIO, diskIOMetricsAdditional[0]);
        addUnitRule(METRIC_DISKIO, diskIOMetricsAdditional[4]);
        addUnitRule(METRIC_DISKIO, diskIOMetricsAdditional[6]);
        addUnitRule(METRIC_DISKIO, diskIOMetricsAdditional[8]);
        addUnitRule(METRIC_DISKIO, diskIOMetricsAdditional[10]);
        addUnitRule(METRIC_DISKIO, diskIOMetricsAdditional[14]);

        //netio
        addUnitRule(METRIC_NETIO, netIOMetricsPrimary[0]);
        addUnitRule(METRIC_NETIO, netIOMetricsPrimary[2]);

        //jmx
        addUnitRule(METRIC_JMX, jmxMetricsPrimary[2]);
        addUnitRule(METRIC_JMX, jmxMetricsPrimary[4]);
        addUnitRule(METRIC_JMX, jmxMetricsPrimary[6]);
        addUnitRule(METRIC_JMX, jmxMetricsPrimary[8]);
    }

    private boolean isUnitRelevent(String metric) {
        return unitRules.contains(type + metric);
    }

    private void enableUnit(String metric) {
        boolean show = isUnitRelevent(metric);
        jLabelUnit.setEnabled(show);
        jComboBoxUnit.setEnabled(show);
    }

    private String getUnit() {
        int unit = jComboBoxUnit.getSelectedIndex();
        switch (unit) {
            case 1:
                return "kb";
            case 2:
                return "mb";
            default:
                return "b";
        }
    }

    private void setUnit(String unit) {
        if ("kb".equalsIgnoreCase(unit)) jComboBoxUnit.setSelectedIndex(1);
        else if ("mb".equalsIgnoreCase(unit)) jComboBoxUnit.setSelectedIndex(2);
        else jComboBoxUnit.setSelectedIndex(0);
    }

    //extract exec or tail command (handle label)
    private String extractExecTailCmd(String params) {
        String ret;
        String[] tmp = params.split("(?<!\\\\)" + separator);
        String labelString = null;
        for (String aTmp : tmp) {
            if (aTmp.startsWith("label=")) {
                labelString = aTmp;
            }
        }

        if (labelString != null) {
            if (params.startsWith(labelString)) {
                ret = params.substring(labelString.length() + separator.length());
            } else {
                ret = params.substring(0, params.indexOf(labelString) - separator.length());
            }
        } else {
            ret = params;
        }

        return ret;
    }

    private void initFields() {
        String existing = parent.getText();

        //for split, avoid ':' preceeded with '\', ie do not process "\:"
        String[] elements = existing.split("(?<!\\\\)" + separator);
        checkProcessScope(existing, elements);
        checkCPUCore(elements);
        checkFilesystem(elements);
        checkNetInterface(elements);
        checkJMX(elements);

        //set metric selected, exec or tail command
        if (METRIC_EXEC.equals(type)) {
            jTextFieldExec.setText(extractExecTailCmd(existing));

            //hide stretch panel as stretch is done with text area
            jPanelStretch.setVisible(false);
        } else if (METRIC_TAIL.equals(type)) {
            jTextFieldTail.setText(extractExecTailCmd(existing));
        } else {
            for (String element : elements) {
                initMetricRadios(element);
            }
        }

        //set label
        int i = 0;
        while (i < elements.length) {
            if (elements[i].startsWith("label=")) {
                jTextFieldMetricLabel.setText(elements[i].substring(6));
                break;
            }
            i++;
        }

        //set unit
        i = 0;
        while (i < elements.length) {
            if (elements[i].startsWith("unit=")) {
                setUnit(elements[i].substring(5));
                break;
            }
            i++;
        }
    }

    private void checkNetInterface(String[] elements) {
        //check network interface filter
        if (METRIC_NETIO.equals(type)) {
            int i = 0;
            while (i < elements.length) {
                if (elements[i].startsWith("iface=")) {
                    jTextFieldNetInterface.setText(elements[i].substring(6));
                    break;
                }
                i++;
            }
        }
    }

    private void checkFilesystem(String[] elements) {
        //check filesystem filter
        if (METRIC_DISKIO.equals(type)) {
            int i = 0;
            while (i < elements.length) {
                if (elements[i].startsWith("fs=")) {
                    jTextFieldFileSystem.setText(elements[i].substring(3));
                    break;
                }
                i++;
            }
        }
    }

    private void checkJMX(String[] elements) {
        //check jmx
        if (METRIC_JMX.equals(type)) {
            int i = 0;
            while (i < elements.length) {
                if (elements[i].startsWith("url=")) {
                    String[] tmp = elements[i].substring(4).split("\\\\:");
                    jTextFieldJmxHost.setText(tmp[0]);
                    if (tmp.length > 1) {
                        jTextFieldJmxPort.setText(tmp[1]);
                    }
                }
                if (elements[i].startsWith("user=")) {
                    jTextFieldJmxUser.setText(elements[i].substring(5));
                }
                if (elements[i].startsWith("password=")) {
                    jTextFieldJmxPassword.setText(elements[i].substring(9));
                }
                i++;
            }
        }
    }

    private void checkCPUCore(String[] elements) {
        //check cpu core
        if (METRIC_CPU.equals(type)) {
            int i = 0;
            while (i < elements.length) {
                if (elements[i].startsWith("core=")) {
                    jRadioCustomCpuCore.setSelected(true);
                    String[] tmp = elements[i].split("=");
                    if (tmp.length > 1) {
                        jTextFieldCoreIndex.setText(tmp[1]);
                    } else {
                        jTextFieldCoreIndex.setText("0");
                    }
                    break;
                }
                i++;
            }
        }
    }

    private void checkProcessScope(String existing, String[] elements) {
        //check process scope
        if (METRIC_CPU.equals(type) || METRIC_MEM.equals(type)) {
            if (existing.contains("pid=")
                    || existing.contains("name=")
                    || existing.contains("ptql=")) {
                jRadioScopePerProcess.setSelected(true);
                showProcessScopePanels();
            }
            int i = 0;
            while (i < elements.length) {
                if (elements[i].startsWith("pid=")) {
                    jRadioPID.setSelected(true);
                    jTextFieldPID.setText(elements[i].substring(4));
                    break;
                } else if (elements[i].startsWith("name=")) {
                    String[] tmp = elements[i].split("#");
                    jRadioProcessName.setSelected(true);
                    jTextFieldPorcessName.setText(tmp[0].substring(5));
                    if (tmp.length == 2) jTextFieldOccurence.setText(tmp[1]);
                    break;
                } else if (elements[i].startsWith("ptql=")) {
                    jRadioPtql.setSelected(true);
                    jTextFieldPtql.setText(elements[i].substring(5));
                    break;
                }
                i++;
            }
        }
    }

    private void initMetricRadios(String metricName) {
        Enumeration<AbstractButton> enu = buttonGroupMetrics.getElements();
        while (enu.hasMoreElements()) {
            JRadioButton radio = (JRadioButton) enu.nextElement();
            if (metricName.equals(radio.getActionCommand())) {
                radio.setSelected(true);
                enableUnit(metricName);
            }
        }
    }

    private void makePtqlLink() {
        jLabelPtqlHelp.setForeground(Color.blue);
        jLabelPtqlHelp.setFont(jLabelPtqlHelp.getFont().deriveFont(Font.PLAIN));
        jLabelPtqlHelp.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jLabelPtqlHelp.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
                    JMeterPluginsUtils.openInBrowser("http://support.hyperic.com/display/SIGAR/PTQL");
                }
            }
        });
        Border border = BorderFactory.createMatteBorder(0, 0, 1, 0, java.awt.Color.blue);
        jLabelPtqlHelp.setBorder(border);
    }

    private void initRules() {
        rules.put(METRIC_CPU, OPTION_PRIMARY_METRIC | OPTION_ADDITIONAL_METRIC | OPTION_PROCESS_SCOPE | OPTION_CPU_CORE_SCOPE);
        rules.put(METRIC_MEM, OPTION_PRIMARY_METRIC | OPTION_ADDITIONAL_METRIC | OPTION_PROCESS_SCOPE | OPTION_UNIT);
        rules.put(METRIC_SWAP, OPTION_PRIMARY_METRIC | OPTION_UNIT);
        rules.put(METRIC_DISKIO, OPTION_PRIMARY_METRIC | OPTION_ADDITIONAL_METRIC | OPTION_FILESYSTEM_SCOPE | OPTION_UNIT);
        rules.put(METRIC_NETIO, OPTION_PRIMARY_METRIC | OPTION_ADDITIONAL_METRIC | OPTION_NET_INTERFACE_SCOPE | OPTION_UNIT);
        rules.put(METRIC_TCP, OPTION_PRIMARY_METRIC | OPTION_ADDITIONAL_METRIC);
        rules.put(METRIC_EXEC, OPTION_EXEC);
        rules.put(METRIC_TAIL, OPTION_TAIL);
        rules.put(METRIC_JMX, OPTION_PRIMARY_METRIC | OPTION_JMX | OPTION_UNIT);
    }

    private void fillMetrics(String[] metrics, JPanel panel) {
        if (metrics != null) {
            MetricActionListener listener = new MetricActionListener();
            for (int i = 0; i < metrics.length / 2; i++) {
                JRadioButton radio = new JRadioButton(metrics[2 * i]);
                String action = metrics[2 * i];
                if (action.endsWith(defaultMarker)) {
                    action = action.substring(0, action.length() - defaultMarker.length());
                }
                radio.setActionCommand(action);
                radio.setToolTipText(metrics[2 * i + 1]);
                radio.addActionListener(listener);
                buttonGroupMetrics.add(radio);
                panel.add(radio);
            }
        } else {
            panel.add(new JLabel(" None..."));
        }
    }

    private void initMetrics(String metricType) {
        //init params
        String[] primaryMetrics = null;
        String[] additionalMetrics = null;
        if (type.equals(METRIC_CPU)) {
            primaryMetrics = cpuMetricsPrimary;
            additionalMetrics = cpuMetricsAdditional;
        } else if (type.equals(METRIC_MEM)) {
            primaryMetrics = memMetricsPrimary;
            additionalMetrics = memMetricsAdditional;
        } else if (type.equals(METRIC_DISKIO)) {
            primaryMetrics = diskIOMetricsPrimary;
            additionalMetrics = diskIOMetricsAdditional;
        } else if (type.equals(METRIC_NETIO)) {
            primaryMetrics = netIOMetricsPrimary;
            additionalMetrics = netIOMetricsAdditional;
        } else if (type.equals(METRIC_TCP)) {
            primaryMetrics = tcpMetricsPrimary;
            additionalMetrics = tcpMetricsAdditional;
        } else if (type.equals(METRIC_SWAP)) {
            primaryMetrics = swapMetricsPrimary;
            additionalMetrics = null;
        } else if (type.equals(METRIC_JMX)) {
            primaryMetrics = jmxMetricsPrimary;
            additionalMetrics = null;
        }

        //show/hide relevent panels
        if (rules.containsKey(metricType)) {
            //handle metric layouts, 2 collumn if one panel only
            if ((rules.get(metricType) & OPTION_PRIMARY_METRIC) != 0
                    && (rules.get(metricType) & OPTION_ADDITIONAL_METRIC) == 0) {
                jPanelPrimaryMetrics.setLayout(new java.awt.GridLayout(0, 2));
            } else {
                jPanelPrimaryMetrics.setLayout(new java.awt.GridLayout(0, 1));
            }

            jPanelScope.setVisible((rules.get(metricType) & OPTION_PROCESS_SCOPE) != 0);
            jPanelPID.setVisible((rules.get(metricType) & OPTION_PROCESS_SCOPE) != 0);
            if ((rules.get(metricType) & OPTION_PRIMARY_METRIC) != 0) {
                fillMetrics(primaryMetrics, jPanelPrimaryMetrics);
            } else {
                jPanelPrimaryMetrics.setVisible(false);
            }
            if ((rules.get(metricType) & OPTION_ADDITIONAL_METRIC) != 0) {
                fillMetrics(additionalMetrics, jPanelAdditionaMetrics);
            } else {
                jPanelAdditionaMetrics.setVisible(false);
            }
            jPanelCustomCommand.setVisible((rules.get(metricType) & OPTION_EXEC) != 0);
            jPanelCpuCore.setVisible((rules.get(metricType) & OPTION_CPU_CORE_SCOPE) != 0);
            jPanelTailCommand.setVisible((rules.get(metricType) & OPTION_TAIL) != 0);
            jPanelFileSystem.setVisible((rules.get(metricType) & OPTION_FILESYSTEM_SCOPE) != 0);
            jPanelNetInterface.setVisible((rules.get(metricType) & OPTION_NET_INTERFACE_SCOPE) != 0);
            jPanelJmxParams.setVisible((rules.get(metricType) & OPTION_JMX) != 0);
            jPanelUnit.setVisible((rules.get(metricType) & OPTION_UNIT) != 0);
        }
    }

    private String getProcessScopeString() {
        String ret = "";
        if (jRadioScopePerProcess.isSelected()) {
            if (buttonGroupPID.getSelection() != null) {
                String tmp = buttonGroupPID.getSelection().getActionCommand();
                if ("pid".equals(tmp)) {
                    ret += "pid=" + jTextFieldPID.getText().trim();
                } else if ("name".equals(tmp)) {
                    String name = jTextFieldPorcessName.getText().trim();
                    if (name.length() == 0) {
                        name = "unknown";
                    }
                    ret += "name=" + name + "#" + jTextFieldOccurence.getText().trim();
                } else if ("ptql".equals(tmp)) {
                    String query = jTextFieldPtql.getText().trim();
                    if (query.length() == 0) {
                        query = "query";
                    }
                    ret += "ptql=" + query;
                }
            } else {
                ret += "pid=0";
            }
        }
        return ret;
    }

    private String getJmxParamsString() {
        StringBuilder ret = new StringBuilder("");

        String host = jTextFieldJmxHost.getText().trim();
        String port = jTextFieldJmxPort.getText().trim();

        if (!port.isEmpty() && host.isEmpty()) {
            host = "localhost";
        }

        String user = jTextFieldJmxUser.getText().trim();
        String password = jTextFieldJmxPassword.getText().trim();

        if (host.length() > 0) {
            String url = "url=" + host;
            if (!port.isEmpty()) {
                url = url + "\\:" + port;
            }
            addStringItem(ret, url);
        }

        if (user.length() != 0 && password.length() != 0) {
            addStringItem(ret, "user=" + user);
            addStringItem(ret, "password=" + password);
        }

        return ret.toString();
    }

    private void addStringItem(StringBuilder buf, String item) {
        if (item != null && item.length() > 0) {
            if (buf.length() > 0) buf.append(separator);
            buf.append(item);
        }
    }

    private String getParamsString() {
        StringBuilder ret = new StringBuilder("");
        String tmp;

        //special preprocessing
        if (type.equals(METRIC_CPU)) {
            if (buttonGroupCpuCores.getSelection() != null) {
                tmp = buttonGroupCpuCores.getSelection().getActionCommand();
                if ("index".equals(tmp)) {
                    addStringItem(ret, "core=" + jTextFieldCoreIndex.getText().trim());
                }
            }
            addStringItem(ret, getProcessScopeString());
        } else if (type.equals(METRIC_MEM)) {
            addStringItem(ret, getProcessScopeString());
        } else if (type.equals(METRIC_NETIO)) {
            tmp = jTextFieldNetInterface.getText();
            if (tmp.trim().length() > 0) {
                addStringItem(ret, "iface=" + tmp.trim());
            }
        } else if (type.equals(METRIC_TCP)) {
        } else if (type.equals(METRIC_SWAP)) {
        } else if (type.equals(METRIC_JMX)) {
            addStringItem(ret, getJmxParamsString());
        }

        //add the metric label
        tmp = jTextFieldMetricLabel.getText();
        if (tmp.trim().length() > 0) {
            addStringItem(ret, "label=" + tmp.trim());
        }

        //add the metric unit
        if (buttonGroupMetrics.getSelection() != null) {
            if (isUnitRelevent(buttonGroupMetrics.getSelection().getActionCommand())) {
                String unit = getUnit();
                if (!"b".equalsIgnoreCase(unit)) {
                    addStringItem(ret, "unit=" + getUnit());
                }
            }
        }

        if (type.equals(METRIC_EXEC)) {
            addStringItem(ret, jTextFieldExec.getText().trim());
        } else if (type.equals(METRIC_TAIL)) {
            addStringItem(ret, jTextFieldTail.getText().trim());
        }

        //add the metric
        if (buttonGroupMetrics.getSelection() != null) {
            addStringItem(ret, buttonGroupMetrics.getSelection().getActionCommand());
        }

        //add filesystem at the end to avoid issue with win filesystem ending with \ which
        //would produce "\:" otherwise

        if (type.equals(METRIC_DISKIO)) {
            tmp = jTextFieldFileSystem.getText();
            if (tmp.trim().length() > 0) {
                addStringItem(ret, "fs=" + tmp.trim());
            }
        }

        return ret.toString();
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroupPID = new javax.swing.ButtonGroup();
        buttonGroupMetrics = new javax.swing.ButtonGroup();
        buttonGroupCpuCores = new javax.swing.ButtonGroup();
        buttonGroupScope = new javax.swing.ButtonGroup();
        jPanelPID = new javax.swing.JPanel();
        jRadioPID = new javax.swing.JRadioButton();
        jTextFieldPID = new javax.swing.JTextField();
        jRadioProcessName = new javax.swing.JRadioButton();
        jTextFieldPorcessName = new javax.swing.JTextField();
        jLabelOccurence = new javax.swing.JLabel();
        jTextFieldOccurence = new javax.swing.JTextField();
        jRadioPtql = new javax.swing.JRadioButton();
        jTextFieldPtql = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabelPtqlHelp = new javax.swing.JLabel();
        jPanelButtons = new javax.swing.JPanel();
        jButtonApply = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jPanelPrimaryMetrics = new javax.swing.JPanel();
        jPanelAdditionaMetrics = new javax.swing.JPanel();
        jPanelCustomCommand = new javax.swing.JPanel();
        jLabelExec = new javax.swing.JLabel();
        jTextFieldExec = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaExecHelp = new javax.swing.JTextArea();
        jPanelCpuCore = new javax.swing.JPanel();
        jRadioCpuAllCores = new javax.swing.JRadioButton();
        jRadioCustomCpuCore = new javax.swing.JRadioButton();
        jTextFieldCoreIndex = new javax.swing.JTextField();
        jPanelScope = new javax.swing.JPanel();
        jRadioScopeAll = new javax.swing.JRadioButton();
        jRadioScopePerProcess = new javax.swing.JRadioButton();
        jPanelTailCommand = new javax.swing.JPanel();
        jLabelTail = new javax.swing.JLabel();
        jTextFieldTail = new javax.swing.JTextField();
        jPanelFileSystem = new javax.swing.JPanel();
        jLabelFileSystem = new javax.swing.JLabel();
        jTextFieldFileSystem = new javax.swing.JTextField();
        jPanelNetInterface = new javax.swing.JPanel();
        jLabelNetInterface = new javax.swing.JLabel();
        jTextFieldNetInterface = new javax.swing.JTextField();
        jPanelMetricLabel = new javax.swing.JPanel();
        jLabelMetricLabel = new javax.swing.JLabel();
        jTextFieldMetricLabel = new javax.swing.JTextField();
        jPanelStretch = new javax.swing.JPanel();
        jPanelJmxParams = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldJmxHost = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldJmxPort = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldJmxUser = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldJmxPassword = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanelUnit = new javax.swing.JPanel();
        jLabelUnit = new javax.swing.JLabel();
        jComboBoxUnit = new javax.swing.JComboBox();

        setLayout(new java.awt.GridBagLayout());

        jPanelPID.setBorder(javax.swing.BorderFactory.createTitledBorder("Process Identification"));
        jPanelPID.setLayout(new java.awt.GridBagLayout());

        buttonGroupPID.add(jRadioPID);
        jRadioPID.setText("Process ID");
        jRadioPID.setActionCommand("pid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelPID.add(jRadioPID, gridBagConstraints);

        jTextFieldPID.setMinimumSize(new java.awt.Dimension(60, 20));
        jTextFieldPID.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanelPID.add(jTextFieldPID, gridBagConstraints);

        buttonGroupPID.add(jRadioProcessName);
        jRadioProcessName.setText("Process Name");
        jRadioProcessName.setActionCommand("name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelPID.add(jRadioProcessName, gridBagConstraints);

        jTextFieldPorcessName.setMinimumSize(new java.awt.Dimension(60, 20));
        jTextFieldPorcessName.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        jPanelPID.add(jTextFieldPorcessName, gridBagConstraints);

        jLabelOccurence.setText(", occurence: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        jPanelPID.add(jLabelOccurence, gridBagConstraints);

        jTextFieldOccurence.setMinimumSize(new java.awt.Dimension(60, 20));
        jTextFieldOccurence.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanelPID.add(jTextFieldOccurence, gridBagConstraints);

        buttonGroupPID.add(jRadioPtql);
        jRadioPtql.setText("PTQL Query");
        jRadioPtql.setActionCommand("ptql");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelPID.add(jRadioPtql, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelPID.add(jTextFieldPtql, gridBagConstraints);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kg/apc/jmeter/vizualizers/information.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelPID.add(jLabel2, gridBagConstraints);

        jLabelPtqlHelp.setText("Help");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        jPanelPID.add(jLabelPtqlHelp, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(jPanelPID, gridBagConstraints);

        jButtonApply.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kg/apc/jmeter/vizualizers/tick.png"))); // NOI18N
        jButtonApply.setText("Apply");
        jButtonApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonApplyActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonApply);

        jButtonCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kg/apc/jmeter/vizualizers/cross.png"))); // NOI18N
        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonCancel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 0);
        add(jPanelButtons, gridBagConstraints);

        jPanelPrimaryMetrics.setBorder(javax.swing.BorderFactory.createTitledBorder("Primary Metrics"));
        jPanelPrimaryMetrics.setLayout(new java.awt.GridLayout(0, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(jPanelPrimaryMetrics, gridBagConstraints);

        jPanelAdditionaMetrics.setBorder(javax.swing.BorderFactory.createTitledBorder("Additional Metrics"));
        jPanelAdditionaMetrics.setLayout(new java.awt.GridLayout(0, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(jPanelAdditionaMetrics, gridBagConstraints);

        jPanelCustomCommand.setBorder(javax.swing.BorderFactory.createTitledBorder("Custom Exec Command"));
        jPanelCustomCommand.setLayout(new java.awt.GridBagLayout());

        jLabelExec.setText("Command to run:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelCustomCommand.add(jLabelExec, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        jPanelCustomCommand.add(jTextFieldExec, gridBagConstraints);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kg/apc/jmeter/vizualizers/information.png"))); // NOI18N
        jLabel1.setText("Quick help:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        jPanelCustomCommand.add(jLabel1, gridBagConstraints);

        jTextAreaExecHelp.setColumns(20);
        jTextAreaExecHelp.setEditable(false);
        jTextAreaExecHelp.setLineWrap(true);
        jTextAreaExecHelp.setRows(15);
        jTextAreaExecHelp.setText("This metric type interprets parameter string as path to process to start and arguments to pass to the process. Parameters separated with colon.\nThe process must print out to standard output single line containing single numeric metric value.\n\nExample1: Monitoring Linux cached memory size, used free utility output:\n/bin/sh:-c:free | grep Mem | awk '{print $7}'\n\nExample2: Monitoring MySQL select query count:\n/bin/sh:-c:echo \"show global status like 'Com_select'\" | mysql -u root | awk '$1 ==\"Com_select\" {print $2}'");
        jTextAreaExecHelp.setWrapStyleWord(true);
        jTextAreaExecHelp.setOpaque(false);
        jScrollPane1.setViewportView(jTextAreaExecHelp);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanelCustomCommand.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanelCustomCommand, gridBagConstraints);

        jPanelCpuCore.setBorder(javax.swing.BorderFactory.createTitledBorder("CPU Cores"));
        jPanelCpuCore.setLayout(new java.awt.GridBagLayout());

        buttonGroupCpuCores.add(jRadioCpuAllCores);
        jRadioCpuAllCores.setSelected(true);
        jRadioCpuAllCores.setText("All Cores (default)");
        jRadioCpuAllCores.setActionCommand("all");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelCpuCore.add(jRadioCpuAllCores, gridBagConstraints);

        buttonGroupCpuCores.add(jRadioCustomCpuCore);
        jRadioCustomCpuCore.setText("Custom CPU Index (0 based)");
        jRadioCustomCpuCore.setActionCommand("index");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelCpuCore.add(jRadioCustomCpuCore, gridBagConstraints);

        jTextFieldCoreIndex.setMinimumSize(new java.awt.Dimension(60, 20));
        jTextFieldCoreIndex.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanelCpuCore.add(jTextFieldCoreIndex, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(jPanelCpuCore, gridBagConstraints);

        jPanelScope.setBorder(javax.swing.BorderFactory.createTitledBorder("Scope"));
        jPanelScope.setLayout(new java.awt.GridLayout(0, 1));

        buttonGroupScope.add(jRadioScopeAll);
        jRadioScopeAll.setSelected(true);
        jRadioScopeAll.setText("All");
        jRadioScopeAll.setActionCommand("all");
        jRadioScopeAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioScopeAllActionPerformed(evt);
            }
        });
        jPanelScope.add(jRadioScopeAll);

        buttonGroupScope.add(jRadioScopePerProcess);
        jRadioScopePerProcess.setText("Per Process");
        jRadioScopePerProcess.setActionCommand("process");
        jRadioScopePerProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioScopePerProcessActionPerformed(evt);
            }
        });
        jPanelScope.add(jRadioScopePerProcess);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(jPanelScope, gridBagConstraints);

        jPanelTailCommand.setBorder(javax.swing.BorderFactory.createTitledBorder("Custom Tail Command"));
        jPanelTailCommand.setLayout(new java.awt.GridBagLayout());

        jLabelTail.setText("Path of the file to tail:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelTailCommand.add(jLabelTail, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        jPanelTailCommand.add(jTextFieldTail, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(jPanelTailCommand, gridBagConstraints);

        jPanelFileSystem.setBorder(javax.swing.BorderFactory.createTitledBorder("Filesystem Filter"));
        jPanelFileSystem.setLayout(new java.awt.GridBagLayout());

        jLabelFileSystem.setText("Filesystem to monitor (if empty: all), eg \"C\\:\\\" or \"/home\":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelFileSystem.add(jLabelFileSystem, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        jPanelFileSystem.add(jTextFieldFileSystem, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(jPanelFileSystem, gridBagConstraints);

        jPanelNetInterface.setBorder(javax.swing.BorderFactory.createTitledBorder("Network Interface Filter"));
        jPanelNetInterface.setLayout(new java.awt.GridBagLayout());

        jLabelNetInterface.setText("Network interface to monitor (if empty: all), eg \"eth0\":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNetInterface.add(jLabelNetInterface, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        jPanelNetInterface.add(jTextFieldNetInterface, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(jPanelNetInterface, gridBagConstraints);

        jPanelMetricLabel.setBorder(javax.swing.BorderFactory.createTitledBorder("Metric Label"));
        jPanelMetricLabel.setLayout(new java.awt.GridBagLayout());

        jLabelMetricLabel.setText("Chart label name (if empty, will be 'Metric parameter' value):");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelMetricLabel.add(jLabelMetricLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        jPanelMetricLabel.add(jTextFieldMetricLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(jPanelMetricLabel, gridBagConstraints);

        jPanelStretch.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanelStretch.setPreferredSize(new java.awt.Dimension(0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        add(jPanelStretch, gridBagConstraints);

        jPanelJmxParams.setBorder(javax.swing.BorderFactory.createTitledBorder("JMX Connection Parameters"));
        jPanelJmxParams.setLayout(new java.awt.GridBagLayout());

        jLabel3.setText("Host:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanelJmxParams.add(jLabel3, gridBagConstraints);

        jTextFieldJmxHost.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelJmxParams.add(jTextFieldJmxHost, gridBagConstraints);

        jLabel4.setText("Port:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanelJmxParams.add(jLabel4, gridBagConstraints);

        jTextFieldJmxPort.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelJmxParams.add(jTextFieldJmxPort, gridBagConstraints);

        jLabel5.setText("User:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanelJmxParams.add(jLabel5, gridBagConstraints);

        jTextFieldJmxUser.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelJmxParams.add(jTextFieldJmxUser, gridBagConstraints);

        jLabel6.setText("Password:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanelJmxParams.add(jLabel6, gridBagConstraints);

        jTextFieldJmxPassword.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelJmxParams.add(jTextFieldJmxPassword, gridBagConstraints);

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kg/apc/jmeter/vizualizers/information.png"))); // NOI18N
        jLabel7.setText("If no host/port specified, local to agent on port 4711");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 2, 0);
        jPanelJmxParams.add(jLabel7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(jPanelJmxParams, gridBagConstraints);

        jPanelUnit.setBorder(javax.swing.BorderFactory.createTitledBorder("Metric Unit"));
        jPanelUnit.setLayout(new java.awt.GridBagLayout());

        jLabelUnit.setText("Retrieve metric from agent in:");
        jLabelUnit.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        jPanelUnit.add(jLabelUnit, gridBagConstraints);

        jComboBoxUnit.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Bytes (b)", "Kilobytes (Kb)", "Megabytes (Mb)"}));
        jComboBoxUnit.setEnabled(false);
        jComboBoxUnit.setMaximumSize(new java.awt.Dimension(32767, 20));
        jComboBoxUnit.setPreferredSize(new java.awt.Dimension(130, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        jPanelUnit.add(jComboBoxUnit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(jPanelUnit, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        getAssociatedDialog().dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonApplyActionPerformed
        parent.setText(getParamsString());
        getAssociatedDialog().dispose();
    }//GEN-LAST:event_jButtonApplyActionPerformed

    private void showProcessScopePanels() {
        if (rules.containsKey(type) && (rules.get(type) & OPTION_PROCESS_SCOPE) != 0) {
            jPanelPID.setVisible(jRadioScopePerProcess.isSelected());
            jPanelPrimaryMetrics.removeAll();
            jPanelAdditionaMetrics.removeAll();
            Enumeration<AbstractButton> enu = buttonGroupMetrics.getElements();
            while (enu.hasMoreElements()) {
                buttonGroupMetrics.remove(enu.nextElement());
            }
            String[] primaryMetrics = null;
            String[] additionalMetrics = null;

            if (jRadioScopePerProcess.isSelected()) {
                if (type.equals(METRIC_CPU)) {
                    primaryMetrics = cpuProcessMetricsPrimary;
                } else if (type.equals(METRIC_MEM)) {
                    primaryMetrics = memProcessMetricsPrimary;
                    additionalMetrics = memProcessMetricsAdditional;
                }
            } else {
                if (type.equals(METRIC_CPU)) {
                    primaryMetrics = cpuMetricsPrimary;
                    additionalMetrics = cpuMetricsAdditional;
                } else if (type.equals(METRIC_MEM)) {
                    primaryMetrics = memMetricsPrimary;
                    additionalMetrics = memMetricsAdditional;
                }
            }

            fillMetrics(primaryMetrics, jPanelPrimaryMetrics);
            fillMetrics(additionalMetrics, jPanelAdditionaMetrics);

            jLabelUnit.setEnabled(false);
            jComboBoxUnit.setEnabled(false);
        }
        repack();
    }

    private void jRadioScopeAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioScopeAllActionPerformed
        showProcessScopePanels();
    }//GEN-LAST:event_jRadioScopeAllActionPerformed

    private void jRadioScopePerProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioScopePerProcessActionPerformed
        showProcessScopePanels();
    }//GEN-LAST:event_jRadioScopePerProcessActionPerformed

    private class MetricActionListener implements java.awt.event.ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JPerfmonParamsPanel.this.enableUnit(e.getActionCommand());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupCpuCores;
    private javax.swing.ButtonGroup buttonGroupMetrics;
    private javax.swing.ButtonGroup buttonGroupPID;
    private javax.swing.ButtonGroup buttonGroupScope;
    private javax.swing.JButton jButtonApply;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JComboBox jComboBoxUnit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabelExec;
    private javax.swing.JLabel jLabelFileSystem;
    private javax.swing.JLabel jLabelMetricLabel;
    private javax.swing.JLabel jLabelNetInterface;
    private javax.swing.JLabel jLabelOccurence;
    private javax.swing.JLabel jLabelPtqlHelp;
    private javax.swing.JLabel jLabelTail;
    private javax.swing.JLabel jLabelUnit;
    private javax.swing.JPanel jPanelAdditionaMetrics;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelCpuCore;
    private javax.swing.JPanel jPanelCustomCommand;
    private javax.swing.JPanel jPanelFileSystem;
    private javax.swing.JPanel jPanelJmxParams;
    private javax.swing.JPanel jPanelMetricLabel;
    private javax.swing.JPanel jPanelNetInterface;
    private javax.swing.JPanel jPanelPID;
    private javax.swing.JPanel jPanelPrimaryMetrics;
    private javax.swing.JPanel jPanelScope;
    private javax.swing.JPanel jPanelStretch;
    private javax.swing.JPanel jPanelTailCommand;
    private javax.swing.JPanel jPanelUnit;
    private javax.swing.JRadioButton jRadioCpuAllCores;
    private javax.swing.JRadioButton jRadioCustomCpuCore;
    private javax.swing.JRadioButton jRadioPID;
    private javax.swing.JRadioButton jRadioProcessName;
    private javax.swing.JRadioButton jRadioPtql;
    private javax.swing.JRadioButton jRadioScopeAll;
    private javax.swing.JRadioButton jRadioScopePerProcess;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaExecHelp;
    private javax.swing.JTextField jTextFieldCoreIndex;
    private javax.swing.JTextField jTextFieldExec;
    private javax.swing.JTextField jTextFieldFileSystem;
    private javax.swing.JTextField jTextFieldJmxHost;
    private javax.swing.JTextField jTextFieldJmxPassword;
    private javax.swing.JTextField jTextFieldJmxPort;
    private javax.swing.JTextField jTextFieldJmxUser;
    private javax.swing.JTextField jTextFieldMetricLabel;
    private javax.swing.JTextField jTextFieldNetInterface;
    private javax.swing.JTextField jTextFieldOccurence;
    private javax.swing.JTextField jTextFieldPID;
    private javax.swing.JTextField jTextFieldPorcessName;
    private javax.swing.JTextField jTextFieldPtql;
    private javax.swing.JTextField jTextFieldTail;
    // End of variables declaration//GEN-END:variables
}
