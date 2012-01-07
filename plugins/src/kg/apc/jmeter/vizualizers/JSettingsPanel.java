package kg.apc.jmeter.vizualizers;

import java.awt.Color;
import java.awt.event.InputEvent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ToolTipManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import kg.apc.charting.ChartSettings;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.graphs.GraphRendererInterface;
import kg.apc.jmeter.graphs.SettingsInterface;

/**
 *
 * @author Stéphane Hoblingre
 */
public class JSettingsPanel extends javax.swing.JPanel implements GraphRendererInterface
{
    public final static int TIMELINE_OPTION = 1;
    public final static int GRADIENT_OPTION = 1 << 1;
    public final static int CURRENTX_OPTION = 1 << 2;
    public final static int FINAL_ZEROING_OPTION = 1 << 3;
    public final static int LIMIT_POINT_OPTION = 1 << 4;
    public final static int BAR_CHART_X_LIMIT_OPTION = 1 << 5;
    public final static int HIDE_NON_REP_VALUES_OPTION = 1 << 6;
    public final static int AGGREGATE_OPTION = 1 << 7;
    public final static int RELATIVE_TIME_OPTION = 1 << 8;
    public final static int MAXY_OPTION = 1 << 9;
    public final static int CHART_TYPE_OPTION = 1 << 10;
    public final static int MARKERS_OPTION = 1 << 11;
    public final static int AUTO_EXPAND_OPTION = 1 << 12;

    private SettingsInterface parent = null;
    private int originalTooltipDisplayTime = 0;

    /** Creates new form JSettingsPanel */
    public JSettingsPanel(SettingsInterface parent, int options)
    {
        boolean showTimelineOption = (options & TIMELINE_OPTION) != 0;
        boolean showGradientOption = (options & GRADIENT_OPTION) != 0;
        boolean showCurrentXOption = (options & CURRENTX_OPTION) != 0;
        boolean showFinalZeroingLinesOption = (options & FINAL_ZEROING_OPTION) != 0;
        boolean showLimitPointOption = (options & LIMIT_POINT_OPTION) != 0;
        boolean showBarChartXAxisLimit = (options & BAR_CHART_X_LIMIT_OPTION) != 0;
        boolean showHideNonRepValues = (options & HIDE_NON_REP_VALUES_OPTION) != 0;
        boolean showAggregateOption = (options & AGGREGATE_OPTION) != 0;
        boolean showRelativeTimeOption = (options & RELATIVE_TIME_OPTION) != 0;
        boolean showMaxYOption = (options & MAXY_OPTION) != 0;
        boolean showGraphTypeOption = (options & CHART_TYPE_OPTION) != 0;
        boolean showMarkersOption = (options & MARKERS_OPTION) != 0;
        boolean showAutoExpandOption = (options & AUTO_EXPAND_OPTION) != 0;

        initComponents();
        this.parent = parent;
        postInitComponents(
                showTimelineOption,
                showGradientOption,
                showCurrentXOption,
                showFinalZeroingLinesOption,
                showLimitPointOption,
                showBarChartXAxisLimit,
                showHideNonRepValues,
                showAggregateOption,
                showRelativeTimeOption,
                showMaxYOption,
                showGraphTypeOption,
                showMarkersOption,
                showAutoExpandOption);
        registerJTextfieldForRefresh(jTextFieldMaxY);
    }

