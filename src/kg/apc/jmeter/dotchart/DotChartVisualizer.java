package kg.apc.jmeter.dotchart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

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
   private int delay = 1000;
   private long lastRepaint = 0;

   public DotChartVisualizer()
   {
      super();
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

      graph.paintComponent(result.getGraphics());

      return result;
   }

   public synchronized void updateGui()
   {
      long time = System.currentTimeMillis();
      if (time - lastRepaint < delay)
         return;

      lastRepaint = time;
      if (graph.getWidth() < 10)
         graph.setPreferredSize(new Dimension(getWidth() - 40, getHeight() - 160));
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
      updateGui();
   }

   @Override
   public String getStaticLabel()
   {
      return "Dot Chart";
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
      return "Show the samples in a dot graph";
   }

   /**
    * Initialize the GUI.
    */
   private void init()
   {
      JPanel lgraphPanel = new JPanel(new BorderLayout());
      lgraphPanel.add(createGraphPanel(), BorderLayout.CENTER);

      JPanel guiPanel = new JPanel(new BorderLayout());
      guiPanel.add(new DotChartGuiPanel(graph), BorderLayout.CENTER);

      JPanel topPanel=new JPanel(new BorderLayout());
      topPanel.add(makeTitlePanel(), BorderLayout.NORTH);
      topPanel.add(guiPanel, BorderLayout.SOUTH);

      this.setLayout(new BorderLayout());
      Border margin = new EmptyBorder(10, 10, 5, 10);
      this.setBorder(margin);
      this.add(topPanel, BorderLayout.NORTH);
      this.add(lgraphPanel, BorderLayout.CENTER);
   }

   // Methods used in creating the GUI
   /**
    * Creates a scroll pane containing the actual graph of the results.
    *
    * @return a scroll pane containing the graph
    */
   private Component createGraphPanel()
   {
      graphPanel = new JPanel();
      graphPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.lightGray, Color.darkGray));
      graphPanel.add(graph);
      graphPanel.setBackground(Color.white);
      return graphPanel;
   }

   /**
    * Creates a label for one of the fields used to display the graph's current
    * values. Neither the label created by this method or the
    * <code>field</code> passed as a parameter is added to the GUI here.
    *
    * @param labelResourceName
    *            the name of the label resource. This is used to look up the
    *            label text using {@link JMeterUtils#getResString(String)}.
    * @param field
    *            the field this label is being created for.
    */
   /*
   private JLabel createInfoLabel(String labelResourceName, JTextField field)
   {
   JLabel label = new JLabel(JMeterUtils.getResString(labelResourceName));
   label.setForeground(field.getForeground());
   label.setLabelFor(field);
   return label;
   }
    */
   /**
    * Creates the information Panel at the bottom
    *
    * @return
    */
   /*
   private Box createGraphInfoPanel()
   {
   Box graphInfoPanel = Box.createHorizontalBox();
   this.noteField = new JTextField();
   graphInfoPanel.add(this.createInfoLabel("distribution_note1", this.noteField)); // $NON-NLS-1$
   return graphInfoPanel;
   }
    */
   /**
    * Method implements Printable, which is suppose to return the correct
    * internal component. The Action class can then print or save the graphics
    * to a file.
    */
   @Override
   public JComponent getPrintableComponent()
   {
      return this.graphPanel;
   }
}
