// TODO: сэмплы при распределенном тестировании можно определить по имени хоста и вычислить истинное количество тестовых юзеров

package kg.apc.jmeter.dotchart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.visualizers.GraphListener;
import org.apache.jmeter.visualizers.ImageVisualizer;
import org.apache.jmeter.visualizers.Sample;
import org.apache.jmeter.visualizers.gui.AbstractVisualizer;

public class DotChartVisualizer
   extends AbstractVisualizer
   implements ImageVisualizer, GraphListener, Clearable
{

   private DotChartModel model;
   private JPanel graphPanel = null;
   private DotChart graph;
   private int delay = 50;
   private long lastRepaint = 0;
   private DotChartGuiPanel controlsPanel;

   public DotChartVisualizer()
   {
      model = new DotChartModel();
      graph = new DotChart(model);
      graph.setBackground(Color.white);
      init();
   }

   /**
    * Gets the Image attribute of the GraphVisualizer object.
    *
    * @return the Image value
    */
   public Image getImage()
   {
      Image result = graph.createImage(graph.getWidth(), graph.getHeight());

      if (result != null)
      {
         graph.paintComponent(result.getGraphics());
      }

      return result;
   }

   public synchronized void updateGui()
   {
      int newWidth = graph.getParent().getWidth() - 50;
      int newHeight = graph.getParent().getHeight() - 50;

      if (graph.getWidth() != newWidth || graph.getHeight() != newHeight)
      {
         graph.setPreferredSize(new Dimension(newWidth, newHeight));
         //controlsPanel.setPreferredSize(new Dimension(50, Integer.MAX_VALUE));
      }

      graphPanel.updateUI();
      graph.repaint();
   }

   public synchronized void updateGui(Sample s)
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

   public synchronized void add(SampleResult res)
   {
      model.addSample(res);
      graph.setCurrentThreads(res.getAllThreads());
      updateGui();
   }

   @Override
   public String getStaticLabel()
   {
      return "Samples vs Active Threads";
   }

   public String getLabelResource()
   {
      return "dot_graph_title"; // $NON-NLS-1$
   }

   public synchronized void clearData()
   {
      this.graph.clearData();
      model.clear();
      repaint();
   }

   @Override
   public String toString()
   {
      return "Show the samples in a nice graph";
   }

   /**
    * Initialize the GUI.
    */
   private void init()
   {
      setLayout(new BorderLayout());
      add(makeTitlePanel(), BorderLayout.NORTH);
      add(createGraphPanel(), BorderLayout.CENTER);
      add(makeControlsPanel(), BorderLayout.SOUTH);
   }

   private Component makeControlsPanel()
   {
      JPanel guiPanel = new JPanel(new FlowLayout());
      controlsPanel = new DotChartGuiPanel(graph);
      guiPanel.add(controlsPanel);

      JPanel topPanel = new JPanel();
      topPanel.add(guiPanel);

      return controlsPanel;
   }

   // Methods used in creating the GUI
   /**
    * Creates a scroll pane containing the actual graph of the results.
    *
    * @return a scroll pane containing the graph
    */
   private Component createGraphPanel()
   {
      graphPanel = new JPanel(new BorderLayout());
      graphPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.lightGray, Color.darkGray));
      graphPanel.add(graph, BorderLayout.CENTER);
      graphPanel.setBackground(Color.white);
      return graphPanel;
   }

   /**
    * Method implements Printable, which is suppose to return the correct
    * internal component. The Action class can then print or save the graphics
    * to a file.
    */
   @Override
   public JComponent getPrintableComponent()
   {
      return this.graph;
   }
}
