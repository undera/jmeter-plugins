package kg.apc.jmeter.reporters;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class JAutoStopPanel extends javax.swing.JPanel {

    /** Creates new form JAutoStopPanel */
    public JAutoStopPanel() {
        initComponents();
        registerJTextfieldForValidation(jTextFieldError, true);
        registerJTextfieldForValidation(jTextFieldErrorSec, false);
        registerJTextfieldForValidation(jTextFieldRespLatency, false);
        registerJTextfieldForValidation(jTextFieldRespLatencySec, false);
        registerJTextfieldForValidation(jTextFieldRespTime, false);
        registerJTextfieldForValidation(jTextFieldRespTimeSec, false);
        registerJTextfieldForValidation(jTextFieldPercentileRespTime, false);
        registerJTextfieldForValidation(jTextFieldPercentileRespTimeSec, false);
        registerJTextfieldForValidation(jTextFieldPercentileValue, false);
        registerJTextfieldForValidation(jTextFieldCustomDuration, false);
        initExtraComponents();
        initFields();
    }

    private boolean isConfigured(String value) {
        return value != null && !value.trim().isEmpty() && !value.equals("0");
    }

    public void configure(AutoStop testElement) {
        boolean hasRespTime = isConfigured(testElement.getResponseTime());
        jCheckBoxRespTime.setSelected(hasRespTime);
        jTextFieldRespTime.setText(hasRespTime ? testElement.getResponseTime() : "10000");
        jTextFieldRespTimeSec.setText(isConfigured(testElement.getResponseTimeSecs()) ? testElement.getResponseTimeSecs() : "10");

        boolean hasLatency = isConfigured(testElement.getResponseLatency());
        jCheckBoxLatency.setSelected(hasLatency);
        jTextFieldRespLatency.setText(hasLatency ? testElement.getResponseLatency() : "5000");
        jTextFieldRespLatencySec.setText(isConfigured(testElement.getResponseLatencySecs()) ? testElement.getResponseLatencySecs() : "10");

        boolean hasError = isConfigured(testElement.getErrorRate());
        jCheckBoxError.setSelected(hasError);
        jTextFieldError.setText(hasError ? testElement.getErrorRate() : "50");
        jTextFieldErrorSec.setText(isConfigured(testElement.getErrorRateSecs()) ? testElement.getErrorRateSecs() : "10");

        boolean hasPercentile = isConfigured(testElement.getPercentileResponseTime());
        jCheckBoxPercentile.setSelected(hasPercentile);
        jTextFieldPercentileRespTime.setText(hasPercentile ? testElement.getPercentileResponseTime() : "15000");
        jTextFieldPercentileRespTimeSec.setText(isConfigured(testElement.getPercentileResponseTimeSecs()) ? testElement.getPercentileResponseTimeSecs() : "10");
        jTextFieldPercentileValue.setText(isConfigured(testElement.getPercentileValue()) ? testElement.getPercentileValue() : "90");

        boolean hasRelPercentile = isConfigured(testElement.getRelPercentileValue());
        jCheckBoxRelPercentile.setSelected(hasRelPercentile);
        jTextFieldRelPercentileValue.setText(hasRelPercentile ? testElement.getRelPercentileValue() : "95");
        jTextFieldRelWindowSecs.setText(isConfigured(testElement.getRelWindowSecs()) ? testElement.getRelWindowSecs() : "30");
        jTextFieldRelThresholdPct.setText(isConfigured(testElement.getRelThresholdPct()) ? testElement.getRelThresholdPct() : "20");

        boolean hasErrorCount = isConfigured(testElement.getErrorCount());
        jCheckBoxErrorCount.setSelected(hasErrorCount);
        jTextFieldErrorCount.setText(hasErrorCount ? testElement.getErrorCount() : "10");
        jTextFieldErrorCountSec.setText(isConfigured(testElement.getErrorCountSecs()) ? testElement.getErrorCountSecs() : "10");

        jTextFieldCustomDuration.setText(testElement.getCustomValidationDuration());

        updateAllRowStates();
        processBullets();
    }

    public void modifyTestElement(AutoStop testElement) {
        if (jCheckBoxRespTime.isSelected()) {
            testElement.setResponseTime(jTextFieldRespTime.getText());
            testElement.setResponseTimeSecs(jTextFieldRespTimeSec.getText());
        } else {
            testElement.setResponseTime("");
            testElement.setResponseTimeSecs("");
        }

        if (jCheckBoxLatency.isSelected()) {
            testElement.setResponseLatency(jTextFieldRespLatency.getText());
            testElement.setResponseLatencySecs(jTextFieldRespLatencySec.getText());
        } else {
            testElement.setResponseLatency("");
            testElement.setResponseLatencySecs("");
        }

        if (jCheckBoxError.isSelected()) {
            testElement.setErrorRate(jTextFieldError.getText());
            testElement.setErrorRateSecs(jTextFieldErrorSec.getText());
        } else {
            testElement.setErrorRate("");
            testElement.setErrorRateSecs("");
        }

        if (jCheckBoxPercentile.isSelected()) {
            testElement.setPercentileResponseTime(jTextFieldPercentileRespTime.getText());
            testElement.setPercentileResponseTimeSecs(jTextFieldPercentileRespTimeSec.getText());
            testElement.setPercentileValue(jTextFieldPercentileValue.getText());
        } else {
            testElement.setPercentileResponseTime("");
            testElement.setPercentileResponseTimeSecs("");
            testElement.setPercentileValue("");
        }

        if (jCheckBoxRelPercentile.isSelected()) {
            testElement.setRelPercentileValue(jTextFieldRelPercentileValue.getText());
            testElement.setRelWindowSecs(jTextFieldRelWindowSecs.getText());
            testElement.setRelThresholdPct(jTextFieldRelThresholdPct.getText());
        } else {
            testElement.setRelPercentileValue("");
            testElement.setRelWindowSecs("");
            testElement.setRelThresholdPct("");
        }

        if (jCheckBoxErrorCount.isSelected()) {
            testElement.setErrorCount(jTextFieldErrorCount.getText());
            testElement.setErrorCountSecs(jTextFieldErrorCountSec.getText());
        } else {
            testElement.setErrorCount("");
            testElement.setErrorCountSecs("");
        }

        testElement.setCustomValidationDuration(jTextFieldCustomDuration.getText());
    }

    public final void initFields() {
        jCheckBoxRespTime.setSelected(true);
        jTextFieldRespTime.setText("10000");
        jTextFieldRespTimeSec.setText("10");

        jCheckBoxLatency.setSelected(true);
        jTextFieldRespLatency.setText("5000");
        jTextFieldRespLatencySec.setText("10");

        jCheckBoxError.setSelected(true);
        jTextFieldError.setText("50");
        jTextFieldErrorSec.setText("10");

        jCheckBoxPercentile.setSelected(true);
        jTextFieldPercentileRespTime.setText("15000");
        jTextFieldPercentileRespTimeSec.setText("10");
        jTextFieldPercentileValue.setText("90");

        jCheckBoxRelPercentile.setSelected(false);
        jTextFieldRelPercentileValue.setText("95");
        jTextFieldRelWindowSecs.setText("30");
        jTextFieldRelThresholdPct.setText("20");

        jCheckBoxErrorCount.setSelected(false);
        jTextFieldErrorCount.setText("10");
        jTextFieldErrorCountSec.setText("10");

        updateAllRowStates();
        processBullets();
    }

    private void updateAllRowStates() {
        updateRowState(jPanel1, jCheckBoxRespTime);
        updateRowState(jPanel2, jCheckBoxLatency);
        updateRowState(jPanel4, jCheckBoxError);
        updateRowState(jPanel5, jCheckBoxPercentile);
        if (jPanelRelPercentile != null && jCheckBoxRelPercentile != null) {
            updateRowState(jPanelRelPercentile, jCheckBoxRelPercentile);
        }
        if (jPanelErrorCount != null && jCheckBoxErrorCount != null) {
            updateRowState(jPanelErrorCount, jCheckBoxErrorCount);
        }
    }

    private void updateRowState(JPanel panel, JCheckBox cb) {
        boolean selected = cb.isSelected();
        for (Component c : panel.getComponents()) {
            if (c != cb) {
                c.setEnabled(selected);
            }
        }
    }

    /** Adds checkboxes and new condition rows to the layout. */
    private void initExtraComponents() {
        GridBagLayout layout = (GridBagLayout) getLayout();

        // Adjust spacing for existing panels
        jPanel1.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
        jPanel2.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
        jPanel4.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
        jPanel5.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));

        // Add checkboxes to existing rows
        jCheckBoxRespTime = new JCheckBox();
        jCheckBoxRespTime.setToolTipText("Enable Average Response Time condition");
        jPanel1.add(jCheckBoxRespTime, 0);

        jCheckBoxLatency = new JCheckBox();
        jCheckBoxLatency.setToolTipText("Enable Average Latency condition");
        jPanel2.add(jCheckBoxLatency, 0);

        jCheckBoxError = new JCheckBox();
        jCheckBoxError.setToolTipText("Enable Error Rate condition");
        jPanel4.add(jCheckBoxError, 0);

        jCheckBoxPercentile = new JCheckBox();
        jCheckBoxPercentile.setToolTipText("Enable Percentile Response Time condition");
        jPanel5.add(jCheckBoxPercentile, 0);

        // Adjust OR label indentation
        adjustOrLabelInsets(jLabel8);
        adjustOrLabelInsets(jLabel9);
        adjustOrLabelInsets(jLabel13);

        // Remove unused placeholder components to eliminate extra blank rows between criteria
        remove(jLabel20);
        remove(jPanel6);
        remove(jPanel3);

        GridBagConstraints gbc;

        // "OR" separator before relative percentile
        JLabel orLabelRel = new JLabel("OR");
        orLabelRel.setFont(new Font("Tahoma", 0, 10));
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 32, 0, 0);
        add(orLabelRel, gbc);

        // Row: [✓] [•] P [95] th percentile grew more than [20] % in a window of [30] seconds
        jPanelRelPercentile = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));

        jCheckBoxRelPercentile = new JCheckBox();
        jCheckBoxRelPercentile.setToolTipText("Enable Relative Percentile Degradation condition");
        jPanelRelPercentile.add(jCheckBoxRelPercentile);

        jLabelBulletRelPercentile = new JLabel();
        jLabelBulletRelPercentile.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/kg/apc/jmeter/reporters/bulletGreen.png")));
        jPanelRelPercentile.add(jLabelBulletRelPercentile);

        jPanelRelPercentile.add(new JLabel("P"));

        jTextFieldRelPercentileValue = new JTextField(3);
        jTextFieldRelPercentileValue.setHorizontalAlignment(JTextField.RIGHT);
        jTextFieldRelPercentileValue.setToolTipText("Percentile rank, e.g. 95 for P95.");
        jPanelRelPercentile.add(jTextFieldRelPercentileValue);

        jPanelRelPercentile.add(new JLabel("th percentile grew more than"));

        jTextFieldRelThresholdPct = new JTextField(4);
        jTextFieldRelThresholdPct.setHorizontalAlignment(JTextField.RIGHT);
        jTextFieldRelThresholdPct.setToolTipText("Max allowed growth % before stop, e.g. 20.");
        jPanelRelPercentile.add(jTextFieldRelThresholdPct);

        jPanelRelPercentile.add(new JLabel("% in a window of"));

        jTextFieldRelWindowSecs = new JTextField(4);
        jTextFieldRelWindowSecs.setHorizontalAlignment(JTextField.RIGHT);
        jTextFieldRelWindowSecs.setToolTipText("Observation window in seconds. Pn is computed per window and compared to the previous one.");
        jPanelRelPercentile.add(jTextFieldRelWindowSecs);

        jPanelRelPercentile.add(new JLabel("seconds"));

        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        add(jPanelRelPercentile, gbc);

        // "OR" separator before error count
        JLabel orLabelErrCount = new JLabel("OR");
        orLabelErrCount.setFont(new Font("Tahoma", 0, 10));
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 32, 0, 0);
        add(orLabelErrCount, gbc);

        // Row: [✓] [•] Error Count is greater than [10] for [10] seconds
        jPanelErrorCount = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));

        jCheckBoxErrorCount = new JCheckBox();
        jCheckBoxErrorCount.setToolTipText("Enable Error Count condition");
        jPanelErrorCount.add(jCheckBoxErrorCount);

        jLabelBulletErrorCount = new JLabel();
        jLabelBulletErrorCount.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/kg/apc/jmeter/reporters/bulletGreen.png")));
        jPanelErrorCount.add(jLabelBulletErrorCount);

        jPanelErrorCount.add(new JLabel("Error Count is greater than"));

        jTextFieldErrorCount = new JTextField(5);
        jTextFieldErrorCount.setHorizontalAlignment(JTextField.RIGHT);
        jTextFieldErrorCount.setToolTipText("Maximum allowed error count within the duration.");
        jPanelErrorCount.add(jTextFieldErrorCount);

        jPanelErrorCount.add(new JLabel("for"));

        jTextFieldErrorCountSec = new JTextField(5);
        jTextFieldErrorCountSec.setHorizontalAlignment(JTextField.RIGHT);
        jTextFieldErrorCountSec.setToolTipText("Duration window in seconds.");
        jPanelErrorCount.add(jTextFieldErrorCountSec);

        jPanelErrorCount.add(new JLabel("seconds"));

        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        add(jPanelErrorCount, gbc);

        // Bottom filler to restore resize room
        JPanel newFiller = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 12;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        add(newFiller, gbc);

        // Attach item listeners to all checkboxes
        ItemListener itemListener = e -> {
            updateAllRowStates();
            processBullets();
        };
        jCheckBoxRespTime.addItemListener(itemListener);
        jCheckBoxLatency.addItemListener(itemListener);
        jCheckBoxError.addItemListener(itemListener);
        jCheckBoxPercentile.addItemListener(itemListener);
        jCheckBoxRelPercentile.addItemListener(itemListener);
        jCheckBoxErrorCount.addItemListener(itemListener);

        // Register new fields for live validation colouring
        registerJTextfieldForValidation(jTextFieldRelPercentileValue, false);
        registerJTextfieldForValidation(jTextFieldRelWindowSecs, false);
        registerJTextfieldForValidation(jTextFieldRelThresholdPct, false);
        registerJTextfieldForValidation(jTextFieldErrorCount, false);
        registerJTextfieldForValidation(jTextFieldErrorCountSec, false);
    }

    private void adjustOrLabelInsets(JLabel label) {
        GridBagLayout layout = (GridBagLayout) getLayout();
        GridBagConstraints gbc = layout.getConstraints(label);
        gbc.insets = new Insets(0, 32, 0, 0);
        layout.setConstraints(label, gbc);
    }

    private int getIntValue(JTextField tf) {
        int ret;
        try {
            ret = Integer.valueOf(tf.getText());
        } catch (NumberFormatException ex) {
            ret = -1;
        }
        return ret;
    }

    private float getFloatValue(JTextField tf) {
        float ret;
        try {
            ret = Float.valueOf(tf.getText());
        } catch (NumberFormatException ex) {
            ret = -1;
        }
        return ret;
    }

    private boolean isVariableValue(JTextField tf) {
        String value = tf.getText();
        if (value != null) {
            return value.startsWith("${") && value.endsWith("}");
        } else {
            return false;
        }
    }

    private void processBullets() {
        jLabelBulletRespTime.setEnabled(jCheckBoxRespTime != null && jCheckBoxRespTime.isSelected()
                && (getIntValue(jTextFieldRespTime) > 0 || isVariableValue(jTextFieldRespTime)));
        jLabelBulletLatency.setEnabled(jCheckBoxLatency != null && jCheckBoxLatency.isSelected()
                && (getIntValue(jTextFieldRespLatency) > 0 || isVariableValue(jTextFieldRespLatency)));
        jLabelBulletError.setEnabled(jCheckBoxError != null && jCheckBoxError.isSelected()
                && (getFloatValue(jTextFieldError) > 0 || isVariableValue(jTextFieldError)));
        jLabelBulletPercentile.setEnabled(jCheckBoxPercentile != null && jCheckBoxPercentile.isSelected()
                && (getIntValue(jTextFieldPercentileRespTime) > 0 || isVariableValue(jTextFieldPercentileRespTime)));
        jLabelBulletRelPercentile.setEnabled(jCheckBoxRelPercentile != null && jCheckBoxRelPercentile.isSelected()
                && (getIntValue(jTextFieldRelPercentileValue) > 0 || isVariableValue(jTextFieldRelPercentileValue)));
        jLabelBulletErrorCount.setEnabled(jCheckBoxErrorCount != null && jCheckBoxErrorCount.isSelected()
                && (getIntValue(jTextFieldErrorCount) > 0 || isVariableValue(jTextFieldErrorCount)));
    }

    private void setJTextFieldColor(final JTextField tf, boolean isFloat) {
        if (!isFloat && (getIntValue(tf) > -1 || isVariableValue(tf))) {
            tf.setForeground(Color.black);
        } else if (isFloat && (getFloatValue(tf) > -1 || isVariableValue(tf))) {
            tf.setForeground(Color.black);
        } else {
            tf.setForeground(Color.red);
        }
    }

    private void registerJTextfieldForValidation(final JTextField tf, final boolean isFloat) {
        tf.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent arg0) {
                setJTextFieldColor(tf, isFloat);
                processBullets();
            }

            @Override
            public void insertUpdate(DocumentEvent arg0) {
                setJTextFieldColor(tf, isFloat);
                processBullets();
            }

            @Override
            public void removeUpdate(DocumentEvent arg0) {
                setJTextFieldColor(tf, isFloat);
                processBullets();
            }
        });
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

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabelBulletRespTime = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldRespTime = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldRespTimeSec = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabelBulletLatency = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextFieldRespLatency = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldRespLatencySec = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabelBulletError = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextFieldError = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldErrorSec = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabelBulletPercentile = new javax.swing.JLabel();
        jTextFieldPercentileValue = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextFieldPercentileRespTime = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextFieldPercentileRespTimeSec = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabelBulletCustomValidation = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jTextFieldCustomDuration = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Test Shutdown Criteria"));
        setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("The test will be automatically stopped if:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(jLabel1, gridBagConstraints);

        jLabelBulletRespTime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kg/apc/jmeter/reporters/bulletGreen.png"))); // NOI18N
        jPanel1.add(jLabelBulletRespTime);

        jLabel10.setText("average Response Time is greater than");
        jPanel1.add(jLabel10);

        jTextFieldRespTime.setColumns(7);
        jTextFieldRespTime.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextFieldRespTime.setMaximumSize(new java.awt.Dimension(100, 20));
        jPanel1.add(jTextFieldRespTime);

        jLabel2.setText("ms for");
        jPanel1.add(jLabel2);

        jTextFieldRespTimeSec.setColumns(5);
        jTextFieldRespTimeSec.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextFieldRespTimeSec.setInheritsPopupMenu(true);
        jTextFieldRespTimeSec.setMaximumSize(new java.awt.Dimension(100, 20));
        jPanel1.add(jTextFieldRespTimeSec);

        jLabel3.setText("seconds");
        jPanel1.add(jLabel3);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jPanel1, gridBagConstraints);

        jLabelBulletLatency.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kg/apc/jmeter/reporters/bulletGreen.png"))); // NOI18N
        jPanel2.add(jLabelBulletLatency);

        jLabel11.setText("average Latency is greater than");
        jPanel2.add(jLabel11);

        jTextFieldRespLatency.setColumns(7);
        jTextFieldRespLatency.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextFieldRespLatency.setMaximumSize(new java.awt.Dimension(100, 20));
        jPanel2.add(jTextFieldRespLatency);

        jLabel4.setText("ms for");
        jPanel2.add(jLabel4);

        jTextFieldRespLatencySec.setColumns(5);
        jTextFieldRespLatencySec.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextFieldRespLatencySec.setMaximumSize(new java.awt.Dimension(100, 20));
        jPanel2.add(jTextFieldRespLatencySec);

        jLabel5.setText("seconds");
        jPanel2.add(jLabel5);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jPanel2, gridBagConstraints);

        jLabelBulletError.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kg/apc/jmeter/reporters/bulletGreen.png"))); // NOI18N
        jPanel4.add(jLabelBulletError);

        jLabel12.setText("Error Rate is greater than");
        jPanel4.add(jLabel12);

        jTextFieldError.setColumns(5);
        jTextFieldError.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextFieldError.setMaximumSize(new java.awt.Dimension(30, 20));
        jPanel4.add(jTextFieldError);

        jLabel6.setText("% for");
        jPanel4.add(jLabel6);

        jTextFieldErrorSec.setColumns(5);
        jTextFieldErrorSec.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextFieldErrorSec.setMaximumSize(new java.awt.Dimension(100, 20));
        jPanel4.add(jTextFieldErrorSec);

        jLabel7.setText("seconds");
        jPanel4.add(jLabel7);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jPanel4, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanel3, gridBagConstraints);

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel8.setText("OR");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        add(jLabel8, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel9.setText("OR");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        add(jLabel9, gridBagConstraints);

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel13.setText("OR");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        add(jLabel13, gridBagConstraints);

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        add(jLabel20, gridBagConstraints);

        jLabelBulletPercentile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kg/apc/jmeter/reporters/bulletGreen.png"))); // NOI18N
        jPanel5.add(jLabelBulletPercentile);

        jTextFieldPercentileValue.setColumns(2);
        jTextFieldPercentileValue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextFieldPercentileValue.setMaximumSize(new java.awt.Dimension(100, 20));
        jPanel5.add(jTextFieldPercentileValue);

        jLabel14.setText("th Percentile Response time is greater than");
        jPanel5.add(jLabel14);

        jTextFieldPercentileRespTime.setColumns(7);
        jTextFieldPercentileRespTime.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextFieldPercentileRespTime.setMaximumSize(new java.awt.Dimension(100, 20));
        jPanel5.add(jTextFieldPercentileRespTime);

        jLabel15.setText("ms for");
        jPanel5.add(jLabel15);

        jTextFieldPercentileRespTimeSec.setColumns(5);
        jTextFieldPercentileRespTimeSec.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextFieldPercentileRespTimeSec.setMaximumSize(new java.awt.Dimension(100, 20));
        jPanel5.add(jTextFieldPercentileRespTimeSec);

        jLabel16.setText("seconds");
        jPanel5.add(jLabel16);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jPanel5, gridBagConstraints);


        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jPanel6, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelBulletCustomValidation;
    private javax.swing.JLabel jLabelBulletError;
    private javax.swing.JLabel jLabelBulletLatency;
    private javax.swing.JLabel jLabelBulletPercentile;
    private javax.swing.JLabel jLabelBulletRespTime;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JTextField jTextFieldCustomDuration;
    private javax.swing.JTextField jTextFieldError;
    private javax.swing.JTextField jTextFieldErrorSec;
    private javax.swing.JTextField jTextFieldPercentileRespTime;
    private javax.swing.JTextField jTextFieldPercentileRespTimeSec;
    private javax.swing.JTextField jTextFieldPercentileValue;
    private javax.swing.JTextField jTextFieldRespLatency;
    private javax.swing.JTextField jTextFieldRespLatencySec;
    private javax.swing.JTextField jTextFieldRespTime;
    private javax.swing.JTextField jTextFieldRespTimeSec;
    // End of variables declaration//GEN-END:variables

    // Checkboxes for enabling/disabling each condition
    private javax.swing.JCheckBox jCheckBoxRespTime;
    private javax.swing.JCheckBox jCheckBoxLatency;
    private javax.swing.JCheckBox jCheckBoxError;
    private javax.swing.JCheckBox jCheckBoxPercentile;
    private javax.swing.JCheckBox jCheckBoxRelPercentile;
    private javax.swing.JCheckBox jCheckBoxErrorCount;

    // Extra fields for relative window-to-window percentile row
    private javax.swing.JPanel jPanelRelPercentile;
    private javax.swing.JLabel jLabelBulletRelPercentile;
    private javax.swing.JTextField jTextFieldRelPercentileValue;
    private javax.swing.JTextField jTextFieldRelWindowSecs;
    private javax.swing.JTextField jTextFieldRelThresholdPct;

    // Extra fields for per-window error count ceiling row
    private javax.swing.JPanel jPanelErrorCount;
    private javax.swing.JLabel jLabelBulletErrorCount;
    private javax.swing.JTextField jTextFieldErrorCount;
    private javax.swing.JTextField jTextFieldErrorCountSec;
}
