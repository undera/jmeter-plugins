package kg.apc.jmeter.vizualizers;

import java.awt.Frame;
import java.util.Enumeration;
import java.util.HashMap;
import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author Stephane Hoblingre
 */
public class JPerfmonParamsDialog extends javax.swing.JDialog {

    private final static int OPTION_PRIMARY_METRIC = 1;
    private final static int OPTION_ADDITIONAL_METRIC = 1 << 1;
    private final static int OPTION_PROCESS_SCOPE = 1 << 2;
    private final static int OPTION_CPU_CORE_SCOPE = 1 << 3;
    private final static int OPTION_FILESYSTEM_SCOPE = 1 << 4;
    private final static int OPTION_NET_INTERFACE_SCOPE = 1 << 5;
    private final static int OPTION_EXEC = 1 << 6;
    private final static int OPTION_TAIL = 1 << 7;

    private JTextField parent = null;
    private String type = null;

    private final static int minWidth = 350;
    private final static String defaultMarker = " (default)";

    private HashMap<String,Integer> rules = new HashMap<String, Integer>();
    
    private static String[] cpuMetricsPrimary = {
       "combined" + defaultMarker, "Get the combined CPU usage, in percent (%)",
       "idle", "Get the idle CPU usage, in percent (%)",
       "system", "Get the system CPU usage, in percent (%)",
       "user", "Get the user CPU usage, in percent (%)",
       "iowait", "Get the iowait CPU usage, in percent (%)"
    };

    private static String[] cpuProcessMetricsPrimary = {
       "percent" + defaultMarker, "Get the process combined CPU usage, in percent (%)",
       "total", "Get the process total CPU usage, in percent (%)",
       "system", "Get the system CPU usage, in percent (%)",
       "user", "Get the user CPU usage, in percent (%)"
    };

    private static String[] cpuMetricsAdditional = {
       "irq", "Get the irq CPU usage, in percent (%)",
       "nice", "Get the nice CPU usage, in percent (%)",
       "softirq", "Get the softirq CPU usage, in percent (%)",
       "stolen", "Get the stolen CPU usage, in percent (%)",
    };

    /** Creates new form JPerfmonParamsDialog */
    public JPerfmonParamsDialog(Frame owner, String type, boolean modal, JTextField parentField) {
        super(owner, "Perfmon helper", modal);
        this.parent = parentField;
        this.type = type;
        initRules();
        this.setTitle("Perfmon [" + type + "] parameters helper");
        initComponents();
        initMetrics(type);
        this.pack();
        //avoid small dialogs
        if(this.getWidth() < minWidth) this.setSize(minWidth, this.getHeight());
        showProcessScopePanels();
    }

    private void initRules() {
        rules.put("CPU", OPTION_PRIMARY_METRIC | OPTION_ADDITIONAL_METRIC | OPTION_PROCESS_SCOPE | OPTION_CPU_CORE_SCOPE);
        rules.put("Memory", OPTION_PRIMARY_METRIC | OPTION_ADDITIONAL_METRIC | OPTION_PROCESS_SCOPE);
        rules.put("Swap", OPTION_PRIMARY_METRIC | OPTION_ADDITIONAL_METRIC);
        rules.put("Disks I/O", OPTION_PRIMARY_METRIC | OPTION_ADDITIONAL_METRIC | OPTION_FILESYSTEM_SCOPE);
        rules.put("Network I/O", OPTION_PRIMARY_METRIC | OPTION_ADDITIONAL_METRIC | OPTION_NET_INTERFACE_SCOPE);
        rules.put("TCP", OPTION_PRIMARY_METRIC | OPTION_ADDITIONAL_METRIC);
        rules.put("EXEC", OPTION_EXEC);
        rules.put("TAIL", OPTION_TAIL);
    }

    private void fillMetrics(String[] metrics, JPanel panel) {
        for(int i=0; i<metrics.length/2; i++) {
            JRadioButton radio = new JRadioButton(metrics[2*i]);
            String action = metrics[2*i];
            if(action.endsWith(defaultMarker)) {
                action = action.substring(0, action.length() - defaultMarker.length());
            }
            radio.setActionCommand(action);
            radio.setToolTipText(metrics[2*i+1]);
            buttonGroupMetrics.add(radio);
            panel.add(radio);
        }
    }

