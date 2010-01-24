package kg.apc.jmeter.vizualizers;

import java.awt.BorderLayout;
import java.awt.Image;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.visualizers.GraphListener;
import org.apache.jmeter.visualizers.ImageVisualizer;
import org.apache.jmeter.visualizers.Sample;
import org.apache.jmeter.visualizers.gui.AbstractVisualizer;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class ThreadsStateOverTimeGui
     extends AbstractVisualizer
     implements ImageVisualizer, GraphListener, Clearable
{
   private static final Logger log = LoggingManager.getLoggerForClass();
   private final ConcurrentHashMap<String, GraphPanelChartRow> model;
   private long lastRepaint = 0;
   private static long delay = 500;
   private GraphPanel graphPanel;
   private final ColorsDispatcher colors;

   public ThreadsStateOverTimeGui()
   {
      model = new ConcurrentHashMap<String, GraphPanelChartRow>();
      colors=new ColorsDispatcher();
      initGui();
   }

   private void initGui()
   {
      setLayout(new BorderLayout());
      add(makeTitlePanel(), BorderLayout.NORTH);
      add(createGraphPanel(), BorderLayout.CENTER);
      //add(makeControlsPanel(), BorderLayout.SOUTH);
   }

   private GraphPanel createGraphPanel()
   {
      graphPanel = new GraphPanel(model);
      return graphPanel;
   }

   public String getLabelResource()
   {
      return this.getClass().getSimpleName();
   }

   @Override
   public String getStaticLabel()
   {
      return this.getClass().getSimpleName();
   }

   public void add(SampleResult res)
   {
      String threadName = res.getThreadName();
      threadName=threadName.substring(0, threadName.lastIndexOf(" "));
      
      GraphPanelChartRow row;
      if (!model.containsKey(threadName))
      {
         row = new GraphPanelChartRow(threadName, colors.getNextColor());
         model.put(threadName, row);
         row.setDrawLine(true);
         row.setMarkerSize(GraphPanelChartRow.MARKER_SIZE_SMALL);
      }
      else
      {
         row=model.get(threadName);
      }

      row.add(System.currentTimeMillis(), res.getGroupThreads());
      updateGui(null);
      /*
      current time
      group name
      samples count
      average active threads in group
      inactive threads (max threads in group - active)
       

      //model.addSample(res);
      log.info(threadName.substring(0, threadName.lastIndexOf(" ")));
      log.info(threadName);
      log.info(Integer.toString(res.getAllThreads()));
      log.info(Integer.toString());

      ThreadGroup group = JMeterContextService.getContext().getThreadGroup();
      log.info(group.getName());
      log.info(Integer.toString(group.getNumThreads()));
      log.info(Integer.toString(group.getNumberOfThreads()));
       * 
       */
   }

   public void clearData()
   {
      model.clear();
      updateGui();
      repaint();
   }

   public Image getImage()
   {
      return graphPanel.getGraphImage();
   }

   public void updateGui(Sample sample)
   {
      // We have received one more sample
      long time = System.currentTimeMillis();
      if (time - lastRepaint >= delay)
      {
         updateGui();
         repaint();
         lastRepaint = time;
      }
   }

   public void updateGui()
   {
      graphPanel.updateGui();
   }
}
