package kg.apc.jmeter.gui;

import org.apache.jmeter.services.FileServer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import org.apache.jmeter.gui.GuiPackage;

public class BrowseAction implements ActionListener {

    private final JTextField control;
    private boolean isDirectoryBrowse = false;
    private String lastPath = ".";

    public BrowseAction(JTextField filename) {
        this.control = filename;
    }

    public BrowseAction(JTextField filename, boolean isDirectoryBrowse) {
        this.control = filename;
        this.isDirectoryBrowse = isDirectoryBrowse;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = getFileChooser();
        if (chooser != null && GuiPackage.getInstance() != null) {
            int returnVal = chooser.showOpenDialog(GuiPackage.getInstance().getMainFrame());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                control.setText(chooser.getSelectedFile().getPath());
            }
            lastPath = chooser.getCurrentDirectory().getPath();
        }
    }

    protected JFileChooser getFileChooser() {
        String baseDir = FileServer.getFileServer().getBaseDir();
        File initialDirectory = baseDir != null && !baseDir.isEmpty()
                ? new File(baseDir)
                : new File(lastPath);

        JFileChooser ret = new JFileChooser(initialDirectory);
        if (isDirectoryBrowse) {
            ret.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        }
        return ret;
    }
}