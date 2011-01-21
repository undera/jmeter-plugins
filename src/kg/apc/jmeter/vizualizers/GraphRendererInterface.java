package kg.apc.jmeter.vizualizers;

import javax.swing.JPanel;

/**
 *
 * @author Stephane Hoblingre
 */
public interface GraphRendererInterface {
    public JPanel getGraphDisplayPanel();
    public boolean isPreview();
}