    private void initMetrics(String metricType) {
        //init params
        String[] primaryMetrics = cpuMetricsPrimary;
        String[] additionalMetrics = cpuMetricsAdditional;

        //show/hide relevent panels
        if(rules.containsKey(metricType)) {
            jPanelScope.setVisible((rules.get(metricType) & OPTION_PROCESS_SCOPE) != 0);
            jPanelPID.setVisible((rules.get(metricType) & OPTION_PROCESS_SCOPE) != 0);
            if((rules.get(metricType) & OPTION_PRIMARY_METRIC) != 0) {
                fillMetrics(primaryMetrics, jPanelPrimaryMetrics);
            } else {
                jPanelPrimaryMetrics.setVisible(false);
            }
            if((rules.get(metricType) & OPTION_ADDITIONAL_METRIC) != 0) {
                fillMetrics(additionalMetrics, jPanelAdditionaMetrics);
            } else {
                jPanelAdditionaMetrics.setVisible(false);
            }
            jPanelCustomCommand.setVisible((rules.get(metricType) & OPTION_EXEC) != 0);
            jPanelCpuCore.setVisible((rules.get(metricType) & OPTION_CPU_CORE_SCOPE) != 0);
        }
    }

    private String getParamsString() {
        String ret = "";
        if(buttonGroupMetrics.getSelection() != null) {
            ret = buttonGroupMetrics.getSelection().getActionCommand();
        }
        return ret;
    }

