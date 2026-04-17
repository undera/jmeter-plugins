package com.adaptive.jmeter.plugins;

import org.apache.jmeter.timers.gui.AbstractTimerGui;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.gui.util.VerticalPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * GUI component for Adaptive Timer From CSV
 */
public class AdaptiveTimerFromCSVGui extends AbstractTimerGui {

    private static final long serialVersionUID = 1L;

    private JTextField csvFilePathField;
    private JTextField minThreadsField;
    private JTextField maxThreadsField;
    private JTextField adjustmentIntervalField;
    private JTextField rampUpStepField;
    private JTextField rampDownStepField;
    private JTextField p90ThresholdField;
    private JTextField startTimeField;
    private JTextField endTimeField;
    private JTextField defaultStartTimeField;
    private JRadioButton defaultRadio;
    private JRadioButton stepBasedRadio;
    private JRadioButton timeBasedRadio;
    private JCheckBox infiniteExecutionCheckbox;
    private JCheckBox enableCsvReloadCheckbox;
    private JPanel rampPanel;
    private JPanel timePanel;
    private JButton browseButton;
    private LoadProfileGraphPanel graphPanel;

    public AdaptiveTimerFromCSVGui() {
        init();
    }

    @Override
    public String getLabelResource() {
        return "adaptive_timer_from_csv";
    }

    @Override
    public String getStaticLabel() {
        return "Adaptive Throughput Timer";
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);
        
        // Create main content panel with split layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(createConfigPanel());
        splitPane.setBottomComponent(createGraphPanel());
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(0.4);
        
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createConfigPanel() {
        VerticalPanel panel = new VerticalPanel();

        // CSV File Path
        JPanel csvPanel = new JPanel(new BorderLayout(5, 0));
        csvPanel.add(new JLabel("CSV File Path:"), BorderLayout.WEST);
        csvFilePathField = new JTextField(40);
        csvPanel.add(csvFilePathField, BorderLayout.CENTER);
        browseButton = new JButton("Browse");
        browseButton.addActionListener(e -> browseFile());
        csvPanel.add(browseButton, BorderLayout.EAST);
        panel.add(csvPanel);

        // Min/Max Threads
        JPanel threadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        threadPanel.add(new JLabel("Min Threads (starting):"));
        minThreadsField = new JTextField("1", 5);
        threadPanel.add(minThreadsField);
        threadPanel.add(new JLabel("Max Threads:"));
        maxThreadsField = new JTextField("100", 5);
        threadPanel.add(maxThreadsField);
        panel.add(threadPanel);

        // Adjustment Interval
        JPanel adjustmentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        adjustmentPanel.add(new JLabel("Adjustment Interval (ms):"));
        adjustmentIntervalField = new JTextField("5000", 10);
        adjustmentPanel.add(adjustmentIntervalField);
        panel.add(adjustmentPanel);

        // Selection Mode: Default, Step-based, or Time-based
        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        modePanel.add(new JLabel("Range Mode:"));
        defaultRadio = new JRadioButton("Default", false);
        stepBasedRadio = new JRadioButton("Step-based", true);
        timeBasedRadio = new JRadioButton("Time-based", false);
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(defaultRadio);
        modeGroup.add(stepBasedRadio);
        modeGroup.add(timeBasedRadio);
        modePanel.add(defaultRadio);
        modePanel.add(stepBasedRadio);
        modePanel.add(timeBasedRadio);
        
        // Add action listeners to enable/disable fields
        defaultRadio.addActionListener(e -> updateFieldStates());
        stepBasedRadio.addActionListener(e -> updateFieldStates());
        timeBasedRadio.addActionListener(e -> updateFieldStates());
        
        panel.add(modePanel);

        // Default Start Time (HH:mm)
        JPanel defaultTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        defaultTimePanel.add(new JLabel("Default Start Time (HH:mm):"));
        defaultStartTimeField = new JTextField("00:00", 8);
        defaultTimePanel.add(defaultStartTimeField);
        panel.add(defaultTimePanel);

        // Ramp Up/Down Steps
        rampPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        rampPanel.add(new JLabel("Start Step:"));
        rampUpStepField = new JTextField("1", 5);
        rampPanel.add(rampUpStepField);
        rampPanel.add(new JLabel("End Step:"));
        rampDownStepField = new JTextField("1", 5);
        rampPanel.add(rampDownStepField);
        panel.add(rampPanel);

        // Start Time / End Time
        timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        timePanel.add(new JLabel("Start Time (HH:mm):"));
        startTimeField = new JTextField("00:00", 8);
        timePanel.add(startTimeField);
        timePanel.add(new JLabel("End Time (HH:mm):"));
        endTimeField = new JTextField("00:00", 8);
        timePanel.add(endTimeField);
        panel.add(timePanel);

        // P90 Threshold
        JPanel p90Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        p90Panel.add(new JLabel("P90 Threshold (ms):"));
        p90ThresholdField = new JTextField("500", 10);
        p90Panel.add(p90ThresholdField);
        panel.add(p90Panel);

        // Infinite Test Execution
        JPanel infinitePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        infiniteExecutionCheckbox = new JCheckBox("Infinite Test Execution (24-hour cycling)");
        infiniteExecutionCheckbox.setToolTipText("Enable to cycle test every 24 hours. Requires CSV with full 24-hour coverage (00:00 to 23:59)");
        infinitePanel.add(infiniteExecutionCheckbox);
        panel.add(infinitePanel);

        // Dynamic CSV Reload
        JPanel csvReloadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        enableCsvReloadCheckbox = new JCheckBox("Enable Dynamic CSV Reload (check every 60 seconds)");
        enableCsvReloadCheckbox.setToolTipText("Enable to automatically reload CSV file every minute if it has been modified. Test can continue running without interruption.");
        enableCsvReloadCheckbox.setSelected(true); // Default: enabled
        csvReloadPanel.add(enableCsvReloadCheckbox);
        panel.add(csvReloadPanel);

        // Initialize field states based on default selection
        updateFieldStates();

        return panel;
    }

