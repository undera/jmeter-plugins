package kg.apc.jmeter.threads;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.jmeter.gui.util.FocusRequester;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.threads.gui.AbstractThreadGroupGui;
import org.apache.jmeter.util.JMeterUtils;

public class SteppingThreadGroupGui
     extends AbstractThreadGroupGui
     implements ItemListener
{
   private VerticalPanel mainPanel;
   private JTextField threadInput;

   public SteppingThreadGroupGui()
   {
      super();
      init();
      initGui();
   }

   public TestElement createTestElement()
   {
      SteppingThreadGroup tg = new SteppingThreadGroup();
      modifyTestElement(tg);
      return tg;
   }

   /**
    * Modifies a given TestElement to mirror the data in the gui components.
    *
    * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
    */
   public void modifyTestElement(TestElement tg)
   {
      super.configureTestElement(tg);
      if (tg instanceof SteppingThreadGroup)
      {
         // ((SteppingThreadGroup) tg).setSamplerController((LoopController) loopPanel.createTestElement());
      }

      tg.setProperty(SteppingThreadGroup.NUM_THREADS, threadInput.getText());
   }

   @Override
   public void configure(TestElement tg)
   {
      super.configure(tg);
      threadInput.setText(tg.getPropertyAsString(SteppingThreadGroup.NUM_THREADS));
   }

   public void itemStateChanged(ItemEvent ie)
   {
      //if (ie.getItem().equals(scheduler))
      {
         // if (scheduler.isSelected())
         {
            mainPanel.setVisible(true);
         }
         // else
         {
            //mainPanel.setVisible(false);
         }
      }
   }

   public String getLabelResource()
   {
      return this.getClass().getSimpleName(); // $NON-NLS-1$
   }

   @Override
   public String getStaticLabel()
   {
      return "Stepping Thread Group";
   }

   @Override
   public void clearGui()
   {
      super.clearGui();
      initGui();
   }

   // Initialise the gui field values
   private void initGui()
   {
      
   }

   private void init()
   {
      // THREAD PROPERTIES
      VerticalPanel threadPropsPanel = new VerticalPanel();
      threadPropsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
           JMeterUtils.getResString("thread_properties"))); // $NON-NLS-1$

      // NUMBER OF THREADS
      JPanel threadPanel = new JPanel(new BorderLayout(5, 0));

      JLabel threadLabel = new JLabel(JMeterUtils.getResString("number_of_threads")); // $NON-NLS-1$
      threadPanel.add(threadLabel, BorderLayout.WEST);

      threadInput = new JTextField(5);
      //threadInput.setName(THREAD_NAME);
      threadLabel.setLabelFor(threadInput);
      threadPanel.add(threadInput, BorderLayout.CENTER);

      threadPropsPanel.add(threadPanel);
      new FocusRequester(threadInput);

      // RAMP-UP
      JPanel rampPanel = new JPanel(new BorderLayout(5, 0));
      JLabel rampLabel = new JLabel(JMeterUtils.getResString("ramp_up")); // $NON-NLS-1$
      rampPanel.add(rampLabel, BorderLayout.WEST);

      threadPropsPanel.add(rampPanel);

   }
}
