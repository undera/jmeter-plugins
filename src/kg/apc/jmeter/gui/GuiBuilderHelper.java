package kg.apc.jmeter.gui;

import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JComponent;
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
}