    /** This method is called from within the constructor to
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
        jPanelButtons = new javax.swing.JPanel();
        jButtonApply = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jPanelPrimaryMetrics = new javax.swing.JPanel();
        jPanelAdditionaMetrics = new javax.swing.JPanel();
        jPanelCustomCommand = new javax.swing.JPanel();
        jLabelExec = new javax.swing.JLabel();
        jTextFieldExec = new javax.swing.JTextField();
        jPanelCpuCore = new javax.swing.JPanel();
        jRadioCpuAllCores = new javax.swing.JRadioButton();
        jRadioCustomCpuCore = new javax.swing.JRadioButton();
        jTextFieldProcessOccurence = new javax.swing.JTextField();
        jPanelScope = new javax.swing.JPanel();
        jRadioScopeAll = new javax.swing.JRadioButton();
        jRadioScopePerProcess = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanelPID.setBorder(javax.swing.BorderFactory.createTitledBorder("Processes Scope"));
        jPanelPID.setLayout(new java.awt.GridBagLayout());

        buttonGroupPID.add(jRadioPID);
        jRadioPID.setText("Process ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelPID.add(jRadioPID, gridBagConstraints);

        jTextFieldPID.setText("jTextField1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanelPID.add(jTextFieldPID, gridBagConstraints);

        buttonGroupPID.add(jRadioProcessName);
        jRadioProcessName.setText("Process Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelPID.add(jRadioProcessName, gridBagConstraints);

        jTextFieldPorcessName.setText("jTextField1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        jPanelPID.add(jTextFieldPorcessName, gridBagConstraints);

        jLabelOccurence.setText(", occurence: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        jPanelPID.add(jLabelOccurence, gridBagConstraints);

        jTextFieldOccurence.setText("jTextField1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanelPID.add(jTextFieldOccurence, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jPanelPID, gridBagConstraints);

        jButtonApply.setText("Apply");
        jButtonApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonApplyActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonApply);

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonCancel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        getContentPane().add(jPanelButtons, gridBagConstraints);

        jPanelPrimaryMetrics.setBorder(javax.swing.BorderFactory.createTitledBorder("Primary Metrics"));
        jPanelPrimaryMetrics.setLayout(new java.awt.GridLayout(0, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanelPrimaryMetrics, gridBagConstraints);

        jPanelAdditionaMetrics.setBorder(javax.swing.BorderFactory.createTitledBorder("Additional Metrics"));
        jPanelAdditionaMetrics.setLayout(new java.awt.GridLayout(0, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanelAdditionaMetrics, gridBagConstraints);

        jPanelCustomCommand.setBorder(javax.swing.BorderFactory.createTitledBorder("Custom Exec Command"));
        jPanelCustomCommand.setLayout(new java.awt.GridLayout(2, 1));

        jLabelExec.setText("Enter your custom command here as in a prompt:");
        jPanelCustomCommand.add(jLabelExec);

        jTextFieldExec.setText("jTextField1");
        jPanelCustomCommand.add(jTextFieldExec);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanelCustomCommand, gridBagConstraints);

        jPanelCpuCore.setBorder(javax.swing.BorderFactory.createTitledBorder("CPU Cores"));
        jPanelCpuCore.setLayout(new java.awt.GridBagLayout());

        buttonGroupCpuCores.add(jRadioCpuAllCores);
        jRadioCpuAllCores.setText("All Cores");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelCpuCore.add(jRadioCpuAllCores, gridBagConstraints);

        buttonGroupCpuCores.add(jRadioCustomCpuCore);
        jRadioCustomCpuCore.setText("Custom CPU Index (0 based)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelCpuCore.add(jRadioCustomCpuCore, gridBagConstraints);

        jTextFieldProcessOccurence.setText("jTextField2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanelCpuCore.add(jTextFieldProcessOccurence, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jPanelCpuCore, gridBagConstraints);

        jPanelScope.setBorder(javax.swing.BorderFactory.createTitledBorder("Scope"));

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
        getContentPane().add(jPanelScope, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonApplyActionPerformed
        parent.setText(getParamsString());
        dispose();
    }//GEN-LAST:event_jButtonApplyActionPerformed

    private void showProcessScopePanels() {
        jPanelPID.setVisible(jRadioScopePerProcess.isSelected());
        if(type.equals("CPU")) {
            jPanelPrimaryMetrics.removeAll();
            jPanelAdditionaMetrics.removeAll();
            Enumeration<AbstractButton> enu = buttonGroupMetrics.getElements();
            while(enu.hasMoreElements()) {
                buttonGroupMetrics.remove(enu.nextElement());
            }
            if(jRadioScopePerProcess.isSelected()) {
                fillMetrics(cpuProcessMetricsPrimary, jPanelPrimaryMetrics);
            } else {
                fillMetrics(cpuMetricsPrimary, jPanelPrimaryMetrics);
                fillMetrics(cpuMetricsAdditional, jPanelAdditionaMetrics);
            }
        }
    }

    private void jRadioScopeAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioScopeAllActionPerformed
        showProcessScopePanels();
    }//GEN-LAST:event_jRadioScopeAllActionPerformed

    private void jRadioScopePerProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioScopePerProcessActionPerformed
        showProcessScopePanels();
    }//GEN-LAST:event_jRadioScopePerProcessActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupCpuCores;
    private javax.swing.ButtonGroup buttonGroupMetrics;
    private javax.swing.ButtonGroup buttonGroupPID;
    private javax.swing.ButtonGroup buttonGroupScope;
    private javax.swing.JButton jButtonApply;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JLabel jLabelExec;
    private javax.swing.JLabel jLabelOccurence;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelAdditionaMetrics;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelCpuCore;
    private javax.swing.JPanel jPanelCustomCommand;
    private javax.swing.JPanel jPanelPID;
    private javax.swing.JPanel jPanelPrimaryMetrics;
    private javax.swing.JPanel jPanelScope;
    private javax.swing.JRadioButton jRadioCpuAllCores;
    private javax.swing.JRadioButton jRadioCustomCpuCore;
    private javax.swing.JRadioButton jRadioPID;
    private javax.swing.JRadioButton jRadioProcessName;
    private javax.swing.JRadioButton jRadioScopeAll;
    private javax.swing.JRadioButton jRadioScopePerProcess;
    private javax.swing.JTextField jTextFieldExec;
    private javax.swing.JTextField jTextFieldOccurence;
    private javax.swing.JTextField jTextFieldPID;
    private javax.swing.JTextField jTextFieldPorcessName;
    private javax.swing.JTextField jTextFieldProcessOccurence;
    // End of variables declaration//GEN-END:variables

}