    private void postInitComponents(
            boolean showTimelineOption,
            boolean showGradientOption,
            boolean showCurrentXOption,
            boolean showFinalZeroingLinesOption,
            boolean showLimitPointOption,
            boolean showBarChartXAxisLimit,
            boolean showHideNonRepValues,
            boolean showAggregateOption,
            boolean showRelativeTimeOption,
            boolean showMaxYOption,
            boolean showGraphTypeOption,
            boolean showMarkersOption,
            boolean showAutoExpandOption)
    {
        jPanelTimeLineContainer.setVisible(
                showTimelineOption ||
                showAggregateOption ||
                showRelativeTimeOption);
        jPanelRenderingOptionsContainer.setVisible(
                showGradientOption ||
                showCurrentXOption ||
                showFinalZeroingLinesOption ||
                showLimitPointOption ||
                showBarChartXAxisLimit ||
                showHideNonRepValues ||
                showMaxYOption);
        jPanelGraphStyle.setVisible(
                showGraphTypeOption ||
                showMarkersOption);

        jLabelTimeline1.setVisible(showTimelineOption);
        jLabelTimeline2.setVisible(showTimelineOption);
        jComboBoxGranulation.setVisible(showTimelineOption);
        jLabelInfoGrpValues.setVisible(showTimelineOption);

        jCheckBoxRelativeTime.setVisible(showRelativeTimeOption);
        jCheckBoxAutoExpand.setVisible(showAutoExpandOption);

        jRadioButtonGraphAggregated.setVisible(showAggregateOption);
        jRadioButtonGraphDetailed.setVisible(showAggregateOption);
        jLabelGraphType.setVisible(showAggregateOption);

        jCheckBoxPaintGradient.setVisible(showGradientOption);
        jCheckBoxDrawCurrentX.setVisible(showCurrentXOption);
        jCheckBoxDrawFinalZeroingLines.setVisible(showFinalZeroingLinesOption);
        jCheckBoxMaxPoints.setVisible(showLimitPointOption);
        jComboBoxMaxPoints.setVisible(showLimitPointOption);
        jLabelMaxPoints.setVisible(showLimitPointOption);
        jLabelInfoMaxPoint.setVisible(showLimitPointOption);

        originalTooltipDisplayTime = ToolTipManager.sharedInstance().getDismissDelay();

        //init default values from global config
        jCheckBoxPaintGradient.setSelected(parent.getGraphPanelChart().getChartSettings().isDrawGradient());
        jCheckBoxDrawCurrentX.setSelected(parent.getGraphPanelChart().getChartSettings().isDrawCurrentX());
        jCheckBoxRelativeTime.setSelected(parent.getGraphPanelChart().getChartSettings().isUseRelativeTime());
        if (showFinalZeroingLinesOption)
        {
            jCheckBoxDrawFinalZeroingLines.setSelected(parent.getGraphPanelChart().getChartSettings().isDrawFinalZeroingLines());
        }

        jCheckBoxLimitMaxXValue.setVisible(showBarChartXAxisLimit);

        jComboBoxHideNonRepValLimit.setVisible(showHideNonRepValues);
        jCheckBoxHideNonRepValues.setVisible(showHideNonRepValues);
        jLabelHideNonRepPoints.setVisible(showHideNonRepValues);

        jCheckBoxMaxY.setVisible(showMaxYOption);
        jTextFieldMaxY.setVisible(showMaxYOption);

        jComboBoxChartType.setVisible(showGraphTypeOption);
        jLabelGraphType.setVisible(showGraphTypeOption);
        jCheckBoxDrawMarkers.setVisible(showMarkersOption);

        //bar is default for now, logic needs to be updated in the future
        if(showGraphTypeOption) jCheckBoxDrawMarkers.setVisible(false);
    }

    private void refreshGraphPreview()
    {
        parent.getGraphPanelChart().invalidateCache();
        jPanelGraphPreview.invalidate();
        jPanelGraphPreview.repaint();
    }

    @Override
    public JPanel getGraphDisplayPanel()
    {
        return jPanelGraphPreview;
    }

    private int getValueFromString(String sValue)
    {
        int ret;
        try
        {
            ret = Integer.valueOf(sValue);
            if (ret <= 0)
            {
                ret = -1;
            }
        } catch (NumberFormatException ex)
        {
            ret = -1;
        }

        return ret;
    }

    public void setAggregateMode(boolean aggregate)
    {
        if (aggregate)
        {
            jRadioButtonGraphAggregated.setSelected(true);
        } else
        {
            jRadioButtonGraphDetailed.setSelected(true);
        }
    }

