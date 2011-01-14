/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
