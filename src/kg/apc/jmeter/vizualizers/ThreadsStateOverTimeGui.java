package kg.apc.jmeter.vizualizers;

import java.awt.BorderLayout;
import java.awt.Image;
import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.visualizers.GraphListener;
import org.apache.jmeter.visualizers.ImageVisualizer;
import org.apache.jmeter.visualizers.Sample;
import org.apache.jmeter.visualizers.gui.AbstractVisualizer;

public class ThreadsStateOverTimeGui
     extends AbstractVisualizer
     implements ImageVisualizer, GraphListener, Clearable
{
   private ThreadsStateOverTimeModel model;
   private long lastRepaint=0;
   private static long delay = 500;
   private GraphPanel graphPanel;

   public ThreadsStateOverTimeGui()
   {
      model = new ThreadsStateOverTimeModel();
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
      graphPanel=new GraphPanel();
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
      model.addSample(res);
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
