package kg.apc.jmeter.gui;

import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Stephane Hoblingre
 */
public abstract class JAbsrtactDialogPanel extends JPanel{
    private int minWidth = 0;

    public int getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    protected void repack() {
        JDialog dlgParent = getAssociatedDialog();
        if(dlgParent != null) {
            Dimension newSize = dlgParent.getPreferredSize();
            if(newSize.width < minWidth) {
                newSize.width = minWidth;
            }
            dlgParent.setSize(newSize);
            dlgParent.validate();
        }
    }

    protected JDialog getAssociatedDialog() {
        return (JDialog)SwingUtilities.getWindowAncestor(this);
    }

}
