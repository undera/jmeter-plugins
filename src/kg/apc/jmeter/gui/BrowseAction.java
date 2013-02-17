package kg.apc.jmeter.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import org.apache.jmeter.gui.GuiPackage;

public class BrowseAction implements ActionListener {

    private final JTextField control;
    private boolean isDirectoryBrowse = false;
    private String lastPath = ".";

    public BrowseAction(JTextField filename) {
        control = filename;
    }

    public BrowseAction(JTextField filename, boolean isDirectoryBrowse) {
        control = filename;
        this.isDirectoryBrowse = isDirectoryBrowse;
    }

   @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = getFileChooser();
        if (chooser != null) {
            if(GuiPackage.getInstance() != null) {
                int returnVal = chooser.showOpenDialog(GuiPackage.getInstance().getMainFrame());
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                   control.setText(chooser.getSelectedFile().getPath());
                }
                lastPath = chooser.getCurrentDirectory().getPath();
            }
        }
    }

    protected JFileChooser getFileChooser() {
        JFileChooser ret = new JFileChooser(lastPath);
        if(isDirectoryBrowse) ret.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        return ret;
    }
}