    public void setGranulationValue(int value)
    {
        jComboBoxGranulation.setSelectedItem(Integer.toString(value));
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

        buttonGroupGraphType = new javax.swing.ButtonGroup();
        jPanelLogo = new javax.swing.JPanel();
        jLabelLogo = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanelAllSettingsContainer = new javax.swing.JPanel();
        jPanelTimeLineContainer = new javax.swing.JPanel();
        jLabelTimeline1 = new javax.swing.JLabel();
        jLabelTimeline2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jComboBoxGranulation = new javax.swing.JComboBox();
        jLabelInfoGrpValues = new javax.swing.JLabel();
        jRadioButtonGraphAggregated = new javax.swing.JRadioButton();
        jRadioButtonGraphDetailed = new javax.swing.JRadioButton();
        jLabelGraphType = new javax.swing.JLabel();
        jCheckBoxRelativeTime = new javax.swing.JCheckBox();
        jCheckBoxAutoExpand = new javax.swing.JCheckBox();
        jPanelRenderingOptionsContainer = new javax.swing.JPanel();
        jCheckBoxPaintGradient = new javax.swing.JCheckBox();
        jCheckBoxDrawFinalZeroingLines = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        jCheckBoxDrawCurrentX = new javax.swing.JCheckBox();
        jCheckBoxMaxPoints = new javax.swing.JCheckBox();
        jLabelMaxPoints = new javax.swing.JLabel();
        jComboBoxMaxPoints = new javax.swing.JComboBox();
        jLabelInfoMaxPoint = new javax.swing.JLabel();
        jCheckBoxLimitMaxXValue = new javax.swing.JCheckBox();
        jCheckBoxHideNonRepValues = new javax.swing.JCheckBox();
        jComboBoxHideNonRepValLimit = new javax.swing.JComboBox();
        jLabelHideNonRepPoints = new javax.swing.JLabel();
        jCheckBoxMaxY = new javax.swing.JCheckBox();
        jTextFieldMaxY = new javax.swing.JTextField();
        jPanelGraphPreviewContainer = new javax.swing.JPanel();
        jPanelGraphPreview = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanelGraphStyle = new javax.swing.JPanel();
        jLabelChartType = new javax.swing.JLabel();
        jComboBoxChartType = new javax.swing.JComboBox();
        jCheckBoxDrawMarkers = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();

        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        setLayout(new java.awt.BorderLayout());

        jPanelLogo.setLayout(new java.awt.GridBagLayout());

        jLabelLogo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kg/apc/jmeter/vizualizers/logoSimple.png"))); // NOI18N
        jLabelLogo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelLogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelLogoMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanelLogo.add(jLabelLogo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelLogo.add(jPanel1, gridBagConstraints);

        add(jPanelLogo, java.awt.BorderLayout.PAGE_END);

        jPanelAllSettingsContainer.setLayout(new java.awt.GridBagLayout());

        jPanelTimeLineContainer.setBorder(javax.swing.BorderFactory.createTitledBorder("Graph Settings"));
        jPanelTimeLineContainer.setLayout(new java.awt.GridBagLayout());

        jLabelTimeline1.setText("Group timeline values for");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 8, 2);
        jPanelTimeLineContainer.add(jLabelTimeline1, gridBagConstraints);

        jLabelTimeline2.setText("ms");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 8, 2);
        jPanelTimeLineContainer.add(jLabelTimeline2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelTimeLineContainer.add(jPanel4, gridBagConstraints);

        jComboBoxGranulation.setEditable(true);
        jComboBoxGranulation.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "100", "500", "1000", "2000", "5000", "10000", "30000", "60000" }));
        jComboBoxGranulation.setPreferredSize(new java.awt.Dimension(80, 20));
        jComboBoxGranulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxGranulationActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 8, 2);
        jPanelTimeLineContainer.add(jComboBoxGranulation, gridBagConstraints);

        jLabelInfoGrpValues.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kg/apc/jmeter/vizualizers/information.png"))); // NOI18N
        jLabelInfoGrpValues.setToolTipText("<html>You can specify here the duration used internally<br>\nby the plugin to combine the values received during<br>\nthe test. This will result in <b>more readable graphs</b> and<br>\n<b>less resources needs</b>. It <b>cannot be undo</b>.<br>\nYou can change the value during the test, but it is not<br>\nrecomended as it may produce inconsistant graphs.<br>\nThis parameter is saved with the test plan.");
        jLabelInfoGrpValues.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                infoLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                infoLabelMouseExited(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 8, 2);
        jPanelTimeLineContainer.add(jLabelInfoGrpValues, gridBagConstraints);

        buttonGroupGraphType.add(jRadioButtonGraphAggregated);
        jRadioButtonGraphAggregated.setText("Aggregated display, all Samplers combined");
        jRadioButtonGraphAggregated.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonGraphAggregatedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelTimeLineContainer.add(jRadioButtonGraphAggregated, gridBagConstraints);

        buttonGroupGraphType.add(jRadioButtonGraphDetailed);
        jRadioButtonGraphDetailed.setSelected(true);
        jRadioButtonGraphDetailed.setText("Detailed display, one row per Sampler");
        jRadioButtonGraphDetailed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonGraphDetailedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelTimeLineContainer.add(jRadioButtonGraphDetailed, gridBagConstraints);

        jLabelGraphType.setText("Type of graph:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 4, 2);
        jPanelTimeLineContainer.add(jLabelGraphType, gridBagConstraints);

        jCheckBoxRelativeTime.setText("Use relative times");
        jCheckBoxRelativeTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxRelativeTimeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelTimeLineContainer.add(jCheckBoxRelativeTime, gridBagConstraints);

        jCheckBoxAutoExpand.setSelected(true);
        jCheckBoxAutoExpand.setText("Auto expand rows");
        jCheckBoxAutoExpand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxAutoExpandActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelTimeLineContainer.add(jCheckBoxAutoExpand, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanelAllSettingsContainer.add(jPanelTimeLineContainer, gridBagConstraints);

        jPanelRenderingOptionsContainer.setBorder(javax.swing.BorderFactory.createTitledBorder("Rendering Options"));
        jPanelRenderingOptionsContainer.setLayout(new java.awt.GridBagLayout());

        jCheckBoxPaintGradient.setText("Paint gradient");
        jCheckBoxPaintGradient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxPaintGradientActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRenderingOptionsContainer.add(jCheckBoxPaintGradient, gridBagConstraints);

        jCheckBoxDrawFinalZeroingLines.setText("Draw final zeroing lines");
        jCheckBoxDrawFinalZeroingLines.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDrawFinalZeroingLinesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRenderingOptionsContainer.add(jCheckBoxDrawFinalZeroingLines, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelRenderingOptionsContainer.add(jPanel6, gridBagConstraints);

        jCheckBoxDrawCurrentX.setText("Draw current X line");
        jCheckBoxDrawCurrentX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDrawCurrentXActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRenderingOptionsContainer.add(jCheckBoxDrawCurrentX, gridBagConstraints);

        jCheckBoxMaxPoints.setText("Limit number of points in row to");
        jCheckBoxMaxPoints.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMaxPointsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRenderingOptionsContainer.add(jCheckBoxMaxPoints, gridBagConstraints);

        jLabelMaxPoints.setText("points");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanelRenderingOptionsContainer.add(jLabelMaxPoints, gridBagConstraints);

        jComboBoxMaxPoints.setEditable(true);
        jComboBoxMaxPoints.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "20", "50", "100", "150", "200" }));
        jComboBoxMaxPoints.setSelectedIndex(1);
        jComboBoxMaxPoints.setPreferredSize(new java.awt.Dimension(50, 20));
        jComboBoxMaxPoints.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxMaxPointsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanelRenderingOptionsContainer.add(jComboBoxMaxPoints, gridBagConstraints);

        jLabelInfoMaxPoint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kg/apc/jmeter/vizualizers/information.png"))); // NOI18N
        jLabelInfoMaxPoint.setToolTipText("<html>This option will <b>dynamically</b> adjust the graph<br>\nrendering so it is <b>more readable</b>. It <b>can be undo</b>.<br>\nYou can change the value during the test.<br>\nThis parameter is not saved with the test plan.<br>");
        jLabelInfoMaxPoint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                infoLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                infoLabelMouseExited(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanelRenderingOptionsContainer.add(jLabelInfoMaxPoint, gridBagConstraints);

        jCheckBoxLimitMaxXValue.setText("Prevent X axis range to adapt to outliers");
        jCheckBoxLimitMaxXValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxLimitMaxXValueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRenderingOptionsContainer.add(jCheckBoxLimitMaxXValue, gridBagConstraints);

        jCheckBoxHideNonRepValues.setText("Hide non representative points, if count is less or equal than");
        jCheckBoxHideNonRepValues.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxHideNonRepValuesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 4;
        jPanelRenderingOptionsContainer.add(jCheckBoxHideNonRepValues, gridBagConstraints);

        jComboBoxHideNonRepValLimit.setEditable(true);
        jComboBoxHideNonRepValLimit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "5", "10", "15", "20", "50", "100" }));
        jComboBoxHideNonRepValLimit.setPreferredSize(new java.awt.Dimension(50, 20));
        jComboBoxHideNonRepValLimit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxHideNonRepValLimitActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanelRenderingOptionsContainer.add(jComboBoxHideNonRepValLimit, gridBagConstraints);

        jLabelHideNonRepPoints.setText("occurences");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        jPanelRenderingOptionsContainer.add(jLabelHideNonRepPoints, gridBagConstraints);

        jCheckBoxMaxY.setText("Force maximum Y axis value to");
        jCheckBoxMaxY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMaxYActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRenderingOptionsContainer.add(jCheckBoxMaxY, gridBagConstraints);

        jTextFieldMaxY.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextFieldMaxY.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanelRenderingOptionsContainer.add(jTextFieldMaxY, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanelAllSettingsContainer.add(jPanelRenderingOptionsContainer, gridBagConstraints);

        jPanelGraphPreviewContainer.setLayout(new java.awt.GridBagLayout());

        jPanelGraphPreview.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 100.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanelGraphPreviewContainer.add(jPanelGraphPreview, gridBagConstraints);

        jLabel2.setText("Preview:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        jPanelGraphPreviewContainer.add(jLabel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelAllSettingsContainer.add(jPanelGraphPreviewContainer, gridBagConstraints);

        jPanelGraphStyle.setBorder(javax.swing.BorderFactory.createTitledBorder("Graph Type"));
        jPanelGraphStyle.setLayout(new java.awt.GridBagLayout());

        jLabelChartType.setText("Chart type:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanelGraphStyle.add(jLabelChartType, gridBagConstraints);

        jComboBoxChartType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Line", "Bar", "Cubic Spline" }));
        jComboBoxChartType.setSelectedIndex(1);
        jComboBoxChartType.setPreferredSize(new java.awt.Dimension(110, 20));
        jComboBoxChartType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxChartTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanelGraphStyle.add(jComboBoxChartType, gridBagConstraints);

        jCheckBoxDrawMarkers.setText("Draw markers");
        jCheckBoxDrawMarkers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDrawMarkersActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 0, 2);
        jPanelGraphStyle.add(jCheckBoxDrawMarkers, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelGraphStyle.add(jPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanelAllSettingsContainer.add(jPanelGraphStyle, gridBagConstraints);

        add(jPanelAllSettingsContainer, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxGranulationActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jComboBoxGranulationActionPerformed
    {//GEN-HEADEREND:event_jComboBoxGranulationActionPerformed
        //notify parent if value changed and valid
        int newValue = getValueFromString((String) jComboBoxGranulation.getSelectedItem());
        if (newValue != -1 && parent.getGranulation() != newValue)
        {
            parent.setGranulation(newValue);
        }
    }//GEN-LAST:event_jComboBoxGranulationActionPerformed

    private void jCheckBoxPaintGradientActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jCheckBoxPaintGradientActionPerformed
    {//GEN-HEADEREND:event_jCheckBoxPaintGradientActionPerformed
        parent.getGraphPanelChart().getChartSettings().setDrawGradient(jCheckBoxPaintGradient.isSelected());
        refreshGraphPreview();
    }//GEN-LAST:event_jCheckBoxPaintGradientActionPerformed

    private void jCheckBoxDrawFinalZeroingLinesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jCheckBoxDrawFinalZeroingLinesActionPerformed
    {//GEN-HEADEREND:event_jCheckBoxDrawFinalZeroingLinesActionPerformed
        parent.getGraphPanelChart().getChartSettings().setDrawFinalZeroingLines(jCheckBoxDrawFinalZeroingLines.isSelected());
        refreshGraphPreview();
    }//GEN-LAST:event_jCheckBoxDrawFinalZeroingLinesActionPerformed

    private void jCheckBoxDrawCurrentXActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jCheckBoxDrawCurrentXActionPerformed
    {//GEN-HEADEREND:event_jCheckBoxDrawCurrentXActionPerformed
        parent.getGraphPanelChart().getChartSettings().setDrawCurrentX(jCheckBoxDrawCurrentX.isSelected());
        refreshGraphPreview();
    }//GEN-LAST:event_jCheckBoxDrawCurrentXActionPerformed

    private void jCheckBoxMaxPointsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jCheckBoxMaxPointsActionPerformed
    {//GEN-HEADEREND:event_jCheckBoxMaxPointsActionPerformed
        if (jCheckBoxMaxPoints.isSelected())
        {
            parent.getGraphPanelChart().getChartSettings().setMaxPointPerRow(getValueFromString((String) jComboBoxMaxPoints.getSelectedItem()));
        } else
        {
            parent.getGraphPanelChart().getChartSettings().setMaxPointPerRow(-1);
        }
        refreshGraphPreview();
    }//GEN-LAST:event_jCheckBoxMaxPointsActionPerformed

    private void jComboBoxMaxPointsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jComboBoxMaxPointsActionPerformed
    {//GEN-HEADEREND:event_jComboBoxMaxPointsActionPerformed
        if (jCheckBoxMaxPoints.isSelected())
        {
            parent.getGraphPanelChart().getChartSettings().setMaxPointPerRow(getValueFromString((String) jComboBoxMaxPoints.getSelectedItem()));
        }
        refreshGraphPreview();
    }//GEN-LAST:event_jComboBoxMaxPointsActionPerformed

    private void infoLabelMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_infoLabelMouseEntered
    {//GEN-HEADEREND:event_infoLabelMouseEntered
        //increase tooltip display duration
        ToolTipManager.sharedInstance().setDismissDelay(60000);
    }//GEN-LAST:event_infoLabelMouseEntered

    private void infoLabelMouseExited(java.awt.event.MouseEvent evt)//GEN-FIRST:event_infoLabelMouseExited
    {//GEN-HEADEREND:event_infoLabelMouseExited
        ToolTipManager.sharedInstance().setDismissDelay(originalTooltipDisplayTime);
    }//GEN-LAST:event_infoLabelMouseExited

    private void jCheckBoxLimitMaxXValueActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jCheckBoxLimitMaxXValueActionPerformed
    {//GEN-HEADEREND:event_jCheckBoxLimitMaxXValueActionPerformed
        parent.getGraphPanelChart().getChartSettings().setPreventXAxisOverScaling(jCheckBoxLimitMaxXValue.isSelected());
        refreshGraphPreview();
    }//GEN-LAST:event_jCheckBoxLimitMaxXValueActionPerformed

    private void jCheckBoxHideNonRepValuesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jCheckBoxHideNonRepValuesActionPerformed
    {//GEN-HEADEREND:event_jCheckBoxHideNonRepValuesActionPerformed
        if (jCheckBoxHideNonRepValues.isSelected())
        {
            parent.getGraphPanelChart().getChartSettings().setHideNonRepValLimit(getValueFromString((String) jComboBoxHideNonRepValLimit.getSelectedItem()));
        } else
        {
            parent.getGraphPanelChart().getChartSettings().setHideNonRepValLimit(-1);
        }
        refreshGraphPreview();
    }//GEN-LAST:event_jCheckBoxHideNonRepValuesActionPerformed

    private void jComboBoxHideNonRepValLimitActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jComboBoxHideNonRepValLimitActionPerformed
    {//GEN-HEADEREND:event_jComboBoxHideNonRepValLimitActionPerformed
        if (jCheckBoxHideNonRepValues.isSelected())
        {
            parent.getGraphPanelChart().getChartSettings().setHideNonRepValLimit(getValueFromString((String) jComboBoxHideNonRepValLimit.getSelectedItem()));
        }
        refreshGraphPreview();
    }//GEN-LAST:event_jComboBoxHideNonRepValLimitActionPerformed

    private void jRadioButtonGraphAggregatedActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jRadioButtonGraphAggregatedActionPerformed
    {//GEN-HEADEREND:event_jRadioButtonGraphAggregatedActionPerformed
        parent.switchModel(true);
        refreshGraphPreview();
    }//GEN-LAST:event_jRadioButtonGraphAggregatedActionPerformed

    private void jRadioButtonGraphDetailedActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jRadioButtonGraphDetailedActionPerformed
    {//GEN-HEADEREND:event_jRadioButtonGraphDetailedActionPerformed
        parent.switchModel(false);
        refreshGraphPreview();
    }//GEN-LAST:event_jRadioButtonGraphDetailedActionPerformed

    private void jCheckBoxRelativeTimeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jCheckBoxRelativeTimeActionPerformed
    {//GEN-HEADEREND:event_jCheckBoxRelativeTimeActionPerformed
        parent.getGraphPanelChart().setUseRelativeTime(jCheckBoxRelativeTime.isSelected());
    }//GEN-LAST:event_jCheckBoxRelativeTimeActionPerformed

    private long getLongValue(String sLong)
    {
        long ret;
        try
        {
            ret = Long.valueOf(sLong);
        } catch (Exception ex)
        {
            ret = -1;
        }
        return Math.max(ret, -1);
    }

    private void jCheckBoxMaxYActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jCheckBoxMaxYActionPerformed
    {//GEN-HEADEREND:event_jCheckBoxMaxYActionPerformed
        long value = getLongValue(jTextFieldMaxY.getText());
        long oldValue = parent.getGraphPanelChart().getChartSettings().getForcedMaxY();
        if (value == -1)
        {
            jTextFieldMaxY.setForeground(Color.red);
        } else
        {
            jTextFieldMaxY.setForeground(Color.black);
        }

        if (jCheckBoxMaxY.isSelected())
        {
            parent.getGraphPanelChart().getChartSettings().setForcedMaxY(value);
            if (oldValue != value)
            {
                refreshGraphPreview();
            }
        } else
        {
            parent.getGraphPanelChart().getChartSettings().setForcedMaxY(-1);
            if (oldValue != -1)
            {
                refreshGraphPreview();
            }
        }
    }//GEN-LAST:event_jCheckBoxMaxYActionPerformed

    private void jLabelLogoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelLogoMouseClicked
        if ((evt.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK)
        {
            JMeterPluginsUtils.openInBrowser("http://code.google.com/p/jmeter-plugins/?utm_source=jmeter&utm_medium=logolink&utm_campaign=" + parent.getWikiPage());
        }
    }//GEN-LAST:event_jLabelLogoMouseClicked

    private void jComboBoxChartTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxChartTypeActionPerformed
       int selectedType = jComboBoxChartType.getSelectedIndex();
       switch(selectedType) {
          case 0:
             parent.getGraphPanelChart().getChartSettings().setChartType(ChartSettings.CHART_TYPE_LINE);
             jCheckBoxDrawMarkers.setVisible(true);
             break;
          case 1:
             parent.getGraphPanelChart().getChartSettings().setChartType(ChartSettings.CHART_TYPE_BAR);
             jCheckBoxDrawMarkers.setVisible(false);
             break;
          case 2:
             parent.getGraphPanelChart().getChartSettings().setChartType(ChartSettings.CHART_TYPE_CSPLINE);
             jCheckBoxDrawMarkers.setVisible(true);
             break;
          default:
             parent.getGraphPanelChart().getChartSettings().setChartType(ChartSettings.CHART_TYPE_DEFAULT);
             break;
       }
       refreshGraphPreview();
    }//GEN-LAST:event_jComboBoxChartTypeActionPerformed

    private void jCheckBoxDrawMarkersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxDrawMarkersActionPerformed
       if(jCheckBoxDrawMarkers.isSelected()) parent.getGraphPanelChart().getChartSettings().setChartMarkers(ChartSettings.CHART_MARKERS_YES);
       else parent.getGraphPanelChart().getChartSettings().setChartMarkers(ChartSettings.CHART_MARKERS_NO);
       refreshGraphPreview();
    }//GEN-LAST:event_jCheckBoxDrawMarkersActionPerformed

    private void jCheckBoxAutoExpandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxAutoExpandActionPerformed
        parent.getGraphPanelChart().getChartSettings().setExpendRows(jCheckBoxAutoExpand.isSelected());
        refreshGraphPreview();
    }//GEN-LAST:event_jCheckBoxAutoExpandActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupGraphType;
    private javax.swing.JCheckBox jCheckBoxAutoExpand;
    private javax.swing.JCheckBox jCheckBoxDrawCurrentX;
    private javax.swing.JCheckBox jCheckBoxDrawFinalZeroingLines;
    private javax.swing.JCheckBox jCheckBoxDrawMarkers;
    private javax.swing.JCheckBox jCheckBoxHideNonRepValues;
    private javax.swing.JCheckBox jCheckBoxLimitMaxXValue;
    private javax.swing.JCheckBox jCheckBoxMaxPoints;
    private javax.swing.JCheckBox jCheckBoxMaxY;
    private javax.swing.JCheckBox jCheckBoxPaintGradient;
    private javax.swing.JCheckBox jCheckBoxRelativeTime;
    private javax.swing.JComboBox jComboBoxChartType;
    private javax.swing.JComboBox jComboBoxGranulation;
    private javax.swing.JComboBox jComboBoxHideNonRepValLimit;
    private javax.swing.JComboBox jComboBoxMaxPoints;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelChartType;
    private javax.swing.JLabel jLabelGraphType;
    private javax.swing.JLabel jLabelHideNonRepPoints;
    private javax.swing.JLabel jLabelInfoGrpValues;
    private javax.swing.JLabel jLabelInfoMaxPoint;
    private javax.swing.JLabel jLabelLogo;
    private javax.swing.JLabel jLabelMaxPoints;
    private javax.swing.JLabel jLabelTimeline1;
    private javax.swing.JLabel jLabelTimeline2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanelAllSettingsContainer;
    private javax.swing.JPanel jPanelGraphPreview;
    private javax.swing.JPanel jPanelGraphPreviewContainer;
    private javax.swing.JPanel jPanelGraphStyle;
    private javax.swing.JPanel jPanelLogo;
    private javax.swing.JPanel jPanelRenderingOptionsContainer;
    private javax.swing.JPanel jPanelTimeLineContainer;
    private javax.swing.JRadioButton jRadioButtonGraphAggregated;
    private javax.swing.JRadioButton jRadioButtonGraphDetailed;
    private javax.swing.JTextField jTextFieldMaxY;
    // End of variables declaration//GEN-END:variables

    @Override
    public boolean isPreview()
    {
        return true;
    }

    private void registerJTextfieldForRefresh(final JTextField tf)
    {
        tf.getDocument().addDocumentListener(new DocumentListener()
        {

            @Override
            public void changedUpdate(DocumentEvent arg0)
            {
                if (tf.hasFocus())
                {
                    jCheckBoxMaxYActionPerformed(null);
                }
            }

            @Override
            public void insertUpdate(DocumentEvent arg0)
            {
                if (tf.hasFocus())
                {
                    jCheckBoxMaxYActionPerformed(null);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent arg0)
            {
                if (tf.hasFocus())
                {
                    jCheckBoxMaxYActionPerformed(null);
                }
            }
        });
    }
}
