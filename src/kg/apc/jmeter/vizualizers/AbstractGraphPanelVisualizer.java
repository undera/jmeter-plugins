// todo: add spacing around panel
package kg.apc.jmeter.vizualizers;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.ConcurrentSkipListMap;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import kg.apc.jmeter.charting.AbstractGraphRow;
import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.LongProperty;
import org.apache.jmeter.visualizers.GraphListener;
import org.apache.jmeter.visualizers.ImageVisualizer;
import org.apache.jmeter.visualizers.Sample;
import org.apache.jmeter.visualizers.gui.AbstractVisualizer;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author apc
 */
public abstract class AbstractGraphPanelVisualizer
      extends AbstractVisualizer
      implements Clearable,
                 GraphListener,
                 ImageVisualizer
{
   private static final Logger log = LoggingManager.getLoggerForClass();
   /**
    *
    */
   protected ConcurrentSkipListMap<String, AbstractGraphRow> model;
   /**
    *
    */
   protected long lastRepaint = 0;
   /**
    *
    */
   private long interval = 500;
   /**
    *
    */
   protected GraphPanel graphPanel;
   /**
    *
    */
   protected ColorsDispatcher colors;
   protected JTextField intervalField;
   private static final long REPAINT_INTERVAL = 500;
   public static final String INTERVAL_PROPERTY = "interval_grouping";

   /**
    *
    */
   public AbstractGraphPanelVisualizer()
   {
      model = new ConcurrentSkipListMap<String, AbstractGraphRow>();
      colors = new ColorsDispatcher();
      initGui();
   }

   private void initGui()
   {
      setLayout(new BorderLayout());
      add(makeTitlePanel(), BorderLayout.NORTH);
      add(createGraphPanel(), BorderLayout.CENTER);
      graphPanel.getSettingsTab().add(getGraphSettingsPanel(), BorderLayout.CENTER);
   }

   /**
    *
    * @return
    */
   protected GraphPanel createGraphPanel()
   {
      graphPanel = new GraphPanel();
      graphPanel.getGraphObject().setRows(model);
      return graphPanel;
   }

   /**
    *
    * @param sample
    */
   public void updateGui(Sample sample)
   {
      long time = System.currentTimeMillis();
      if ((time - lastRepaint) >= REPAINT_INTERVAL)
      {
         updateGui();
         repaint();
         lastRepaint = time;
      }
   }

   /**
    *
    */
   public void updateGui()
   {
      graphPanel.updateGui();
   }

   public void clearData()
   {
      model.clear();
      colors.reset();
      graphPanel.clearRowsTab();
      updateGui();
      repaint();
   }

   /**
    *
    * @return
    */
   public Image getImage()
   {
      return graphPanel.getGraphImage();
   }

   protected JComponent getGraphSettingsPanel()
   {
      JPanel panel = new JPanel(new FlowLayout());
      panel.setBorder(BorderFactory.createTitledBorder("Timeline settings"));
      panel.add(new JLabel("Group values for (msec):"));
      intervalField = new JTextField(Long.toString(interval), 10);
      intervalField.addKeyListener(new IntervalChangeListener());
      panel.add(intervalField);
      return panel;
   }

   protected long getGranulation()
   {
      return interval;
   }

   protected void setGranulation(long i)
   {
      if (i < 1)
      {
         throw new IllegalArgumentException("Interval cannot be less than zero");
      }
      interval = i;
      if (intervalField != null)
      {
         intervalField.setText(Long.toString(interval));
      }
   }

   private class IntervalChangeListener
         implements KeyListener
   {
      public void keyTyped(KeyEvent e)
      {
      }

      public void keyPressed(KeyEvent e)
      {
      }

      public void keyReleased(KeyEvent e)
      {
         long i = Long.parseLong(intervalField.getText());
         interval = i > 0 ? i : 1;
      }
   }

   @Override
   public TestElement createTestElement()
   {
      TestElement el = super.createTestElement();
      return el;
   }

   @Override
   public void modifyTestElement(TestElement c)
   {
      super.modifyTestElement(c);
      c.setProperty(new LongProperty(INTERVAL_PROPERTY, interval));
   }

   @Override
   public void configure(TestElement el)
   {
      super.configure(el);
      long intervalProp = el.getPropertyAsLong(INTERVAL_PROPERTY);
      if (intervalProp > 0)
      {
         setGranulation(intervalProp);
      }
   }
}