    /**
     * Update field enable/disable states based on selected mode
     */
    private void updateFieldStates() {
        if (defaultRadio.isSelected()) {
            // Default mode: only default start time is enabled
            defaultStartTimeField.setEnabled(true);
            rampUpStepField.setEnabled(false);
            rampDownStepField.setEnabled(false);
            startTimeField.setEnabled(false);
            endTimeField.setEnabled(false);
        } else if (stepBasedRadio.isSelected()) {
            // Step-based mode: only ramp fields are enabled
            defaultStartTimeField.setEnabled(false);
            rampUpStepField.setEnabled(true);
            rampDownStepField.setEnabled(true);
            startTimeField.setEnabled(false);
            endTimeField.setEnabled(false);
        } else if (timeBasedRadio.isSelected()) {
            // Time-based mode: only time fields are enabled
            defaultStartTimeField.setEnabled(false);
            rampUpStepField.setEnabled(false);
            rampDownStepField.setEnabled(false);
            startTimeField.setEnabled(true);
            endTimeField.setEnabled(true);
        }
    }

    private JPanel createGraphPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Load Profile Visualization"));
        
        graphPanel = new LoadProfileGraphPanel();
        panel.add(graphPanel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createFieldPanel(String label, JTextField field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.add(new JLabel(label));
        panel.add(field);
        return panel;
    }

    private void browseFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        // Add file filters for supported formats
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("All Supported Files", "csv", "txt", "xlsx", "xls"));
        chooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));
        chooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));
        chooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files", "xlsx", "xls"));
        
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            csvFilePathField.setText(selectedFile.getAbsolutePath());
            
            // Update the graph with the selected file
            updateGraph(selectedFile.getAbsolutePath());
        }
    }
    
    /**
     * Update the graph with data from the selected file
     */
    private void updateGraph(String filePath) {
        try {
            List<CSVThroughputEntry> entries = CSVThroughputReader.readFile(filePath);
            if (graphPanel != null) {
                graphPanel.updateGraph(entries);
            }
        } catch (Exception ex) {
            // Silently fail - file might not be readable yet
            if (graphPanel != null) {
                graphPanel.clear();
            }
        }
    }

    @Override
    public void configure(org.apache.jmeter.testelement.TestElement el) {
        super.configure(el);
        if (el instanceof AdaptiveTimerFromCSV) {
            AdaptiveTimerFromCSV timer = (AdaptiveTimerFromCSV) el;
            csvFilePathField.setText(timer.getCsvFilePath());
            minThreadsField.setText(String.valueOf(timer.getMinThreads()));
            maxThreadsField.setText(String.valueOf(timer.getMaxThreads()));
            adjustmentIntervalField.setText(String.valueOf(timer.getAdjustmentIntervalMs()));
            rampUpStepField.setText(String.valueOf(timer.getRampUpStep()));
            rampDownStepField.setText(String.valueOf(timer.getRampDownStep()));
            p90ThresholdField.setText(String.valueOf(timer.getP90ThresholdMs()));
            startTimeField.setText(timer.getStartTime() != null ? timer.getStartTime() : "00:00");
            endTimeField.setText(timer.getEndTime() != null ? timer.getEndTime() : "00:00");
            defaultStartTimeField.setText(timer.getDefaultStartTime() != null ? timer.getDefaultStartTime() : "00:00");
            
            // Set mode selection
            String mode = timer.getRangeMode();
            defaultRadio.setSelected("default".equals(mode));
            stepBasedRadio.setSelected("step".equals(mode));
            timeBasedRadio.setSelected("time".equals(mode));
            
            // Load infinite execution checkbox
            infiniteExecutionCheckbox.setSelected(timer.isInfiniteExecution());
            
            // Load CSV reload checkbox
            enableCsvReloadCheckbox.setSelected(timer.isEnableCsvReload());
            
            // Update field states based on mode
            updateFieldStates();
            
            // Update graph with the configured file
            String filePath = timer.getCsvFilePath();
            if (filePath != null && !filePath.isEmpty()) {
                updateGraph(filePath);
            }
        }
    }

    @Override
    public org.apache.jmeter.testelement.TestElement createTestElement() {
        AdaptiveTimerFromCSV timer = new AdaptiveTimerFromCSV();
        modifyTestElement(timer);
        return timer;
    }

    @Override
    public void modifyTestElement(org.apache.jmeter.testelement.TestElement el) {
        super.modifyTestElement(el);
        if (el instanceof AdaptiveTimerFromCSV) {
            AdaptiveTimerFromCSV timer = (AdaptiveTimerFromCSV) el;
            
            // Validate 24-hour coverage if infinite execution is enabled
            if (infiniteExecutionCheckbox.isSelected()) {
                AdaptiveTimerFromCSV tempTimer = new AdaptiveTimerFromCSV();
                tempTimer.setCsvFilePath(csvFilePathField.getText());
                tempTimer.setInfiniteExecution(true);
                
                // Load CSV entries for validation
                try {
                    java.util.List<CSVThroughputEntry> entries = CSVThroughputReader.readFile(csvFilePathField.getText());
                    // Manually validate by checking time range
                    if (!entries.isEmpty()) {
                        long minTimeMs = Long.MAX_VALUE;
                        long maxTimeMs = Long.MIN_VALUE;
                        for (CSVThroughputEntry entry : entries) {
                            minTimeMs = Math.min(minTimeMs, entry.getTotalTimeMs());
                            maxTimeMs = Math.max(maxTimeMs, entry.getTotalTimeMs());
                        }
                        
                        long twentyThreeFiftyNineMs = 23 * 60 * 60 * 1000 + 59 * 60 * 1000;
                        if (minTimeMs > 0 || maxTimeMs < twentyThreeFiftyNineMs) {
                            String foundRange = formatTimeFromMs(minTimeMs) + " to " + formatTimeFromMs(maxTimeMs);
                            String errorMsg = "CSV requires full 24-hour coverage (00:00 to 23:59) for infinite execution.\n" +
                                            "Found: " + foundRange + " (incomplete coverage).\n\n" +
                                            "Please update your CSV file to include entries for 00:00 and 23:59.";
                            JOptionPane.showMessageDialog(this, errorMsg, "Invalid Configuration", JOptionPane.ERROR_MESSAGE);
                            return; // Don't save if validation fails
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error reading CSV file: " + ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            // All validations passed, now save the configuration
            timer.setCsvFilePath(csvFilePathField.getText());
            timer.setMinThreads(Long.parseLong(minThreadsField.getText()));
            timer.setMaxThreads(Long.parseLong(maxThreadsField.getText()));
            timer.setAdjustmentIntervalMs(Long.parseLong(adjustmentIntervalField.getText()));
            timer.setRampUpStep(Long.parseLong(rampUpStepField.getText()));
            timer.setRampDownStep(Long.parseLong(rampDownStepField.getText()));
            timer.setP90ThresholdMs(Long.parseLong(p90ThresholdField.getText()));
            timer.setStartTime(startTimeField.getText());
            timer.setEndTime(endTimeField.getText());
            timer.setDefaultStartTime(defaultStartTimeField.getText());
            timer.setInfiniteExecution(infiniteExecutionCheckbox.isSelected());
            timer.setEnableCsvReload(enableCsvReloadCheckbox.isSelected());
            
            // Set mode based on selection
            if (defaultRadio.isSelected()) {
                timer.setRangeMode("default");
            } else if (stepBasedRadio.isSelected()) {
                timer.setRangeMode("step");
            } else if (timeBasedRadio.isSelected()) {
                timer.setRangeMode("time");
            }
        }
    }
    
    /**
     * Format milliseconds as HH:mm time string
     */
    private String formatTimeFromMs(long timeMs) {
        if (timeMs == Long.MAX_VALUE || timeMs == Long.MIN_VALUE) {
            return "N/A";
        }
        long totalSeconds = timeMs / 1000;
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        return String.format("%02d:%02d", hours, minutes);
    }
}
