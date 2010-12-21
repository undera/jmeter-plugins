// todo: add spacing around panel
package kg.apc.jmeter.vizualizers;

import java.awt.BorderLayout;
import java.awt.Image;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListMap;
import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.charting.GraphPanelChart;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.BooleanProperty;
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
                 ImageVisualizer,
                 SettingsInterface                 
{
   private static final Logger log = LoggingManager.getLoggerForClass();
   /**
    *
    */
   protected ConcurrentSkipListMap<String, AbstractGraphRow> model;
   protected ConcurrentSkipListMap<String, AbstractGraphRow> modelAggregate;
   /**
    *
    */
   protected long lastRepaint = 0;
   /**
    *
    */
   private int interval = 500;

   private boolean isAggregate = false;
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
   public static final String GRAPH_AGGRAGATED = "graph_aggregated";

   private JSettingsPanel settingsPanel = null;

   /**
    *
    */
   public AbstractGraphPanelVisualizer()
   {
      super();
      model = new ConcurrentSkipListMap<String, AbstractGraphRow>();
      modelAggregate = new ConcurrentSkipListMap<String, AbstractGraphRow>();
      colors = new ColorsDispatcher();
      setModel(new RowsProviderResultCollector(model));
      initGui();
   }

   protected abstract JSettingsPanel getSettingsPanel();

   private void initGui()
   {
      setLayout(new BorderLayout());
      add(makeTitlePanel(), BorderLayout.NORTH);
      add(createGraphPanel(), BorderLayout.CENTER);
      settingsPanel = getSettingsPanel();
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
   @Override
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
   @Override
   public void updateGui()
   {
      graphPanel.updateGui();
   }

   @Override
   public void clearData()
   {
      model.clear();
      modelAggregate.clear();
      colors.reset();
      graphPanel.clearRowsTab();
      updateGui();
      repaint();
   }

   /**
    *
    * @return
    */
    @Override
    public Image getImage()
    {
       return graphPanel.getGraphImage();
    }

    @Override
    public int getGranulation()
    {
       return interval;
    }

    @Override
    public void setGranulation(int granulation)
    {
        if (granulation < 1)
        {
            throw new IllegalArgumentException("Interval cannot be less than 1");
        }
        interval = granulation;
        settingsPanel.setGranulationValue(granulation);
    }

   @Override
   public TestElement createTestElement()
   {
        ResultCollector aModel = getModel();
        if (aModel == null) {
            aModel = new RowsProviderResultCollector(model);
            setModel(aModel);
        }
        modifyTestElement(aModel);
        return aModel;
   }

   @Override
   public void modifyTestElement(TestElement c)
   {
      super.modifyTestElement(c);
      c.setProperty(new LongProperty(INTERVAL_PROPERTY, interval));
      c.setProperty(new BooleanProperty(GRAPH_AGGRAGATED, isAggregate));
   }

   @Override
   public void configure(TestElement el)
   {
      super.configure(el);
      int intervalProp = el.getPropertyAsInt(INTERVAL_PROPERTY);
      boolean aggregatedProp = el.getPropertyAsBoolean(GRAPH_AGGRAGATED, false);
      if (intervalProp > 0)
      {
         setGranulation(intervalProp);
      }
      switchModel(aggregatedProp);
   }

   @Override
   public GraphPanelChart getGraphPanelChart() {
       return graphPanel.getGraphObject();
   }

   @Override
   public void switchModel(boolean aggregate) {

       ConcurrentSkipListMap<String, AbstractGraphRow> selectedModel;
       if(aggregate)
       {
           selectedModel = modelAggregate;
       } else
       {
           selectedModel = model;
       }

       graphPanel.getGraphObject().setRows(selectedModel);
       graphPanel.clearRowsTab();

       Iterator<AbstractGraphRow> rowsIter = selectedModel.values().iterator();
       while(rowsIter.hasNext())
       {
           graphPanel.addRow(rowsIter.next());
       }

       isAggregate = aggregate;
       settingsPanel.setAggregateMode(aggregate);
   }

    public Collection<String> getRowNames() {
        return model.keySet();
    }

    public AbstractGraphRow getRowByName(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
