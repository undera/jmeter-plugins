// TODO: сделать прореживание рядов вдвое

package kg.apc.jmeter.vizualizers;

import java.awt.BorderLayout;
import java.awt.Image;
import java.util.concurrent.ConcurrentSkipListMap;
import kg.apc.jmeter.charting.AbstractGraphRow;
import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.visualizers.GraphListener;
import org.apache.jmeter.visualizers.ImageVisualizer;
import org.apache.jmeter.visualizers.Sample;
import org.apache.jmeter.visualizers.gui.AbstractVisualizer;

public abstract class AbstractGraphPanelVisualizer
     extends AbstractVisualizer
     implements Clearable, GraphListener, ImageVisualizer
{
   protected ConcurrentSkipListMap<String, AbstractGraphRow> model;
   protected long lastRepaint = 0;
   protected long delay = 500;
   protected GraphPanel graphPanel;
   protected ColorsDispatcher colors;

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
   }

   protected GraphPanel createGraphPanel()
   {
      graphPanel = new GraphPanel();
      graphPanel.getGraphObject().setRows(model);
      return graphPanel;
   }

   public void updateGui(Sample sample)
   {
      long time = System.currentTimeMillis();
      if ((time - lastRepaint) >= delay)
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

   public void clearData()
   {
      model.clear();
      colors.reset();
      graphPanel.clearRowsTab();
      updateGui();
      repaint();
   }

   public Image getImage()
   {
      return graphPanel.getGraphImage();
   }
}
