package kg.apc.jmeter.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

/**
 *
 * @author Stephane Hoblingre
 */
public class DialogFactory {

    public static JDialog getJDialogInstance(Frame owner, String title, boolean modal, JAbsrtactDialogPanel content, String imagePath) {
        if(!GraphicsEnvironment.isHeadless()) {
            JDialog ret = new JDialog(owner, title, modal);
            ret.add(content);
            ret.pack();
            Dimension size = ret.getPreferredSize();
            if(size.width < content.getMinWidth()) {
                size.width = content.getMinWidth();
            }
            ret.setSize(size);
            ret.validate();
            if(imagePath != null) {
                ImageIcon imageIcon = new ImageIcon(DialogFactory.class.getResource(imagePath));
                if(imageIcon != null) {
                    ret.setIconImage(imageIcon.getImage());
                }
            }
            return ret;
        } else {
            return null;
        }
    }

    public static void centerDialog(Frame parent, JDialog dialog) {
        if(parent != null && dialog != null) {
            dialog.setLocation(parent.getLocation().x + (parent.getSize().width - dialog.getSize().width) / 2,
                        parent.getLocation().y + (parent.getSize().height - dialog.getSize().height) / 2);
        }
    }
}
