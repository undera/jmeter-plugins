package kg.apc.jmeter.threads;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import kg.apc.jmeter.vizualizers.AbstractGraphRow;
import kg.apc.jmeter.vizualizers.DateTimeRenderer;
import kg.apc.jmeter.vizualizers.GraphPanelChart;
import kg.apc.jmeter.vizualizers.GraphRowAverages;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.threads.gui.AbstractThreadGroupGui;
import org.apache.jorphan.collections.HashTree;

public class SteppingThreadGroupGui
      extends AbstractThreadGroupGui
{
   protected ConcurrentHashMap<String, AbstractGraphRow> model;
   private GraphPanelChart chart;

   public SteppingThreadGroupGui()
   {
      init();
   }

   @Override
   protected void init()
   {
      super.init();
      chart=new GraphPanelChart();
      model=new ConcurrentHashMap<String, AbstractGraphRow>();
      chart.setRows(model);
      chart.setDrawFinalZeroingLines(true);
      chart.setxAxisLabelRenderer(new DateTimeRenderer("HH:mm:ss"));
      add(chart, BorderLayout.CENTER);
   }

   public String getLabelResource()
   {
      return this.getClass().getSimpleName();
   }

   @Override
   public String getStaticLabel()
   {
      return "Stepping Thread Group";
   }

   public TestElement createTestElement()
   {
      SteppingThreadGroup tg = new SteppingThreadGroup();
      modifyTestElement(tg);
      return tg;
   }

   public void modifyTestElement(TestElement tg)
   {
      super.configureTestElement(tg);
      if (tg instanceof SteppingThreadGroup)
      {
         updateChart((SteppingThreadGroup) tg);
      }
   }

   @Override
   public void configure(TestElement tg)
   {
      super.configure(tg);
   }

   private void updateChart(SteppingThreadGroup tg)
   {
      model.clear();
      GraphRowAverages row=new GraphRowAverages();
      row.setColor(Color.RED);
      row.setDrawLine(true);
      row.setMarkerSize(AbstractGraphRow.MARKER_SIZE_SMALL);

      final HashTree hashTree = new HashTree();
      hashTree.add(new LoopController());
      JMeterThread thread = new JMeterThread(hashTree, null, null);

      HashMap<Long, Long> counts=new HashMap<Long, Long>();

      for (int n=0; n<tg.getNumThreads(); n++)
      {
         thread.setThreadNum(n);
         tg.scheduleThread(thread);
         addCount(counts, thread.getStartTime());
         addCount(counts, thread.getEndTime());
      }

      Iterator it=counts.keySet().iterator();
      while (it.hasNext())
      {
         Long time = (Long) it.next();
         row.add(time, counts.get(time));
      }
      model.put("Expected parallel users count", row);
   }

   private void addCount(HashMap<Long, Long> counts, long xVal)
   {
      if (counts.containsKey(xVal))
      {
         counts.put(xVal, counts.get(xVal)+1);
      }
      else
      {
         counts.put(xVal, 1L);
      }
   }
}
