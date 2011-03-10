package kg.apc.jmeter.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import org.apache.jmeter.gui.util.FileDialoger;

public class BrowseAction implements ActionListener {

    private final JTextField control;

    public BrowseAction(JTextField filename) {
        control = filename;
    }

    public void actionPerformed(ActionEvent e) {
        String path = "";
        JFileChooser chooser = getFileChooser();
        if (chooser != null) {
            //File f=new File(control.getText());
            //chooser.setSelectedFile(f);
            File file = chooser.getSelectedFile();
            if (file != null) {
                path = file.getPath();
                control.setText(path);
            }
        }
    }

    protected JFileChooser getFileChooser() {
        return FileDialoger.promptToOpenFile();
    }
}
