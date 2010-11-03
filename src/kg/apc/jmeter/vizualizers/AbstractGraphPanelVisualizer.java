// todo: add spacing around panel
package kg.apc.jmeter.vizualizers;

import java.awt.BorderLayout;
import java.awt.Image;
import java.util.concurrent.ConcurrentSkipListMap;
import javax.swing.JComponent;
import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.charting.GraphPanelChart;
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
   private static final long REPAINT_INTERVAL = 500;
   public static final String INTERVAL_PROPERTY = "interval_grouping";

   private JSettingsPanel settingsPanel = null;

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
      settingsPanel = new JSettingsPanel(this);
      graphPanel.getSettingsTab().add(settingsPanel, BorderLayout.CENTER);
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

   protected long getGranulation()
   {
      return interval;
   }

    public void setGranulation(long i)
    {
        if (i < 1)
        {
            throw new IllegalArgumentException("Interval cannot be less than zero");
        }
        interval = i;
        settingsPanel.setGranulationValue((int) i);
    }

    protected JComponent getGraphSettingsPanel() {
        return settingsPanel;
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

   public GraphPanelChart getGraphPanelChart() {
       return graphPanel.getGraphObject();
   }
}
