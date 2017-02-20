package com.blazemeter.jmeter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class TestDirectoryListingAction implements ActionListener {

    private final DirectoryListingConfigGui directoryListingConfigGui;

    public TestDirectoryListingAction(DirectoryListingConfigGui fileListingGui) {
        this.directoryListingConfigGui = fileListingGui;
    }


    @Override
    public void actionPerformed(ActionEvent event) {
        String path = directoryListingConfigGui.getSourceDirectoryField().getText();

        boolean isRandomOrder = directoryListingConfigGui.getIsRandomOrderCheckBox().isSelected();
        boolean isRecursiveListing = directoryListingConfigGui.getIsRecursiveListing().isSelected();


        JTextArea checkArea = directoryListingConfigGui.getCheckArea();

        try {
            final List<File> files = DirectoryListingConfig.getDirectoryListing(path, isRandomOrder, isRecursiveListing);

            boolean isUseFullPath = directoryListingConfigGui.getIsUseFullPathCheckBox().isSelected();
            String variableName = directoryListingConfigGui.getDestinationVariableField().getText();

            final StringBuilder builder = new StringBuilder();

            builder.append("Listing of directory successfully finished, ").append(files.size()).append(" files found:\r\n");

            for (File file : files) {
                builder.append("${").append(variableName).append("} = ");
                builder.append(isUseFullPath ? file.getAbsolutePath() : file.getName());
                builder.append("\r\n");
            }

            checkArea.setText(builder.toString());
            // move scroll to top
            checkArea.setCaretPosition(0);
        } catch (FileNotFoundException e) {
            checkArea.setText(e.getMessage());
        }
    }
}
