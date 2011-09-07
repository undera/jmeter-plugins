package kg.apc.jmeter.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Simple class to help building GUI
 * @author Stephane Hoblingre
 */

public class GuiBuilderHelper {
   
   public static JScrollPane getTextAreaScrollPaneContainer(JTextArea textArea, int nbLines) {
      JScrollPane ret = new JScrollPane();
      textArea.setRows(nbLines);
      textArea.setColumns(20);
      ret.setViewportView(textArea);
      return ret;
   }

   public static void strechButtonToComponent(JComponent component, JButton button) {
      int bWidth = (int)button.getPreferredSize().getWidth();
      int bHeight = (int)component.getPreferredSize().getHeight();
      button.setPreferredSize(new Dimension(bWidth, bHeight));
   }

   public static JPanel getComponentWithMargin(Component component, int top, int left, int bottom, int right) {
      JPanel ret = new JPanel(new GridBagLayout());
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.weightx = 1.0;
      constraints.weighty = 1.0;
      constraints.fill = GridBagConstraints.BOTH;
      constraints.insets = new Insets(top, left, bottom, right);
      ret.add(component, constraints);
      return ret;
   }
}
