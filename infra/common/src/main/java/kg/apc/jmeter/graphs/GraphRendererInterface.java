package kg.apc.jmeter.graphs;

import javax.swing.JPanel;

public interface GraphRendererInterface {
    public JPanel getGraphDisplayPanel();
    public boolean isPreview();
}
