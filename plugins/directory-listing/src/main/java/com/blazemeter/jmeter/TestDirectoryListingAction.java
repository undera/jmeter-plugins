package com.blazemeter.jmeter;

import org.apache.jmeter.engine.util.CompoundVariable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class TestDirectoryListingAction implements ActionListener {

    private final StringBuilder builder = new StringBuilder();
    private final CompoundVariable compoundVariable = new CompoundVariable();

    private final DirectoryListingConfigGui directoryListingConfigGui;

    public TestDirectoryListingAction(DirectoryListingConfigGui fileListingGui) {
        this.directoryListingConfigGui = fileListingGui;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        final DirectoryListingConfig config = (DirectoryListingConfig) directoryListingConfigGui.createTestElement();

        JTextArea checkArea = directoryListingConfigGui.getCheckArea();

        builder.setLength(0);

        try {
            compoundVariable.setParameters(config.getSourceDirectory());
            config.setSourceDirectory(compoundVariable.execute());

            compoundVariable.setParameters(config.getDestinationVariableName());
            config.setDestinationVariableName(compoundVariable.execute());

            final List<File> files = config.createDirectoryListingIterator().getDirectoryListing();

            if (config.getRandomOrder()) {
                DirectoryListingIterator.shuffleList(files);
            }

            String variableName = config.getDestinationVariableName();

            builder.append("Listing of directory successfully finished, ").append(files.size()).append(" files found:\r\n");

            for (File file : files) {
                builder.append("${").append(variableName).append("} = ");
                builder.append(config.getFilePath(file));
                builder.append("\r\n");
            }

            checkArea.setText(builder.toString());
            // move scroll to top
            checkArea.setCaretPosition(0);
        } catch (Exception e) {
            checkArea.setText(e.getMessage());
        }
    }

}
