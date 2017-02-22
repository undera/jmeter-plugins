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
        final DirectoryListingConfig config = (DirectoryListingConfig) directoryListingConfigGui.createTestElement();

        JTextArea checkArea = directoryListingConfigGui.getCheckArea();

        try {
            final DirectoryListingIterator listingIterator  = config.createDirectoryListingIterator();

            final List<File> files = listingIterator.getDirectoryListing();

            String variableName = config.getDestinationVariableName();

            final StringBuilder builder = new StringBuilder();

            builder.append("Listing of directory successfully finished, ").append(files.size()).append(" files found:\r\n");

            for (File file : files) {
                builder.append("${").append(variableName).append("} = ");
                builder.append(config.getFilePath(file));
                builder.append("\r\n");
            }

            checkArea.setText(builder.toString());
            // move scroll to top
            checkArea.setCaretPosition(0);
        } catch (RuntimeException e) {
            checkArea.setText(e.getMessage());
        }
    }
}
