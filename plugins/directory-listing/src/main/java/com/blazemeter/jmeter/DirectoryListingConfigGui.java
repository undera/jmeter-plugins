package com.blazemeter.jmeter;

import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.gui.BrowseAction;
import kg.apc.jmeter.gui.GuiBuilderHelper;
import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DirectoryListingConfigGui extends AbstractConfigGui {
    public static final String WIKIPAGE = "DirectoryListing";

    private JTextField sourceDirectoryField;
    private JButton browseButton;

    private JTextField destinationVariableField;

    private JCheckBox isUseFullPathCheckBox;
    private JCheckBox isRecursiveListing;
    private JCheckBox isRandomOrderCheckBox;
    private JCheckBox isRewindOnTheEndCheckBox;
    private JCheckBox isIndependentListCheckBox;
    private JCheckBox isReReadDirectoryCheckBox;

    private JButton checkButton;
    private JTextArea checkArea;


    public DirectoryListingConfigGui() {
        initGui();
        initGuiValues();
    }

    private void initGui() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

        Container topPanel = makeTitlePanel();

        add(JMeterPluginsUtils.addHelpLinkToPanel(topPanel, WIKIPAGE), BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.anchor = GridBagConstraints.FIRST_LINE_END;

        GridBagConstraints editConstraints = new GridBagConstraints();
        editConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        editConstraints.weightx = 1.0;
        editConstraints.fill = GridBagConstraints.HORIZONTAL;

        addToPanel(mainPanel, labelConstraints, 0, 0, new JLabel("Source directory: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 0, sourceDirectoryField = new JTextField(20));
        addToPanel(mainPanel, labelConstraints, 2, 0, browseButton = new JButton("Browse..."));

        GuiBuilderHelper.strechItemToComponent(sourceDirectoryField, browseButton);

        editConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(2, 0, 0, 0);

        browseButton.addActionListener(new BrowseAction(sourceDirectoryField, true));

        addToPanel(mainPanel, labelConstraints, 0, 1, new JLabel("Destination variable name: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 1, destinationVariableField = new JTextField(20));

        addToPanel(mainPanel, labelConstraints, 0, 2, new JLabel("Use full path: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 2, isUseFullPathCheckBox = new JCheckBox());

        addToPanel(mainPanel, labelConstraints, 0, 3, new JLabel("Random order: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 3, isRandomOrderCheckBox = new JCheckBox());

        addToPanel(mainPanel, labelConstraints, 0, 4, new JLabel("Recursive listing: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 4, isRecursiveListing = new JCheckBox());

        addToPanel(mainPanel, labelConstraints, 0, 5, new JLabel("Rewind on end of list: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 5, isRewindOnTheEndCheckBox = new JCheckBox());

        addToPanel(mainPanel, labelConstraints, 0, 6, new JLabel("Re-read directory on end of list: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 6, isReReadDirectoryCheckBox = new JCheckBox());

        addToPanel(mainPanel, labelConstraints, 0, 7, new JLabel("Independent list per thread: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 7, isIndependentListCheckBox = new JCheckBox());

        isRewindOnTheEndCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                isReReadDirectoryCheckBox.setEnabled(isRewindOnTheEndCheckBox.isSelected());
            }
        });


        editConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(4, 0, 0, 2);

        addToPanel(mainPanel, labelConstraints, 0, 8, checkButton = new JButton("Test Directory Listing"));

        labelConstraints.insets = new java.awt.Insets(4, 0, 0, 0);

        checkArea = new JTextArea();
        addToPanel(mainPanel, editConstraints, 1, 8, GuiBuilderHelper.getTextAreaScrollPaneContainer(checkArea, 10));
        checkButton.addActionListener(new TestDirectoryListingAction(this));
        checkArea.setEditable(false);
        checkArea.setOpaque(false);


        JPanel container = new JPanel(new BorderLayout());
        container.add(mainPanel, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);
    }

    private void initGuiValues() {
        sourceDirectoryField.setText("");
        destinationVariableField.setText("");
        isUseFullPathCheckBox.setSelected(false);
        isRandomOrderCheckBox.setSelected(false);
        isRecursiveListing.setSelected(false);
        isRewindOnTheEndCheckBox.setSelected(true);
        isIndependentListCheckBox.setSelected(false);
        isReReadDirectoryCheckBox.setSelected(false);
        checkArea.setText("");
    }

    private void addToPanel(JPanel panel, GridBagConstraints constraints, int col, int row, JComponent component) {
        constraints.gridx = col;
        constraints.gridy = row;
        panel.add(component, constraints);
    }

    @Override
    public String getLabelResource() {
        return "directory_listing_data_source";
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Directory Listing Data Source");
    }

    @Override
    public TestElement createTestElement() {
        DirectoryListingConfig element = new DirectoryListingConfig();
        modifyTestElement(element);
        return element;
    }

    @Override
    public void modifyTestElement(TestElement element) {
        configureTestElement(element);

        if (element instanceof DirectoryListingConfig) {
            DirectoryListingConfig directoryListingConfig = (DirectoryListingConfig) element;

            directoryListingConfig.setSourceDirectory(this.sourceDirectoryField.getText());
            directoryListingConfig.setDestinationVariableName(this.destinationVariableField.getText());
            directoryListingConfig.setUseFullPath(this.isUseFullPathCheckBox.isSelected());
            directoryListingConfig.setRandomOrder(this.isRandomOrderCheckBox.isSelected());
            directoryListingConfig.setRecursiveListing(this.isRecursiveListing.isSelected());
            directoryListingConfig.setRewindOnTheEnd(this.isRewindOnTheEndCheckBox.isSelected());
            directoryListingConfig.setReReadDirectoryOnTheEndOfList(this.isReReadDirectoryCheckBox.isSelected());
            directoryListingConfig.setIndependentListPerThread(this.isIndependentListCheckBox.isSelected());
        }
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);

        if (element instanceof DirectoryListingConfig) {
            DirectoryListingConfig directoryListingConfig = (DirectoryListingConfig) element;

            sourceDirectoryField.setText(directoryListingConfig.getSourceDirectory());
            destinationVariableField.setText(directoryListingConfig.getDestinationVariableName());
            isUseFullPathCheckBox.setSelected(directoryListingConfig.getUseFullPath());
            isRandomOrderCheckBox.setSelected(directoryListingConfig.getRandomOrder());
            isRecursiveListing.setSelected(directoryListingConfig.getRecursiveListing());
            isRewindOnTheEndCheckBox.setSelected(directoryListingConfig.getRewindOnTheEnd());
            isReReadDirectoryCheckBox.setSelected(directoryListingConfig.getReReadDirectoryOnTheEndOfList());
            isIndependentListCheckBox.setSelected(directoryListingConfig.getIndependentListPerThread());

            isReReadDirectoryCheckBox.setEnabled(isRewindOnTheEndCheckBox.isSelected());

        }
    }

    @Override
    public void clearGui() {
        super.clearGui();
        initGuiValues();
    }

    public JTextArea getCheckArea() {
        return checkArea;
    }

}
